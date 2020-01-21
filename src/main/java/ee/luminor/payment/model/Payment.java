package ee.luminor.payment.model;

import lombok.Data;
import org.hibernate.annotations.DiscriminatorFormula;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("case when CREDITOR_BIC is null then "
        + "case when CURRENCY = 'EUR' then 1 "
        + "when CURRENCY = 'USD' then 2 end "
        + "else 3 end")
@Data
@SequenceGenerator(name = "PAYMENT_SEQ_GENERATOR", allocationSize = 1)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_SEQ")
    private Long id;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Currency - Not specified")
    private Currency currency;
    @NotNull(message = "Amount - Not specified")
    @DecimalMin(value = "0.0", message = "Amount - Must be greater than or equal to 0.0")
    @Digits(integer = 10, fraction = 2, message = "Amount - Numeric value out of bounds (<10 digits>.<2 digits> expected)")
    private BigDecimal amount;
    @NotNull(message = "Debtor IBAN - Not specified")
    @Pattern(regexp = "[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}", message = "Debtor - Invalid IBAN format")
    private String debtorIban;
    @NotNull(message = "Creditor IBAN - Not specified")
    @Pattern(regexp = "[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}", message = "Creditor - Invalid IBAN format")
    private String creditorIban;
    private LocalDateTime paymentDate;
    private BigDecimal cancellationFee;

}
