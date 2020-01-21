package ee.luminor.payment.service;

import ee.luminor.payment.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class PaymentCancellationCalculator {

    private final BigDecimal PAYMENT_1_COEFFICIENT = BigDecimal.valueOf(0.05);
    private final BigDecimal PAYMENT_2_COEFFICIENT = BigDecimal.valueOf(0.1);
    private final BigDecimal PAYMENT_3_COEFFICIENT = BigDecimal.valueOf(0.15);
    private final BigDecimal USD_TO_EUR_RATE = BigDecimal.valueOf(0.902248);

    public boolean isValidForCancellation(Payment payment) {
        var now = LocalDateTime.now();
        return payment.getCancellationFee() == null
                && payment.getPaymentDate().until(now, ChronoUnit.YEARS) == 0
                && payment.getPaymentDate().until(now, ChronoUnit.MONTHS) == 0
                && payment.getPaymentDate().until(now, ChronoUnit.DAYS) == 0
                && payment.getPaymentDate().until(now, ChronoUnit.HOURS) < 24;
    }

    public void cancel(Payment payment) {
        long hours = payment.getPaymentDate().until(LocalDateTime.now(), ChronoUnit.HOURS);
        BigDecimal coefficient = null;
        if (payment instanceof Payment1) {
            coefficient = PAYMENT_1_COEFFICIENT;
        } else if (payment instanceof Payment2) {
            coefficient = PAYMENT_2_COEFFICIENT;
        } else if (payment instanceof Payment3) {
            coefficient = PAYMENT_3_COEFFICIENT;
        }

        BigDecimal cancellationFee = BigDecimal.valueOf(hours).multiply(coefficient);
        payment.setCancellationFee(payment.getCurrency() == Currency.EUR ?
                cancellationFee :
                cancellationFee.multiply(USD_TO_EUR_RATE));
    }

}
