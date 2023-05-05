package cn.mysilicon.housekeep.model;

public class Comment {
    private Integer id;
    private Integer service_id;
    private Integer user_id;
    private Integer merchant_id;
    private String comment;
    private String time;

    public Comment() {
    }

    public Comment(Integer id, Integer service_id, Integer user_id, Integer merchant_id, String comment, String time) {
        this.id = id;
        this.service_id = service_id;
        this.user_id = user_id;
        this.merchant_id = merchant_id;
        this.comment = comment;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getService_id() {
        return service_id;
    }

    public void setService_id(Integer service_id) {
        this.service_id = service_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(Integer merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
