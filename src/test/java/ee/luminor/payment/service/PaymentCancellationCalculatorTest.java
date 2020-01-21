package ee.luminor.payment.service;

import ee.luminor.payment.model.Currency;
import ee.luminor.payment.model.Payment1;
import ee.luminor.payment.model.Payment2;
import ee.luminor.payment.model.Payment3;
import ee.luminor.payment.util.PaymentCreator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PaymentCancellationCalculatorTest {

    private final BigDecimal PAYMENT_1_COEFFICIENT = BigDecimal.valueOf(0.05);
    private final BigDecimal PAYMENT_2_COEFFICIENT = BigDecimal.valueOf(0.1);
    private final BigDecimal PAYMENT_3_COEFFICIENT = BigDecimal.valueOf(0.15);
    private final BigDecimal USD_TO_EUR_RATE = BigDecimal.valueOf(0.902248);

    @Autowired
    private PaymentCancellationCalculator calculator;

    @Test
    public void testIsValidForCancellationTrue() {
        var now = LocalDateTime.now();
        var paymentDate = now.minus(1, ChronoUnit.HOURS);
        Payment1 payment1 = PaymentCreator.createPayment1(1L, "Details 1 for Payment1", "EE1234567890123", "DE9876543210987", BigDecimal.valueOf(123.45), paymentDate);

        assertTrue(calculator.isValidForCancellation(payment1));
    }

    @Test
    public void testIsValidForCancellationFalseWhenPaymentDateIsTooOld() {
        var now = LocalDateTime.now();
        var paymentDate = now.minus(24, ChronoUnit.HOURS);
        System.out.println(now);
        System.out.println(paymentDate);
        Payment1 payment1 = PaymentCreator.createPayment1(1L, "Details 1 for Payment1", "EE1234567890123", "DE9876543210987", BigDecimal.valueOf(123.45), paymentDate);

        assertFalse(calculator.isValidForCancellation(payment1));
    }

    @Test
    public void testIsValidForCancellationFalseWhenCancellationFeeIsSet() {
        var now = LocalDateTime.now();
        var paymentDate = now.minusHours(1);
        Payment1 payment1 = (Payment1) PaymentCreator.createPayment1(1L, "Details 1 for Payment1", "EE1234567890123", "DE9876543210987", BigDecimal.valueOf(123.45), paymentDate)
                .setCancellationFee(BigDecimal.ONE);

        assertFalse(calculator.isValidForCancellation(payment1));
    }

    @Test
    public void testCancelPayment1() {
        var now = LocalDateTime.now();
        var hours = now.getHour();
        var paymentDate = now.minusHours(hours);
        Payment1 payment1 = (Payment1) PaymentCreator.createPayment1(1L, "Details 1 for Payment1", "EE1234567890123", "DE9876543210987", BigDecimal.valueOf(123.45), paymentDate)
                .setCancellationFee(BigDecimal.ONE);

        var expectedFee = BigDecimal.valueOf(hours).multiply(PAYMENT_1_COEFFICIENT);

        calculator.cancel(payment1);

        assertEquals(payment1.getCancellationFee(), expectedFee);
    }

    @Test
    public void testCancelPayment2() {
        var now = LocalDateTime.now();
        var hours = now.getHour();
        var paymentDate = now.minusHours(hours);
        Payment2 payment2 = PaymentCreator.createPayment2(2L, "Details 1 for Payment2", "EE9876543210987", "US1233214566549", BigDecimal.valueOf(1001.01), paymentDate);

        var expectedFee = BigDecimal.valueOf(hours).multiply(PAYMENT_2_COEFFICIENT).multiply(USD_TO_EUR_RATE);

        calculator.cancel(payment2);

        assertEquals(payment2.getCancellationFee(), expectedFee);
    }

    @Test
    public void testCancelPayment3() {
        var now = LocalDateTime.now();
        var hours = now.getHour();
        var paymentDate = now.minusHours(hours);
        Payment3 payment3 = PaymentCreator.createPayment3(3L, "TRWSCA15", Currency.USD, "LV4561237891234", "US1233214566549", BigDecimal.valueOf(500005.55), paymentDate);

        var expectedFee = BigDecimal.valueOf(hours).multiply(PAYMENT_3_COEFFICIENT).multiply(USD_TO_EUR_RATE);

        calculator.cancel(payment3);

        assertEquals(payment3.getCancellationFee(), expectedFee);
    }

}
