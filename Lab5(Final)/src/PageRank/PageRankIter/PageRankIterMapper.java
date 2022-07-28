package PageRankIter;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Hashtable;

public class PageRankIterMapper extends Mapper<Object, Text, Text, PageRankBean>
{
    @Override
    public void map(Object key, Text value, Mapper.Context context)
            throws IOException, InterruptedException {
        String[] tmpStr = value.toString().split("\t");
        String name = tmpStr[0];
        double cur_rank = Double.parseDouble(tmpStr[1]);
        String str = tmpStr[2];
        Hashtable<String, Double> link_list = new Hashtable<>();
        str = str.substring(1, str.length() - 1);
        String[] pairs = str.split("\\|");
        for (String pair : pairs) {
            String[] split = pair.split("，");
            String u = split[0];
            double o = Double.parseDouble(split[1]);
            link_list.put(u, o);
            PageRankBean prb = new PageRankBean();
            prb.setPage_rank(cur_rank * o);
            context.write(new Text(u), prb);
        }
        PageRankBean prb = new PageRankBean();
        prb.setLink_list(link_list);
        context.write(new Text(name), prb);
    }
}