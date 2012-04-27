package nl.orange11.hippo.common.mocks;

import javax.jcr.*;
import javax.jcr.nodetype.PropertyDefinition;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Mock for the {@link Property} interface.
 * <p/>
 * Some of the methods are not implemented. An UnsupportedOperationException is thrown in that case to make the unit
 * test that depends on this method fail. For now methods with respect to Parent, ancestor and Node are not supported.
 * <p/>
 * If you need to call save or refresh, you need to provide a session. If you do not provide a session and still call
 * these methods, an {@link UnsupportedOperationException} is thrown.
 * <p/>
 * Binaries and Streams are not really used by me, so if you need them be sure to test if it works for you.
 */
public class MockProperty implements Property {

    private Value[] values = {};
    private Value value;
    private boolean multiple = false;

    private final String name;
    private boolean removed;
    private boolean isModified;
    private boolean isNew;
    private Session session;

    public MockProperty(String name) {
        this(name, false, false);
    }

    public MockProperty(String name, boolean modified, boolean aNew) {
        this(name, null, modified, aNew);
    }

    public MockProperty(String name, Session session) {
        this(name, session, false, false);
    }

    public MockProperty(String name, Session session, boolean modified, boolean aNew) {
        this.name = name;
        this.session = session;
        this.isModified = modified;
        this.isNew = aNew;
    }

    public boolean getBoolean() throws RepositoryException {
        return getValue().getBoolean();
    }

    public Calendar getDate() throws RepositoryException {
        return getValue().getDate();
    }

    public double getDouble() throws RepositoryException {
        return getValue().getDouble();
    }

    public long getLong() throws RepositoryException {
        return getValue().getLong();
    }

    public BigDecimal getDecimal() throws RepositoryException {
        return getValue().getDecimal();
    }

    public Binary getBinary() throws RepositoryException {
        return getValue().getBinary();
    }

    public InputStream getStream() throws RepositoryException {
        return getValue().getStream();
    }

    public String getString() throws RepositoryException {
        return getValue().getString();
    }

    public Value getValue() throws RepositoryException {
        if (multiple) throw new ValueFormatException("The value is a multiple value and a singular value is requested");
        return value;
    }

    public PropertyDefinition getDefinition() throws RepositoryException {
        return new MockPropertyDefinition(isMultiple());
    }

    public int getType() throws RepositoryException {
        checkSingleValue();
        return value.getType();
    }

    @Override
    public boolean isMultiple() throws RepositoryException {
        return multiple;
    }

    public Value[] getValues() throws RepositoryException {
        return values;
    }

    public void setValue(boolean value) throws RepositoryException {
        setValue(new MockValue(value));
    }

    public void setValue(Calendar value) throws RepositoryException {
        setValue(new MockValue(value));
    }

    public void setValue(double value) throws RepositoryException {
        setValue(new MockValue(value));
    }

    @Override
    public void setValue(final BigDecimal value) throws RepositoryException {
        setValue(new MockValue(value));
    }

    public void setValue(InputStream value) throws RepositoryException {
        setValue(new MockValue(value));
    }

    @Override
    public void setValue(final Binary value) throws RepositoryException {
        setValue(new MockValue(value));
    }

    public void setValue(long value) throws RepositoryException {
        setValue(new MockValue(value));
    }

    public void setValue(String value) throws RepositoryException {
        setValue(new MockValue(value));
    }

    public void setValue(String[] inputValues) throws RepositoryException {
        Value[] values = new Value[inputValues.length];
        int i = 0;
        for (String str : inputValues) {
            values[i++] = new MockValue(str);
        }
        setValue(values);
    }

    public void setValue(Value value) throws RepositoryException {
        checkSingleValue();
        this.value = value;
    }

    public void setValue(Value[] values) throws RepositoryException {
        this.multiple = true;
        this.values = values;
    }

    public void accept(ItemVisitor visitor) throws RepositoryException {
        visitor.visit(this);
    }

    public String getName() throws RepositoryException {
        return name;
    }

    public Session getSession() throws RepositoryException {
        if (session == null) {
            throw new UnsupportedOperationException();
        }
        return session;
    }

    public boolean isModified() {
        return isModified;
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isNode() {
        return false;
    }

    public boolean isSame(Item otherItem) throws RepositoryException {
        return this == otherItem;
    }

    public void refresh(boolean keepChanges) throws RepositoryException {
        getSession().refresh(keepChanges);
    }

    public void remove() throws RepositoryException {
        removed = true;
    }

    public void save() throws RepositoryException {
        getSession().save();
    }

    public boolean isRemoved() {
        return removed;
    }

    private void checkSingleValue() throws RepositoryException {
        if (isMultiple())
            throw new ValueFormatException("The value is a multiple value and a singular value is requested");
    }

    /* Unsupported methods */

    public void setValue(Node value) throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public Item getAncestor(int depth) throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public long getLength() throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public long[] getLengths() throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public Node getNode() throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public Property getProperty() throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public int getDepth() throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public Node getParent() throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public String getPath() throws RepositoryException {
        throw new UnsupportedOperationException();
    }
}
