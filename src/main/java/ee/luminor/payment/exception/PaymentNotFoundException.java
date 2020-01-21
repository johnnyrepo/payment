package ee.luminor.payment.exception;

import lombok.Value;

@Value
public class PaymentNotFoundException extends RuntimeException {

    private String message;

}
