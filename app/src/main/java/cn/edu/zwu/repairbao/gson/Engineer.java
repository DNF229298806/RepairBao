package cn.edu.zwu.repairbao.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/19
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class Engineer implements Serializable {
    //private static final long serializableUID = 1;
    public String success;
    public String message;
    @SerializedName("data")
    public EngineerData engineerData;

    public class EngineerData implements Serializable{
        public String id;   //工程师id
        public String phoneNumber;  //手机号码
        public String password;     //密码
        public String name; //工程师姓名
        public String wechat;   //工程师微信号
        public String specialty;    //个人特长代码
        public String introduce;    //个人特长
        public String idCard;   //工程师身份证
        public String grade;    //星级
        public String receiveNumber;    //接单数
        public String endNumber;    //结单数
        public String backNumber;   //退单数
        public String status;   //状态码
    }
}
