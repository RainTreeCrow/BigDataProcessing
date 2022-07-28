package DivideCluster;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class DivideCluster
{
    public static void main(String[] args) throws IOException {
        String dataPath = args[0];
        String centerPath = args[1];
        String resultPath = args[2];

        try {
            Configuration conf = new Configuration();
            conf.set("centerPath", centerPath);
            Job job = Job.getInstance(conf, "DivideCluster");
            job.setJarByClass(DivideCluster.class);
            job.setInputFormatClass(TextInputFormat.class);
            job.setMapperClass(DivideMapper.class);
            job.setReducerClass(DivideReducer.class);
            job.setOutputKeyClass(NullWritable.class);
            job.setOutputValueClass(VectorBean.class);
            job.setMapOutputKeyClass(IntWritable.class);
            job.setMapOutputValueClass(VectorBean.class);
            FileInputFormat.addInputPath(job, new Path(dataPath));
            FileOutputFormat.setOutputPath(job, new Path(resultPath));
            for (int i = 0; i < 20; i++) {
                MultipleOutputs.addNamedOutput(job, "cluster" + i,
                        TextOutputFormat.class, IntWritable.class, VectorBean.class);
            }
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
