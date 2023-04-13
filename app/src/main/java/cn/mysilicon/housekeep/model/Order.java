package cn.mysilicon.housekeep.model;

public class Order {
    private int id;
    private int shop_id;
    private String shop_name;
    private int product_id;
    private String title;
    private String content;
    private String price;
    private String image_url;
    private String order_time;
    private String cur_status;

    public Order() {
    }

    public Order(int id, int shop_id, String shop_name, int product_id, String title, String content, String price, String image_url, String order_time, String cur_status) {
        this.id = id;
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.product_id = product_id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.image_url = image_url;
        this.order_time = order_time;
        this.cur_status = cur_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getCur_status() {
        return cur_status;
    }

    public void setCur_status(String cur_status) {
        this.cur_status = cur_status;
    }

}
