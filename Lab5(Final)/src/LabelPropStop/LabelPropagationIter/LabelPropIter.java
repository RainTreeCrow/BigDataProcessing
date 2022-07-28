package LabelPropagationIter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.LineReader;

public class LabelPropIter
{
    public static boolean main(String[] args, int itrCnt, String filePath)
    {
        try {
            Configuration conf = new Configuration();
            conf.set("filePath", filePath);
            Job job = Job.getInstance(conf, "LabelPropIter" + itrCnt);
            job.setJarByClass(LabelPropIter.class);
            job.setInputFormatClass(TextInputFormat.class);
            job.setMapperClass(LabelPropIterMapper.class);
            job.setReducerClass(LabelPropIterReducer.class);
            job.setOutputKeyClass(IntWritable.class);
            job.setOutputValueClass(Text.class);
            job.setMapOutputKeyClass(NullWritable.class);
            job.setMapOutputValueClass(LabelPropBean.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            job.waitForCompletion(true);

            Configuration configuration = new Configuration();
            Path path = new Path(filePath);
            FileSystem fileSystem = path.getFileSystem(configuration);
            FSDataInputStream inputStream = fileSystem.open(path);
            LineReader lineReader = new LineReader(inputStream, conf);
            Text line = new Text();
            if (lineReader.readLine(line) > 0) {
                String finished = line.toString();
                lineReader.close();
                return finished.equals("true");
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}