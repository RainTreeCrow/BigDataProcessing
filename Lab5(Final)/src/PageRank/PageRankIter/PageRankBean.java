package PageRankIter;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Hashtable;

public class PageRankBean implements Writable
{
    private Hashtable<String, Double> link_list;
    private int list_length;
    private double page_rank;
    private String type;

    public Hashtable<String, Double> getLink_list() {
        Hashtable<String, Double> ll = new Hashtable<>();
        for (String key: link_list.keySet()) {
            ll.put(key, link_list.get(key));
        }
        return ll;
    }

    public void setLink_list(Hashtable<String, Double> ll) {
        link_list = new Hashtable<>();
        for (String key: ll.keySet()) {
            link_list.put(key, ll.get(key));
        }
        page_rank = -1;
        type = "link_list";
        list_length = link_list.size();
    }

    public double getPage_rank() {
        return page_rank;
    }

    public void setPage_rank(double pr) {
        link_list = new Hashtable<>();
        page_rank = pr;
        type = "page_rank";
        list_length = 0;
    }

    public String getType() {
        return type;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(type);
        dataOutput.writeDouble(page_rank);
        dataOutput.writeInt(list_length);
        for (String key : link_list.keySet()) {
            dataOutput.writeUTF(key);
            dataOutput.writeDouble(link_list.get(key));
        }
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        type = dataInput.readUTF();
        page_rank = dataInput.readDouble();
        list_length = dataInput.readInt();
        link_list = new Hashtable<>();
        for (int i = 0; i < list_length; i++) {
            String key = dataInput.readUTF();
            double value = dataInput.readDouble();
            link_list.put(key, value);
        }
    }
}
