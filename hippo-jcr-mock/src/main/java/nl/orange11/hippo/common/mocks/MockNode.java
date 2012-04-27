package nl.orange11.hippo.common.mocks;

import javax.jcr.*;
import javax.jcr.lock.Lock;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Mock Node for testing purposes. Has some convenience methods that allow setting up properties of this fake node in a
 * fluent interface fashion. Eg. see {@link #setName(String)}.
 * <p/>
 * There are also a lot of methods that are not supported by the mock. These are positioned at the bottom.
 * <p/>
 * We do not support the methods related to versioning, all the checkin, checkout, merge methods throw an exception
 */
public class MockNode implements Node {
    private String path;
    private Map<String, Property> properties = new HashMap<String, Property>();
    private NodeType nodeType;
    private Session session;
    private String uuid;
    private final Set<String> nodeTypes = new HashSet<String>();
    private final Map<String, Node> nodesByPath = new HashMap<String, Node>();
    private String name;
    private boolean removed;
    private List<NodeType> nodeTypeMixins = new ArrayList<NodeType>();
    private Node parent;

    /*
     * Constructors
     */
    public MockNode() {
        this("", null, null);
    }

    public MockNode(Session session) {
        this("", null, session);
    }

    public MockNode(String path) {
        this(path == null ? "" : path, null, null);
    }

    public MockNode(String path, String type) {
        this(path, new MockNodeType(type), null);
    }

    public MockNode(String path, Session session) {
        this(path, null, session);
    }

    public MockNode(String path, NodeType nodeType, Session session) {
        this.path = path;
        this.nodeType = nodeType;
        this.session = session;
    }

    /*
     * Utility functions for construction of the node
     */
    public MockNode withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public MockNode withType(String nodeTypeName) {
        nodeTypes.add(nodeTypeName);
        return this;
    }

    public MockNode withNode(String path, Node node) {
        nodesByPath.put(path, node);
        return this;
    }

    public MockNode setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public Node getParent() {
        if (parent != null) {
            return parent;
        }
        return new MockNode(path.substring(0, path.lastIndexOf('/'))).withUuid("parent-uuid");
    }

    public String getPath() {
        return path;
    }

    public NodeType getPrimaryNodeType() throws RepositoryException {
        return nodeType;
    }

    public boolean isSame(Item otherItem) {
        return equals(otherItem);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof MockNode)) {
            return false;
        }

        return ((MockNode) obj).getPath().equals(getPath());
    }

    @SuppressWarnings({"EmptyMethod"})
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "MockNode: path=" + getPath();
    }

    public void addMixin(String mixinName) {
        nodeTypeMixins.add(new MockNodeType(mixinName));
    }

    public MockNode addNode(String relPath) {
        MockNode node = new MockNode(relPath);
        nodesByPath.put(relPath, node);
        node.setParent(this);
        return node;
    }

    public MockNode addNode(String relPath, String primaryNodeTypeName) {
        MockNode node = new MockNode(relPath, primaryNodeTypeName);
        nodesByPath.put(relPath, node);
        node.setParent(this);
        return node;
    }

    /**
     * Add a node at the given path.
     *
     * @param relPath the path to add it to
     * @param node    the node to add
     * @return self for fluent interface
     */
    public Node addNode(String relPath, Node node) {
        nodesByPath.put(relPath, node);
        return this;
    }

    public boolean canAddMixin(String mixinName) {
        return true;
    }

    public NodeType[] getMixinNodeTypes() {
        return nodeTypeMixins.toArray(new NodeType[nodeTypeMixins.size()]);
    }

    public Node getNode(String relPath) {
        return nodesByPath.get(relPath);
    }

    public NodeIterator getNodes() {
        Collection<Node> nodes = nodesByPath.values();
        return new MockNodeIterator(nodes.toArray(new Node[nodes.size()]));
    }

    public NodeIterator getNodes(String namePattern) {
        Collection<Node> nodes = nodesByPath.values();
        return new MockNodeIterator(nodes.toArray(new Node[nodes.size()]));
    }

    public PropertyIterator getProperties() {
        return new MockPropertyIterator(properties.values());
    }

    public Property getProperty(String relPath) {
        return properties.get(relPath);
    }

    @Override
    public PropertyIterator getWeakReferences() throws RepositoryException {
        return null;
    }

    @Override
    public PropertyIterator getWeakReferences(final String name) throws RepositoryException {
        return null;
    }

    public String getUUID() {
        return uuid;
    }

    @Override
    public String getIdentifier() throws RepositoryException {
        return uuid;
    }

    public VersionHistory getVersionHistory() {
        return null;
    }

    public boolean hasNode(String relPath) {
        return nodesByPath.containsKey(relPath);
    }

    public boolean hasNodes() {
        return false;
    }

    public boolean hasProperties() {
        return false;
    }

    public boolean hasProperty(String relPath) {
        return properties.containsKey(relPath);
    }

    public boolean holdsLock() {
        return false;
    }

    public boolean isCheckedOut() {
        return false;
    }

    public boolean isLocked() {
        return false;
    }

    @Override
    public void followLifecycleTransition(final String transition) throws RepositoryException {
    }

    @Override
    public String[] getAllowedLifecycleTransistions() throws RepositoryException {
        return new String[0];
    }

    public boolean isNodeType(String nodeTypeName) {
        return ((nodeType != null) && nodeType.getName().equals(nodeTypeName)) || nodeTypes.contains(nodeTypeName) || hasMixin(nodeTypeName);
    }

    @Override
    public void setPrimaryType(final String nodeTypeName) throws RepositoryException {
        this.nodeType = new MockNodeType(nodeTypeName);
    }

    public Property setProperty(String name, Value value) throws RepositoryException {
        return doSetProperty(name, value);
    }

    public Property setProperty(String name, Value[] values) {
        return null;
    }

    public MockProperty setProperty(String name, String[] values) throws RepositoryException {
        MockProperty p = new MockProperty(name);
        p.setValue(values);
        properties.put(name, p);
        return p;
    }

    public MockProperty setProperty(String name, String value) throws RepositoryException {
        return doSetProperty(name, new MockValue(value));
    }

    public Property setProperty(String name, InputStream value) throws RepositoryException {
        return doSetProperty(name, new MockValue(value));
    }

    private MockProperty doSetProperty(String name, Value value) throws RepositoryException {
        MockProperty p = new MockProperty(name);
        p.setValue(value);
        properties.put(name, p);
        return p;
    }

    @Override
    public Property setProperty(final String name, final Binary value) throws RepositoryException {
        return doSetProperty(name, new MockValue(value));
    }

    public Property setProperty(String name, boolean value) throws RepositoryException {
        return doSetProperty(name, new MockValue(value));
    }

    public Property setProperty(String name, double value) throws RepositoryException {
        return doSetProperty(name, new MockValue(value));
    }

    @Override
    public Property setProperty(final String name, final BigDecimal value) throws RepositoryException {
        return doSetProperty(name, new MockValue(value));
    }

    public Property setProperty(String name, long value) throws RepositoryException {
        return doSetProperty(name, new MockValue(value));
    }

    public Property setProperty(String name, Calendar value) throws RepositoryException {
        return doSetProperty(name, new MockValue(value));
    }

    public void update(String srcWorkspaceName) {
        throw new UnsupportedOperationException();
    }

    public void accept(ItemVisitor visitor) throws RepositoryException {
        visitor.visit(this);
    }

    public Item getAncestor(int depth) {
        throw new UnsupportedOperationException();
    }

    public int getDepth() {
        throw new UnsupportedOperationException();
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public boolean isModified() {
        return false;
    }

    public boolean isNew() {
        return false;
    }

    public boolean isNode() {
        return true;
    }

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public boolean hasMixin(String mixin) {
        for (Object nodeTypeMixin : nodeTypeMixins) {
            NodeType type = (NodeType) nodeTypeMixin;
            if (type.getName().equals(mixin)) {
                return true;
            }
        }
        return false;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    /*
     * All unsupported methods
     */
    public void cancelMerge(Version version) {
        throw new UnsupportedOperationException();
    }

    public Version checkin() {
        throw new UnsupportedOperationException();
    }

    public void checkout() {
        throw new UnsupportedOperationException();
    }

    public void doneMerge(Version version) {
        throw new UnsupportedOperationException();
    }

    public Version getBaseVersion() {
        throw new UnsupportedOperationException();
    }

    public String getCorrespondingNodePath(String workspaceName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeIterator getSharedSet() throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSharedSet() throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeShare() throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public NodeDefinition getDefinition() {
        throw new UnsupportedOperationException();
    }

    public int getIndex() {
        throw new UnsupportedOperationException();
    }

    public Lock getLock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeIterator getNodes(final String[] nameGlobs) throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public Item getPrimaryItem() {
        throw new UnsupportedOperationException();
    }

    public PropertyIterator getProperties(String namePattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PropertyIterator getProperties(final String[] nameGlobs) throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public PropertyIterator getReferences() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PropertyIterator getReferences(final String name) throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    public Lock lock(boolean isDeep, boolean isSessionScoped) {
        throw new UnsupportedOperationException();
    }

    public NodeIterator merge(String srcWorkspace, boolean bestEffort) {
        throw new UnsupportedOperationException();
    }

    public void orderBefore(String srcChildRelPath, String destChildRelPath) {
        throw new UnsupportedOperationException();
    }

    public void removeMixin(String mixinName) {
        throw new UnsupportedOperationException();
    }

    public void restore(String versionName, boolean removeExisting) {
        throw new UnsupportedOperationException();
    }

    public void restore(Version version, boolean removeExisting) {
        throw new UnsupportedOperationException();
    }

    public void restore(Version version, String relPath, boolean removeExisting) {
        throw new UnsupportedOperationException();
    }

    public void restoreByLabel(String versionLabel, boolean removeExisting) {
        throw new UnsupportedOperationException();
    }

    public Property setProperty(String name, Node value) {
        throw new UnsupportedOperationException();
    }

    public Property setProperty(String name, Value value, int type) {
        throw new UnsupportedOperationException();
    }

    public Property setProperty(String name, Value[] values, int type) {
        throw new UnsupportedOperationException();
    }

    public Property setProperty(String name, String[] values, int type) {
        throw new UnsupportedOperationException();
    }

    public Property setProperty(String name, String value, int type) {
        throw new UnsupportedOperationException();
    }

    public void unlock() {
        throw new UnsupportedOperationException();
    }

    public void save() {
        throw new UnsupportedOperationException();
    }

    public void refresh(boolean keepChanges) {
        throw new UnsupportedOperationException();
    }
}
