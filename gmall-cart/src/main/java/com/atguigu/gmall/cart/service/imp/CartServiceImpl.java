package com.atguigu.gmall.cart.service.imp;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.cart.bean.Cart;
import com.atguigu.gmall.cart.bean.CartItem;
import com.atguigu.gmall.cart.bean.SkuResponse;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.pms.service.SkuStockService;
import com.atguigu.gmall.ums.entity.Member;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.redisson.api.RMap;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
@Component
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 几种容错模式；
     * 【failover】：失败重试其他服务器；
     * 【failfast】：失败就立即返回错误，也不重试。
     * failsafe：失败了写进日志，不管了。
     * failback：如果此次失败，等一会再发送请求。
     * forking：并行调用多个服务器，只要一个成功即返回。
     *
     */
    @Reference(cluster = "failover")
    ProductService productService;

    @Autowired
    RedissonClient redissonClient;

    @Reference
    SkuStockService skuStockService;


    public void limiter(){

        //简单限流；没有根据时间窗。
        RSemaphore orderCountId =
                redissonClient.getSemaphore("orderCountId");

        //orderCountId.acquire(); acquire到0
        orderCountId.tryAcquire();


        /**
         * 1、秒杀
         *      1）、限流？
         *      2）、业务层需要关注超卖问题；（减库存）
         *          1）、整点秒杀活动
         *              1、定时任务去将数据库中的需要秒杀的商品库存上架到redis；
         *              2、库存（过了时间点）减完，秒杀结束；
         *              3、每来一个商品减一个库存；
         *                  set(skuId,100);
         *                  getSemaphore(skuId)
         *
         *
         *
         */

    }

    /**
     *
     * 登陆不登录都带cart-key，没登陆我们会返回给cart-key，以后就用这个
     * 登陆了就额外加上自己的访问令牌：token
     *
     *
     *
     * @param skuId
     * @param num
     * @param cartKey
     * @return
     */
    @HystrixCommand(fallbackMethod = "reliable")
    @Override
    public SkuResponse addToCart(Long skuId, Integer num, String cartKey) {
        SkuResponse skuResponse = new SkuResponse();

        String token = RpcContext.getContext().getAttachment("gmallusertoken");//远程传输过来的
        System.out.println("有没有token？？？？"+token);
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);
        Long memberId = member==null?0L:member.getId();
        String memberName = member==null?"":member.getNickname();

        //1、查询这个sku信息，把他添加到购物车
        SkuStock skuStock =  productService.getSkuInfo(skuId);

        //2、查询出这个对应的spu信息
        Product product = productService.getProductByIdFromCache(skuStock.getProductId());
        //3、查询优惠券系统
        //List<Coupon> item = couponService.getCouponBtyProductID(product);
        //4、封装成一个cartItem；
        CartItem item = new CartItem(product.getId(),
                skuStock.getId(),
                memberId,
                num,
                skuStock.getPrice(),
                skuStock.getPrice(),
                num,
                skuStock.getSp1(), skuStock.getSp2(), skuStock.getSp3(),
                product.getPic(),
                product.getName(),
                memberName,
                product.getProductCategoryId(),
                product.getBrandName(),
                false,
                "满199减90"
        );


        //前端把key都带过来   gmall:cart:temp:uuuid
        if(StringUtils.isEmpty(memberJson)){
            //这个令牌没数据，没登录；离线购物车流程
            if(!StringUtils.isEmpty(cartKey)){
                skuResponse.setCartKey(cartKey);
                //用户有老购物车
                cartKey = RedisCacheConstant.CART_TEMP +cartKey;

                addItemToCart(item,num,cartKey);
            }else {
                //新建一个购物车，以后用这个
                String replace = UUID.randomUUID().toString().replace("-", "");
                String newCartKey = RedisCacheConstant.CART_TEMP+replace;
                skuResponse.setCartKey(replace);
                addItemToCart(item,num,newCartKey);
            }
        }else {
            //在线购物车流程
            String loginCartKey = RedisCacheConstant.USER_CART+member.getId();
            //合并购物车；

            mergeCart(RedisCacheConstant.CART_TEMP+cartKey,loginCartKey);
            //5、放入购物车
            addItemToCart(item,num,loginCartKey);
        }

        skuResponse.setItem(item);



        return skuResponse;
    }

    public SkuResponse reliable(Long skuId, Integer num, String cartKey){
        return null;
    }


    @Override
    public boolean updateCount(Long skuId, Integer num, String cartKey) {

        String token = RpcContext.getContext().getAttachment("gmallusertoken");//远程传输过来的
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);
        RMap<String, String> map = null;
        if(member == null){
            //用户未登录
            map = redissonClient.getMap(RedisCacheConstant.CART_TEMP + cartKey);
        }else {
            //用户登陆
            map = redissonClient.getMap(RedisCacheConstant.USER_CART + member.getId());
        }
        String s = map.get(skuId + "");
        CartItem item = JSON.parseObject(s, CartItem.class);
        item.setNum(num);
        String json = JSON.toJSONString(item);
        map.put(skuId + "",json);

        return true;
    }

    @Override
    public boolean deleteCart(Long skuId, String cartKey) {
        String token = RpcContext.getContext().getAttachment("gmallusertoken");//远程传输过来的
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);
        RMap<String, String> map = null;
        if(member == null){
            //用户未登录
            map = redissonClient.getMap(RedisCacheConstant.CART_TEMP + cartKey);
        }else {
            //用户登陆
            map = redissonClient.getMap(RedisCacheConstant.USER_CART + member.getId());
        }
        map.remove(skuId+"");
        return true;
    }

    @Override
    public boolean checkCart(Long skuId, Integer flag, String cartKey) {
        String token = RpcContext.getContext().getAttachment("gmallusertoken");//远程传输过来的
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);
        RMap<String, String> map = null;
        if(member == null){
            //用户未登录
            map = redissonClient.getMap(RedisCacheConstant.CART_TEMP + cartKey);
        }else {
            //用户登陆
            map = redissonClient.getMap(RedisCacheConstant.USER_CART + member.getId());
        }

        String s = map.get(skuId + "");
        CartItem item = JSON.parseObject(s, CartItem.class);
        item.setChecked(flag==0?false:true);
        String json = JSON.toJSONString(item);
        map.put(skuId + "",json);

        //维护checked字段的set
        String checked = map.get("checked");
        Set<String> checkedSkuIds = new HashSet<>();
        //复杂的泛型数据转换
        if(!StringUtils.isEmpty(checked)){
            //有
            Set<String> strings = JSON.parseObject(checked, new TypeReference<Set<String>>() {
            });
            if(flag == 0){
                //不勾中
                strings.remove(skuId+"");
            }else {
                strings.add(skuId+"");
            }

            String s1 = JSON.toJSONString(strings);
            map.put("checked",s1);

        }else {
            //没有
            checkedSkuIds.add(skuId+"");
            String s1 = JSON.toJSONString(checkedSkuIds);
            map.put("checked",s1);
        }
       

        return true;
    }




    @Override
    public Cart cartItemsList(String cartKey) {
        String token = RpcContext.getContext().getAttachment("gmallusertoken");//远程传输过来的
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);
        RMap<String, String> map = null;
        if(member == null){
            //用户未登录
            map = redissonClient.getMap(RedisCacheConstant.CART_TEMP + cartKey);
        }else {
            //用户登陆
            //尝试合并购物车
            mergeCart(RedisCacheConstant.CART_TEMP+cartKey,RedisCacheConstant.USER_CART+member.getId());

            //合并完成后再操作
            map = redissonClient.getMap(RedisCacheConstant.USER_CART + member.getId());
            //
        }


        if(map !=null){
            Cart cart = new Cart();
            cart.setItems(new ArrayList<CartItem>());
            map.entrySet().forEach((o)->{

                if(!o.getKey().equals("checked")){
                    String json = o.getValue();
                    CartItem item = JSON.parseObject(json, CartItem.class);
                    cart.getItems().add(item);
                }
            });

            return  cart;
        }else {
            return new Cart();
        }
    }

    @Override
    public Cart cartItemsForLoginUser(String token) {
        return null;
    }

    @Override
    public List<CartItem> cartItemsForJieSuan(String token) {
        //1、根据用户令牌查出用户购物车信息
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);

        Long id = member.getId();
        String cartKey = RedisCacheConstant.USER_CART + member.getId();

        //获取到的整个购物车
        RMap<String, String> map = redissonClient.getMap(cartKey);

        String checked = map.get("checked");
        Set<String> checkedItems = JSON.parseObject(checked, new TypeReference<Set<String>>() {
        });



        //购物车选中的购物项
        List<CartItem> cartItems = new ArrayList<>();
        
        if(checkedItems!=null&&!checkedItems.isEmpty()){
            checkedItems.forEach((e)->{
                String s = map.get(e);
                CartItem item = JSON.parseObject(s, CartItem.class);
                //查询商品的最新价格
                Long skuId = item.getProductSkuId();
                //远程查询最新价格
                //getSkuPriceById   查缓存（读写锁）
                BigDecimal price = skuStockService.getSkuPriceById(skuId);
                if(item.getNewPrice()!=null){
                    item.setPrice(item.getNewPrice());
                }
                item.setNewPrice(price);
                cartItems.add(item);
            });
        }


        //对比购物项的价格
        return cartItems;
    }

    //给购物车添加一项
    //1）、第一次用购物车的时候都必须合并
    //1、查看购物车数据
    //2、加入购物车需要合并
    private void addItemToCart(CartItem item,Integer num,String cartKey){
        //1、拿到购物车
        RMap<String, String> map = redissonClient.getMap(cartKey);
        //2、先看这个有没有
        boolean b = map.containsKey(item.getProductSkuId()+"");
        if(b){
            //3、购物车已有此项
            String json = map.get(item.getProductSkuId()+"");
            CartItem cartItem = JSON.parseObject(json, CartItem.class);
            cartItem.setNum(cartItem.getNum() + num);
            String string = JSON.toJSONString(cartItem);
            map.put(item.getProductSkuId()+"",string);

        }else {
            String string = JSON.toJSONString(item);
            map.put(item.getProductSkuId()+"",string);
        }


    }


    private void mergeCart(String oldCartKey,String newCartKey){
        //2、获取新购物车
       // RMap<String, String> loginCart = redissonClient.getMap(newCartKey);

        //1、获取老购物车
        RMap<String, String> map = redissonClient.getMap(oldCartKey);

        if(map!=null&&map.entrySet()!=null){
            map.entrySet().forEach((entry)->{
                String key = entry.getKey();
                if(!key.equals("checked")){
                    String value = entry.getValue();
                    CartItem item = JSON.parseObject(value, CartItem.class);
                    //将老购物车的数据转移过去
                    addItemToCart(item,item.getNum(),newCartKey);
                    map.remove(item.getProductSkuId()+"");
                }

            });
        }
    }









}
