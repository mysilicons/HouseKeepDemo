package cn.mysilicon.housekeep.model;

public class DetailsBean {
    private Integer id;
    private String image_url;
    private String title;
    private String content;
    private String price;

    public DetailsBean(Integer id, String image_url, String title, String content, String price) {
        this.id = id;
        this.image_url = image_url;
        this.title = title;
        this.content = content;
        this.price = price;
    }

    public DetailsBean() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
