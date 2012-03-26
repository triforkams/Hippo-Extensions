package nl.orange11.hippo.common.mocks;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import java.util.NoSuchElementException;

/**
 * Mock {@link javax.jcr.NodeIterator} implementation for testing purposes.
 */
public class MockNodeIterator implements NodeIterator {

    private Node[] nodes;
    private int idx;
    private static final Node[] EMPTY_NODE_ARRAY = {};

    public MockNodeIterator() {
        this(EMPTY_NODE_ARRAY);
    }

    public MockNodeIterator(Node[] nodes) {
        this.nodes = (nodes != null) ? nodes : new Node[0];
        this.idx = 0;
    }

    public Node nextNode() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return nodes[idx++];
    }

    public long getPosition() {
        return idx - 1;
    }

    public long getSize() {
        return nodes.length;
    }

    public void skip(long skipNum) {
        idx += skipNum;
    }

    public boolean hasNext() {
        return idx < nodes.length;
    }

    public Object next() {
        return nextNode();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
