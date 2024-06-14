package degreesapp.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ApiModel ( description = "A specific section of a course." )
public class CourseSection {
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    @ApiModelProperty ( "The section's ID" )
    private Long id;

    @ManyToOne
    private Course course;

    @ManyToOne
    @JoinColumn ( name = "instructorId" )
    @ApiModelProperty ( "The instructor who teaches the section" )
    private Instructor instructor;

    //    @NotBlank ( message = "Semester and year is required" )
    @Column ( name = "semesterAndYear" )
    @ApiModelProperty ( "The section's semester, such as \"Spring 2023\"" )
    private Semester semester;

    //    @NotBlank ( message = "Section identifier is required" )
    @ApiModelProperty ( "The number or letter, or combination of both, used to identify the section." )
    private String sectionIdentifier;
    //    @NotBlank ( message = "Location is required" )
    @ApiModelProperty ( "Where the section is held." )
    private String location;
    //    @NotBlank ( message = "Start date is required" )
    @ApiModelProperty ( "The first day the section is taught." )
    private LocalDate startDate;
    //    @NotBlank ( message = "End date is required" )
    @ApiModelProperty ( "The last day the section is taught." )
    private LocalDate endDate;
    //    @NotBlank ( message = "date of week is required" )
    @JsonSerialize ( using = DaysOfWeekConversion.Serializer.class )
    @JsonDeserialize ( using = DaysOfWeekConversion.Deserializer.class )
    @Convert ( converter = DaysOfWeekConversion.Converter.class )
    @ApiModelProperty ( value = "The days of the week that the section is taught.", example = "MTWRFSU" )
    private EnumSet<DayOfWeek> daysOfWeek;
    //    @NotBlank ( message = "Start time is required" )
    @ApiModelProperty ( value = "The time that the section starts each day." )
    private LocalTime startTime;
    //    @NotBlank ( message = "End time is required" )
    @ApiModelProperty ( value = "The time that the section ends each day." )
    private LocalTime endTime;

    private static final class DaysOfWeekConversion {
        private static final DayOfWeek[] values = DayOfWeek.values();

        public static byte dayOfWeekToChar(DayOfWeek dayOfWeek) {
            return switch ( dayOfWeek ) {
                case MONDAY -> 'M';
                case TUESDAY -> 'T';
                case WEDNESDAY -> 'W';
                case THURSDAY -> 'R';
                case FRIDAY -> 'F';
                case SATURDAY -> 'S';
                case SUNDAY -> 'U';
            };
        }

        public static DayOfWeek dayOfWeekFromChar(char dayOfWeek) {
            return switch ( dayOfWeek ) {
                default -> null;
                case 'M' -> DayOfWeek.MONDAY;
                case 'T' -> DayOfWeek.TUESDAY;
                case 'W' -> DayOfWeek.WEDNESDAY;
                case 'R' -> DayOfWeek.THURSDAY;
                case 'F' -> DayOfWeek.FRIDAY;
                case 'S' -> DayOfWeek.SATURDAY;
                case 'U' -> DayOfWeek.SUNDAY;
            };
        }

        public static String daysOfWeekToString(EnumSet<DayOfWeek> daysOfWeek) {
            byte[] chars = new byte[7];
            int charsIdx = 0;
            for ( DayOfWeek dayOfWeek : daysOfWeek ) {
                chars[charsIdx++] = dayOfWeekToChar(dayOfWeek);
            }
            return new String(chars , 0 , charsIdx);
        }

        public static EnumSet<DayOfWeek> daysOfWeekFromString(CharSequence string) {
            EnumSet<DayOfWeek> daysOfWeek = EnumSet.noneOf(DayOfWeek.class);
            for ( int i = 0; i < string.length(); i++ ) {
                DayOfWeek dayOfWeek = dayOfWeekFromChar(string.charAt(i));
                if ( dayOfWeek != null ) {
                    daysOfWeek.add(dayOfWeek);
                }
            }
            return daysOfWeek;
        }

        public static class Serializer extends JsonSerializer<EnumSet<DayOfWeek>> {
            public void serialize(EnumSet<DayOfWeek> daysOfWeek , JsonGenerator jsonGenerator , SerializerProvider serializerProvider) throws IOException {
                if ( daysOfWeek == null ) {
                    jsonGenerator.writeNull();
                    return;
                }
                jsonGenerator.writeString(daysOfWeekToString(daysOfWeek));
            }
        }

        public static class Deserializer extends JsonDeserializer<EnumSet<DayOfWeek>> {
            public EnumSet<DayOfWeek> deserialize(JsonParser jsonParser , DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                String string = jsonParser.getValueAsString();
                if ( string == null )
                    throw InvalidFormatException.from(jsonParser , "Not a string" , null , Semester.class);
                return daysOfWeekFromString(string);
            }
        }

        @javax.persistence.Converter
        public static class Converter implements AttributeConverter<EnumSet<DayOfWeek>, String> {
            @Override
            public String convertToDatabaseColumn(EnumSet<DayOfWeek> daysOfWeek) {
                if ( daysOfWeek == null )
                    return null;
                return daysOfWeekToString(daysOfWeek);
            }

            @Override
            public EnumSet<DayOfWeek> convertToEntityAttribute(String s) {
                if ( s == null )
                    return null;
                return daysOfWeekFromString(s);
            }
        }
    }
}
