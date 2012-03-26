package nl.orange11.hippo.common.mocks;

import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.api.Localized;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

/**
 * Mock {@link org.hippoecm.repository.api.HippoNode} implementation for testing purposes.
 */
public class MockHippoNode extends MockNode implements HippoNode {

    private static final String NOT_IMPLEMENTED_MESSAGE = "Not implemented";
    private Node canonicalNode;

    public MockHippoNode() {
        super();
    }

    public MockHippoNode(String path) {
        super(path);
    }

    public MockHippoNode(String path, String type) {
        super(path, type);
    }

    @Override
    public String getLocalizedName() throws RepositoryException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public String getLocalizedName(Localized localized) throws RepositoryException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public Node getCanonicalNode() throws RepositoryException {
        return canonicalNode;
    }

    @Override
    public NodeIterator pendingChanges(String name, boolean bool) throws RepositoryException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public NodeIterator pendingChanges(String name) throws RepositoryException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public NodeIterator pendingChanges() throws RepositoryException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_MESSAGE);
    }

    /**
     * Set the canonical node.
     *
     * @param canonicalNode the canonical node
     */
    public void setCanonicalNode(Node canonicalNode) {
        this.canonicalNode = canonicalNode;
    }
}
