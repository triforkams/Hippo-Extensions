package nl.orange11.hippo.common.mocks;

import org.junit.Test;

import javax.jcr.PropertyType;
import javax.jcr.ValueFormatException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p>Test class for the {@link MockValue}.</p>
 *
 * @author Jettro Coenradie
 */
public class MockValueTest {
    @Test
    public void testBooleanValue() throws Exception {
        MockValue mockValue = new MockValue(true);
        assertTrue(mockValue.getBoolean());
        assertEquals("true", mockValue.getInternalStringValue());
        assertEquals(PropertyType.BOOLEAN, mockValue.getType());
    }

    @Test
    public void testDateValue() throws Exception {
        MockValue mockValue = new MockValue(new GregorianCalendar(2012, 10, 1));
        assertEquals(2012, mockValue.getDate().get(Calendar.YEAR));
        assertEquals(10, mockValue.getDate().get(Calendar.MONTH));
        assertEquals(1, mockValue.getDate().get(Calendar.DAY_OF_MONTH));
        assertEquals("Thu Nov 01 00:00:00 CET 2012", mockValue.getInternalStringValue());
        assertEquals(PropertyType.DATE, mockValue.getType());
    }

    @Test
    public void testLongValue() throws Exception {
        MockValue mockValue = new MockValue(99l);
        assertEquals(99, mockValue.getLong());
        assertEquals("99", mockValue.getInternalStringValue());
        assertEquals(PropertyType.LONG, mockValue.getType());
    }

    @Test
    public void testDoubleValue() throws Exception {
        MockValue mockValue = new MockValue(23.1);
        assertEquals(23.1, mockValue.getDouble(), 0.1);
        assertEquals("23.1", mockValue.getInternalStringValue());
        assertEquals(PropertyType.DOUBLE, mockValue.getType());
    }

    @Test
    public void testStringValue() throws Exception {
        MockValue mockValue = new MockValue("Test string");
        assertEquals("Test string", mockValue.getString());
        assertEquals("Test string", mockValue.getInternalStringValue());
        assertEquals(PropertyType.STRING, mockValue.getType());
    }

    @Test
    public void testDecimalValue() throws Exception {
        MockValue mockValue = new MockValue(new BigDecimal(23));
        assertEquals(23, mockValue.getDecimal().intValue());
        assertEquals("23", mockValue.getInternalStringValue());
        assertEquals(PropertyType.DECIMAL, mockValue.getType());
    }

    @Test
    public void testNonSupportedValue() throws Exception {
        MockValue mockValue = new MockValue("Test");

        try {
            mockValue.getLong();
        } catch (ValueFormatException e) {
            assertTrue(e.getMessage().contains("long"));
        }
        try {
            mockValue.getDecimal();
        } catch (ValueFormatException e) {
            assertTrue(e.getMessage().contains("decimal"));
        }
        try {
            mockValue.getDouble();
        } catch (ValueFormatException e) {
            assertTrue(e.getMessage().contains("double"));
        }
        try {
            mockValue.getBinary();
        } catch (ValueFormatException e) {
            assertTrue(e.getMessage().contains("binary"));
        }
        try {
            mockValue.getBoolean();
        } catch (ValueFormatException e) {
            assertTrue(e.getMessage().contains("boolean"));
        }
        try {
            mockValue.getDate();
        } catch (ValueFormatException e) {
            assertTrue(e.getMessage().contains("date"));
        }

        mockValue = new MockValue(1l);
        try {
            mockValue.getString();
        } catch (ValueFormatException e) {
            assertTrue(e.getMessage().contains("string"));
        }
    }


}
