package edu.sustech.cs307.aggregation;

import edu.sustech.cs307.meta.TabCol;
import edu.sustech.cs307.value.Value;
import edu.sustech.cs307.value.ValueType;

public class MaxFunction implements AggregateFunction{
    private final String columnName;
    private final TabCol parameter;
    private Value max;
    double max_value;

    public MaxFunction(String columnName, TabCol parameter) {
        this.columnName = columnName;
        this.parameter = parameter;
        max_value = 0.0;
        this.max = new Value(Double.valueOf(max_value)); // 初始化为零值
    }

    @Override
    public void reset() {
        max_value = 0.0F;
        this.max = new Value(Double.valueOf(max_value));
    }

    @Override
    public void accumulate(Value v) {
        double d = Double.valueOf(v.toString());
        if (d > max_value) max_value = d;
    }

    @Override
    public Value result() {
        return new Value(Double.valueOf(max_value));
    }

    @Override
    public ValueType outputType() {
        return ValueType.FLOAT;
    }

    @Override
    public String alias() {
        return "MAX(" + columnName + ")";
    }

    @Override
    public TabCol getTabCol() {
        return parameter;
    }
}
