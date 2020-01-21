package ee.luminor.payment.model;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class PaymentFactoryTest {

    @Test
    public void testPaymentType1Created() {
        BigDecimal amount = BigDecimal.ONE;
        String creditorIban = "EE123456789";
        String debtorIban = "DE987654321";
        String details = "Details for payment 1";
        Payment1 payment = (Payment1) Payment1.of()
                .setDetails(details)
                .setAmount(amount)
                .setCreditorIban(creditorIban)
                .setDebtorIban(debtorIban);

        assertThat(payment.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(payment.getAmount()).isEqualTo(amount);
        assertThat(payment.getCreditorIban()).isEqualTo(creditorIban);
        assertThat(payment.getDebtorIban()).isEqualTo(debtorIban);
        assertThat(payment.getDetails()).isEqualTo(details);
    }

    @Test
    public void testPaymentType2Created() {
        BigDecimal amount = BigDecimal.ZERO;
        String creditorIban = "EE123456789";
        String debtorIban = "DE987654321";
        String details = "Details for payment 2";
        Payment2 payment = (Payment2) Payment2.of()
                .setDetails(details)
                .setAmount(amount)
                .setCreditorIban(creditorIban)
                .setDebtorIban(debtorIban);

        assertThat(payment.getCurrency()).isEqualTo(Currency.USD);
        assertThat(payment.getAmount()).isEqualTo(amount);
        assertThat(payment.getCreditorIban()).isEqualTo(creditorIban);
        assertThat(payment.getDebtorIban()).isEqualTo(debtorIban);
        assertThat(payment.getDetails()).isEqualTo(details);
    }

    @Test
    public void testPaymentType3Created() {
        var amount = BigDecimal.TEN;
        var currency = Currency.EUR;
        var creditorIban = "EE123456789";
        var debtorIban = "DE987654321";
        var creditorBic = "Creditor BIC Code";
        Payment3 payment = (Payment3) Payment3.of()
                .setCreditorBic(creditorBic)
                .setAmount(amount)
                .setCurrency(currency)
                .setCreditorIban(creditorIban)
                .setDebtorIban(debtorIban);

        assertThat(payment.getCreditorBic()).isEqualTo(creditorBic);
        assertThat(payment.getAmount()).isEqualTo(amount);
        assertThat(payment.getCurrency()).isEqualTo(currency);
        assertThat(payment.getCreditorIban()).isEqualTo(creditorIban);
        assertThat(payment.getDebtorIban()).isEqualTo(debtorIban);
    }

}
