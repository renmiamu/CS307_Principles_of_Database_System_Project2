package edu.sustech.cs307.logicalOperator.dml;

import edu.sustech.cs307.exception.DBException;
import edu.sustech.cs307.system.DBManager;

public class DescribeExecutor implements DMLExecutor{
    private final DBManager dbManager;
    private final String table_name;
    public DescribeExecutor(DBManager dbManager, String table_name) {
        this.dbManager = dbManager;
        this.table_name = table_name;
    }

    @Override
    public void execute() throws DBException {
        dbManager.descTable(table_name);
    }
}
