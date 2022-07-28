import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CoOccurrencePair
{
    public static void main(String[] args)
    {
        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "CoOccurrencePair");;
            job.setJarByClass(CoOccurrencePair.class);
            job.setInputFormatClass(TextInputFormat.class);
            job.setMapperClass(P2Mapper.class);
            job.setReducerClass(P2Reducer.class);
            job.setOutputKeyClass(CharacterPair.class);
            job.setOutputValueClass(IntWritable.class);
            job.setMapOutputKeyClass(CharacterPair.class);
            job.setMapOutputValueClass(IntWritable.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}