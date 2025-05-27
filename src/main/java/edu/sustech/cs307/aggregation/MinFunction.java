package edu.sustech.cs307.aggregation;

import edu.sustech.cs307.meta.TabCol;
import edu.sustech.cs307.value.Value;
import edu.sustech.cs307.value.ValueType;

public class MinFunction implements AggregateFunction{
    private final String columnName;
    private final TabCol parameter;
    private Value min;
    double min_value;

    public MinFunction(String columnName, TabCol parameter) {
        this.columnName = columnName;
        this.parameter = parameter;
        min_value = 0.0;
        this.min = new Value(Double.valueOf(min_value)); // 初始化为零值
    }

    @Override
    public void reset() {
        min_value = 0.0F;
        this.min = new Value(Double.valueOf(min_value));
    }

    @Override
    public void accumulate(Value v) {
        double d = Double.valueOf(v.toString());
        if (d < min_value) min_value = d;
    }

    @Override
    public Value result() {
        return new Value(Double.valueOf(min_value));
    }

    @Override
    public ValueType outputType() {
        return ValueType.FLOAT;
    }

    @Override
    public String alias() {
        return "MIN(" + columnName + ")";
    }

    @Override
    public TabCol getTabCol() {
        return parameter;
    }
}
