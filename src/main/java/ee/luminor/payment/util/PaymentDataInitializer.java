package ee.luminor.payment.util;

import ee.luminor.payment.model.Currency;
import ee.luminor.payment.model.Payment1;
import ee.luminor.payment.model.Payment2;
import ee.luminor.payment.model.Payment3;
import ee.luminor.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PaymentDataInitializer implements InitializingBean {

    private final PaymentService paymentService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Payment1 payment1 = PaymentCreator.createPayment1(null, "Details 1 for Payment1", "EE1234567890123", "DE9876543210987", BigDecimal.valueOf(10.01), LocalDateTime.of(2020, 1, 15, 15, 0));
        Payment1 payment11 = (Payment1) PaymentCreator.createPayment1(null, "Details 1 for Payment11", "EE3453768546579", "DE9876543210987", BigDecimal.valueOf(345.6), LocalDateTime.of(2020, 1, 15, 18, 0))
                .setCancellationFee(BigDecimal.valueOf(1.07));
        Payment1 payment111 = PaymentCreator.createPayment1(null, "Details 1 for Payment111", "EE1234567890123", "DE9876543210987", BigDecimal.valueOf(1023.45), LocalDateTime.now().minusHours(1));
        Payment2 payment2 = PaymentCreator.createPayment2(null, "Details 1 for Payment2", "EE9876543210987", "US1233214566549", BigDecimal.valueOf(123.45), LocalDateTime.of(2020, 1, 16, 16, 0));
        Payment2 payment22 = (Payment2) PaymentCreator.createPayment2(null, "Details 1 for Payment22", "EE9876543210987", "US1233214566549", BigDecimal.valueOf(3030.15), LocalDateTime.of(2020, 1, 16, 19, 0))
                .setCancellationFee(BigDecimal.valueOf(2.08));
        Payment2 payment222 = PaymentCreator.createPayment2(null, "Details 1 for Payment222", "EE9876543210987", "US1233214566549", BigDecimal.valueOf(70007.17), LocalDateTime.now().minusHours(2));
        Payment3 payment3 = PaymentCreator.createPayment3(null, "TRWSCA15", Currency.USD, "LV4561237891234", "US1233214566549", BigDecimal.valueOf(5005.55), LocalDateTime.of(2020, 1, 17, 17, 0));
        Payment3 payment33 = (Payment3) PaymentCreator.createPayment3(null, "TRWSCA15", Currency.USD, "LV4561237891234", "US1233214566549", BigDecimal.valueOf(90000.99), LocalDateTime.of(2020, 1, 17, 21, 0))
                .setCancellationFee(BigDecimal.valueOf(3.09));
        Payment3 payment333 = PaymentCreator.createPayment3(null, "TRWSCA15", Currency.EUR, "LV4561237891234", "US1233214566549", BigDecimal.valueOf(500500.55), LocalDateTime.now().minusHours(3));

        paymentService.savePayment(payment1);
        paymentService.savePayment(payment11);
        paymentService.savePayment(payment111);
        paymentService.savePayment(payment2);
        paymentService.savePayment(payment22);
        paymentService.savePayment(payment222);
        paymentService.savePayment(payment3);
        paymentService.savePayment(payment33);
        paymentService.savePayment(payment333);
    }

}
