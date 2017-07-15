package lu.gab.com.alertapet.Model;

import java.io.Serializable;
import java.util.Date;

import lu.gab.com.alertapet.Model.User;

/**
 * Created by Lu on 25/02/2017.
 */

public class Report {

    private String reportText;
    private long reportDate;
    private String reportImage;
    private String address;
    private String category;
    private String userName;
    private String userUid;
    private String phone;

    public Report(){
    }

    public Report( String reportText, long reportDate, String reportImage, String address, String category,
                  String userName, String userUid, String phone) {

        this.reportText = reportText;
        this.reportDate = reportDate;
        this.reportImage = reportImage;
        this.address = address;
        this.category = category;
        this.userName = userName;
        this.userUid = userUid;
        this.phone = phone;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public long getReportDate() {
        return reportDate;
    }

    public void setReportDate(long reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportImage() {
        return reportImage;
    }

    public void setReportImage(String reportImage) {
        this.reportImage = reportImage;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
