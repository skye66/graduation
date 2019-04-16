package common;

/**
 * @Description 定义一些常用的变量
 * @Author Skye
 * @Date 2019/3/26 9:01
 * @Version 1.0
 **/
public class Const {
    /**
     * 用户登陆session key
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * 产品状态1，表示在售
     */
    public static final Integer ON_SALE = 1;

    /**
     * 限制产品数量成功，表示库存充足
     */
    public static final String LIMIT_CART_COUNT_SUCCESS="SUCCESS";
    /**
     * 限制产品数量失败，表示库存不够，将购物车中的数量更新为产品库存的最大值
     */
    public static final String LIMIT_CART_COUNT_FAIL="FAIL";
    /**
     * 购物车中产品的状态1为选中，0为未选中
     */
    public static final int CART_PRODUCT_CHECKED = 1;
    public static final int CART_PRODUCT_UNCHECKED=0;

    public static final String image_host="http://www.xxx.com";

    public static final String ORDER_BY_CREATE_TIME_DESC = "create_time desc";

    public interface Role{
        int ROLE_ADMIN = 1;
        int ROLE_USER=0;
    }
    public interface Type{
        String USERNAME="username";
        String EMAIL="email";
    }

    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已支付"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");
        int code;
        String msg;
        OrderStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
        public static OrderStatusEnum codeOf(int code){
            for (OrderStatusEnum item:
            OrderStatusEnum.values()){
                if (item.code==code) return item;
            }
            throw new RuntimeException("没有找到对应的枚举类");
        }
    }

    /**
     * 支付宝支付回调状态
     */
    public interface AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    /**
     * 支付平台类型
     */
    public enum PayPlatformEnum{
        ALIPAY(1, "支付宝");
        PayPlatformEnum(int code, String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }
    }

    /**
     * 支付类型
     */
    public enum PaymentStatusEnum{
        ONLINE_PAY(1,"在线支付");
        int code;
        String msg;
        PaymentStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }
        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
        public static PaymentStatusEnum codeOf(int code){
            for (PaymentStatusEnum item:
                    PaymentStatusEnum.values()){
                if (item.code==code) return item;
            }
            throw new RuntimeException("没有找到对应的枚举类");
        }
    }




}
