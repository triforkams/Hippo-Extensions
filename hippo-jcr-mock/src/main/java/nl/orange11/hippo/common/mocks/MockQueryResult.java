package nl.orange11.hippo.common.mocks;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.QueryResult;
import javax.jcr.query.RowIterator;

/**
 * Mock {@link javax.jcr.query.QueryResult} implementation to support testing.
 */
public class MockQueryResult implements QueryResult {

    private final NodeIterator nodeIterator;

    /**
     * Construct a new MockQueryResult wrapping the given nodes.
     *
     * @param nodes the nodes to wrap
     */
    public MockQueryResult(Node... nodes) {
        nodeIterator = new MockNodeIterator(nodes);
    }

    @Override
    public String[] getColumnNames() throws RepositoryException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public RowIterator getRows() throws RepositoryException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public NodeIterator getNodes() throws RepositoryException {
        return nodeIterator;
    }

    @Override
    public String[] getSelectorNames() throws RepositoryException {
        return new String[0];
    }
}
