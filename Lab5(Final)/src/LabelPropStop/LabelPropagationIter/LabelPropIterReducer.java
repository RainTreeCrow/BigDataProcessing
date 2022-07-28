package LabelPropagationIter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LabelPropIterReducer extends Reducer<NullWritable, LabelPropBean, IntWritable, Text>
{
    Hashtable<String, Integer> name_label = new Hashtable<>();
    Hashtable<String, Hashtable<String, Double>> link_lists = new Hashtable<>();
    Hashtable<String,  String> neighbor_list = new Hashtable<>();

    @Override
    protected void reduce(NullWritable key, Iterable<LabelPropBean> values, Context context)
    {
        Hashtable<String, Double> link_list;
        for (LabelPropBean bean : values) {
            if (bean.getType().equals("label")) {
                name_label.put(bean.getName(), bean.getLabel());
            }
            else if (bean.getType().equals("link_list")) {
                StringBuilder neighbors = new StringBuilder();
                String keyrepr = bean.getName();
                neighbors.append(keyrepr).append("\t[");
                link_list = bean.getLink_list();
                link_lists.put(keyrepr, link_list);
                for (String u : link_list.keySet())
                    neighbors.append(u).append("，").append(link_list.get(u)).append("|");
                if (!link_list.isEmpty()) {
                    neighbors.deleteCharAt(neighbors.length() - 1);
                    neighbors.append("]");
                }
                neighbor_list.put(keyrepr, neighbors.toString());
            }
        }
    }

    @Override
    protected void cleanup(Context context)
            throws IOException, InterruptedException {
        List<String> list = new ArrayList<>(link_lists.keySet());
        Collections.shuffle(list);
        for (String name : list) {
            int chosen;
            Hashtable<String, Double> link_list = link_lists.get(name);
            Hashtable<Integer, Double> label_score = new Hashtable<>();
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
            name_label.put(name, chosen);
            context.write(new IntWritable(chosen), new Text(neighbor_list.get(name)));
        }
        boolean finished = true;
        for (String name : list) {
            Hashtable<String, Double> link_list = link_lists.get(name);
            int the_label = name_label.get(name);
            double score = 0;
            for (String neighbor : link_list.keySet()) {
                int label = name_label.get(neighbor);
                if (label == the_label)
                    score += link_list.get(neighbor);
            }
            if (score < 0.5)
                finished = false;
        }
        Configuration configuration = context.getConfiguration();
        String filePath = configuration.get("filePath");
        Path path = new Path(filePath);
        FileSystem fileSystem = path.getFileSystem(configuration);
        FSDataOutputStream outputStream = fileSystem.create(path, true);
        outputStream.write(String.valueOf(finished).getBytes());
        outputStream.close();
    }
}
