package PageRankIter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Hashtable;

public class PageRankIterReducer extends Reducer<Text, PageRankBean, Text, Text>
{
    private static final double d = 0.85;

    @Override
    protected void reduce(Text key, Iterable<PageRankBean> values, Context context)
        throws IOException, InterruptedException {
        StringBuilder link_list = new StringBuilder();
        Hashtable<String, Double> ll;
        double val = 0;
        Configuration config = context.getConfiguration();
        double pr_init = Double.parseDouble(config.get("PR_init"));
        for (PageRankBean bean : values) {
            if (bean.getType().equals("page_rank"))
                val += bean.getPage_rank();
            else if (bean.getType().equals("link_list")) {
                link_list.append("\t[");
                ll = bean.getLink_list();
                for (String u : ll.keySet())
                    link_list.append(u).append("，").append(ll.get(u)).append("|");
                if (!ll.isEmpty()) {
                    link_list.deleteCharAt(link_list.length() - 1);
                    link_list.append("]");
                }
            }
        }
        double page_rank = (1 - d) * pr_init + (d * val);
        context.write(key, new Text(String.valueOf(page_rank) + link_list));
    }
}
