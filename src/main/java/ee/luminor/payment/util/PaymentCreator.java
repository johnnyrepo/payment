package ee.luminor.payment.util;

import ee.luminor.payment.model.Currency;
import ee.luminor.payment.model.Payment1;
import ee.luminor.payment.model.Payment2;
import ee.luminor.payment.model.Payment3;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentCreator {

    public static Payment1 createPayment1(Long id, String details, String debtorIban, String creditorIban, BigDecimal amount, LocalDateTime date) {
        return (Payment1) Payment1.of()
                .setDetails(details)
                .setId(id)
                .setDebtorIban(debtorIban)
                .setCreditorIban(creditorIban)
                .setAmount(amount)
                .setPaymentDate(date);
    }

    public static Payment2 createPayment2(Long id, String details, String debtorIban, String creditorIban, BigDecimal amount, LocalDateTime date) {
        return (Payment2) Payment2.of()
                .setDetails(details)
                .setId(id)
                .setDebtorIban(debtorIban)
                .setCreditorIban(creditorIban)
                .setAmount(amount)
                .setPaymentDate(date);
    }

    public static Payment3 createPayment3(Long id, String creditorBic, Currency currency, String debtorIban, String creditorIban, BigDecimal amount, LocalDateTime date) {
        return (Payment3) Payment3.of()
                .setCreditorBic(creditorBic)
                .setId(id)
                .setCurrency(currency)
                .setDebtorIban(debtorIban)
                .setCreditorIban(creditorIban)
                .setAmount(amount)
                .setPaymentDate(date);
    }

}
