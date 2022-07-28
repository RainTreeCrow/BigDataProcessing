package KMeans;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;

import java.io.IOException;
import java.util.ArrayList;

public class KMeansUtils {

    public static ArrayList<double[]> readCenters(String centerPath) throws IOException {
        ArrayList<double[]> centers = new ArrayList<>();
        Path path = new Path(centerPath);
        Configuration configuration = new Configuration();
        FileSystem fileSystem = path.getFileSystem(configuration);
        FSDataInputStream inputStream = fileSystem.open(path);
        LineReader lineReader = new LineReader(inputStream, configuration);
        Text line = new Text();
        while (lineReader.readLine(line) > 0) {
            String[] vectorStr = line.toString().split(": ")[1].split(",");
            double[] vectorDbl = new double[15];
            for (int i = 0; i < 15; i++)
                vectorDbl[i] = new Double(vectorStr[i]);
            centers.add(vectorDbl);
        }
        return centers;
    }

    public static void deletePath(String pathStr, boolean isDeleteDir) throws IOException {
        if (isDeleteDir)
            pathStr = pathStr.substring(0, pathStr.lastIndexOf('/'));
        Path path = new Path(pathStr);
        Configuration configuration = new Configuration();
        FileSystem fileSystem = path.getFileSystem(configuration);
        fileSystem.delete(path, true);
    }

    public static void copyFile(String from_path, String to_path) throws IOException {
        Path path_from = new Path(from_path);
        Path path_to = new Path(to_path);
        Configuration configuration = new Configuration();
        FileSystem fileSystem = path_from.getFileSystem(configuration);
        FSDataInputStream inputStream = fileSystem.open(path_from);
        LineReader lineReader = new LineReader(inputStream, configuration);
        FSDataOutputStream outputStream = fileSystem.create(path_to);
        Text line = new Text();
        while (lineReader.readLine(line) > 0) {
            String str = line + "\n";
            outputStream.write(str.getBytes());
        }
        lineReader.close();
        outputStream.close();
    }

    public static boolean compareCenters(String from_path, String to_path) throws IOException {
        ArrayList<double[]> fromCenters = readCenters(from_path);
        ArrayList<double[]> toCenters = readCenters(to_path);
        double distance;
        for (int i = 0; i < fromCenters.size(); i++) {
            double[] from = fromCenters.get(i);
            double[] to = toCenters.get(i);
            distance = 0;
            for (int j = 0; j < 15; j++)
                distance += Math.pow(from[j] - to[j], 2);
            if (distance != 0)
                return false;
        }
        return true;
    }
}
