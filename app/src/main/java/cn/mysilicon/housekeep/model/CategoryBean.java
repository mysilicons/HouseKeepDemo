package cn.mysilicon.housekeep.model;

import java.io.Serializable;
import java.util.List;

public class CategoryBean implements Serializable {

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {


        private String type;
        private String moduleTitle;
        private String moreUniversalNavigator;
        private List<DataListBean> dataList;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getModuleTitle() {
            return moduleTitle;
        }

        public void setModuleTitle(String moduleTitle) {
            this.moduleTitle = moduleTitle;
        }

        public String getMoreUniversalNavigator() {
            return moreUniversalNavigator;
        }

        public void setMoreUniversalNavigator(String moreUniversalNavigator) {
            this.moreUniversalNavigator = moreUniversalNavigator;
        }

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public static class DataListBean {

            private String id;
            private String type;
            private String title;
            private String imgURL;
            private String universalNavigator;

            public DataListBean() {
                super();
            }

            public DataListBean(String type) {
                this.type = type;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImgURL() {
                return imgURL;
            }

            public void setImgURL(String imgURL) {
                this.imgURL = imgURL;
            }

            public String getUniversalNavigator() {
                return universalNavigator;
            }

            public void setUniversalNavigator(String universalNavigator) {
                this.universalNavigator = universalNavigator;
            }
        }
    }
}
