package Preprocess;

import com.huaban.analysis.jieba.JiebaSegmenter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.LineReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PreprocessMapper extends Mapper<Object, Text, Text, NullWritable>
{
    ArrayList<String> nameList = new ArrayList<>();

    @Override
    protected void setup(Context context)
            throws IOException {
        Configuration config = context.getConfiguration();
        String nameFile = config.get("namePath");
        Path path = new Path(nameFile);
        FileSystem fileSystem = FileSystem.get(config);
        FSDataInputStream inputStream = fileSystem.open(path);
        LineReader lineReader = new LineReader(inputStream, config);
        Text line = new Text();
        while (lineReader.readLine(line) > 0) {
            String name = line.toString();
            nameList.add(name);
        }
    }

    @Override
    protected void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
        String line = value.toString();
        String names = "";
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> segments = segmenter.sentenceProcess(line);
        for (String segment : segments) {
            if (nameList.contains(segment))
                names += (segment + " ");
        }
        if (names.length() > 0) {
            Text text = new Text(names.trim());
            context.write(text, NullWritable.get());
        }
    }
}
