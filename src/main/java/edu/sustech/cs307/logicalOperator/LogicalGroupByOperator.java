package edu.sustech.cs307.logicalOperator;

import net.sf.jsqlparser.statement.select.GroupByElement;

import java.util.Collections;

public class LogicalGroupByOperator extends LogicalOperator {
    private final GroupByElement element;
    private final LogicalOperator child;

    public LogicalGroupByOperator(LogicalOperator child, GroupByElement element) {
        super(Collections.singletonList(child));
        this.child = child;
        this.element = element;
    }
    public GroupByElement getElement(){
        return element;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String nodeHeader = "LogicalGroupByOperator(group by " + element + ")";
        LogicalOperator child = getChildren().get(0); // 获取过滤的子节点

        // 拆分子节点的多行字符串
        String[] childLines = child.toString().split("\\R");

        // 当前节点
        sb.append(nodeHeader);

        // 子节点处理
        if (childLines.length > 0) {
            sb.append("\n    └── ").append(childLines[0]);
            for (int i = 1; i < childLines.length; i++) {
                sb.append("\n    ").append(childLines[i]);
            }
        }

        return sb.toString();
    }

}
