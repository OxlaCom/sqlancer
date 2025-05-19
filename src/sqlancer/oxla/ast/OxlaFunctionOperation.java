package sqlancer.oxla.ast;

import sqlancer.IgnoreMeException;
import sqlancer.common.ast.newast.NewFunctionNode;
import sqlancer.oxla.schema.OxlaDataType;

import java.util.ArrayList;
import java.util.List;

public class OxlaFunctionOperation extends NewFunctionNode<OxlaExpression, OxlaFunctionOperation.OxlaFunction>
        implements OxlaExpression {
    public OxlaFunctionOperation(List<OxlaExpression> args, OxlaFunction func) {
        super(args, func);
    }

    @Override
    public OxlaConstant getExpectedValue() {
        OxlaConstant[] expectedValues = new OxlaConstant[args.size()];
        for (int index = 0; index < args.size(); ++index) {
            if (args.get(index) == null) {
                return null;
            }
            expectedValues[index] = args.get(index).getExpectedValue();
        }
        return func.apply(expectedValues);
    }

    public static class OxlaFunction implements OxlaExpression {
        public final String textRepresentation;
        public final OxlaTypeOverload overload;
        private final OxlaApplyFunction applyFunction;

        public OxlaFunction(String textRepresentation, OxlaTypeOverload overload, OxlaApplyFunction applyFunction) {
            this.textRepresentation = textRepresentation;
            this.overload = overload;
            this.applyFunction = applyFunction;
        }

        public OxlaConstant apply(OxlaConstant[] constants) {
            if (applyFunction == null) {
                // NOTE: `applyFunction` is not implemented, thus PQS oracle should ignore this operator.
                throw new IgnoreMeException();
            }
            if (constants.length != overload.inputTypes.length) {
                throw new AssertionError(String.format("OxlaFunction::apply* failed: expected %d arguments, but got %d", overload.inputTypes.length, constants.length));
            }
            return applyFunction.apply(constants);
        }

        @Override
        public String toString() {
            return textRepresentation;
        }
    }

    public static final List<OxlaFunction> MATH = List.of(
            new OxlaFunction("abs", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT32), null),
            new OxlaFunction("abs", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.INT64), null),
            new OxlaFunction("abs", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("abs", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("cbrt", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("cbrt", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("ceil", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("ceil", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("ceiling", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("ceiling", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("degrees", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("exp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("floor", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("floor", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("round", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("round", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("sin", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("sin", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("sind", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("sind", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("asind", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("asind", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("cos", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("cos", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("cosd", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("cosd", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("acos", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("acos", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("cot", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("cot", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("cotd", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("cotd", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("acosd", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("acosd", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("radians", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("radians", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("sqrt", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("sqrt", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("ln", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("ln", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("log10", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("log10", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("log", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("log", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("log", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("log", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("atan2", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("atan2", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("atan2d", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("atan2d", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("atan", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("atan", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("atan2d", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("atan2d", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("pi", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("random", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("tan", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("tan", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("tand", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("tand", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("power", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("power", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("power", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INT32), null),
            new OxlaFunction("power", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.INT64), null),
            new OxlaFunction("sign", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("sign", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("sign", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT32), null),
            new OxlaFunction("sign", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.INT64), null)
    );

    public static final List<OxlaFunction> STRING = List.of(
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN}, OxlaDataType.TEXT), null),
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE}, OxlaDataType.TEXT), null),
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.TEXT), null),
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.TEXT), null),
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL}, OxlaDataType.TEXT), null),
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.JSON}, OxlaDataType.TEXT), null),
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME}, OxlaDataType.TEXT), null),
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP}, OxlaDataType.TEXT), null),
            new OxlaFunction("concat", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ}, OxlaDataType.TEXT), null),
            new OxlaFunction("length", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.INT32), null),
            new OxlaFunction("lower", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("upper", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("replace", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("starts_with", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("substr", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("substr", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("substring", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("substring", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("strpos", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.INT32), null),
            new OxlaFunction("position", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.INT32), null),
            new OxlaFunction("regexp_replace", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("regexp_replace", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT), null)
    );

    public static final List<OxlaFunction> MISC = List.of(
            new OxlaFunction("current_database", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.TEXT), null),
            new OxlaFunction("current_schema", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.TEXT), null),
            new OxlaFunction("version", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.TEXT), null)
    );

    public static final List<OxlaFunction> AGGREGATE;

    static {
        List<OxlaFunction> tempList = new ArrayList<>();
        tempList.add(new OxlaFunction("sum", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null));
        tempList.add(new OxlaFunction("sum", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("sum", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT32), null));
        tempList.add(new OxlaFunction("sum", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("sum", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), null));
        tempList.add(new OxlaFunction("sum", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME}, OxlaDataType.TIME), null));

        tempList.add(new OxlaFunction("min", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE}, OxlaDataType.DATE), null));
        tempList.add(new OxlaFunction("min", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null));
        tempList.add(new OxlaFunction("min", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("min", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT32), null));
        tempList.add(new OxlaFunction("min", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("min", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), null));
        tempList.add(new OxlaFunction("min", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.TEXT), null));
        tempList.add(new OxlaFunction("min", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ}, OxlaDataType.TIMESTAMPTZ), null));
        tempList.add(new OxlaFunction("min", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP}, OxlaDataType.TIMESTAMP), null));
        tempList.add(new OxlaFunction("min", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME}, OxlaDataType.TIME), null));

        tempList.add(new OxlaFunction("max", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE}, OxlaDataType.DATE), null));
        tempList.add(new OxlaFunction("max", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null));
        tempList.add(new OxlaFunction("max", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("max", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT32), null));
        tempList.add(new OxlaFunction("max", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("max", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), null));
        tempList.add(new OxlaFunction("max", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.TEXT), null));
        tempList.add(new OxlaFunction("max", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ}, OxlaDataType.TIMESTAMPTZ), null));
        tempList.add(new OxlaFunction("max", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP}, OxlaDataType.TIMESTAMP), null));
        tempList.add(new OxlaFunction("max", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME}, OxlaDataType.TIME), null));

        tempList.add(new OxlaFunction("avg", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("avg", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("avg", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("avg", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("avg", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME}, OxlaDataType.TIME), null));
        tempList.add(new OxlaFunction("avg", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), null));

        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.JSON}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME}, OxlaDataType.INT64), null));

        tempList.add(new OxlaFunction("bool_and", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), null));
        tempList.add(new OxlaFunction("bool_or", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), null));

        tempList.add(new OxlaFunction("mode", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), null));
        tempList.add(new OxlaFunction("mode", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE}, OxlaDataType.DATE), null));
        tempList.add(new OxlaFunction("mode", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null));
        tempList.add(new OxlaFunction("mode", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("mode", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT32), null));
        tempList.add(new OxlaFunction("mode", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("mode", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.TEXT), null));
        tempList.add(new OxlaFunction("mode", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ}, OxlaDataType.TIMESTAMPTZ), null));
        tempList.add(new OxlaFunction("mode", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP}, OxlaDataType.TIMESTAMP), null));
        tempList.add(new OxlaFunction("mode", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME}, OxlaDataType.TIME), null));

        tempList.add(new OxlaFunction("percentile_disc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), null));
        tempList.add(new OxlaFunction("percentile_disc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.DATE}, OxlaDataType.DATE), null));
        tempList.add(new OxlaFunction("percentile_disc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null));
        tempList.add(new OxlaFunction("percentile_disc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("percentile_disc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.INT32}, OxlaDataType.INT32), null));
        tempList.add(new OxlaFunction("percentile_disc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.INT64}, OxlaDataType.INT64), null));
        tempList.add(new OxlaFunction("percentile_disc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.TEXT}, OxlaDataType.TEXT), null));
        tempList.add(new OxlaFunction("percentile_disc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.TIMESTAMPTZ), null));
        tempList.add(new OxlaFunction("percentile_disc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.TIMESTAMP}, OxlaDataType.TIMESTAMP), null));
        tempList.add(new OxlaFunction("percentile_disc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.TIME}, OxlaDataType.TIME), null));

        tempList.add(new OxlaFunction("percentile_cont", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("percentile_cont", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("percentile_cont", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.INT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("percentile_cont", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.INT64}, OxlaDataType.FLOAT64), null));

        // TODO: FIXME
//        tempList.addAll(generateVariants("for_min", , , OxlaDataType.AGGREGABLE, new OxlaDataType[]{OxlaDataType.AGGREGABLE, OxlaDataType.ALL, OxlaDataType.ALL}, null, OxlaDataType.ALL  /*return type*/));
//        tempList.addAll(generateVariants("for_max", , , OxlaDataType.AGGREGABLE, new OxlaDataType[]{OxlaDataType.AGGREGABLE, OxlaDataType.ALL, OxlaDataType.ALL}, null, OxlaDataType.ALL  /*return type*/));

        tempList.add(new OxlaFunction("corr", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("covar_pop", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("covar_samp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("regr_avgx", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("regr_avgy", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("regr_count", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("regr_intercept", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("regr_r2", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("regr_slope", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("regr_sxx", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("regr_sxy", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("regr_syy", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev_pop", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev_pop", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev_pop", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev_pop", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev_samp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev_samp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev_samp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("stddev_samp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("var_pop", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("var_pop", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("var_pop", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("var_pop", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("var_samp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("var_samp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("var_samp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("var_samp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("variance", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("variance", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("variance", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.FLOAT64), null));
        tempList.add(new OxlaFunction("variance", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.FLOAT64), null));
        AGGREGATE = List.of(tempList.toArray(OxlaFunction[]::new));
    }
}
