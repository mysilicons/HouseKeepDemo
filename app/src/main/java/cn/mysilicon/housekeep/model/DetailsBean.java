package cn.mysilicon.housekeep.model;

public class DetailsBean {
    private Integer id;
    private String img;
    private String title;
    private String content;
    private String price;

    public DetailsBean(Integer id, String img, String title, String content) {
        this.id = id;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
