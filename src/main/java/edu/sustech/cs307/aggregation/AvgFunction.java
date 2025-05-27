package edu.sustech.cs307.aggregation;

import edu.sustech.cs307.meta.TabCol;
import edu.sustech.cs307.value.Value;
import edu.sustech.cs307.value.ValueType;

import java.text.DecimalFormat;

public class AvgFunction implements AggregateFunction{
    private final String columnName;
    private final TabCol parameter;
    private Value avg;
    private int count;
    double sum_value;

    public AvgFunction(String columnName, TabCol parameter) {
        this.columnName = columnName;
        this.parameter = parameter;
        sum_value = 0.0;
        count = 0;
        this.avg = new Value(Double.valueOf(sum_value)); // 初始化为零值
    }

    @Override
    public void reset() {
        sum_value = 0.0F;
        this.avg = new Value(Double.valueOf(sum_value));
    }

    @Override
    public void accumulate(Value v) {
        double d = Double.valueOf(v.toString());
        sum_value += d;
        count += 1;
    }

    @Override
    public Value result() {
        sum_value /= count;
        String format = String.format("%.2f", sum_value);
        return new Value(Double.valueOf(format));
    }

    @Override
    public ValueType outputType() {
        return ValueType.FLOAT;
    }

    @Override
    public String alias() {
        return "AVG(" + columnName + ")";
    }

    @Override
    public TabCol getTabCol() {
        return parameter;
    }
}
