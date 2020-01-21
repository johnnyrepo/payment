package ee.luminor.payment.exception;

import lombok.Value;

@Value
public class PaymentProcessingException extends RuntimeException {

    private String message;

}
