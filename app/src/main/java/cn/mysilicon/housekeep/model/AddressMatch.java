package cn.mysilicon.housekeep.model;

public class AddressMatch {
    private int id;
    private int user_id;
    private String uname;
    private String phone;
    private String address;

    public AddressMatch() {
    }

    public AddressMatch(int id, int user_id, String uname, String phone, String address) {
        this.id = id;
        this.user_id = user_id;
        this.uname = uname;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
