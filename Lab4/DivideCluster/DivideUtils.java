package DivideCluster;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;

import java.io.IOException;
import java.util.ArrayList;

public class DivideUtils {

    public static ArrayList<double[]> readCenters(String centerPath) throws IOException {
        ArrayList<double[]> centers = new ArrayList<>();
        Path path = new Path(centerPath);
        Configuration configuration = new Configuration();
        FileSystem fileSystem = path.getFileSystem(configuration);
        FSDataInputStream inputStream = fileSystem.open(path);
        LineReader lineReader = new LineReader(inputStream, configuration);
        Text line = new Text();
        while (lineReader.readLine(line) > 0) {
            String[] vectorStr = line.toString().split("\t")[1].split(",");
            double[] vectorDbl = new double[15];
            for (int i = 0; i < 15; i++)
                vectorDbl[i] = new Double(vectorStr[i]);
            centers.add(vectorDbl);
        }
        return centers;
    }
}
