package edu.sustech.cs307.index;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sustech.cs307.record.RID;
import edu.sustech.cs307.value.Value;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * BPlusTreeIndex — 基于内存 B+ 树的 PRIMARY KEY 索引实现，**不使用 mirror／descendingMap**，
 * 所有范围迭代均按键 **升序** 返回。<br/>
 * 设计假设：键唯一（主键约束）；若插入重复键需在更高层抛异常。
 */
public class BPlusTreeIndex implements Index {
    /** 实际存储结构 */
    private final BPlusTree<Value, RID> tree;
    /** 可选持久化路径 */
    private final String persistPath;

    /** 默认阶数 128 的 B+ 树，不持久化 */
    public BPlusTreeIndex() {
        this(null, 128);
    }

    /** 指定持久化文件、阶数 */
    public BPlusTreeIndex(String persistPath, int branchingFactor) {
        this.persistPath = persistPath;
        this.tree = new BPlusTree<>(branchingFactor);
        // 若给了持久化路径，则尝试恢复
        if (persistPath != null) {
            restoreFromDisk();
        }
    }

    /** 方便与旧实现保持一致的构造 —— 只给持久化路径，阶数用默认值 */
    public BPlusTreeIndex(String persistPath) {
        this(persistPath, 128);
    }

    /* -------------------------------------------------- Index 接口实现 -------------------------------------------------- */

    @Override
    public RID EqualTo(Value key) {
        return tree.search(key);
    }

    @Override
    public Iterator<Entry<Value, RID>> LessThan(Value key, boolean inclusive) {
//        Value minSentinel = new Value(Integer.MIN_VALUE); // 假定系统最小值；若值域不同请替换
//        List<RID> list = tree.searchRange(minSentinel, BPlusTree.RangePolicy.EXCLUSIVE,
//                key, inclusive ? BPlusTree.RangePolicy.INCLUSIVE : BPlusTree.RangePolicy.EXCLUSIVE);
//        return toEntryIterator(list, true);
        return null;
    }

    @Override
    public Iterator<Entry<Value, RID>> MoreThan(Value key, boolean inclusive) {
//        Value maxSentinel = new Value(Integer.MAX_VALUE); // 假定系统最大值；若值域不同请替换
//        List<RID> list = tree.searchRange(key, inclusive ? BPlusTree.RangePolicy.INCLUSIVE : BPlusTree.RangePolicy.EXCLUSIVE,
//                maxSentinel, BPlusTree.RangePolicy.EXCLUSIVE);
//        return toEntryIterator(list, false);
        return null;
    }

    @Override
    public Iterator<Entry<Value, RID>> Range(Value low, Value high, boolean leftInclusive, boolean rightInclusive) {
        List<RID> list = tree.searchRange(low,
                leftInclusive ? BPlusTree.RangePolicy.INCLUSIVE : BPlusTree.RangePolicy.EXCLUSIVE,
                high,
                rightInclusive ? BPlusTree.RangePolicy.INCLUSIVE : BPlusTree.RangePolicy.EXCLUSIVE);
        return toEntryIterator(list, false);
    }

    @Override
    public void SaveIndexes(String persistPath, Map<Value, RID> map) {

    }

    /* -------------------------------------------------- 额外操作 -------------------------------------------------- */

    /** 插入新键 —— 供上层 InsertOperator 调用 */
    public void insert(Value key, RID rid) {
        tree.insert(key, rid);
    }

    /** 删除键 —— 供 Delete/Update 调用 */
    public void delete(Value key) {
        tree.delete(key);
    }

    /** 在系统关闭时持久化索引 */
    public void flush() {
//        if (persistPath == null) return;
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            Map<Value, RID> dump = new TreeMap<>();
//            Iterator<Entry<Value, RID>> it = Range(new Value(Long.valueOf(Integer.MIN_VALUE)), new Value(Integer.MAX_VALUE), false, false);
//            while (it.hasNext()) {
//                Entry<Value, RID> e = it.next();
//                dump.put(e.getKey(), e.getValue());
//            }
//            mapper.writeValue(new File(persistPath), dump);
//        } catch (IOException e) {
//            Logger.error("[BPlusTreeIndex] flush failed: " + e.getMessage());
//        }
    }

    /* -------------------------------------------------- 私有工具 -------------------------------------------------- */

    private void restoreFromDisk() {
        try {
            File f = new File(persistPath);
            if (!f.exists()) return;
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<TreeMap<Value, RID>> typeRef = new TypeReference<>() {};
            TreeMap<Value, RID> snapshot = mapper.readValue(f, typeRef);
            snapshot.forEach(this::insert);
        } catch (IOException e) {
            Logger.error("[BPlusTreeIndex] restore failed: " + e.getMessage());
        }
    }

    private Iterator<Entry<Value, RID>> toEntryIterator(List<RID> rids, boolean reverse) {
        List<Entry<Value, RID>> entries = new ArrayList<>(rids.size());
        for (RID rid : rids) {
            Value key = null; // B+ 树当前实现不直接返回键，这里视业务需要补充
            entries.add(Map.entry(key, rid));
        }
        if (reverse) Collections.reverse(entries);
        return entries.iterator();
    }
}
