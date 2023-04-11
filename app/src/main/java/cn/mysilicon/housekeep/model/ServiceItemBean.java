package cn.mysilicon.housekeep.model;

public class ServiceItemBean {
    private Integer id;
    private Integer classification;
    private String image_url;
    private String title;
    private String content;
    private String price;

    public ServiceItemBean(Integer id,Integer classification, String image_url, String title, String content, String price) {
        this.id = id;
        this.image_url = image_url;
        this.title = title;
        this.content = content;
        this.price = price;
    }

    public ServiceItemBean() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassification() {
        return classification;
    }

    public void setClassification(Integer classification) {
        this.classification = classification;
    }

    public String getURL() {
        return image_url;
    }

    public void setURL(String image_url) {
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

}
