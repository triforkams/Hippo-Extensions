package nl.orange11.hippo.common.mocks;

import javax.jcr.Value;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.PropertyDefinition;

/**
 * Mock {@link javax.jcr.nodetype.NodeType} implementation for testing purposes.
 */
public class MockNodeType implements NodeType {

    private String name;

    public MockNodeType(String name) {
        this.name = (name == null) ? "nt:unstructured" : name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String[] getDeclaredSupertypeNames() {
        return new String[0];
    }

    @Override
    public boolean isAbstract() {
        return false;
    }

    public boolean canAddChildNode(String childNodeName) {
        return false;
    }

    public boolean canAddChildNode(String childNodeName, String nodeTypeName) {
        return false;
    }

    public boolean canRemoveItem(String itemName) {
        return false;
    }

    @Override
    public boolean canRemoveNode(final String nodeName) {
        return false;
    }

    @Override
    public boolean canRemoveProperty(final String propertyName) {
        return false;
    }

    public boolean canSetProperty(String propertyName, Value value) {
        return false;
    }

    public boolean canSetProperty(String propertyName, Value[] values) {
        return false;
    }

    public NodeDefinition[] getChildNodeDefinitions() {
        return new NodeDefinition[0];
    }

    public NodeDefinition[] getDeclaredChildNodeDefinitions() {
        return new NodeDefinition[0];
    }

    public PropertyDefinition[] getDeclaredPropertyDefinitions() {
        return new PropertyDefinition[0];
    }

    public NodeType[] getDeclaredSupertypes() {
        return new NodeType[0];
    }

    @Override
    public NodeTypeIterator getSubtypes() {
        return null;
    }

    @Override
    public NodeTypeIterator getDeclaredSubtypes() {
        return null;
    }

    public String getPrimaryItemName() {
        return null;
    }

    public PropertyDefinition[] getPropertyDefinitions() {
        return new PropertyDefinition[0];
    }

    public NodeType[] getSupertypes() {
        return new NodeType[0];
    }

    public boolean hasOrderableChildNodes() {
        return false;
    }

    @Override
    public boolean isQueryable() {
        return false;
    }

    public boolean isMixin() {
        return false;
    }

    public boolean isNodeType(String nodeTypeName) {
        return false;
    }

}
