package ostro.veda.test;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ostro.veda.common.dto.CouponDTO;
import ostro.veda.config.AppConfig;
import ostro.veda.service.CouponServiceImpl;

import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class CouponServiceImplTest {

    public CouponDTO getCouponDTO() {
        return new CouponDTO(0, "SALES2025", "Sales 2025",
                "percentage", 15, LocalDateTime.now().plusDays(1),
                10, null, 0);
    }

    @Test
    public void add() {

        try {
            ResetTables.resetTables();
            ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
            CouponServiceImpl couponService = context.getBean(CouponServiceImpl.class);

            CouponDTO couponDTO = couponService.add(getCouponDTO());
            assertNotNull(couponDTO);
        } catch (BeansException e) {
            fail();
        }
    }

    @Test
    public void update() {
    }
}