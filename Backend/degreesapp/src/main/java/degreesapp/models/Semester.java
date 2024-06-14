package degreesapp.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.jackson.JsonComponent;

import javax.persistence.AttributeConverter;
import javax.persistence.Embeddable;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Optional;

@AllArgsConstructor
@Getter
@Accessors ( fluent = true )
@EqualsAndHashCode
public final class Semester implements Comparable<Semester> {
    static final MonthDay FALL_SEMESTER_START = MonthDay.of(Month.AUGUST , 21);
    static final MonthDay FALL_SEMESTER_END = MonthDay.of(Month.DECEMBER , 15);

    static final MonthDay SPRING_SEMESTER_START = MonthDay.of(Month.JANUARY , 16);
    static final MonthDay SPRING_SEMESTER_END = MonthDay.of(Month.MAY , 10);

    public enum Season {
        SPRING, FALL;

        @Override
        public String toString() {
            return switch ( this ) {
                case SPRING -> "Spring";
                case FALL -> "Fall";
            };
        }

        public static Season fromString(String string) {
            return switch ( string ) {
                case "Spring" -> SPRING;
                case "Fall" -> FALL;
                default -> null;
            };
        }

        MonthDay start() {
            return switch ( this ) {
                case SPRING -> SPRING_SEMESTER_START;
                case FALL -> FALL_SEMESTER_START;
            };
        }

        MonthDay end() {
            return switch ( this ) {
                case SPRING -> SPRING_SEMESTER_END;
                case FALL -> FALL_SEMESTER_END;
            };
        }
    }

    public Season season;
    public int year;

    @Override
    public int compareTo(Semester other) {
        return Integer.compare(this.toInteger() , other.toInteger());
    }

    @Override
    public String toString() {
        return this.season + " " + this.year;
    }

    public static Semester fromString(String string) {
        String[] components = string.trim().split("\\s+" , 2);
        if ( components.length != 2 )
            return null;
        Season season = Season.fromString(components[0]);
        if ( season == null )
            return null;
        int year;
        try {
            year = Integer.parseInt(components[1]);
        } catch ( NumberFormatException e ) {
            return null;
        }
        return new Semester(season , year);
    }

    public int toInteger() {
        return switch ( this.season ) {
            case SPRING -> this.year * 2;
            case FALL -> this.year * 2 + 1;
        };
    }

    public LocalDate start() {
        return season.start().atYear(year);
    }

    public LocalDate end() {
        return season.end().atYear(year);
    }

    public boolean contains(LocalDate date) {
        if ( date.isEqual(start()) )
            return true;
        if ( date.isEqual(end()) )
            return true;
        if ( date.isAfter(start()) && date.isBefore(end()) )
            return true;
        return false;
    }

    public static Optional<Semester> during(LocalDate date) {
        int year = date.getYear();
        MonthDay monthDay = MonthDay.from(date);
        if ( monthDay.equals(Season.FALL.start()) || monthDay.equals(Season.FALL.end()) ) {
            return Optional.of(new Semester(Season.FALL , year));
        }
        if ( monthDay.equals(Season.SPRING.start()) || monthDay.equals(Season.SPRING.end()) ) {
            return Optional.of(new Semester(Season.SPRING , year));
        }
        if ( monthDay.isAfter(Season.FALL.start()) && monthDay.isBefore(Season.FALL.end()) ) {
            return Optional.of(new Semester(Season.FALL , year));
        }
        if ( monthDay.isAfter(Season.SPRING.start()) && monthDay.isBefore(Season.SPRING.end()) ) {
            return Optional.of(new Semester(Season.SPRING , year));
        }
        return Optional.empty();
    }

    public static Semester before(LocalDate date) {
        int year = date.getYear();
        MonthDay monthDay = MonthDay.from(date);
        if ( Season.FALL.end().isBefore(monthDay) )
            return new Semester(Season.FALL , year);
        if ( Season.SPRING.end().isBefore(monthDay) )
            return new Semester(Season.SPRING , year);
        return new Semester(Season.FALL , year - 1);
    }

    public static Semester after(LocalDate date) {
        int year = date.getYear();
        MonthDay monthDay = MonthDay.from(date);
        if ( Season.SPRING.start().isAfter(monthDay) )
            return new Semester(Season.SPRING , year);
        if ( Season.FALL.start().isAfter(monthDay) )
            return new Semester(Season.FALL , year);
        return new Semester(Season.SPRING , year + 1);
    }

    @JsonComponent
    public static class Serializer extends JsonSerializer<Semester> {
        public void serialize(Semester semester , JsonGenerator jsonGenerator , SerializerProvider serializerProvider) throws IOException {
            if ( semester == null ) {
                jsonGenerator.writeNull();
                return;
            }
            jsonGenerator.writeString(semester.toString());
        }
    }

    @JsonComponent
    public static class Deserializer extends JsonDeserializer<Semester> {
        public Semester deserialize(JsonParser jsonParser , DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            String string = jsonParser.getValueAsString();
            if ( string == null )
                throw InvalidFormatException.from(jsonParser , "Not a string" , null , Semester.class);
            Semester semester = Semester.fromString(string);
            if ( semester == null )
                throw InvalidFormatException.from(jsonParser , "Invalid semester format" , string , Semester.class);
            return semester;
        }
    }

    @javax.persistence.Converter ( autoApply = true )
    public static class Converter implements AttributeConverter<Semester, String> {
        @Override
        public String convertToDatabaseColumn(Semester semester) {
            if ( semester == null )
                return null;
            return semester.toString();
        }

        @Override
        public Semester convertToEntityAttribute(String s) {
            if ( s == null )
                return null;
            Semester semester = Semester.fromString(s);
            if ( semester == null )
                throw new RuntimeException("Invalid string in database: \"%s\"".formatted(s));
            return semester;
        }
    }
}
