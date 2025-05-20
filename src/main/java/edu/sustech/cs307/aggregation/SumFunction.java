package edu.sustech.cs307.aggregation;

import edu.sustech.cs307.exception.DBException;
import edu.sustech.cs307.tuple.Tuple;
import edu.sustech.cs307.value.Value;
import edu.sustech.cs307.value.ValueType;

public class SumFunction implements AggregateFunction {
    private final String columnName;
    private Value sum;

    public SumFunction(String columnName) {
        this.columnName = columnName;
        this.sum = new Value(Long.valueOf(0)); // 初始化为零值
    }

    @Override
    public AggregateFunction newInstance() {
        return new SumFunction(columnName);
    }

    @Override
    public void accumulate(Tuple tuple) throws DBException {
//        Value value = tuple.getColumnValue(columnName);
//        sum = sum.add(value);
    }

    @Override
    public Object getResult() {
        return sum;
    }

    @Override
    public ValueType getType() {
        return sum.getType();
    }

    @Override
    public String getAlias() {
        return "SUM(" + columnName + ")";
    }
}