package ee.luminor.payment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
@DiscriminatorValue("2")
@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
public class Payment2 extends Payment {

    {
        setCurrency(Currency.USD);
    }

    @Size(max = 3000, message = "Details - has to be up to 3000 characters long")
    private String details;

}
