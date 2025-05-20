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

    public static List<OxlaFunction> MATH = OxlaFunctionBuilder.create()
            .addOneParamSameReturn("abs", OxlaDataType.NUMERIC, null)
            .addOneParamSameReturn("cbrt", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("ceil", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("ceiling", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("degrees", OxlaDataType.FLOAT64, null)
            .addOneParamSameReturn("exp", OxlaDataType.FLOAT64, null)
            .addOneParamSameReturn("floor", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("round", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("sin", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("sind", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("asind", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("cos", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("cosd", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("acos", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("cot", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("cotd", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("acosd", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("radians", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("sqrt", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("ln", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("log10", OxlaDataType.FLOATING_POINT, null)
//            new OxlaFunction("log", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
//            new OxlaFunction("log", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
//            new OxlaFunction("log", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
//            new OxlaFunction("log", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
//            new OxlaFunction("atan2", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
//            new OxlaFunction("atan2", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
//            new OxlaFunction("atan2d", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32), null),
//            new OxlaFunction("atan2d", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64), null),
            .addOneParamSameReturn("atan", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("atan2d", OxlaDataType.FLOATING_POINT, null)
            .addSimpleNoParam("pi", OxlaDataType.FLOAT64, null)
            .addOneParamSameReturn("tan", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("tand", OxlaDataType.FLOATING_POINT, null)
            .addOneParamSameReturn("power", OxlaDataType.NUMERIC, null)
            .addOneParamSameReturn("sign", OxlaDataType.NUMERIC, null)
            .build();

    public static final List<OxlaFunction> STRING = OxlaFunctionBuilder.create()
            .addSimple("concat", OxlaDataType.ALL, OxlaDataType.TEXT, null)
            .addSimple("length", OxlaDataType.TEXT, OxlaDataType.INT32, null)
            .addSimple("lower", OxlaDataType.TEXT, OxlaDataType.TEXT, null)
            .addSimple("upper", OxlaDataType.TEXT, OxlaDataType.TEXT, null)
            .addSimple("replace", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT, null)
            .addSimple("starts_with", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT, null)
            .addSimple("substr", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32}, OxlaDataType.TEXT, null)
            .addSimple("substr", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.TEXT, null)
            .addSimple("substring", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32}, OxlaDataType.TEXT, null)
            .addSimple("substring", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.TEXT, null)
            .addSimple("strpos", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.INT32, null)
            .addSimple("position", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.INT32, null)
            .addSimple("regexp_replace", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT, null)
            .addSimple("regexp_replace", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT, null)
            .build();

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

    public static final List<OxlaFunction> SYSTEM = List.of(
            new OxlaFunction("current_database", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.TEXT), null),
            new OxlaFunction("current_schema", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.TEXT), null),
            new OxlaFunction("version", new OxlaTypeOverload(new OxlaDataType[]{}, OxlaDataType.TEXT), null)
    );

    public static final List<OxlaFunction> MISC = List.of(
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
            new OxlaFunction("col_description", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.TEXT), null)
    );

    public static final List<OxlaFunction> WINDOW = OxlaFunctionBuilder.create()
            .addSimpleNoParam("row_number", OxlaDataType.INT64, null)
            .addSimpleNoParam("rank", OxlaDataType.INT64, null)
            .addSimpleNoParam("dense_rank", OxlaDataType.INT64, null)
            .addSimpleNoParam("percent_rank", OxlaDataType.FLOAT64, null)
            .addSimpleNoParam("cume_dist", OxlaDataType.FLOAT64, null)
            .addSimple("ntile", new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT32, null)
            .build();

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

    public static final List<OxlaFunction> AGGREGATE = OxlaFunctionBuilder.create()
            .addOneParamSameReturn("sum", new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64, OxlaDataType.INT32, OxlaDataType.INT64, OxlaDataType.INTERVAL, OxlaDataType.TIME}, null)
            .addOneParamSameReturn("min", new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.FLOAT32, OxlaDataType.FLOAT64, OxlaDataType.INT32, OxlaDataType.INT64, OxlaDataType.INTERVAL, OxlaDataType.TEXT, OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP, OxlaDataType.TIME}, null)
            .addOneParamSameReturn("max", new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.FLOAT32, OxlaDataType.FLOAT64, OxlaDataType.INT32, OxlaDataType.INT64, OxlaDataType.INTERVAL, OxlaDataType.TEXT, OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP, OxlaDataType.TIME}, null)
            .addOneParamSameReturn("avg", new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64, OxlaDataType.INT32, OxlaDataType.INT64, OxlaDataType.INTERVAL, OxlaDataType.TIME}, null)
            .addSingleParamSpecificReturn("count", OxlaDataType.ALL, OxlaDataType.INT64, null)
            .addOneParamSameReturn("bool_and", new OxlaDataType[]{OxlaDataType.BOOLEAN}, null)
            .addOneParamSameReturn("bool_or", new OxlaDataType[]{OxlaDataType.BOOLEAN}, null)
            .addOneParamSameReturn("mode", new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.DATE, OxlaDataType.FLOAT32, OxlaDataType.FLOAT64, OxlaDataType.INT32, OxlaDataType.INT64, OxlaDataType.TEXT, OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP, OxlaDataType.TIME}, null)
            .addTwoParamMatrix("percentile_disc", new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.COMPARABLE_WITHOUT_INTERVAL, false, null)
            .addTwoParamMatrix("percentile_cont", new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.NUMERIC, true, null)
            .addTwoParamMatrix("for_min", OxlaDataType.AGGREGABLE, OxlaDataType.ALL, false, null)
            .addTwoParamMatrix("for_max", OxlaDataType.AGGREGABLE, OxlaDataType.ALL, false, null)
            .addSimple("corr", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addSimple("covar_pop", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addSimple("covar_samp", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addSimple("regr_avgx", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addSimple("regr_avgy", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addSimple("regr_count", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addSimple("regr_intercept", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addSimple("regr_r2", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addSimple("regr_slope", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addSimple("regr_sxx", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addSimple("regr_sxy", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addSimple("regr_syy", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addOneParamSameReturn("stddev", OxlaDataType.NUMERIC, null)
            .addOneParamSameReturn("stddev_pop", OxlaDataType.NUMERIC, null)
            .addOneParamSameReturn("stddev_samp", OxlaDataType.NUMERIC, null)
            .addOneParamSameReturn("var_pop", OxlaDataType.NUMERIC, null)
            .addOneParamSameReturn("var_samp", OxlaDataType.NUMERIC, null)
            .addOneParamSameReturn("variance", OxlaDataType.NUMERIC, null)
            .build();

    public static class OxlaFunctionBuilder {
        private final List<OxlaFunction> overloads = new ArrayList<>();

        public static OxlaFunctionBuilder create() {
            return new OxlaFunctionBuilder();
        }

        public OxlaFunctionBuilder addSimple(String textRepresentation, OxlaDataType[] inputParams, OxlaDataType returnType, OxlaApplyFunction applyFunction) {
            overloads.add(new OxlaFunction(textRepresentation, new OxlaTypeOverload(inputParams, returnType), applyFunction));
            return this;
        }

        public OxlaFunctionBuilder addSimple(String textRepresentation, OxlaDataType inputParam, OxlaDataType returnType, OxlaApplyFunction applyFunction) {
            overloads.add(new OxlaFunction(textRepresentation, new OxlaTypeOverload(new OxlaDataType[]{inputParam}, returnType), applyFunction));
            return this;
        }

        public OxlaFunctionBuilder addSimpleNoParam(String textRepresentation, OxlaDataType returnType, OxlaApplyFunction applyFunction) {
            return addSimple(textRepresentation, new OxlaDataType[]{}, returnType, applyFunction);
        }

        public OxlaFunctionBuilder addOneParamSameReturn(String textRepresentation, OxlaDataType[] types, OxlaApplyFunction applyFunction) {
            for (OxlaDataType type : types) {
                overloads.add(new OxlaFunction(textRepresentation, new OxlaTypeOverload(new OxlaDataType[]{type}, type), applyFunction));
            }
            return this;
        }

        public OxlaFunctionBuilder addOneParamSameReturn(String textRepresentation, OxlaDataType type, OxlaApplyFunction applyFunction) {
            overloads.add(new OxlaFunction(textRepresentation, new OxlaTypeOverload(new OxlaDataType[]{type}, type), applyFunction));
            return this;
        }

        public OxlaFunctionBuilder addSingleParamSpecificReturn(String textRepresentation, OxlaDataType[] types, OxlaDataType returnType, OxlaApplyFunction applyFunction) {
            for (OxlaDataType type : types) {
                overloads.add(new OxlaFunction(textRepresentation, new OxlaTypeOverload(new OxlaDataType[]{type}, returnType), applyFunction));
            }
            return this;
        }

        public OxlaFunctionBuilder addTwoParamMatrix(String textRepresentation, OxlaDataType[] firstParam, OxlaDataType[] secondParam, boolean isFirstParamReturnType, OxlaApplyFunction applyFunction) {
            for (OxlaDataType firstType : firstParam) {
                for (OxlaDataType secondType : secondParam) {
                    OxlaDataType returnType = isFirstParamReturnType ? firstType : secondType;
                    OxlaTypeOverload overload = new OxlaTypeOverload(new OxlaDataType[]{firstType, secondType}, returnType);
                    overloads.add(new OxlaFunction(textRepresentation, overload, applyFunction));
                }
            }
            return this;
        }

        public List<OxlaFunction> build() {
            return List.of(overloads.toArray(OxlaFunction[]::new));
        }
    }
}
