package ostro.veda.test;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.common.dto.CouponDTO;
import ostro.veda.config.AppConfig;
import ostro.veda.service.CouponServiceImpl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class CouponServiceImplTest {

    private static Helper helper = new Helper();

    @Test
    public void add() {

        ResetTables.resetTables();
        try {
            ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
            CouponServiceImpl couponService = context.getBean(CouponServiceImpl.class);

            CouponDTO couponDTO = couponService.add(helper.getCouponDTO());
            assertNotNull(couponDTO);
        } catch (BeansException e) {
            fail();
        }
    }

    @Test
    public void update() {

        ResetTables.resetTables();
    }
}