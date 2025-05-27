package edu.sustech.cs307.aggregation;

import edu.sustech.cs307.exception.DBException;
import edu.sustech.cs307.meta.TabCol;
import edu.sustech.cs307.tuple.TableTuple;
import edu.sustech.cs307.tuple.Tuple;
import edu.sustech.cs307.value.Value;
import edu.sustech.cs307.value.ValueType;

public class SumFunction implements AggregateFunction {
    private final String columnName;
    private final TabCol parameter;
    private Value sum;
    double sum_value;

    public SumFunction(String columnName, TabCol parameter) {
        this.columnName = columnName;
        this.parameter = parameter;
        sum_value = 0.0;
        this.sum = new Value(Double.valueOf(sum_value)); // 初始化为零值
    }


    @Override
    public void reset() {
        sum_value = 0.0F;
        this.sum = new Value(Double.valueOf(sum_value));
    }

    @Override
    public void accumulate(Value v) {
        double d = Double.valueOf(v.toString());
        sum_value += d;
    }

    @Override
    public Value result() {
        return new Value(Double.valueOf(sum_value));
    }

    @Override
    public ValueType outputType() {
        return ValueType.FLOAT;
    }

    @Override
    public String alias() {
        return "SUM(" + columnName + ")";
    }

    @Override
    public TabCol getTabCol() {
        return parameter;
    }
}