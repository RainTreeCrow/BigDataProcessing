package PageRankIter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class PageRankIter
{
    public static void main(String[] args, int itrCnt, double pr_init)
    {
        try {
            Configuration conf = new Configuration();
            conf.set("PR_init", String.valueOf(pr_init));
            Job job = Job.getInstance(conf, "PageRankIter" + itrCnt);
            job.setJarByClass(PageRankIter.class);
            job.setInputFormatClass(TextInputFormat.class);
            job.setMapperClass(PageRankIterMapper.class);
            job.setCombinerClass(PageRankIterCombiner.class);
            job.setReducerClass(PageRankIterReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(PageRankBean.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            job.waitForCompletion(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
