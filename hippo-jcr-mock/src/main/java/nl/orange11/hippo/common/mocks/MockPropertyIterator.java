package nl.orange11.hippo.common.mocks;

import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import java.util.Collection;
import java.util.Iterator;

public class MockPropertyIterator implements PropertyIterator {

    private final Collection<Property> properties;

    private final Iterator<Property> iterator;

    public MockPropertyIterator(Collection<Property> properties) {
        this.properties = properties;
        iterator = properties.iterator();
    }

    public Property nextProperty() {
        return iterator.next();
    }

    public long getPosition() {
        throw new UnsupportedOperationException();
    }

    public long getSize() {
        return properties.size();
    }

    public void skip(long skipNum) {
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public Object next() {
        return nextProperty();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
