package edu.sustech.cs307.logicalOperator;

import edu.sustech.cs307.exception.DBException;
import edu.sustech.cs307.exception.ExceptionTypes;
import edu.sustech.cs307.meta.TabCol;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogicalAggregateOperator extends LogicalOperator {
    private final List<SelectItem<?>> selectItems;
    private final LogicalOperator child;

    public LogicalAggregateOperator(LogicalOperator child, List<SelectItem<?>> selectItems) {
        super(Collections.singletonList(child));
        this.selectItems = selectItems;
        this.child = child;
    }
    public LogicalOperator getChild() {
        return child;
    }
    public List<TabCol> getOutputSchema() throws DBException {
        List<TabCol> outputSchema = new ArrayList<>();
        LogicalOperator iter = child;
        while (!(iter instanceof LogicalTableScanOperator)) {
            iter = iter.getChild();
        }
        LogicalTableScanOperator op = (LogicalTableScanOperator)iter;
        String table_name = op.getTableName();
        for (SelectItem<?> selectItem : selectItems) {
            if (selectItem.getExpression() instanceof Function function) {
                outputSchema.add(new TabCol(table_name, function.toString()));
            } else if (selectItem.getExpression() instanceof Column column) {
                //check whether column exists in GROUP BY
                if (child instanceof LogicalGroupByOperator groupByOp) {
                    if (groupByOp.getElement().toString().endsWith(column.getColumnName())) {
                        outputSchema.add(new TabCol(table_name, column.getColumnName()));
                    } else {
                        throw new DBException(ExceptionTypes.NotSupportedOperation(selectItem.getExpression()));
                    }
                } else {
                    throw new DBException(ExceptionTypes.NotSupportedOperation(selectItem.getExpression()));
                }
            } else {
                throw new DBException(ExceptionTypes.NotSupportedOperation(selectItem.getExpression()));
            }
        }
        return outputSchema;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String nodeHeader = "ProjectOperator(selectItems=" + selectItems + ")";
        String[] childLines = child.toString().split("\\R");

        // 当前节点
        sb.append(nodeHeader);

        // 子节点处理
        if (childLines.length > 0) {
            sb.append("\n└── ").append(childLines[0]);
            for (int i = 1; i < childLines.length; i++) {
                sb.append("\n    ").append(childLines[i]);
            }
        }

        return sb.toString();
    }
}
