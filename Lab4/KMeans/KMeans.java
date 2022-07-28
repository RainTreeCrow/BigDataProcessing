package KMeans;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class KMeans {

    public static void run(String dataPath, String centerPath, String resultPath, int time) {
        try {
            System.out.println("Running for the " + time + " time");
            Configuration conf = new Configuration();
            conf.set("centerPath", centerPath);
            Job job = Job.getInstance(conf, "KMeans");
            job.setJarByClass(KMeans.class);
            job.setInputFormatClass(TextInputFormat.class);
            job.setMapperClass(KMeansMapper.class);
            job.setCombinerClass(KMeansCombiner.class);
            job.setReducerClass(KMeansReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);
            job.setMapOutputKeyClass(IntWritable.class);
            job.setMapOutputValueClass(VectorBean.class);
            FileInputFormat.addInputPath(job, new Path(dataPath));
            FileOutputFormat.setOutputPath(job, new Path(resultPath));
            job.waitForCompletion(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String dataPath = args[0];
        String centerPath = args[1];
        String resultPath = args[2];
        String tempCenter = resultPath + "_temp";
        KMeansUtils.copyFile(centerPath, tempCenter);
        int time = 1;
        while (true) {
            run(dataPath, tempCenter, resultPath, time++);
            if (KMeansUtils.compareCenters(tempCenter, resultPath + "/part-r-00000")) {
                KMeansUtils.deletePath(resultPath, false);
                break;
            }
            else {
                KMeansUtils.deletePath(tempCenter, false);
                KMeansUtils.copyFile(resultPath + "/part-r-00000", tempCenter);
                KMeansUtils.deletePath(resultPath, false);
            }
        }
        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "KMeans");
            job.setJarByClass(KMeans.class);
            job.setInputFormatClass(TextInputFormat.class);
            job.setMapperClass(OutputMapper.class);
            job.setNumReduceTasks(0);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(NullWritable.class);
            FileInputFormat.addInputPath(job, new Path(tempCenter));
            FileOutputFormat.setOutputPath(job, new Path(resultPath));
            if (job.waitForCompletion(true)) {
                KMeansUtils.deletePath(tempCenter, false);
                System.exit(0);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
