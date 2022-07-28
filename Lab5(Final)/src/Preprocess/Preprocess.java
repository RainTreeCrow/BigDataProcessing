package Preprocess;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Preprocess
{
    public static void main(String[] args)
    {
        try {
            Configuration conf = new Configuration();
            conf.set("namePath", args[1]);
            Job job = Job.getInstance(conf, "Preprocess");
            job.setJarByClass(Preprocess.class);
            job.setInputFormatClass(TextInputFormat.class);
            job.setMapperClass(PreprocessMapper.class);
            job.setNumReduceTasks(0);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(NullWritable.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[2]));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
