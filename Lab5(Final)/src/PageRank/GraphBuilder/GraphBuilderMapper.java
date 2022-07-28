package GraphBuilder;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class GraphBuilderMapper extends Mapper<Object,Text,Text,Text>
{
    @Override
    public void map(Object key, Text value, Mapper.Context context)
            throws IOException, InterruptedException {
        Configuration config = context.getConfiguration();
        String pageRank = config.get("PR_init") + "\t";
        String[] tmpStr = value.toString().split("\t");
        Text name = new Text(tmpStr[0]);
        pageRank += tmpStr[1];
        Text pair = new Text(pageRank);
        context.write(name, pair);
    }
}