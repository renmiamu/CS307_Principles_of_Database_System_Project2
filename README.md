# CS307_Principles_of_Database_System_Project2

## Task3: Optimizer（Index Optimization）

#### 2.Advanced:  Index Optimization Implementation

项目实现了两种索引机制，分别为原有的InMemoryOrderedIndex和advance部分要求的B+树索引。

大致功能如下：

#### create index:

创建索引，要求无重复元素。如果出现重复值会报错：Duplicated index.

会自动创建B+树索引。（可以在Logical Planner中切换）

SQL:

```sql
create index index_name on student(name);
```

执行该SQL后程序会在 `CS307-DB/meta` 目录下创建该index对应的 `tableName_columnName_IndexType.json` 文件，用于持久化在硬盘上存储索引信息，格式大致如下。

```json
{
  "char_Alice": {
    "pageNum": 1,
    "slotNum": 0
  },
  "char_Amy": {
    "pageNum": 1,
    "slotNum": 26
  },
  "char_Bob": {
    "pageNum": 1,
    "slotNum": 1
  },
 ... 
}  
```

重启数据库后，如果查询可以通过B+树索引执行，程序可以直接从对应的json文件中读取index信息进行查询。

应该实现了**Persistent Storage:**

- Store index structures on disk 
- Ensure index persistence after system restart

#### drop index:

删除先前创建的索引

SQL:

```sql
drop index name on student;
```

#### 基于索引范围查找

可以根据SQL简单生成相应的查询逻辑，可以在filter层出现**Binary Expression** 或 **Between Expression** 的时候生成基于索引的查询逻辑。（Between查询左闭右开）

如果使用B+树索引查询，会打印B+树（直接调用toString方法）

SQL:

```sql
select id, name from student where name = 'Sam';
select * from student where id between 20 and 40;
```

#### 多索引支持

同一表能建多个索引文件，允许**按列建多个索引**， 能自动区分不同 `col` 对应哪个索引

SQL与先前create index相同

```sql
create index index_id on student(id);
```

