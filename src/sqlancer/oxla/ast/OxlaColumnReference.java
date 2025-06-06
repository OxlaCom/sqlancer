package sqlancer.oxla.ast;

import sqlancer.common.ast.newast.ColumnReferenceNode;
import sqlancer.oxla.OxlaToStringVisitor;
import sqlancer.oxla.schema.OxlaColumn;

public class OxlaColumnReference extends ColumnReferenceNode<OxlaExpression, OxlaColumn>
        implements OxlaExpression {
    private final OxlaConstant expectedValue;

    public OxlaColumnReference(OxlaColumn oxlaColumn) {
        this(oxlaColumn, null);
    }

    public OxlaColumnReference(OxlaColumn oxlaColumn, OxlaConstant expectedValue) {
        super(oxlaColumn);
        this.expectedValue = expectedValue;
    }

    @Override
    public OxlaConstant getExpectedValue() {
        return expectedValue;
    }

    @Override
    public String toString() {
        return OxlaToStringVisitor.asString(this);
    }
}
