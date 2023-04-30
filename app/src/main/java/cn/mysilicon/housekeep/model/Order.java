package cn.mysilicon.housekeep.model;

public class Order {
    private int id;
    private long order_number;
    private int user_id;
    private int address_id;
    private int classification2_id;
    private String classification2_name;
    private int service_id;
    private String name;
    private String content;
    private String price;
    private String image;
    private String order_time;
    private String server_time;
    private String cur_status;
    private String address;

    public Order() {
    }

    public Order(int id, long order_number, int user_id, int address_id, int classification2_id, String classification2_name, int service_id, String name, String content, String price, String image, String order_time, String server_time, String cur_status, String address) {
        this.id = id;
        this.order_number = order_number;
        this.user_id = user_id;
        this.address_id = address_id;
        this.classification2_id = classification2_id;
        this.classification2_name = classification2_name;
        this.service_id = service_id;
        this.name = name;
        this.content = content;
        this.price = price;
        this.image = image;
        this.order_time = order_time;
        this.server_time = server_time;
        this.cur_status = cur_status;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOrder_number() {
        return order_number;
    }

    public void setOrder_number(long order_number) {
        this.order_number = order_number;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getClassification2_id() {
        return classification2_id;
    }

    public void setClassification2_id(int classification2_id) {
        this.classification2_id = classification2_id;
    }

    public String getClassification2_name() {
        return classification2_name;
    }

    public void setClassification2_name(String classification2_name) {
        this.classification2_name = classification2_name;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getCur_status() {
        return cur_status;
    }

    public void setCur_status(String cur_status) {
        this.cur_status = cur_status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
