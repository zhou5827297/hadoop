
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void upload(FileSystem fileSystem) throws Exception {
        FileInputStream inputStream = new FileInputStream(new File("input/test.segmented"));
        FSDataOutputStream out = fileSystem.create(new Path("/input/test.segmented"));
        IOUtils.copyBytes(inputStream, out, 2048, true);
    }


    public static void main(String[] args) throws Exception {
        String dst = "hdfs://192.168.0.50:9000";
        URI uri = URI.create(dst);
        System.out.println(uri);
        Configuration configuration = new Configuration();
        try {
            FileSystem fileSystem = FileSystem.get(uri, configuration);
            Path path = new Path("/user");
            FileStatus fsStatus = fileSystem.getFileStatus(path);
            System.out.println(fsStatus.getPath());
            final String pathStr = "/input";
            boolean flag = fileSystem.exists(new Path(pathStr));
            if (!flag) {
                boolean result = fileSystem.mkdirs(new Path(pathStr));
                System.out.println(result);
            } else {
//                boolean result = fileSystem.delete(new Path(pathStr), true);
//                System.out.println(result);
            }
            upload(fileSystem);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path("hdfs://192.168.0.50:9000/input"));
        FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.0.50:9000/output"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}