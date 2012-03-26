package nl.orange11.hippo.common.mocks;

import org.junit.Test;

import javax.jcr.ItemVisitor;
import javax.jcr.PropertyType;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.nodetype.PropertyDefinition;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Jettro Coenradie
 */
public class MockPropertyTest {
    @Test
    public void testCheckBoolean() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        mockProperty.setValue(true);

        assertTrue(mockProperty.getBoolean());
    }

    @Test
    public void testString() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        mockProperty.setValue("test");

        assertEquals("test", mockProperty.getString());
    }

    @Test
    public void testLong() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        mockProperty.setValue(23l);

        assertEquals(23l, mockProperty.getLong());
    }

    @Test
    public void testDouble() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        mockProperty.setValue(23.35);

        assertEquals(23.35, mockProperty.getDouble(), 0.01);
    }

    @Test
    public void testDecimal() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        mockProperty.setValue(new BigDecimal(23));

        assertEquals(23, mockProperty.getDecimal().intValue());
    }

    @Test
    public void testDate() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        mockProperty.setValue(new GregorianCalendar(2012, 10, 1));

        assertEquals(2012, mockProperty.getDate().get(Calendar.YEAR));
        assertEquals(10, mockProperty.getDate().get(Calendar.MONTH));
        assertEquals(1, mockProperty.getDate().get(Calendar.DAY_OF_MONTH));
        assertEquals("Thu Nov 01 00:00:00 CET 2012", ((MockValue) mockProperty.getValue()).getInternalStringValue());
    }

    @Test
    public void testType() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        mockProperty.setValue("A String");

        assertEquals(PropertyType.STRING, mockProperty.getType());
    }

    @Test
    public void testname() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        assertEquals("test", mockProperty.getName());
    }

    @Test
    public void testSession() throws Exception {
        Session mockSession = mock(Session.class);
        MockProperty mockProperty = new MockProperty("test", mockSession);

        mockProperty.save();
        mockProperty.refresh(true);

        verify(mockSession).save();
        verify(mockSession).refresh(true);

        assertSame(mockSession, mockProperty.getSession());
    }

    @Test
    public void testMultiple() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        Value[] values = new Value[]{new MockValue("item1"), new MockValue("item2")};
        mockProperty.setValue(values);

        assertTrue(mockProperty.isMultiple());
    }

    @Test
    public void testMultipleStringArray() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        String[] values = new String[]{"item1", "item2"};
        mockProperty.setValue(values);

        assertTrue(mockProperty.isMultiple());
        Value[] valuesCreated = mockProperty.getValues();
        assertEquals("item1", valuesCreated[0].getString());
        assertEquals("item2", valuesCreated[1].getString());
    }

    @Test
    public void testRemove() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        mockProperty.remove();

        assertTrue(mockProperty.isRemoved());
    }

    @Test
    public void testNewAndModified() throws Exception {
        MockProperty mockProperty = new MockProperty("test", true, true);
        assertTrue(mockProperty.isNew());
        assertTrue(mockProperty.isModified());
        mockProperty = new MockProperty("test", false, false);
        assertFalse(mockProperty.isNew());
        assertFalse(mockProperty.isModified());
    }

    @Test
    public void testPropertyDefinition() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        mockProperty.setValue("test");
        PropertyDefinition definition = mockProperty.getDefinition();
        assertNotNull(definition);
        assertFalse(definition.isMultiple());
    }

    @Test
    public void testSame() throws Exception {
        MockProperty mockProperty = new MockProperty("test");
        MockProperty mockProperty2 = new MockProperty("test");
        assertTrue(mockProperty.isSame(mockProperty));
        assertFalse(mockProperty.isSame(mockProperty2));
    }

    @Test
    public void testAccept() throws Exception {
        ItemVisitor mockVisitor = mock(ItemVisitor.class);
        
        MockProperty mockProperty = new MockProperty("test");
        mockProperty.accept(mockVisitor);
        
        verify(mockVisitor).visit(mockProperty);
    }
}
