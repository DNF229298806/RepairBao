package cn.edu.zwu.repairbao.db;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/9
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */

import java.io.Serializable;

public class MarkInfo implements Serializable {
    private double latitude;
    private double longitude;
    private String repair_loc;
    private String repair_type;
    private String user_quote;
    private String user_time;
    private String breakdown_content;
    private String username;
    private String phone;
    private String[] imagePath = new String[5];

    public MarkInfo() {

    }

    public MarkInfo(double latitude, double longitude, String repair_loc, String repair_type, String user_quote, String user_time, String breakdown_content, String username, String phone, String[] imagePath) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.repair_loc = repair_loc;
        this.repair_type = repair_type;
        this.user_quote = user_quote;
        this.user_time = user_time;
        this.breakdown_content = breakdown_content;
        this.username = username;
        this.phone = phone;
        this.imagePath = imagePath;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRepair_loc() {
        return repair_loc;
    }

    public void setRepair_loc(String repair_loc) {
        this.repair_loc = repair_loc;
    }

    public String getRepair_type() {
        return repair_type;
    }

    public void setRepair_type(String repair_type) {
        this.repair_type = repair_type;
    }

    public String getUser_quote() {
        return user_quote;
    }

    public void setUser_quote(String user_quote) {
        this.user_quote = user_quote;
    }

    public String getUser_time() {
        return user_time;
    }

    public void setUser_time(String user_time) {
        this.user_time = user_time;
    }

    public String getBreakdown_content() {
        return breakdown_content;
    }

    public void setBreakdown_content(String breakdown_content) {
        this.breakdown_content = breakdown_content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String[] getImagePath() {
        return imagePath;
    }

    public void setImagePath(String[] imagePath) {
        this.imagePath = imagePath;
    }
}
