import org.apache.hadoop.io.Writable;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ValuePair implements Writable
{
    private int value;
    private String name;

    public ValuePair() {
        this.value = 0;
        this.name = "";
    }

    public  ValuePair(ValuePair obj) {
        this.value = obj.getValue();
        this.name = obj.getName();
    }
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(value);
        dataOutput.writeUTF(name);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.value = dataInput.readInt();
        this.name = dataInput.readUTF();
    }

    @Override
    public String toString() {
        return  name
                + "，"
                + value;
    }
}