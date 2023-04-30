package cn.mysilicon.housekeep.model;

public class ServiceItemBean {
    private Integer id;
    private Integer classification1;
    private Integer classification2;
    private String image_url;
    private String title;
    private String content;
    private String price;
    private String city;

    public ServiceItemBean(Integer id, Integer classification1, Integer classification2, String image_url, String title, String content, String price, String city) {
        this.id = id;
        this.classification1 = classification1;
        this.classification2 = classification2;
        this.image_url = image_url;
        this.title = title;
        this.content = content;
        this.price = price;
        this.city = city;
    }

    public ServiceItemBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassification1() {
        return classification1;
    }

    public void setClassification1(Integer classification1) {
        this.classification1 = classification1;
    }

    public Integer getClassification2() {
        return classification2;
    }

    public void setClassification2(Integer classification2) {
        this.classification2 = classification2;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean contains(String query) {
        return title.contains(query) || content.contains(query);
    }
}
