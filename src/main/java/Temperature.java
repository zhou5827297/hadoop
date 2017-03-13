import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 气温MapperReducer
 */
public class Temperature {

    /**
     * 四个泛型类型分别代表：
     * KeyIn        Mapper的输入数据的Key，这里是每行文字的起始位置（0,11,...）
     * ValueIn      Mapper的输入数据的Value，这里是每行文字
     * KeyOut       Mapper的输出数据的Key，这里是每行文字中的“年份”
     * ValueOut     Mapper的输出数据的Value，这里是每行文字中的“气温”
     */
    static class TempMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 打印样本: Before Mapper: 0, 2000010115
            System.out.print("Before Mapper: " + key + ", " + value);
            String line = value.toString();
            String year = line.substring(0, 4);
            int temperature = Integer.parseInt(line.substring(8));
            context.write(new Text(year), new IntWritable(temperature));
            // 打印样本: After Mapper:2000, 15
            System.out.println("======" + "After Mapper:" + new Text(year) + ", " + new IntWritable(temperature));
        }
    }

    /**
     * 四个泛型类型分别代表：
     * KeyIn        Reducer的输入数据的Key，这里是每行文字中的“年份”
     * ValueIn      Reducer的输入数据的Value，这里是每行文字中的“气温”
     * KeyOut       Reducer的输出数据的Key，这里是不重复的“年份”
     * ValueOut     Reducer的输出数据的Value，这里是这一年中的“最高气温”
     */
    static class TempReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int maxValue = Integer.MIN_VALUE;
            StringBuffer sb = new StringBuffer();
            //取values的最大值
            for (IntWritable value : values) {
                maxValue = Math.max(maxValue, value.get());
                sb.append(value).append(", ");
            }
            // 打印样本： Before Reduce: 2000, 15, 23, 99, 12, 22,
            System.out.print("Before Reduce: " + key + ", " + sb.toString());
            context.write(key, new IntWritable(maxValue));
            // 打印样本： After Reduce: 2000, 99
            System.out.println("======" + "After Reduce: " + key + ", " + maxValue);
        }
    }

    public static void main(String[] args) throws Exception {
        //输入路径
        String dst = "hdfs://192.168.0.50:9000/input/temperature.txt";
        //输出路径，必须是不存在的，空文件加也不行。
        String dstOut = "hdfs://192.168.0.50:9000/output";
        Configuration hadoopConfig = new Configuration();

        hadoopConfig.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        hadoopConfig.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        Job job = Job.getInstance(hadoopConfig, "word count");
        //如果需要打成jar运行，需要下面这句
        //job.setJarByClass(NewMaxTemperature.class);

        //job执行作业时输入和输出文件的路径
        FileInputFormat.addInputPath(job, new Path(dst));
        FileOutputFormat.setOutputPath(job, new Path(dstOut));

        //指定自定义的Mapper和Reducer作为两个阶段的任务处理类
        job.setMapperClass(TempMapper.class);
        job.setReducerClass(TempReducer.class);

        //设置最后输出结果的Key和Value的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //执行job，直到完成
        job.waitForCompletion(true);
        System.out.println("Finished");
    }
//    Before Mapper: 0, 2014010114======After Mapper:2014, 14
//    Before Mapper: 11, 2014010216======After Mapper:2014, 16
//    Before Mapper: 22, 2014010317======After Mapper:2014, 17
//    Before Mapper: 33, 2014010410======After Mapper:2014, 10
//    Before Mapper: 44, 2014010506======After Mapper:2014, 6
//    Before Mapper: 55, 2012010609======After Mapper:2012, 9
//    Before Mapper: 66, 2012010732======After Mapper:2012, 32
//    Before Mapper: 77, 2012010812======After Mapper:2012, 12
//    Before Mapper: 88, 2012010919======After Mapper:2012, 19
//    Before Mapper: 99, 2012011023======After Mapper:2012, 23
//    Before Mapper: 110, 2001010116======After Mapper:2001, 16
//    Before Mapper: 121, 2001010212======After Mapper:2001, 12
//    Before Mapper: 132, 2001010310======After Mapper:2001, 10
//    Before Mapper: 143, 2001010411======After Mapper:2001, 11
//    Before Mapper: 154, 2001010529======After Mapper:2001, 29
//    Before Mapper: 165, 2013010619======After Mapper:2013, 19
//    Before Mapper: 176, 2013010722======After Mapper:2013, 22
//    Before Mapper: 187, 2013010812======After Mapper:2013, 12
//    Before Mapper: 198, 2013010929======After Mapper:2013, 29
//    Before Mapper: 209, 2013011023======After Mapper:2013, 23
//    Before Mapper: 220, 2008010105======After Mapper:2008, 5
//    Before Mapper: 231, 2008010216======After Mapper:2008, 16
//    Before Mapper: 242, 2008010337======After Mapper:2008, 37
//    Before Mapper: 253, 2008010414======After Mapper:2008, 14
//    Before Mapper: 264, 2008010516======After Mapper:2008, 16
//    Before Mapper: 275, 2007010619======After Mapper:2007, 19
//    Before Mapper: 286, 2007010712======After Mapper:2007, 12
//    Before Mapper: 297, 2007010812======After Mapper:2007, 12
//    Before Mapper: 308, 2007010999======After Mapper:2007, 99
//    Before Mapper: 319, 2007011023======After Mapper:2007, 23
//    Before Mapper: 330, 2010010114======After Mapper:2010, 14
//    Before Mapper: 341, 2010010216======After Mapper:2010, 16
//    Before Mapper: 352, 2010010317======After Mapper:2010, 17
//    Before Mapper: 363, 2010010410======After Mapper:2010, 10
//    Before Mapper: 374, 2010010506======After Mapper:2010, 6
//    Before Mapper: 385, 2015010649======After Mapper:2015, 49
//    Before Mapper: 396, 2015010722======After Mapper:2015, 22
//    Before Mapper: 407, 2015010812======After Mapper:2015, 12
//    Before Mapper: 418, 2015010999======After Mapper:2015, 99
//    Before Mapper: 429, 2015011023======After Mapper:2015, 23
//    Before Reduce: 2001, 12, 10, 11, 29, 16, ======After Reduce: 2001, 29
//    Before Reduce: 2007, 23, 19, 12, 12, 99, ======After Reduce: 2007, 99
//    Before Reduce: 2008, 16, 14, 37, 16, 5, ======After Reduce: 2008, 37
//    Before Reduce: 2010, 10, 6, 14, 16, 17, ======After Reduce: 2010, 17
//    Before Reduce: 2012, 19, 12, 32, 9, 23, ======After Reduce: 2012, 32
//    Before Reduce: 2013, 23, 29, 12, 22, 19, ======After Reduce: 2013, 29
//    Before Reduce: 2014, 14, 6, 10, 17, 16, ======After Reduce: 2014, 17
//    Before Reduce: 2015, 23, 49, 22, 12, 99, ======After Reduce: 2015, 99
//    Finished
}
