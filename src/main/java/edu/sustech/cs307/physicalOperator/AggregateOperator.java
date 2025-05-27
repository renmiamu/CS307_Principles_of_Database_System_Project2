package edu.sustech.cs307.physicalOperator;

import edu.sustech.cs307.aggregation.AggregateFunction;
import edu.sustech.cs307.exception.DBException;
import edu.sustech.cs307.meta.ColumnMeta;
import edu.sustech.cs307.meta.TabCol;
import edu.sustech.cs307.tuple.TempTuple;
import edu.sustech.cs307.tuple.Tuple;
import edu.sustech.cs307.value.Value;

import java.util.ArrayList;
import java.util.List;

public class AggregateOperator implements PhysicalOperator {
    private final PhysicalOperator child;
    private final List<AggregateFunction> aggFuncs;
    private List<TabCol> outputSchema;
    private Tuple inputTuple;
    private boolean produced = false;

    public AggregateOperator(PhysicalOperator child,
                             List<AggregateFunction> aggFuncs,
                             List<TabCol> outputSchema) {
        this.child = child;
        this.aggFuncs = aggFuncs;
        this.outputSchema = outputSchema;
    }

    @Override
    public void Begin() throws DBException {
        child.Begin();
        for (AggregateFunction aggFunc : aggFuncs) {
            aggFunc.reset();
        }
        while (child.hasNext()) {
            child.Next();
            inputTuple = child.Current();
            Value[] values = getValues();
            for (int i = 0; i < aggFuncs.size(); i++) {
                AggregateFunction aggFunc = aggFuncs.get(i);
                aggFunc.accumulate(values[i]);
            }
        }
        child.Close();
    }
    private Value getValue(TabCol tabCol) throws DBException {
        for (TabCol projectColumn : outputSchema) {
            if (projectColumn.getColumnName().contains(tabCol.getColumnName())) {
                return inputTuple.getValue(tabCol); // Get value from input tuple
            }
        }
        return null;
    }
    private Value[] getValues() throws DBException {
        // 通过 meta 顺序和信息获取所有 Value
        ArrayList<Value> values = new ArrayList<>();
        for (AggregateFunction aggFunc : this.aggFuncs) {
            TabCol tabCol = aggFunc.getTabCol();
            Value value = getValue(tabCol);
            values.add(value);
        }

        return values.toArray(new Value[0]);
    }
    @Override
    public boolean hasNext() {
        return !produced;
    }

    @Override
    public void Next() {
        produced = true;
    }

    @Override
    public Tuple Current() {
        List<Value> values = new ArrayList<>();
        for (AggregateFunction aggFunc : aggFuncs) {
            values.add(aggFunc.result());
        }
        return new TempTuple(values);
    }

    @Override
    public void Close() {}

    @Override
    public ArrayList<ColumnMeta> outputSchema() {
        ArrayList<ColumnMeta> result = new ArrayList<>();
        String table_name = "";

        for (int i = 0; i < aggFuncs.size(); i++) {
            AggregateFunction func = aggFuncs.get(i);
            int offset = 0;
            result.add(new ColumnMeta(table_name, func.alias(), func.outputType(),
                    func.outputType().getLen(), offset));
            offset += func.outputType().getLen();
        }


        return result;
    }
}
