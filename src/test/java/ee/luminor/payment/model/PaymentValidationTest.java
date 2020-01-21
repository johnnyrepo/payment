package ee.luminor.payment.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Random;
import java.util.Set;

public class PaymentValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testPaymentType1IsValid() {
        Payment1 payment = (Payment1) Payment1.of()
                .setDetails("Details")
                .setDebtorIban("EE1234567890123")
                .setCreditorIban("DE9876543210987")
                .setAmount(BigDecimal.ONE);

        Set<ConstraintViolation<Payment1>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void testPaymentType1DebtorIbanIsMissing() {
        Payment1 payment = (Payment1) Payment1.of()
                .setDetails("details")
                .setCreditorIban("DE9876543210987")
                .setAmount(BigDecimal.ONE);

        Set<ConstraintViolation<Payment1>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo("Debtor IBAN - Not specified"));
    }

    @Test
    public void testPaymentType1CreditorIbanIsMissing() {
        Payment1 payment = (Payment1) Payment1.of()
                .setDetails("details")
                .setDebtorIban("EE1234567890123")
                .setAmount(BigDecimal.ONE);

        Set<ConstraintViolation<Payment1>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo("Creditor IBAN - Not specified"));
    }

    @Test
    public void testPaymentType1AmountIsMissing() {
        Payment1 payment = (Payment1) Payment1.of()
                .setDetails("Details")
                .setDebtorIban("EE1234567890123")
                .setCreditorIban("DE9876543210987");

        Set<ConstraintViolation<Payment1>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo("Amount - Not specified"));
    }

    @Test
    public void testPaymentType1DetailsIsMissing() {
        Payment1 payment = (Payment1) Payment1.of()
                .setDebtorIban("EE1234567890123")
                .setCreditorIban("DE9876543210987")
                .setAmount(BigDecimal.ONE);

        Set<ConstraintViolation<Payment1>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo("Details - Not specified for this type of payment(Type 1)"));
    }

    @Test
    public void testPaymentType1DetailsLengthIsOutOfRange() {
        var random = new Random();
        var veryLongString = random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(3001)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        Payment1 payment = (Payment1) Payment1.of()
                .setDetails(veryLongString)
                .setDebtorIban("EE1234567890123")
                .setCreditorIban("DE9876543210987")
                .setAmount(BigDecimal.ONE);

        Set<ConstraintViolation<Payment1>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo("Details - has to be up to 3000 characters long"));
    }

    @Test
    public void testPaymentType1AmountIsNegative() {
        Payment1 payment = (Payment1) Payment1.of()
                .setDetails("details")
                .setDebtorIban("EE1234567890123")
                .setCreditorIban("DE9876543210987")
                .setAmount(BigDecimal.valueOf(-2.5));

        Set<ConstraintViolation<Payment1>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo("Amount - Must be greater than or equal to 0.0"));
    }

    @Test
    public void testPaymentType1AmountFractionIsTooSmall() {
        Payment1 payment = (Payment1) Payment1.of()
                .setDetails("details")
                .setDebtorIban("EE1234567890123")
                .setCreditorIban("DE9876543210987")
                .setAmount(BigDecimal.valueOf(12.505));

        Set<ConstraintViolation<Payment1>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo("Amount - Numeric value out of bounds (<10 digits>.<2 digits> expected)"));
    }

    @Test
    public void testPaymentType1AmountFractionIsTooBig() {
        Payment1 payment = (Payment1) Payment1.of()
                .setDetails("details")
                .setDebtorIban("EE1234567890123")
                .setCreditorIban("DE9876543210987")
                .setAmount(BigDecimal.valueOf(1234567890123.35));

        Set<ConstraintViolation<Payment1>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo("Amount - Numeric value out of bounds (<10 digits>.<2 digits> expected)"));
    }

    @Test
    public void testPaymentType1DebtorIbanIsInvalid() {
        Payment1 payment = (Payment1) Payment1.of()
                .setDetails("details")
                .setCreditorIban("DE9876543210987")
                .setDebtorIban("INCORRECT IBAN")
                .setAmount(BigDecimal.ONE);

        Set<ConstraintViolation<Payment1>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo("Debtor - Invalid IBAN format"));
    }

    @Test
    public void testPaymentType1CreditorIbanIsInvalid() {
        Payment1 payment = (Payment1) Payment1.of()
                .setDetails("details")
                .setDebtorIban("EE1234567890123")
                .setCreditorIban("INCORRECT IBAN")
                .setAmount(BigDecimal.ONE);

        Set<ConstraintViolation<Payment1>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo("Creditor - Invalid IBAN format"));
    }

    @Test
    public void testPaymentType3IsValid() {
        Payment3 payment = (Payment3) Payment3.of()
                .setCreditorBic("REVOGB21")
                .setDebtorIban("EE1234567890123")
                .setCreditorIban("DE9876543210987")
                .setCurrency(Currency.EUR)
                .setAmount(BigDecimal.ONE);

        Set<ConstraintViolation<Payment3>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void testPaymentType3CreditorBicIsInvalid() {
        Payment3 payment = (Payment3) Payment3.of()
                .setCreditorBic("INVALID BIC CODE")
                .setDebtorIban("EE1234567890123")
                .setCreditorIban("DE9876543210987")
                .setCurrency(Currency.EUR)
                .setAmount(BigDecimal.ONE);

        Set<ConstraintViolation<Payment3>> violations = validator.validate(payment);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo("Creditor Bank BIC code - Invalid format"));
    }

}
