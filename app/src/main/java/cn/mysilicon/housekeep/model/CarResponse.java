package cn.mysilicon.housekeep.model;

import java.util.List;

/**
 * 购物车返回数据
 *
 * @author llw
 */
public class CarResponse {


    /**
     * code : 200
     * orderData : [{"shopId":1,"shopName":"京东自营","cartlist":[{"id":1,"shopId":1,"shopName":"京东自营","defaultPic":"https://img30.360buyimg.com/popWareDetail/jfs/t3208/194/7616404169/244198/369625db/58b7d093N03520fb7.jpg","productId":1,"productName":"三只松鼠_零食大礼包","color":"黑色","size":"18L","price":20,"count":1},{"id":2,"shopId":1,"shopName":"京东自营","defaultPic":"https://img14.360buyimg.com/n0/jfs/t2971/15/167732091/93002/204c1016/574d9d9aNe4e6fa7a.jpg","productId":2,"productName":"小米心跳手环","color":"白色","size":"20XXL","price":148,"count":1}]},{"shopId":2,"shopName":"海澜之家","cartlist":[{"id":1,"shopId":2,"shopName":"海澜之家","defaultPic":"https://img30.360buyimg.com/popWaterMark/jfs/t4075/83/1343091204/132469/9034cb9c/5873496bN68020ba8.jpg","productId":1,"productName":"短袖T恤男 2017夏季新品","color":"蓝色","size":"30X","price":181,"count":1}]},{"shopId":3,"shopName":"OPPO官方旗舰店","cartlist":[{"id":1,"shopId":3,"shopName":"OPPO官方旗舰店","defaultPic":"https://img10.360buyimg.com/cms/jfs/t6064/272/2163314583/157700/442d6477/593c1c49N7c63a7d9.jpg","productId":1,"productName":"OPPO R11 全网通","color":"蓝色","size":"30X","price":1999,"count":1},{"id":2,"shopId":3,"shopName":"OPPO官方旗舰店","defaultPic":"https://img14.360buyimg.com/n0/jfs/t3142/194/4953241722/254855/1651c2b1/585b9021Nf653e48a.jpg","productId":1,"productName":"OPPO R9 全网通","color":"蓝色","size":"30X","price":999,"count":1}]}]
     */


    private int code;
    private List<OrderDataBean> orderData;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<OrderDataBean> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<OrderDataBean> orderData) {
        this.orderData = orderData;
    }

    public static class OrderDataBean {
        /**
         * shopId : 1
         * shopName : 京东自营
         * cartlist : [{"id":1,"shopId":1,"shopName":"京东自营","defaultPic":"https://img30.360buyimg.com/popWareDetail/jfs/t3208/194/7616404169/244198/369625db/58b7d093N03520fb7.jpg","productId":1,"productName":"三只松鼠_零食大礼包","color":"黑色","size":"18L","price":20,"count":1},{"id":2,"shopId":1,"shopName":"京东自营","defaultPic":"https://img14.360buyimg.com/n0/jfs/t2971/15/167732091/93002/204c1016/574d9d9aNe4e6fa7a.jpg","productId":2,"productName":"小米心跳手环","color":"白色","size":"20XXL","price":148,"count":1}]
         */

        private int classification2_id;
        private String classification2_name;
        private List<CartlistBean> collectionList;
        private boolean isChecked;//店铺是否选中

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

        public List<CartlistBean> getCollectionList() {
            return collectionList;
        }

        public void setCollectionList(List<CartlistBean> collectionList) {
            this.collectionList = collectionList;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public static class CartlistBean {
            /**
             * id : 1
             * shopId : 1
             * shopName : 京东自营
             * defaultPic : https://img30.360buyimg.com/popWareDetail/jfs/t3208/194/7616404169/244198/369625db/58b7d093N03520fb7.jpg
             * productId : 1
             * productName : 三只松鼠_零食大礼包
             * color : 黑色
             * size : 18L
             * price : 20
             * count : 1
             */

            private int id;
            private int classification2_id;
            private String classification2_name;
            private String image;
            private int service_id;
            private String name;
            private double price;
            private boolean isChecked;//商品是否选中

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
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

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }
        }
    }
}

