package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton_X() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // Thread A: A 사용자 10000원 주문
        statefulService1.order("userA", 10000);

        // Thread B: B 사용자 20000원 주문
        statefulService2.order("userB", 20000);

        // Thread A: 사용자 A 주문 금액 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    @Test
    void statefulServiceSingleton_O() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // Thread A: A 사용자 10000원 주문
        int userAPrice = statefulService1.order2("userA", 10000);

        // Thread B: B 사용자 20000원 주문
        int userBPrice = statefulService2.order2("userB", 20000);

        Assertions.assertThat(userAPrice).isNotEqualTo(userBPrice);
    }

    @Configuration
    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

}