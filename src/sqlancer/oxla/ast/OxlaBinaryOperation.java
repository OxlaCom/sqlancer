package sqlancer.oxla.ast;

import sqlancer.common.ast.BinaryOperatorNode;
import sqlancer.common.ast.newast.NewBinaryOperatorNode;
import sqlancer.oxla.schema.OxlaDataType;

import java.util.List;
import java.util.function.IntPredicate;

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

    public static final List<OxlaOperator> LOGICAL = List.of();

    public static final List<OxlaOperator> ARITHMETIC = List.of(
            // Addition
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.INT64), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT32}, OxlaDataType.INT64), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.INT32}, OxlaDataType.DATE), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.INTERVAL}, OxlaDataType.TIMESTAMP), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.TIME}, OxlaDataType.TIMESTAMP), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT64}, OxlaDataType.INT64), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.DATE}, OxlaDataType.DATE), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INT32), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.DATE}, OxlaDataType.TIMESTAMP), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.TIMESTAMP}, OxlaDataType.TIMESTAMP), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.TIMESTAMPTZ), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.INTERVAL}, OxlaDataType.TIMESTAMP), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.INTERVAL}, OxlaDataType.TIMESTAMPTZ), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME, OxlaDataType.DATE}, OxlaDataType.TIMESTAMP), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME, OxlaDataType.INTERVAL}, OxlaDataType.TIME), OxlaBinaryOperation::applyAdd),
            new OxlaBinaryOperator("+", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.TIME}, OxlaDataType.TIME), OxlaBinaryOperation::applyAdd),
            // Subtraction
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.INT64), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT32}, OxlaDataType.INT64), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.DATE}, OxlaDataType.INT32), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.INT32}, OxlaDataType.DATE), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.INTERVAL}, OxlaDataType.TIMESTAMP), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT64}, OxlaDataType.INT64), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INT32), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.INTERVAL}, OxlaDataType.TIMESTAMP), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMP}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.INTERVAL}, OxlaDataType.TIMESTAMPTZ), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME, OxlaDataType.INTERVAL}, OxlaDataType.TIME), OxlaBinaryOperation::applySub),
            new OxlaBinaryOperator("-", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME, OxlaDataType.TIME}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applySub),
            // Multiplication
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.INT64), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT32}, OxlaDataType.INT64), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT64}, OxlaDataType.INT64), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INT32), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INT32}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.FLOAT32}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INT64}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.FLOAT64}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applyMul),
            new OxlaBinaryOperator("*", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), OxlaBinaryOperation::applyMul),
            // Division
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.INT64), OxlaBinaryOperation::applyDiv),
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT32}, OxlaDataType.INT64), OxlaBinaryOperation::applyDiv),
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applyDiv),
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applyDiv),
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT64}, OxlaDataType.INT64), OxlaBinaryOperation::applyDiv),
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INT32), OxlaBinaryOperation::applyDiv),
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INT32}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyDiv),
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.INT64}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyDiv),
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.FLOAT32}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyDiv),
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.FLOAT64}, OxlaDataType.INTERVAL), OxlaBinaryOperation::applyDiv),
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applyDiv),
            new OxlaBinaryOperator("/", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), OxlaBinaryOperation::applyDiv),
            // Modulus
            new OxlaBinaryOperator("%", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.INT64), OxlaBinaryOperation::applyMod),
            new OxlaBinaryOperator("%", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INT32), OxlaBinaryOperation::applyMod),
            new OxlaBinaryOperator("%", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), OxlaBinaryOperation::applyMod),
            new OxlaBinaryOperator("%", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), OxlaBinaryOperation::applyMod)

    );

    private static OxlaConstant applyComparison(OxlaConstant[] constants, IntPredicate comparisonFunction) {
        if (constants[0] instanceof OxlaConstant.OxlaNullConstant || constants[1] instanceof OxlaConstant.OxlaNullConstant) {
            return OxlaConstant.createNullConstant();
        }
        return OxlaConstant.createBooleanConstant(comparisonFunction.test(constants[0].compareTo(constants[1])));
    }

    private static OxlaConstant applyLess(OxlaConstant[] constants) {
        return applyComparison(constants, (a) -> a < 0);
    }

    private static OxlaConstant applyLessEqual(OxlaConstant[] constants) {
        return applyComparison(constants, (a) -> a <= 0);
    }

    private static OxlaConstant applyNotEqual(OxlaConstant[] constants) {
        return applyComparison(constants, (a) -> a != 0);
    }

    private static OxlaConstant applyEqual(OxlaConstant[] constants) {
        return applyComparison(constants, (a) -> a == 0);
    }

    private static OxlaConstant applyGreater(OxlaConstant[] constants) {
        return applyComparison(constants, (a) -> a > 0);
    }

    private static OxlaConstant applyGreaterEqual(OxlaConstant[] constants) {
        return applyComparison(constants, (a) -> a >= 0);
    }

    private static OxlaConstant applyAdd(OxlaConstant[] constants) {
        throw new AssertionError("not implemented yet.");
    }

    private static OxlaConstant applySub(OxlaConstant[] constants) {
        throw new AssertionError("not implemented yet.");
    }

    private static OxlaConstant applyMul(OxlaConstant[] constants) {
        throw new AssertionError("not implemented yet.");
    }

    private static OxlaConstant applyDiv(OxlaConstant[] constants) {
        throw new AssertionError("not implemented yet.");
    }

    private static OxlaConstant applyMod(OxlaConstant[] constants) {
        throw new AssertionError("not implemented yet.");
    }
}
