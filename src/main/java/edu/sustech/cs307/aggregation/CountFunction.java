package edu.sustech.cs307.aggregation;

import edu.sustech.cs307.meta.TabCol;
import edu.sustech.cs307.value.Value;
import edu.sustech.cs307.value.ValueType;

public class CountFunction implements AggregateFunction{
    private final String columnName;
    private final TabCol parameter;
    private Value count;
    private int count_value;

    public CountFunction(String columnName, TabCol parameter) {
        this.columnName = columnName;
        this.parameter = parameter;
        count_value = 0;
        this.count = new Value(Double.valueOf(count_value)); // 初始化为零值
    }

    @Override
    public void reset() {
        count_value = 0;
        this.count = new Value(Double.valueOf(count_value)); // 初始化为零值
    }

    @Override
    public void accumulate(Value v) {
        count_value += 1;
    }

    @Override
    public Value result() {
        return new Value(Long.valueOf(count_value));
    }

    @Override
    public ValueType outputType() {
        return ValueType.INTEGER;
    }

    @Override
    public String alias() {
        return "COUNT(" + columnName + ")";
    }

    @Override
    public TabCol getTabCol() {
        return parameter;
    }
}
