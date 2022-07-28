import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ReduceJoin {
    public static void main(String[] args)
    {
        try { Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "join table");;
            job.setJarByClass(ReduceJoin.class);
            job.setInputFormatClass(TextInputFormat.class);
            job.setMapperClass(TableMapper.class);
            job.setReducerClass(TableReduce.class);

            job.setOutputKeyClass(TableBean.class);
            job.setOutputValueClass(NullWritable.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(TableBean.class);

            FileInputFormat.addInputPath(job, new Path(args[0] + "/university.tbl"));//"/Users/bytedance/Documents/hadoop/dataset_example"
            FileInputFormat.addInputPath(job, new Path(args[0] + "/country.tbl"));
            FileOutputFormat.setOutputPath(job, new Path( args[1]) );//"/Users/bytedance/Documents/hadoop/output/out20"
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
