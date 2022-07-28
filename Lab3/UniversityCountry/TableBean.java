import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TableBean implements Writable {

    /*
    country.tbl为国家信息数据表，包含国家2字母编码（n_alpha-2-code）、国家名（n_name）、国家3字母编码（n_alpha-3-code）、国家数字码（n_numeric-code）和ISO国家代码（n_ISO-3166-2）五个字段。
    学校序号（u_key）、学校名称（u_name）、学校所属国家2字母编码（u_alpha-2-code）、学校所在省份（u_province）、学校主页网址（u_webpage）、学校域名（u_domain）
     */
//    private String alpha_2_code;
    private  String n_name;
    private int u_key;
    private String u_name;
    private String u_webpage;
    private String flag;//标记是哪个表 university country

    public TableBean() {
    }

//    public String getAlpha_2_code() {
//        return alpha_2_code;
//    }
//
//    public void setAlpha_2_code(String alpha_2_code) {
//        this.alpha_2_code = alpha_2_code;
//    }

    public String getN_name() {
        return n_name;
    }

    public void setN_name(String n_name) {
        this.n_name = n_name;
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
        dataOutput.writeInt(u_key);
        dataOutput.writeUTF(u_name);
        dataOutput.writeUTF(u_webpage);
        dataOutput.writeUTF(n_name);
        dataOutput.writeUTF(flag);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        //this.alpha_2_code = dataInput.readUTF();
        this.u_key = dataInput.readInt();
        this.u_name = dataInput.readUTF();
        this.u_webpage = dataInput.readUTF();
        this.n_name = dataInput.readUTF();
        this.flag = dataInput.readUTF();
    }

    /*
    join后的表名为UniversityCountry，从左至右应包含学校信息表（university.tbl）的学校序号（u_key）、学校名称（u_name）、学校主页（u_webpage）字段和国家信息表（country.tbl）的国家名称（n_name）字段，
     */
    @Override
    public String toString() {
        return  Integer.toString(u_key) + '|' + u_name
                + '|' +  u_webpage
                + '|' + n_name;
    }
}
