package nl.orange11.hippo.common.mocks;

import javax.jcr.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * <p>Mock for the {@link Value} interface. Utility constructors are provided for all supported types. Each constructor
 * also assigns the right value to the Type parameter.</p>
 * <p>BEWARE: There is no real support at the moment for the Binary format. This will always return the provided binary
 * only. It will not convert other values to a binary format.</p>
 */
public class MockValue implements Value {
    protected static final String DEFAULT_ENCODING = "UTF-8";

    private String stringValue = null;
    private Calendar dateValue = null;
    private Long longValue = null;
    private Boolean booleanValue = null;
    private Double doubleValue = null;
    private BigDecimal decimalValue = null;
    private Binary binaryValue = null;
    private InputStream inputStreamValue = null;

    private int type;

    public MockValue(String str) {
        this.stringValue = str;
        this.type = PropertyType.STRING;
    }

    public MockValue(Calendar date) {
        this.dateValue = date;
        this.type = PropertyType.DATE;
    }

    public MockValue(Long longValue) {
        this.longValue = longValue;
        this.type = PropertyType.LONG;
    }

    public MockValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
        this.type = PropertyType.BOOLEAN;
    }

    public MockValue(BigDecimal decimalValue) {
        this.decimalValue = decimalValue;
        this.type = PropertyType.DECIMAL;
    }

    public MockValue(Double doubleValue) {
        this.doubleValue = doubleValue;
        this.type = PropertyType.DOUBLE;
    }

    public MockValue(Binary binaryValue) {
        this.binaryValue = binaryValue;
        this.type = PropertyType.BINARY;
    }

    public MockValue(InputStream inputStreamValue) {
        this.inputStreamValue = inputStreamValue;
        this.type= PropertyType.UNDEFINED;
    }

    @Override
    public boolean getBoolean() throws RepositoryException {
        if (booleanValue == null) {
            throw new ValueFormatException("No boolean value was assigned");
        }
        return booleanValue;
    }

    @Override
    public Calendar getDate() throws RepositoryException {
        if (dateValue == null) {
            throw new ValueFormatException("No date value was assigned");
        }

        return dateValue;
    }

    @Override
    public double getDouble() throws RepositoryException {
        if (doubleValue == null) {
            throw new ValueFormatException("No double value was assigned");
        }
        return doubleValue;
    }

    @Override
    public BigDecimal getDecimal() throws RepositoryException {
        if (decimalValue == null) {
            throw new ValueFormatException("No decimal value was assigned");
        }

        return decimalValue;
    }

    @Override
    public long getLong() throws RepositoryException {
        if (longValue == null) {
            throw new ValueFormatException("No long value was assigned");
        }

        return longValue;
    }

    @Override
    public InputStream getStream() throws RepositoryException {
        if (inputStreamValue != null) {
            return inputStreamValue;
        }

        try {
            // convert via string
            inputStreamValue = new ByteArrayInputStream(getInternalStringValue().getBytes(DEFAULT_ENCODING));
            return inputStreamValue;
        } catch (UnsupportedEncodingException e) {
            throw new RepositoryException(DEFAULT_ENCODING
                    + " not supported on this platform", e);
        }

    }

    @Override
    public Binary getBinary() throws RepositoryException {
        if (binaryValue == null) {
            throw new ValueFormatException("No binary stream available");
        }
        return binaryValue;
    }

    @Override
    public String getString() throws RepositoryException {
        if (stringValue == null) {
            throw new ValueFormatException("No string value was assigned");
        }

        return stringValue;
    }

    @Override
    public int getType() {
        return type;
    }

    String getInternalStringValue() {
        String value = null;
        switch (type) {
            case PropertyType.STRING:
                value = stringValue;
                break;
            case PropertyType.DATE:
                value = dateValue.getTime().toString();
                break;
            case PropertyType.LONG:
                value = longValue.toString();
                break;
            case PropertyType.BOOLEAN:
                value = booleanValue.toString();
                break;
            case PropertyType.DECIMAL:
                value = decimalValue.toPlainString();
                break;
            case PropertyType.DOUBLE:
                value = doubleValue.toString();
                break;
        }
        return value;
    }

}
