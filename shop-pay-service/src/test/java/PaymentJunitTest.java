import com.my.shop.PayApplication;
import com.my.shop.common.constant.ShopCode;
import com.my.shop.pojo.Payment;
import com.my.shop.service.PayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/5/6
 */
@SpringBootTest(classes = PayApplication.class)
@RunWith(value = SpringRunner.class)
public class PaymentJunitTest {
    @Resource
    private PayService payService;

    /**
     * 测试创建订单
     */
    @Test
    public void createPayment(){
        Payment payment = new Payment();
        payment.setOrder_id(BigInteger.valueOf(6466742020145152l));
        payment.setIs_paid(ShopCode.SHOP_ORDER_PAY_STATUS_NO_PAY.getCode());
        payment.setPay_amount(0);
        payService.createPayment(payment);
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 测试 三方支付订单 回调
     */
    @Test
    public void callbackPayment(){
        Payment payment = new Payment();
        payment.setOrder_id(BigInteger.valueOf(6466742020145152l));
        payment.setIs_paid(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode());
        payment.setPay_amount(0);
        payment.setId(BigInteger.valueOf(6466744105304064l));
        payService.callbackPayment(payment);
        try {
            TimeUnit.SECONDS.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
