package LabelPropagationIter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class LabelPropIter
{
    public static void main(String[] args, int itrCnt)
    {
        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "LabelPropIter" + itrCnt);
            job.setJarByClass(LabelPropIter.class);
            job.setInputFormatClass(TextInputFormat.class);
            job.setMapperClass(LabelPropIterMapper.class);
            job.setReducerClass(LabelPropIterReducer.class);
            job.setOutputKeyClass(IntWritable.class);
            job.setOutputValueClass(Text.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(LabelPropBean.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            job.waitForCompletion(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}