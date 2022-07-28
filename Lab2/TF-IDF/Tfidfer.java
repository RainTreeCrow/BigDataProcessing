package Tfidf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Tfidfer {
    public static void main(String[] args)
    {
        try { Configuration conf = new Configuration();
            FileSystem hdfs = FileSystem.get(conf);
            FileStatus[] status = hdfs.listStatus(new Path(args[2]));
            int totalFileCount = status.length;
            hdfs.close();
            conf.set("total", String.valueOf(totalFileCount));
            Job job = Job.getInstance(conf, "TF-IDFer");
            job.setJarByClass(Tfidfer.class);
            job.setInputFormatClass(TextInputFormat.class);
            job.setMapperClass(TfidfMapper.class);
            job.setReducerClass(TfidfReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (Exception e) { e.printStackTrace(); }
    }
}