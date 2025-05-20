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
            .addOneParamMatchReturnOverloads("abs", OxlaDataType.NUMERIC, null)
            .addOneParamMatchReturnOverloads("cbrt", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("ceil", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("ceiling", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverload("degrees", OxlaDataType.FLOAT64, null)
            .addOneParamMatchReturnOverload("exp", OxlaDataType.FLOAT64, null)
            .addOneParamMatchReturnOverloads("floor", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("round", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("sin", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("sind", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("asind", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("cos", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("cosd", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("acos", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("cot", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("cotd", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("acosd", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("radians", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("sqrt", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("ln", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("log10", OxlaDataType.FLOATING_POINT, null)
            .addMultipleParamOverload("log", new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32, null)
            .addMultipleParamOverload("log", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addOneParamMatchReturnOverloads("log", OxlaDataType.FLOATING_POINT, null)
            .addMultipleParamOverload("atan2", new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32, null)
            .addMultipleParamOverload("atan2", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("atan2d", new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT32}, OxlaDataType.FLOAT32, null)
            .addMultipleParamOverload("atan2d", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addOneParamMatchReturnOverloads("atan", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("atan2d", OxlaDataType.FLOATING_POINT, null)
            .addNoParamOverload("pi", OxlaDataType.FLOAT64, null)
            .addOneParamMatchReturnOverloads("tan", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("tand", OxlaDataType.FLOATING_POINT, null)
            .addOneParamMatchReturnOverloads("power", OxlaDataType.NUMERIC, null)
            .addOneParamMatchReturnOverloads("sign", OxlaDataType.NUMERIC, null)
            .build();

    public static final List<OxlaFunction> STRING = OxlaFunctionBuilder.create()
            .addMultipleParamOverload("concat", OxlaDataType.ALL, OxlaDataType.TEXT, null)
            .addOneParamOverload("length", OxlaDataType.TEXT, OxlaDataType.INT32, null)
            .addOneParamOverload("lower", OxlaDataType.TEXT, OxlaDataType.TEXT, null)
            .addOneParamOverload("upper", OxlaDataType.TEXT, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("replace", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("starts_with", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("substr", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32}, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("substr", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("substring", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32}, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("substring", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("strpos", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.INT32, null)
            .addMultipleParamOverload("position", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.INT32, null)
            .addMultipleParamOverload("regexp_replace", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("regexp_replace", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.TEXT, null)
            .build();

    public static final List<OxlaFunction> PG_FUNCTIONS = OxlaFunctionBuilder.create()
            .addNoParamOverload("pg_backend_pid", OxlaDataType.INT32, null)
            .addMultipleParamOverload("pg_get_expr", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32}, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("pg_get_expr", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32, OxlaDataType.BOOLEAN}, OxlaDataType.TEXT, null)
            .addOneParamOverloads("pg_total_relation_size", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32}, OxlaDataType.INT64, null)
            .addOneParamOverloads("pg_table_size", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32}, OxlaDataType.INT64, null)
            .addOneParamOverload("pg_encoding_to_char", OxlaDataType.INT32, OxlaDataType.TEXT, null)
            .addOneParamOverload("pg_size_pretty", OxlaDataType.INT64, OxlaDataType.TEXT, null)
            .addOneParamOverload("pg_get_userbyid", OxlaDataType.INT64, OxlaDataType.TEXT, null)
            .addOneParamOverload("pg_get_indexdef", OxlaDataType.INT32, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("pg_get_indexdef", new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32, OxlaDataType.BOOLEAN}, OxlaDataType.TEXT, null)
            .addOneParamOverload("pg_table_is_visible", OxlaDataType.INT32, OxlaDataType.BOOLEAN, null)
            .addOneParamOverloads("pg_typeof", OxlaDataType.ALL, OxlaDataType.TEXT, null)
            .addOneParamOverload("pg_get_constraintdef", OxlaDataType.INT32, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("pg_get_constraintdef", new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.BOOLEAN}, OxlaDataType.TEXT, null)
            .addOneParamOverload("pg_get_statisticsobjdef_c", OxlaDataType.INT32, OxlaDataType.TEXT, null)
            .addOneParamOverloads("pg_relation_is_publishable", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INT32}, OxlaDataType.BOOLEAN, null)
            .build();

    public static final List<OxlaFunction> SYSTEM = OxlaFunctionBuilder.create()
            .addNoParamOverload("current_database", OxlaDataType.TEXT, null)
            .addNoParamOverload("current_schema", OxlaDataType.TEXT, null)
            .addNoParamOverload("version", OxlaDataType.TEXT, null)
            .addOneParamMatchReturnOverload("quote_ident", OxlaDataType.TEXT, null)
            .addMultipleParamOverload("format_type", new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.TEXT, null)
            .addTwoParamMatrixOverloads("to_char", new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT64, OxlaDataType.FLOAT32, OxlaDataType.FLOAT64, OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMPTZ}, new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.INTERVAL}, false, null)
            .addMultipleParamOverload("obj_description", new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.TEXT}, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("shobj_description", new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.TEXT}, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("col_description", new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.INT32}, OxlaDataType.TEXT, null)
            .addMultipleParamOverload("has_schema_privilege", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN, null)
            .addMultipleParamOverload("has_schema_privilege", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN, null)
            .addMultipleParamOverload("has_database_privilege", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN, null)
            .addMultipleParamOverload("has_database_privilege", new OxlaDataType[]{OxlaDataType.TEXT, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN, null)
            .addMultipleParamOverload("has_database_privilege", new OxlaDataType[]{OxlaDataType.INT32, OxlaDataType.TEXT, OxlaDataType.TEXT}, OxlaDataType.BOOLEAN, null)
            .build();

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
            new OxlaFunction("nullif", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.TIMESTAMP, OxlaDataType.TIMESTAMP}, OxlaDataType.TIMESTAMP), null)
    );

    public static final List<OxlaFunction> WINDOW = OxlaFunctionBuilder.create()
            .addNoParamOverload("row_number", OxlaDataType.INT64, null)
            .addNoParamOverload("rank", OxlaDataType.INT64, null)
            .addNoParamOverload("dense_rank", OxlaDataType.INT64, null)
            .addNoParamOverload("percent_rank", OxlaDataType.FLOAT64, null)
            .addNoParamOverload("cume_dist", OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("ntile", new OxlaDataType[]{OxlaDataType.INT32}, OxlaDataType.INT32, null)
            .addOneParamMatchReturnOverloads("lag", OxlaDataType.ALL, null)
            .addTwoParamMatrixOverloads("lag", OxlaDataType.ALL, new OxlaDataType[]{OxlaDataType.INT32}, true, null)
//TODO new OxlaFunction("lag", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.ALL(v), OxlaDataType.INT32, OxlaDataType.ALL(v)}, OxlaDataType.ALL(v)), null),
            .addOneParamMatchReturnOverloads("lead", OxlaDataType.ALL, null)
//TODO new OxlaFunction("lead", new OxlaTypeOverload(new OxlaDataType[]{OxlaDataType.ALL(v), OxlaDataType.INT32, OxlaDataType.ALL(v)}, OxlaDataType.ALL(v)), null),
            .addTwoParamMatrixOverloads("lead", OxlaDataType.ALL, new OxlaDataType[]{OxlaDataType.INT32}, true, null)
            .addOneParamMatchReturnOverloads("first_value", OxlaDataType.ALL, null)
            .addOneParamMatchReturnOverloads("last_value", OxlaDataType.ALL, null)
            .addTwoParamMatrixOverloads("nth_value", OxlaDataType.ALL, new OxlaDataType[]{OxlaDataType.INT32}, true, null)
            .build();


    public static final List<OxlaFunction> AGGREGATE = OxlaFunctionBuilder.create()
            .addOneParamMatchReturnOverloads("sum", new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64, OxlaDataType.INT32, OxlaDataType.INT64, OxlaDataType.INTERVAL, OxlaDataType.TIME}, null)
            .addOneParamMatchReturnOverloads("min", new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.FLOAT32, OxlaDataType.FLOAT64, OxlaDataType.INT32, OxlaDataType.INT64, OxlaDataType.INTERVAL, OxlaDataType.TEXT, OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP, OxlaDataType.TIME}, null)
            .addOneParamMatchReturnOverloads("max", new OxlaDataType[]{OxlaDataType.DATE, OxlaDataType.FLOAT32, OxlaDataType.FLOAT64, OxlaDataType.INT32, OxlaDataType.INT64, OxlaDataType.INTERVAL, OxlaDataType.TEXT, OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP, OxlaDataType.TIME}, null)
            .addOneParamMatchReturnOverloads("avg", new OxlaDataType[]{OxlaDataType.FLOAT32, OxlaDataType.FLOAT64, OxlaDataType.INT32, OxlaDataType.INT64, OxlaDataType.INTERVAL, OxlaDataType.TIME}, null)
            .addOneParamOverloads("count", OxlaDataType.ALL, OxlaDataType.INT64, null)
            .addOneParamMatchReturnOverloads("bool_and", new OxlaDataType[]{OxlaDataType.BOOLEAN}, null)
            .addOneParamMatchReturnOverloads("bool_or", new OxlaDataType[]{OxlaDataType.BOOLEAN}, null)
            .addOneParamMatchReturnOverloads("mode", new OxlaDataType[]{OxlaDataType.BOOLEAN, OxlaDataType.DATE, OxlaDataType.FLOAT32, OxlaDataType.FLOAT64, OxlaDataType.INT32, OxlaDataType.INT64, OxlaDataType.TEXT, OxlaDataType.TIMESTAMPTZ, OxlaDataType.TIMESTAMP, OxlaDataType.TIME}, null)
            .addTwoParamMatrixOverloads("percentile_disc", new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.COMPARABLE_WITHOUT_INTERVAL, false, null)
            .addTwoParamMatrixOverloads("percentile_cont", new OxlaDataType[]{OxlaDataType.FLOAT64}, OxlaDataType.NUMERIC, true, null)
            .addTwoParamMatrixOverloads("for_min", OxlaDataType.AGGREGABLE, OxlaDataType.ALL, false, null)
            .addTwoParamMatrixOverloads("for_max", OxlaDataType.AGGREGABLE, OxlaDataType.ALL, false, null)
            .addMultipleParamOverload("corr", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("covar_pop", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("covar_samp", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("regr_avgx", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("regr_avgy", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("regr_count", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("regr_intercept", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("regr_r2", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("regr_slope", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("regr_sxx", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("regr_sxy", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addMultipleParamOverload("regr_syy", new OxlaDataType[]{OxlaDataType.FLOAT64, OxlaDataType.FLOAT64}, OxlaDataType.FLOAT64, null)
            .addOneParamMatchReturnOverloads("stddev", OxlaDataType.NUMERIC, null)
            .addOneParamMatchReturnOverloads("stddev_pop", OxlaDataType.NUMERIC, null)
            .addOneParamMatchReturnOverloads("stddev_samp", OxlaDataType.NUMERIC, null)
            .addOneParamMatchReturnOverloads("var_pop", OxlaDataType.NUMERIC, null)
            .addOneParamMatchReturnOverloads("var_samp", OxlaDataType.NUMERIC, null)
            .addOneParamMatchReturnOverloads("variance", OxlaDataType.NUMERIC, null)
            .build();

    public static class OxlaFunctionBuilder {
        private final List<OxlaFunction> overloads = new ArrayList<>();

        public static OxlaFunctionBuilder create() {
            return new OxlaFunctionBuilder();
        }

        /**
         * Creates a new function overload that takes no parameters and return specific function type.
         */
        public OxlaFunctionBuilder addNoParamOverload(String textRepresentation, OxlaDataType returnType, OxlaApplyFunction applyFunction) {
            overloads.add(new OxlaFunction(textRepresentation, new OxlaTypeOverload(new OxlaDataType[]{}, returnType), applyFunction));
            return this;
        }

        public OxlaFunctionBuilder addMultipleParamOverload(String textRepresentation, OxlaDataType[] inputParams, OxlaDataType returnType, OxlaApplyFunction applyFunction) {
            overloads.add(new OxlaFunction(textRepresentation, new OxlaTypeOverload(inputParams, returnType), applyFunction));
            return this;
        }

        public OxlaFunctionBuilder addOneParamOverload(String textRepresentation, OxlaDataType inputParam, OxlaDataType returnType, OxlaApplyFunction applyFunction) {
            overloads.add(new OxlaFunction(textRepresentation, new OxlaTypeOverload(new OxlaDataType[]{inputParam}, returnType), applyFunction));
            return this;
        }

        public OxlaFunctionBuilder addOneParamOverloads(String textRepresentation, OxlaDataType[] types, OxlaDataType returnType, OxlaApplyFunction applyFunction) {
            for (OxlaDataType type : types) {
                overloads.add(new OxlaFunction(textRepresentation, new OxlaTypeOverload(new OxlaDataType[]{type}, returnType), applyFunction));
            }
            return this;
        }

        public OxlaFunctionBuilder addOneParamMatchReturnOverloads(String textRepresentation, OxlaDataType[] types, OxlaApplyFunction applyFunction) {
            for (OxlaDataType type : types) {
                overloads.add(new OxlaFunction(textRepresentation, new OxlaTypeOverload(new OxlaDataType[]{type}, type), applyFunction));
            }
            return this;
        }

        public OxlaFunctionBuilder addOneParamMatchReturnOverload(String textRepresentation, OxlaDataType type, OxlaApplyFunction applyFunction) {
            overloads.add(new OxlaFunction(textRepresentation, new OxlaTypeOverload(new OxlaDataType[]{type}, type), applyFunction));
            return this;
        }

        public OxlaFunctionBuilder addTwoParamMatrixOverloads(String textRepresentation, OxlaDataType[] firstParam, OxlaDataType[] secondParam, boolean isFirstParamReturnType, OxlaApplyFunction applyFunction) {
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
