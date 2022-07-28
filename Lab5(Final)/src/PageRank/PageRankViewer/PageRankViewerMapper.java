package PageRankViewer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PageRankViewerMapper extends Mapper<Object, Text, DoubleWritable, Text>
{
    @Override
    protected void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] tmpStr = value.toString().split("\t");
        String name = tmpStr[0];
        double page_rank = Double.parseDouble(tmpStr[1]);
        context.write(new DoubleWritable(-page_rank), new Text(name));
    }
}