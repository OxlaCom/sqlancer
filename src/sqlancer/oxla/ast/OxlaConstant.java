package sqlancer.oxla.ast;

import sqlancer.IgnoreMeException;
import sqlancer.Randomly;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.schema.OxlaDataType;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class OxlaConstant implements OxlaExpression {
    private OxlaConstant() {
    }

    /**
     * @param toType Data type to cast the OxlaConstant to.
     * @return OxlaConstant of a given type if successful, nullptr if the cast couldn't be performed.
     */
    public abstract OxlaConstant tryCast(OxlaDataType toType);

    /**
     * @param toType Data type to compare the OxlaConstant to.
     * @return -1, 0 or 1 depending on if the toType is less, equal or greater than this type.
     */
    public abstract int compareTo(OxlaConstant toType);

    /** Returns the constant as a string (possibly) without a type coercion prefix. */
    public String asPlainLiteral() {
        return toString();
    }

    @Override
    public OxlaConstant getExpectedValue() {
        return this;
    }

    public static OxlaConstant getRandomForType(OxlaGlobalState state, OxlaDataType type) {
        final Randomly randomly = state.getRandomly();
        return switch (type) {
            case BOOLEAN -> createBooleanConstant(Randomly.getBoolean());
            case DATE -> createDateConstant(randomly.getInteger32());
            case FLOAT32 -> createFloat32Constant(randomly.getFloat());
            case FLOAT64 -> createFloat64Constant(randomly.getDouble());
            case INT32 -> createInt32Constant(randomly.getInteger32());
            case INT64 -> createInt64Constant(randomly.getLong());
            case INTERVAL -> createIntervalConstant(randomly.getInteger32(), randomly.getInteger32(), randomly.getLong());
            case JSON -> createJsonConstant(randomly.getString());
            case TEXT -> createTextConstant(randomly.getString());
            case TIME -> createTimeConstant(randomly.getInteger32());
            case TIMESTAMP -> createTimestampConstant(randomly.getLong());
            case TIMESTAMPTZ -> createTimestamptzConstant(randomly.getLong());
            default -> throw new AssertionError(type);
        };
    }

    public static OxlaConstant getRandom(OxlaGlobalState state) {
        return getRandomForType(state, OxlaDataType.getRandomType());
    }

    public static OxlaConstant createNullConstant() {
        return new OxlaNullConstant();
    }

    public static OxlaConstant createBooleanConstant(boolean value) {
        return new OxlaBooleanConstant(value);
    }

    public static OxlaConstant createDateConstant(int value) {
        return new OxlaDateConstant(value);
    }

    public static OxlaConstant createFloat32Constant(float value) {
        return new OxlaFloat32Constant(value);
    }

    public static OxlaConstant createFloat64Constant(double value) {
        return new OxlaFloat64Constant(value);
    }

    public static OxlaConstant createInt32Constant(int value) {
        return new OxlaInt32Constant(value);
    }

    public static OxlaConstant createInt64Constant(long value) {
        return new OxlaInt64Constant(value);
    }

    public static OxlaConstant createIntervalConstant(int months, int days, long microseconds) {
        return new OxlaIntervalConstant(months, days, microseconds);
    }

    public static OxlaConstant createJsonConstant(String json) {
        return new OxlaJsonConstant(json);
    }

    public static OxlaConstant createTextConstant(String text) {
        return new OxlaTextConstant(text);
    }

    public static OxlaConstant createTimeConstant(int value) {
        return new OxlaTimeConstant(value);
    }

    public static OxlaConstant createTimestampConstant(long value) {
        return new OxlaTimestampConstant(value);
    }

    public static OxlaConstant createTimestamptzConstant(long value) {
        return new OxlaTimestamptzConstant(value);
    }

    //
    //
    //

    public static class OxlaNullConstant extends OxlaConstant {
        @Override
        public String toString() {
            return "NULL";
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType unused) {
            return createNullConstant(); // No-op.
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            return 0; // NOTE: NULL is less, equal and greater than any type.
        }
    }

    public static class OxlaBooleanConstant extends OxlaConstant {
        public final boolean value;

        public OxlaBooleanConstant(boolean value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            return switch (toType) {
                case BOOLEAN -> this;
                case INT32 -> createInt32Constant(value ? 1 : 0);
                default -> null; // Impossible.
            };
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaBooleanConstant) {
                return Boolean.compare(this.value, ((OxlaBooleanConstant) toType).value);
            }
            throw new AssertionError(String.format("OxlaBooleanConstant::compareTo not implemented for type: %s", toType.getClass()));
        }
    }

    public static class OxlaDateConstant extends OxlaConstant {
        public final int value;

        public OxlaDateConstant(int value) {
            this.value = value;
        }

        @Override
        public String asPlainLiteral() {
            return String.format("'%s'", new SimpleDateFormat("yyyy-MM-dd").format(new Timestamp(value)));
        }

        @Override
        public String toString() {
            return String.format("DATE %s", asPlainLiteral());
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            return switch (toType) {
                case DATE -> this;
                case TIMESTAMP -> OxlaTimestamptzConstant.createTimestampConstant(value);
                case TIMESTAMPTZ -> OxlaTimestamptzConstant.createTimestamptzConstant(value);
                default -> null; // Impossible.
            };
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaDateConstant) {
                return Integer.compare(this.value, ((OxlaDateConstant) toType).value);
            } else if (toType instanceof OxlaTimestampConstant) {
                OxlaConstant thisType = tryCast(OxlaDataType.TIMESTAMP);
                return thisType.compareTo(toType);
            }
            throw new AssertionError(String.format("OxlaDateConstant::compareTo not implemented for type: %s", toType.getClass()));
        }
    }

    public static class OxlaFloat32Constant extends OxlaConstant {
        public final float value;

        public OxlaFloat32Constant(float value) {
            this.value = value;
        }

        @Override
        public String asPlainLiteral() {
            return String.valueOf(value);
        }

        @Override
        public String toString() {
            if (value == Float.POSITIVE_INFINITY) {
                return "'infinity'::FLOAT4";
            } else if (value == Float.NEGATIVE_INFINITY) {
                return "'-infinity'::FLOAT4";
            }
            return String.valueOf(value);
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            return switch (toType) {
                case FLOAT32 -> this;
                case FLOAT64 -> OxlaConstant.createFloat64Constant(value);
                case INT32 -> OxlaConstant.createInt32Constant((int) value);
                case INT64 -> OxlaConstant.createInt64Constant((long) value);
                case TEXT -> OxlaConstant.createTextConstant(toString());
                default -> null; // Impossible.
            };
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaInt32Constant) {
                return Float.compare(this.value, ((OxlaInt32Constant) toType).value);
            } else if (toType instanceof OxlaInt64Constant) {
                return Float.compare(this.value, ((OxlaInt64Constant) toType).value);
            } else if (toType instanceof OxlaFloat32Constant) {
                return Float.compare(this.value, ((OxlaFloat32Constant) toType).value);
            } else if (toType instanceof OxlaFloat64Constant) {
                return Float.compare(this.value, (float) ((OxlaFloat64Constant) toType).value);
            }
            throw new AssertionError(String.format("OxlaFloat64Constant::compareTo not implemented for type: %s", toType.getClass()));
        }
    }

    public static class OxlaFloat64Constant extends OxlaConstant {
        public final double value;

        public OxlaFloat64Constant(double value) {
            this.value = value;
        }

        @Override
        public String asPlainLiteral() {
            return String.valueOf(value);
        }

        @Override
        public String toString() {
            if (value == Double.POSITIVE_INFINITY) {
                return "'infinity'::FLOAT8";
            } else if (value == Double.NEGATIVE_INFINITY) {
                return "'-infinity'::FLOAT8";
            }
            return String.valueOf(value);
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            return switch (toType) {
                case FLOAT32 -> OxlaConstant.createFloat32Constant((float) value);
                case FLOAT64 -> this;
                case INT32 -> OxlaConstant.createInt32Constant((int) value);
                case INT64 -> OxlaConstant.createInt64Constant((long) value);
                case TEXT -> OxlaConstant.createTextConstant(toString());
                default -> null; // Impossible.
            };
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaInt32Constant) {
                return Double.compare(this.value, ((OxlaInt32Constant) toType).value);
            } else if (toType instanceof OxlaInt64Constant) {
                return Double.compare(this.value, ((OxlaInt64Constant) toType).value);
            } else if (toType instanceof OxlaFloat32Constant) {
                return Double.compare(this.value, ((OxlaFloat32Constant) toType).value);
            } else if (toType instanceof OxlaFloat64Constant) {
                return Double.compare(this.value, ((OxlaFloat64Constant) toType).value);
            }
            throw new AssertionError(String.format("OxlaFloat64Constant::compareTo not implemented for type: %s", toType.getClass()));
        }
    }

    public static class OxlaInt32Constant extends OxlaConstant {
        public final int value;

        public OxlaInt32Constant(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            return switch (toType) {
                case BOOLEAN -> OxlaConstant.createBooleanConstant(value != 0);
                case FLOAT32 -> OxlaConstant.createFloat32Constant(value);
                case FLOAT64 -> OxlaConstant.createFloat64Constant(value);
                case INT32 -> this;
                case INT64 -> OxlaConstant.createInt64Constant(value);
                case TEXT -> OxlaConstant.createTextConstant(toString());
                default -> null; // Impossible.
            };
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaInt32Constant) {
                return Long.compare(this.value, ((OxlaInt32Constant) toType).value);
            } else if (toType instanceof OxlaInt64Constant) {
                return Long.compare(this.value, ((OxlaInt64Constant) toType).value);
            } else if (toType instanceof OxlaFloat32Constant) {
                return Float.compare(this.value, ((OxlaFloat32Constant) toType).value);
            } else if (toType instanceof OxlaFloat64Constant) {
                return Double.compare(this.value, ((OxlaFloat64Constant) toType).value);
            }
            throw new AssertionError(String.format("OxlaInt32Constant::compareTo not implemented for type: %s", toType.getClass()));
        }
    }

    public static class OxlaInt64Constant extends OxlaConstant {
        public final long value;

        public OxlaInt64Constant(long value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            return switch (toType) {
                case BOOLEAN -> OxlaConstant.createBooleanConstant(value != 0);
                case FLOAT32 -> OxlaConstant.createFloat32Constant(value);
                case FLOAT64 -> OxlaConstant.createFloat64Constant(value);
                case INT32 -> OxlaConstant.createInt32Constant((int) value);
                case INT64 -> this;
                case TEXT -> OxlaConstant.createTextConstant(toString());
                default -> null; // Impossible.
            };
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaInt32Constant) {
                return Long.compare(this.value, ((OxlaInt32Constant) toType).value);
            } else if (toType instanceof OxlaInt64Constant) {
                return Long.compare(this.value, ((OxlaInt64Constant) toType).value);
            } else if (toType instanceof OxlaFloat32Constant) {
                return Float.compare(this.value, ((OxlaFloat32Constant) toType).value);
            } else if (toType instanceof OxlaFloat64Constant) {
                return Double.compare(this.value, ((OxlaFloat64Constant) toType).value);
            }
            throw new AssertionError(String.format("OxlaInt64Constant::compareTo not implemented for type: %s", toType.getClass()));
        }
    }

    public static class OxlaIntervalConstant extends OxlaConstant {
        public final int months;
        public final int days;
        public final long microseconds;

        public OxlaIntervalConstant(int months, int days, long microseconds) {
            this.months = months;
            this.days = days;
            this.microseconds = microseconds;
        }

        @Override
        public String asPlainLiteral() {
            return String.format("'%d months %d days %d microseconds'", months, days, microseconds);
        }

        @Override
        public String toString() {
            return String.format("INTERVAL %s", asPlainLiteral());
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            return switch (toType) {
                case INTERVAL -> this;
                case TEXT -> OxlaConstant.createTextConstant(toString());
                default -> null; // Impossible.
            };
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaIntervalConstant) {
                BigInteger left = asBigInteger(this);
                BigInteger right = asBigInteger((OxlaIntervalConstant) toType);
                return left.compareTo(right);
            }
            throw new AssertionError(String.format("OxlaIntervalConstant::compareTo not implemented for type: %s", toType.getClass()));
        }

        private static BigInteger asBigInteger(OxlaIntervalConstant interval) {
            ByteBuffer buffer = ByteBuffer.allocate(16); // sizeof(months) + sizeof(days) + sizeof(micro).
            buffer.putInt(interval.months);
            buffer.putInt(interval.days);
            buffer.putLong(interval.microseconds);
            return new BigInteger(1, buffer.array());
        }
    }

    public static class OxlaJsonConstant extends OxlaConstant {
        public final String value;

        public OxlaJsonConstant(String value) {
            this.value = value;
        }

        @Override
        public String asPlainLiteral() {
            final String valString = value
                    .replace("'", "")
                    .replace("\\", "\\\\");
            return String.format("'%s'", String.format("{\"key\":\"%s\"}", valString));
        }

        @Override
        public String toString() {
            return String.format("JSON %s", asPlainLiteral());
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            switch (toType) {
                case BOOLEAN:
                    return OxlaConstant.createBooleanConstant(Boolean.parseBoolean(value));
                case FLOAT32:
                    try {
                        return OxlaConstant.createFloat32Constant(Float.parseFloat(value));
                    } catch (NumberFormatException ignored) {
                        throw new IgnoreMeException();
                    }
                case FLOAT64:
                    try {
                        return OxlaConstant.createFloat64Constant(Double.parseDouble(value));
                    } catch (NumberFormatException ignored) {
                        throw new IgnoreMeException();
                    }
                case INT32:
                    try {
                        return OxlaConstant.createInt32Constant(Integer.parseInt(value));
                    } catch (NumberFormatException ignored) {
                        throw new IgnoreMeException();
                    }
                case INT64:
                    try {
                        return OxlaConstant.createInt64Constant(Long.parseLong(value));
                    } catch (NumberFormatException ignored) {
                        throw new IgnoreMeException();
                    }
                case JSON:
                    return this;
                case TEXT:
                    return OxlaConstant.createTextConstant(toString());
                default:
                    return null; // Impossible.
            }
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaJsonConstant) {
                return value.compareTo(((OxlaJsonConstant) toType).value);
            }
            throw new AssertionError(String.format("OxlaJsonConstant::compareTo not implemented for type: %s", toType.getClass()));
        }
    }

    public static class OxlaTextConstant extends OxlaConstant {
        public final String value;

        public OxlaTextConstant(String value) {
            this.value = value;
        }

        @Override
        public String asPlainLiteral() {
            final String valString = value
                    .replace("'", "")
                    .replace("\\", "\\\\");
            return String.format("'%s'", valString);
        }

        @Override
        public String toString() {
            return String.format("TEXT %s", asPlainLiteral());
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            switch (toType) {
                case BOOLEAN:
                    try {
                        return OxlaConstant.createBooleanConstant(Boolean.parseBoolean(value));
                    } catch (NumberFormatException e) {
                        throw new IgnoreMeException();
                    }
                case DATE:
                    try {
                        return OxlaConstant.createDateConstant((int) new SimpleDateFormat("yyyy-MM-dd").parse(value).getTime());
                    } catch (ParseException e) {
                        throw new IgnoreMeException();
                    }
                case FLOAT32:
                    try {
                        return OxlaConstant.createFloat32Constant(Float.parseFloat(value));
                    } catch (NumberFormatException e) {
                        throw new IgnoreMeException();
                    }
                case FLOAT64:
                    try {
                        return OxlaConstant.createFloat64Constant(Double.parseDouble(value));
                    } catch (NumberFormatException e) {
                        throw new IgnoreMeException();
                    }
                case INT32:
                    try {
                        return OxlaConstant.createInt32Constant(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        throw new IgnoreMeException();
                    }
                case INT64:
                    try {
                        return OxlaConstant.createInt64Constant(Long.parseLong(value));
                    } catch (NumberFormatException e) {
                        throw new IgnoreMeException();
                    }
                case INTERVAL:
                    // FIXME: Invalid for now; silently ignore the query that tries this.
                    //        Implementing this correctly would require me to duplicate our `parseInterval` code here,
                    //        which would take a lot of time, also we cannot do it naively here in our defined toString()
                    //        format, because the potential values can come from the database itself.
                    throw new IgnoreMeException();
                case JSON:
                    return OxlaConstant.createJsonConstant(value);
                case TEXT:
                    return this;
                case TIME:
                    try {
                        return OxlaConstant.createTimeConstant((int) new SimpleDateFormat("HH:mm::ss").parse(value).getTime());
                    } catch (ParseException e) {
                        throw new IgnoreMeException();
                    }
                case TIMESTAMP:
                    try {
                        return OxlaConstant.createTimestampConstant(new SimpleDateFormat("yyyy-MM-dd HH:mm::ss").parse(value).getTime());
                    } catch (ParseException e) {
                        throw new IgnoreMeException();
                    }
                case TIMESTAMPTZ:
                    try {
                        return OxlaConstant.createTimestamptzConstant(new SimpleDateFormat("yyyy-MM-dd HH:mm::ss+00").parse(value).getTime());
                    } catch (ParseException e) {
                        throw new IgnoreMeException();
                    }
                default:
                    return null; // Impossible.
            }
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaTextConstant) {
                return value.compareTo(((OxlaTextConstant) toType).value);
            }
            throw new AssertionError(String.format("OxlaTextConstant::compareTo not implemented for type: %s", toType.getClass()));
        }
    }

    public static class OxlaTimeConstant extends OxlaConstant {
        public final int value;

        public OxlaTimeConstant(int value) {
            this.value = value;
        }

        @Override
        public String asPlainLiteral() {
            return String.format("'%s'", new Time(value));
        }

        @Override
        public String toString() {
            return String.format("TIME %s", asPlainLiteral());
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            return switch (toType) {
                case INTERVAL -> OxlaConstant.createIntervalConstant(0, 0, value);
                case TEXT -> OxlaConstant.createTextConstant(toString());
                case TIME -> this;
                default -> null; // Impossible.
            };
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaTimeConstant) {
                return Integer.compare(this.value, ((OxlaTimeConstant) toType).value);
            }
            throw new AssertionError(String.format("OxlaTimeConstant::compareTo not implemented for type: %s", toType.getClass()));
        }
    }

    public static class OxlaTimestampConstant extends OxlaConstant {
        public final long value;

        public OxlaTimestampConstant(long value) {
            this.value = value;
        }

        @Override
        public String asPlainLiteral() {
            return String.format("'%s'", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value));
        }

        @Override
        public String toString() {
            return String.format("TIMESTAMP WITHOUT TIME ZONE %s", asPlainLiteral());
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            return switch (toType) {
                case DATE -> OxlaConstant.createDateConstant((int) value);
                case TEXT -> OxlaConstant.createTextConstant(toString());
                case TIME -> OxlaConstant.createTimeConstant((int) value);
                case TIMESTAMP -> this;
                case TIMESTAMPTZ -> OxlaConstant.createTimestamptzConstant(value);
                default -> null; // Impossible.
            };
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaDateConstant) {
                return Long.compare(this.value, ((OxlaDateConstant) toType).value);
            } else if (toType instanceof OxlaTimestampConstant) {
                return Long.compare(this.value, ((OxlaTimestampConstant) toType).value);
            } else if (toType instanceof OxlaTimestamptzConstant) {
                return Long.compare(this.value, ((OxlaTimestamptzConstant) toType).value);
            }
            throw new AssertionError(String.format("OxlaTimestamptzConstant::compareTo not implemented for type: %s", toType.getClass()));
        }
    }

    public static class OxlaTimestamptzConstant extends OxlaConstant {
        public final long value;

        public OxlaTimestamptzConstant(long value) {
            this.value = value;
        }

        @Override
        public String asPlainLiteral() {
            return String.format("'%s'", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss+00").format(value));
        }

        @Override
        public String toString() {
            return String.format("TIMESTAMP WITH TIME ZONE %s", asPlainLiteral());
        }

        @Override
        public OxlaConstant tryCast(OxlaDataType toType) {
            return switch (toType) {
                case DATE -> OxlaConstant.createDateConstant((int) value);
                case TEXT -> OxlaConstant.createTextConstant(toString());
                case TIME -> OxlaConstant.createTimeConstant((int) value);
                case TIMESTAMP -> OxlaConstant.createTimestampConstant(value);
                case TIMESTAMPTZ -> this;
                default -> null; // Impossible.
            };
        }

        @Override
        public int compareTo(OxlaConstant toType) {
            if (toType instanceof OxlaTimestampConstant) {
                return Long.compare(this.value, ((OxlaTimestampConstant) toType).value);
            } else if (toType instanceof OxlaTimestamptzConstant) {
                return Long.compare(this.value, ((OxlaTimestamptzConstant) toType).value);
            }
            throw new AssertionError(String.format("OxlaTimestamptzConstant::compareTo not implemented for type: %s", toType.getClass()));
        }
    }
}
