package LabelPropagationIter;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Hashtable;

public class LabelPropIterMapper extends Mapper<Object, Text, Text, LabelPropBean>
{
    @Override
    public void map(Object key, Text value, Mapper.Context context)
            throws IOException, InterruptedException {
        String[] tmpStr = value.toString().split("\t");
        int label = Integer.parseInt(tmpStr[0]);
        String name = tmpStr[1];
        String str = tmpStr[2];
        Hashtable<String, Double> link_list = new Hashtable<>();
        str = str.substring(1, str.length() - 1);
        String[] pairs = str.split("\\|");
        for (String pair : pairs) {
            String[] split = pair.split("，");
            String u = split[0];
            double o = Double.parseDouble(split[1]);
            link_list.put(u, o);
            LabelPropBean lpb = new LabelPropBean();
            lpb.setLabel_Name(label, name);
            context.write(new Text(u), lpb);
        }
        LabelPropBean lpb = new LabelPropBean();
        lpb.setLink_list(link_list, label);
        context.write(new Text(name), lpb);
    }
}