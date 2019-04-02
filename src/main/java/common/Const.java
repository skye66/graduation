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
     * 产品状态0，表示在售
     */
    public static final Integer ON_SALE = 0;

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




}
