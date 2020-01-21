package ee.luminor.payment.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IdAndCancellationFee {

    private Long id;
    private BigDecimal cancellationFee;

}
