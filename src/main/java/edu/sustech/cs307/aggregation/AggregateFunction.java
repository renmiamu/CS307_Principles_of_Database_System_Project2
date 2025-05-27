package edu.sustech.cs307.aggregation;

import edu.sustech.cs307.meta.TabCol;
import edu.sustech.cs307.value.Value;
import edu.sustech.cs307.value.ValueType;

public interface AggregateFunction {
    void reset();                 // 初始化或复用时归零
    void accumulate(Value v);     // 每读到一行时更新聚合状态
    Value result();               // 扫描结束后返回最终结果
    ValueType outputType();       // 结果列类型（SUM int ⇒ BIGINT 等）
    String alias();               // 结果列名，建议形如 "SUM(salary)"
    TabCol getTabCol();
}
