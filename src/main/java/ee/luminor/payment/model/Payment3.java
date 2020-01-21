package ee.luminor.payment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@DiscriminatorValue("3")
@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
public class Payment3 extends Payment {

    @NotNull(message = "Creditor Bank BIC code - Not specified, but is mandatory for this type of payment(Type 3)")
    @Pattern(regexp = "[a-zA-Z]{6}[0-9a-zA-Z]{2}([0-9a-zA-Z]{3})?", message = "Creditor Bank BIC code - Invalid format")
    private String creditorBic;

}
