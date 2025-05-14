package sqlancer.oxla.ast;

import sqlancer.common.ast.BinaryOperatorNode;
import sqlancer.common.ast.newast.NewBinaryOperatorNode;
import sqlancer.oxla.schema.OxlaDataType;

import java.util.List;

public class OxlaBinaryOperation extends NewBinaryOperatorNode<OxlaExpression>
        implements OxlaExpression {
    public OxlaBinaryOperation(OxlaExpression left, OxlaExpression right, BinaryOperatorNode.Operator op) {
        super(left, right, op);
    }

    @Override
    public OxlaConstant getExpectedValue() {
        OxlaConstant leftValue = getLeft().getExpectedValue();
        OxlaConstant rightValue = getRight().getExpectedValue();
        if (leftValue == null || rightValue == null) {
            return null;
        }
        return ((OxlaBinaryOperation.OxlaBinaryOperator) op).apply(new OxlaConstant[]{leftValue, rightValue});

    }

    public static class OxlaBinaryOperator extends OxlaOperator {
        private final OxlaApplyFunction applyFunction;

        public OxlaBinaryOperator(String textRepresentation, OxlaTypeOverload overload, OxlaApplyFunction applyFunction) {
            super(textRepresentation, overload);
            this.applyFunction = applyFunction;
        }

        public OxlaConstant apply(OxlaConstant[] constants) {
            if (constants.length != 2) {
                throw new AssertionError(String.format("OxlaUnaryBinaryOperation::apply* failed: expected 2 arguments, but got %d", constants.length));
            }
            return applyFunction.apply(constants);
        }
    }

    public static final List<OxlaOperator> COMPARISON = List.of(
            // Less
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INTERVAL}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            new OxlaBinaryOperator("<", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME, OxlaDataType.TIME}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLess),
            // Less Equal
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INTERVAL}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            new OxlaBinaryOperator("<=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME, OxlaDataType.TIME}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyLessEqual),
            // Not Equal
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INTERVAL}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            new OxlaBinaryOperator("!=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME, OxlaDataType.TIME}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyNotEqual),
            // Equal
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INTERVAL}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            new OxlaBinaryOperator("=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME, OxlaDataType.TIME}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyEqual),
            // Greater
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INTERVAL}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            new OxlaBinaryOperator(">", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME, OxlaDataType.TIME}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreater),
            // Greater Equal
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INTERVAL}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.DATE}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual),
            new OxlaBinaryOperator(">=", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME, OxlaDataType.TIME}, OxlaDataType.BOOLEAN), OxlaBinaryOperation::applyGreaterEqual)
    );

    private static OxlaConstant applyLess(OxlaConstant[] constants) {
        final OxlaConstant left = constants[0];
        final OxlaConstant right = constants[1];
        throw new AssertionError(String.format("OxlaBinaryOperation::applyLess failed: %s < %s", left.getClass(), right.getClass()));
    }

    private static OxlaConstant applyLessEqual(OxlaConstant[] constants) {
        final OxlaConstant left = constants[0];
        final OxlaConstant right = constants[1];
        throw new AssertionError(String.format("OxlaBinaryOperation::applyLessEqual failed: %s < %s", left.getClass(), right.getClass()));
    }

    private static OxlaConstant applyNotEqual(OxlaConstant[] constants) {
        final OxlaConstant left = constants[0];
        final OxlaConstant right = constants[1];
        throw new AssertionError(String.format("OxlaBinaryOperation::applyNotEqual failed: %s < %s", left.getClass(), right.getClass()));
    }

    private static OxlaConstant applyEqual(OxlaConstant[] constants) {
        final OxlaConstant left = constants[0];
        final OxlaConstant right = constants[1];
        throw new AssertionError(String.format("OxlaBinaryOperation::applyEqual failed: %s < %s", left.getClass(), right.getClass()));
    }

    private static OxlaConstant applyGreater(OxlaConstant[] constants) {
        final OxlaConstant left = constants[0];
        final OxlaConstant right = constants[1];
        throw new AssertionError(String.format("OxlaBinaryOperation::applyGreater failed: %s < %s", left.getClass(), right.getClass()));
    }

    private static OxlaConstant applyGreaterEqual(OxlaConstant[] constants) {
        final OxlaConstant left = constants[0];
        final OxlaConstant right = constants[1];
        throw new AssertionError(String.format("OxlaBinaryOperation::applyGreaterEqual failed: %s < %s", left.getClass(), right.getClass()));
    }
}
