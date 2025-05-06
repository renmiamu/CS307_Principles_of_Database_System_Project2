package edu.sustech.cs307.logicalOperator;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.update.UpdateSet;

import java.util.Collections;
import java.util.List;

public class LogicalDeleteOperator extends LogicalOperator{
    private final String tableName;
    private final LogicalOperator child;
    public LogicalDeleteOperator(LogicalOperator child, String tableName) {
        super(Collections.singletonList(child));
        this.tableName = tableName;
        this.child = child;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String nodeHeader = "LogicalDeleteOperator(table=" + tableName + ")";
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
