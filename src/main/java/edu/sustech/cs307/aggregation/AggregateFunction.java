package edu.sustech.cs307.aggregation;

import edu.sustech.cs307.exception.DBException;
import edu.sustech.cs307.tuple.Tuple;
import edu.sustech.cs307.value.ValueType;

public interface AggregateFunction {
    /**
     * 创建一个新的聚合函数实例（每个分组独立）
     */
    AggregateFunction newInstance();

    /**
     * 累加元组中的值
     */
    void accumulate(Tuple tuple) throws DBException;

    /**
     * 获取聚合结果
     */
    Object getResult();

    /**
     * 获取聚合列的输出类型（如 ValueType.INTEGER）
     */
    ValueType getType();

    /**
     * 获取聚合列的别名（如 "SUM(salary)"）
     */
    String getAlias();
}