package sqlancer.oxla.ast.util;

import sqlancer.oxla.ast.OxlaConstant;

@FunctionalInterface
public interface OxlaApplyFunction {
    OxlaConstant apply(OxlaConstant[] constants);
}
