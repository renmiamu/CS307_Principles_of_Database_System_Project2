package edu.sustech.cs307.logicalOperator;

import edu.sustech.cs307.aggregation.AggregateFunction;
import net.sf.jsqlparser.expression.Expression;
import java.util.Collections;
import java.util.List;

public class LogicalAggregateOperator extends LogicalOperator {
    private final LogicalOperator child;
    private final List<Expression> groupByExpressions;  // 分组列（如 GROUP BY dept）
    private final List<AggregateFunction> aggregateFunctions;  // 聚合函数（如 SUM(salary))

    public LogicalAggregateOperator(LogicalOperator child,
            List<Expression> groupByExpressions,
            List<AggregateFunction> aggregateFunctions) {
        super(Collections.singletonList(child));
        this.child = child;
        this.groupByExpressions = groupByExpressions;
        this.aggregateFunctions = aggregateFunctions;
    }

    public LogicalOperator getChild() {
        return child;
    }

    public List<Expression> getGroupByExpressions() {
        return groupByExpressions;
    }

    public List<AggregateFunction> getAggregateFunctions() {
        return aggregateFunctions;
    }

    @Override
    public String toString() {
        return "LogicalAggregateOperator(" +
                "groupBy=" + groupByExpressions.get(0) +
                ", aggregates=" + aggregateFunctions.get(0) +
                ")";
    }
}