package LabelPropagationIter;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Hashtable;

public class LabelPropBean implements Writable
{
    private int label;
    private String name;
    private int old_label;
    private Hashtable<String, Double> link_list;
    private int list_length;
    private String type;

    public int getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public void setLabel_Name(int l, String n) {
        label = l;
        name = n;
        old_label = -1;
        link_list = new Hashtable<>();
        list_length = 0;
        type = "label";
    }

    public int getOld_label() {
        return old_label;
    }

    public Hashtable<String, Double> getLink_list() {
        Hashtable<String, Double> ll = new Hashtable<>();
        for (String key: link_list.keySet()) {
            ll.put(key, link_list.get(key));
        }
        return ll;
    }

    public void setLink_list(Hashtable<String, Double> ll, int ol) {
        label = -1;
        name = " ";
        old_label = ol;
        link_list = new Hashtable<>();
        for (String key: ll.keySet()) {
            link_list.put(key, ll.get(key));
        }
        list_length = link_list.size();
        type = "link_list";
    }

    public String getType() {
        return type;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(type);
        dataOutput.writeInt(label);
        dataOutput.writeUTF(name);
        dataOutput.writeInt(old_label);
        dataOutput.writeInt(list_length);
        for (String key : link_list.keySet()) {
            dataOutput.writeUTF(key);
            dataOutput.writeDouble(link_list.get(key));
        }
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        type = dataInput.readUTF();
        label = dataInput.readInt();
        name = dataInput.readUTF();
        old_label = dataInput.readInt();
        list_length = dataInput.readInt();
        link_list = new Hashtable<>();
        for (int i = 0; i < list_length; i++) {
            String key = dataInput.readUTF();
            double value = dataInput.readDouble();
            link_list.put(key, value);
        }
    }
}