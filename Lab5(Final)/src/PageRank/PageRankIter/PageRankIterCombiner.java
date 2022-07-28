package PageRankIter;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PageRankIterCombiner extends Reducer<Text, PageRankBean, Text, PageRankBean>
{
    @Override
    protected void reduce(Text key, Iterable<PageRankBean> values, Context context)
        throws IOException, InterruptedException {
        double val = 0;
        for (PageRankBean bean : values) {
            if (bean.getType().equals("page_rank"))
                val += bean.getPage_rank();
            else if (bean.getType().equals("link_list"))
                context.write(key, bean);
        }
        PageRankBean prb = new PageRankBean();
        prb.setPage_rank(val);
        context.write(key, prb);
    }
}
