package ee.luminor.payment.controller;

import ee.luminor.payment.model.*;
import ee.luminor.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.findAllPayments();
    }

    @GetMapping("/{id}")
    public IdAndCancellationFee getPaymentCancellationFeeById(@PathVariable("id") Long id) {
        var payment = paymentService.findPaymentById(id);

        return new IdAndCancellationFee().setId(payment.getId())
                .setCancellationFee(payment.getCancellationFee());
    }

    @GetMapping("/non-cancelled")
    public List<Long> getNonCancelledPaymentIds(@RequestParam("minAmount") Optional<BigDecimal> minAmount,
                                                @RequestParam("maxAmount") Optional<BigDecimal> maxAmount) {
        return paymentService.findNonCancelledPaymentIds(minAmount.orElse(null), maxAmount.orElse(null));
    }

    @PatchMapping("/{id}")
    public Payment cancelPayment(@PathVariable("id") Long id) {
        return paymentService.cancelPayment(id);
    }

    @Validated
    @PostMapping("/payment1")
    public void savePayment1(@RequestBody Payment1 payment1) {
        paymentService.savePayment(payment1);
    }

    @Validated
    @PostMapping("/payment2")
    public void savePayment2(@RequestBody Payment2 payment2) {
        paymentService.savePayment(payment2);
    }

    @Validated
    @PostMapping("/payment3")
    public void savePayment3(@RequestBody Payment3 payment3) {
        paymentService.savePayment(payment3);
    }

}
