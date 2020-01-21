package ee.luminor.payment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@DiscriminatorValue("1")
@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
public class Payment1 extends Payment {

    {
        setCurrency(Currency.EUR);
    }

    @NotNull(message = "Details - Not specified for this type of payment(Type 1)")
    @Size(max = 3000, message = "Details - has to be up to 3000 characters long")
    private String details;

}
