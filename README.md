# hadoop简单用例
连接hdfs，实现文件读写，通过mapreduce简单运算。

# 统计一年当中每月的最高气温，步骤如下：

1. 原始数据，一年中的气温如：2014010114 代表2014年1月1日，14度
2. 进行map操作，合并为12组数据，每组中包含当月的气温
3. 记性reduce操作，每组中，比较算出最高的气温，输出最高气温
4. 汇总数据，写入输出文件
5. 查看结果，如下：
>Before Mapper: 0, 2014010114======After Mapper:2014, 14
>Before Mapper: 11, 2014010216======After Mapper:2014, 16
>Before Mapper: 22, 2014010317======After Mapper:2014, 17
>Before Mapper: 33, 2014010410======After Mapper:2014, 10
>Before Mapper: 44, 2014010506======After Mapper:2014, 6
>Before Mapper: 55, 2012010609======After Mapper:2012, 9
>Before Mapper: 66, 2012010732======After Mapper:2012, 32
>Before Mapper: 77, 2012010812======After Mapper:2012, 12
>Before Mapper: 88, 2012010919======After Mapper:2012, 19
>Before Mapper: 99, 2012011023======After Mapper:2012, 23
>Before Mapper: 110, 2001010116======After Mapper:2001, 16
>Before Mapper: 121, 2001010212======After Mapper:2001, 12
>Before Mapper: 132, 2001010310======After Mapper:2001, 10
>Before Mapper: 143, 2001010411======After Mapper:2001, 11
>Before Mapper: 154, 2001010529======After Mapper:2001, 29
>Before Mapper: 165, 2013010619======After Mapper:2013, 19
>Before Mapper: 176, 2013010722======After Mapper:2013, 22
>Before Mapper: 187, 2013010812======After Mapper:2013, 12
>Before Mapper: 198, 2013010929======After Mapper:2013, 29
>Before Mapper: 209, 2013011023======After Mapper:2013, 23
>Before Mapper: 220, 2008010105======After Mapper:2008, 5
>Before Mapper: 231, 2008010216======After Mapper:2008, 16
>Before Mapper: 242, 2008010337======After Mapper:2008, 37
>Before Mapper: 253, 2008010414======After Mapper:2008, 14
>Before Mapper: 264, 2008010516======After Mapper:2008, 16
>Before Mapper: 275, 2007010619======After Mapper:2007, 19
>Before Mapper: 286, 2007010712======After Mapper:2007, 12
>Before Mapper: 297, 2007010812======After Mapper:2007, 12
>Before Mapper: 308, 2007010999======After Mapper:2007, 99
>Before Mapper: 319, 2007011023======After Mapper:2007, 23
>Before Mapper: 330, 2010010114======After Mapper:2010, 14
>Before Mapper: 341, 2010010216======After Mapper:2010, 16
>Before Mapper: 352, 2010010317======After Mapper:2010, 17
>Before Mapper: 363, 2010010410======After Mapper:2010, 10
>Before Mapper: 374, 2010010506======After Mapper:2010, 6
>Before Mapper: 385, 2015010649======After Mapper:2015, 49
>Before Mapper: 396, 2015010722======After Mapper:2015, 22
>Before Mapper: 407, 2015010812======After Mapper:2015, 12
>Before Mapper: 418, 2015010999======After Mapper:2015, 99
>Before Mapper: 429, 2015011023======After Mapper:2015, 23
>Before Reduce: 2001, 12, 10, 11, 29, 16, ======After Reduce: 2001, 29
>Before Reduce: 2007, 23, 19, 12, 12, 99, ======After Reduce: 2007, 99
>Before Reduce: 2008, 16, 14, 37, 16, 5, ======After Reduce: 2008, 37
>Before Reduce: 2010, 10, 6, 14, 16, 17, ======After Reduce: 2010, 17
>Before Reduce: 2012, 19, 12, 32, 9, 23, ======After Reduce: 2012, 32
>Before Reduce: 2013, 23, 29, 12, 22, 19, ======After Reduce: 2013, 29
>Before Reduce: 2014, 14, 6, 10, 17, 16, ======After Reduce: 2014, 17
>Before Reduce: 2015, 23, 49, 22, 12, 99, ======After Reduce: 2015, 99
>Finished

# 问题反馈
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流

* 邮件: (zhou5827297@163.com)
