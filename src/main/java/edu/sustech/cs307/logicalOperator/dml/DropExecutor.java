package edu.sustech.cs307.logicalOperator.dml;

import edu.sustech.cs307.exception.DBException;
import edu.sustech.cs307.system.DBManager;

public class DropExecutor implements DMLExecutor{
    private final DBManager dbManager;
    private final String table_name;

    public DropExecutor(DBManager dbManager, String tableName) {
        this.dbManager = dbManager;
        table_name = tableName;
    }

    @Override
    public void execute() throws DBException {
        dbManager.dropTable(table_name);
    }
}
