package dgreesapp.restAssured;

import degreesapp.Main;
import io.restassured.RestAssured;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@SpringBootTest ( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@RunWith ( SpringRunner.class )
@ContextConfiguration ( classes = Main.class )
abstract class BaseTest {

    @LocalServerPort
    private int port;

    @Before
    public final void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    public static BaseMatcher<Number> numEqualTo(String decNum) {
        return numEqualTo(new BigDecimal(decNum));
    }

    public static BaseMatcher<Number> numEqualTo(Number number) {
        return new BaseMatcher<Number>() {
            final Number numericValue = numericDowncast(number);
            static Number numericDowncast(Number num) {
                if (num == null) {
                    return null;
                } else if (num instanceof Byte ||
                        num instanceof Short ||
                        num instanceof Integer ||
                        num instanceof Long) {
                    if (num.byteValue() == num.longValue())
                        return num.byteValue();
                    if (num.shortValue() == num.longValue())
                        return num.shortValue();
                    if (num.intValue() == num.longValue())
                        return num.intValue();
                    return num.longValue();
                } else if (num instanceof Float || num instanceof Double) {
                    if (num.equals(num.longValue()))
                        return numericDowncast(num.longValue());
                    if (num.equals(num.floatValue())) {
                        return num.floatValue();
                    }
                    return num.doubleValue();
                } else if (num instanceof BigInteger bigInt) {
                    if (bigInt.equals(BigInteger.valueOf(bigInt.longValue())))
                        return numericDowncast(bigInt.longValue());
                    return bigInt;
                } else if (num instanceof BigDecimal bigDec) {
                    BigInteger bigInt = bigDec.toBigInteger();
                    if (bigDec.equals(new BigDecimal(bigInt))) {
                        return numericDowncast(bigInt);
                    }
                    return bigDec;
                } else {
                    return num;
                }
            }
            @Override
            public boolean matches(Object o) {
                if (Objects.equals(numericValue, o)) {
                    return true;
                }
                if (o instanceof Number other) {
                    return Objects.equals(numericValue, numericDowncast(other));
                } else if (o instanceof String other) {
                    BigDecimal decOther;
                    try {
                        decOther = new BigDecimal(other);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                    return numericValue.equals(decOther);
                } else {
                    return false;
                }
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("numerically equal to " + numericValue);
            }
        };
    }

    public static <T extends Comparable<T>> Matcher<T> between(T lowerBound, T upperBound) {
        return Matchers.both(Matchers.greaterThanOrEqualTo(lowerBound)).and(Matchers.lessThan(upperBound));
    }
}
