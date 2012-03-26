package nl.orange11.hippo.common.mocks;

import javax.jcr.Value;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.PropertyDefinition;

public class MockPropertyDefinition implements PropertyDefinition {

    private boolean multiple;

    public MockPropertyDefinition(boolean multiple) {
        this.multiple = multiple;
    }

    public Value[] getDefaultValues() {
        return new Value[0];
    }

    public int getRequiredType() {
        return 0;
    }

    public String[] getValueConstraints() {
        return new String[0];
    }

    public boolean isMultiple() {
        return multiple;
    }

    @Override
    public String[] getAvailableQueryOperators() {
        return new String[0];
    }

    @Override
    public boolean isFullTextSearchable() {
        return false;
    }

    @Override
    public boolean isQueryOrderable() {
        return false;
    }

    public NodeType getDeclaringNodeType() {
        return null;
    }

    public String getName() {
        return null;
    }

    public int getOnParentVersion() {
        return 0;
    }

    public boolean isAutoCreated() {
        return false;
    }

    public boolean isMandatory() {
        return false;
    }

    public boolean isProtected() {
        return false;
    }
}
