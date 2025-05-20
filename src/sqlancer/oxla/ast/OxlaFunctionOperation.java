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

    public static final List<OxlaFunction> PG_FUNCTIONS = List.of(

            new OxlaFunction("pg_backend_pid", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.INT32), null),
            new OxlaFunction("pg_get_expr", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_get_expr", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32, OxlaDataType.BOOLEAN}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_total_relation_size", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.INT64), null),
            new OxlaFunction("pg_total_relation_size", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT64), null),
            new OxlaFunction("pg_table_size", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.INT64), null),
            new OxlaFunction("pg_table_size", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT64), null),
            new OxlaFunction("pg_encoding_to_char", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_size_pretty", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_get_userbyid", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_get_indexdef", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_get_indexdef", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.BOOLEAN}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_table_is_visible", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.BOOLEAN), null),

            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.DATE}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.JSON}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIME}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_typeof", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ}, OxlaDataType.TEXT), null),

            new OxlaFunction("pg_get_constraintdef", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_get_constraintdef", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.BOOLEAN}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_get_statisticsobjdef_c", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("pg_relation_is_publishable", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), null),
            new OxlaFunction("pg_relation_is_publishable", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.BOOLEAN), null)
    );

    public static final List<OxlaFunction> MISC = List.of(
            new OxlaFunction("current_database", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.TEXT), null),
            new OxlaFunction("current_schema", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.TEXT), null),
            new OxlaFunction("version", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.TEXT), null),

            new OxlaFunction("date_trunc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TIMESTAMP}, OxlaDataType.TIMESTAMP), null),
            new OxlaFunction("date_trunc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.TIMESTAMPTZ), null),
            new OxlaFunction("date_trunc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), null),
            new OxlaFunction("format_timestamp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TIMESTAMP}, OxlaDataType.TEXT), null),
            new OxlaFunction("unix_seconds", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP}, OxlaDataType.INT64), null),
            new OxlaFunction("unix_seconds", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ}, OxlaDataType.INT64), null),
            new OxlaFunction("unix_millis", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP}, OxlaDataType.INT64), null),
            new OxlaFunction("unix_millis", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ}, OxlaDataType.INT64), null),
            new OxlaFunction("unix_micros", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP}, OxlaDataType.INT64), null),
            new OxlaFunction("unix_micros", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ}, OxlaDataType.INT64), null),
            new OxlaFunction("timestamp_seconds", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.TIMESTAMP), null),
            new OxlaFunction("timestamp_millis", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.TIMESTAMP), null),
            new OxlaFunction("timestamp_micros", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64}, OxlaDataType.TIMESTAMP), null),
            new OxlaFunction("timestamp_trunc", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.INT32}, OxlaDataType.TIMESTAMP), null),
            new OxlaFunction("to_timestamp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.TIMESTAMPTZ), null),
            new OxlaFunction("to_timestamp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TIMESTAMPTZ), null),
            new OxlaFunction("make_date", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.DATE), null),
            new OxlaFunction("make_interval", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.INTERVAL), null),
            new OxlaFunction("make_interval", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INTERVAL), null),
            new OxlaFunction("make_interval", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INTERVAL), null),
            new OxlaFunction("make_interval", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INTERVAL), null),
            new OxlaFunction("make_interval", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INTERVAL), null),
            new OxlaFunction("make_interval", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INTERVAL), null),
            new OxlaFunction("make_interval", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INTERVAL), null),
            new OxlaFunction("make_interval", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.FLOAT64}, OxlaDataType.INTERVAL), null),
            new OxlaFunction("make_time", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.FLOAT64}, OxlaDataType.TIME), null),
            new OxlaFunction("make_timestamp", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.FLOAT64}, OxlaDataType.TIMESTAMP), null),
            new OxlaFunction("make_timestamptz", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.FLOAT64}, OxlaDataType.TIMESTAMPTZ), null),
            new OxlaFunction("make_timestamptz", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.FLOAT64, OxlaDataType.TEXT}, OxlaDataType.TIMESTAMPTZ), null),
            new OxlaFunction("date", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.DATE), null),
            new OxlaFunction("date", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP}, OxlaDataType.DATE), null),
            new OxlaFunction("date", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ}, OxlaDataType.DATE), null),
            new OxlaFunction("json_extract_path", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.JSON, OxlaDataType.TEXT}, OxlaDataType.JSON), null),
            new OxlaFunction("json_extract_path_text", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.JSON, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("json_array_length", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.JSON}, OxlaDataType.INT32), null),
            new OxlaFunction("json_array_extract", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.JSON, OxlaDataType.INT32}, OxlaDataType.JSON), null),
            new OxlaFunction("json_array_extract", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.JSON, OxlaDataType.INT64}, OxlaDataType.JSON), null),

            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.BOOLEAN, OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), null),
            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.DATE, OxlaDataType.DATE}, OxlaDataType.DATE), null),
            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INT32), null),
            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.INT64), null),
            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.INTERVAL, OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), null),
            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.JSON, OxlaDataType.JSON}, OxlaDataType.JSON), null),
            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.TIME, OxlaDataType.TIME}, OxlaDataType.TIME), null),
            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMP}, OxlaDataType.TIMESTAMP), null),
            new OxlaFunction("if", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.TIMESTAMPTZ), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.BOOLEAN, OxlaDataType.BOOLEAN}, OxlaDataType.BOOLEAN), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.DATE, OxlaDataType.DATE}, OxlaDataType.DATE), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.INT32), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.INT64, OxlaDataType.INT64}, OxlaDataType.INT64), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.INTERVAL, OxlaDataType.INTERVAL}, OxlaDataType.INTERVAL), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.JSON, OxlaDataType.JSON}, OxlaDataType.JSON), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.TIME, OxlaDataType.TIME}, OxlaDataType.TIME), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMP}, OxlaDataType.TIMESTAMP), null),
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMPTZ}, OxlaDataType.TIMESTAMPTZ), null),

            // privileges
            new OxlaFunction("has_schema_privilege", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), null),
            new OxlaFunction("has_schema_privilege", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), null),
            new OxlaFunction("has_database_privilege", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), null),
            new OxlaFunction("has_database_privilege", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), null),
            new OxlaFunction("has_database_privilege", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN), null),

            // postgres system functions other than pg_*
            new OxlaFunction("quote_ident", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("format_type", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.TEXT), null),
            new OxlaFunction("to_char", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("to_char", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT64, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("to_char", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("to_char", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("to_char", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMP, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("to_char", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.TIMESTAMPTZ, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("to_char", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INTERVAL, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("obj_description", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("shobj_description", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.TEXT}, OxlaDataType.TEXT), null),
            new OxlaFunction("col_description", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.TEXT), null),

            // window functions
            new OxlaFunction("row_number", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.INT64), null),
            new OxlaFunction("rank", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.INT64), null),
            new OxlaFunction("dense_rank", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.INT64), null),
            new OxlaFunction("percent_rank", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("cume_dist", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.FLOAT64), null),
            new OxlaFunction("ntile", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT32), null)

// TODO ANY!
//new OxlaFunction("lag", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.}, OxlaDataType., null),
//new OxlaFunction("lag", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType., OxlaDataType.INT32}, OxlaDataType.), null),
//new OxlaFunction("lag", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType., OxlaDataType.INT32, OxlaDataType.}, OxlaDataType.), null),
//new OxlaFunction("lead", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.}, OxlaDataType.), null),
//new OxlaFunction("lead", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType., OxlaDataType.INT32}, OxlaDataType.), null),
//new OxlaFunction("lead", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType., OxlaDataType.INT32, OxlaDataType.}, OxlaDataType.), null),
//new OxlaFunction("first_value", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.}, OxlaDataType.), null),
//new OxlaFunction("last_value", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.}, OxlaDataType.), null),
//new OxlaFunction("nth_value", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType., OxlaDataType.INT32}, OxlaDataType.), null)
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
