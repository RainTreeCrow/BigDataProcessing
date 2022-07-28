package LabelPropagationIter;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

public class LabelPropIterReducer extends Reducer<Text, LabelPropBean, IntWritable, Text>
{
    private static final double d = 0.85;

    @Override
    protected void reduce(Text key, Iterable<LabelPropBean> values, Context context)
            throws IOException, InterruptedException {
        HashMap<Integer, Double> label_score = new HashMap<>();
        Hashtable<String, Integer> name_label = new Hashtable<>();
        Hashtable<String, Double> link_list = new Hashtable<>();
        StringBuilder neighbors = new StringBuilder(key.toString());
        int chosen = 0;
        for (LabelPropBean bean : values) {
            if (bean.getType().equals("label")) {
                name_label.put(bean.getName(), bean.getLabel());
            }
            else if (bean.getType().equals("link_list")) {
                neighbors.append("\t[");
                chosen = bean.getOld_label();
                link_list = bean.getLink_list();
                for (String u : link_list.keySet())
                    neighbors.append(u).append("，").append(link_list.get(u)).append("|");
                if (!link_list.isEmpty()) {
                    neighbors.deleteCharAt(neighbors.length() - 1);
                    neighbors.append("]");
                }
            }
        }
        double random = new Random().nextDouble();
        if (random < d) {
            for (String neighbor : link_list.keySet()) {
                int label = name_label.get(neighbor);
                double score;
                if (!label_score.containsKey(label))
                    score = link_list.get(neighbor);
                else
                    score = link_list.get(neighbor) + label_score.get(label);
                label_score.put(label, score);
            }
            double score, max_score = 0;
            ArrayList<Integer> labels = new ArrayList<>();
            for (int label : label_score.keySet()) {
                score = label_score.get(label);
                if (score >= max_score) {
                    max_score = score;
                }
            }
            for (int label : label_score.keySet()) {
                score = label_score.get(label);
                if (score == max_score) {
                    labels.add(label);
                }
            }
            int randInt = new Random().nextInt(labels.size());
            chosen = labels.get(randInt);
        }
        context.write(new IntWritable(chosen), new Text(neighbors.toString()));
    }
}
