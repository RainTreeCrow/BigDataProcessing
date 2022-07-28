import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TableBean implements Writable {

    private int c_key;
    private String c_name;
    private String c_subject;
    private String c_hours;
    private int u_key;
    private String u_name;
    private String u_webpage;
    private String flag;//标记是哪个表 university country

    public int getC_key() {
        return c_key;
    }

    public void setC_key(int c_key) {
        this.c_key = c_key;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_subject() {
        return c_subject;
    }

    public void setC_subject(String c_subject) {
        this.c_subject = c_subject;
    }

    public String getC_hours() {
        return c_hours;
    }

    public void setC_hours(String c_hours) {
        this.c_hours = c_hours;
    }

    public TableBean() {
    }

    public int getU_key() {
        return u_key;
    }

    public void setU_key(int u_key) {
        this.u_key = u_key;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_webpage() {
        return u_webpage;
    }

    public void setU_webpage(String u_webpage) {
        this.u_webpage = u_webpage;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        //dataOutput.writeUTF(alpha_2_code);
        dataOutput.writeInt(c_key);
        dataOutput.writeUTF(c_name);
        dataOutput.writeUTF(c_subject);
        dataOutput.writeUTF(c_hours);

        dataOutput.writeInt(u_key);
        dataOutput.writeUTF(u_name);
        dataOutput.writeUTF(u_webpage);
        dataOutput.writeUTF(flag);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.c_key = dataInput.readInt();
        this.c_name = dataInput.readUTF();
        this.c_subject = dataInput.readUTF();
        this.c_hours = dataInput.readUTF();

        this.u_key = dataInput.readInt();
        this.u_name = dataInput.readUTF();
        this.u_webpage = dataInput.readUTF();
        this.flag = dataInput.readUTF();
    }

    /*
    join后的表名为UniversityCountry，从左至右应包含学校信息表（university.tbl）的学校序号（u_key）、学校名称（u_name）、学校主页（u_webpage）字段和国家信息表（country.tbl）的国家名称（n_name）字段，
     */
    @Override
    public String toString() {
        return  Integer.toString(c_key)
                + '|' + c_name
                + '|' +  c_subject
                + '|' +  c_hours
                + '|' + Integer.toString(u_key)
                + '|' + u_name
                + '|' +  u_webpage;
    }
}
