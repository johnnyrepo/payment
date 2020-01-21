package ee.luminor.payment.service;

import ee.luminor.payment.exception.PaymentNotFoundException;
import ee.luminor.payment.exception.PaymentProcessingException;
import ee.luminor.payment.model.Payment;
import ee.luminor.payment.model.Payment1;
import ee.luminor.payment.model.Payment2;
import ee.luminor.payment.model.Payment3;
import ee.luminor.payment.repository.Payment1Repository;
import ee.luminor.payment.repository.Payment2Repository;
import ee.luminor.payment.repository.Payment3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final Payment1Repository payment1Repository;
    private final Payment2Repository payment2Repository;
    private final Payment3Repository payment3Repository;
    private final PaymentCancellationCalculator calculator;

    public List<Payment> findAllPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.addAll(payment1Repository.findAll());
        payments.addAll(payment2Repository.findAll());
        payments.addAll(payment3Repository.findAll());

        return payments;
    }

    public Payment findPaymentById(Long id) throws PaymentNotFoundException {
        return payment1Repository.findById(id)
                .map(payment1 -> (Payment) payment1)
                .orElseGet(() -> payment2Repository.findById(id)
                        .map(payment2 -> (Payment) payment2)
                        .orElseGet(() -> payment3Repository.findById(id)
                                .map(payment3 -> (Payment) payment3)
                                .orElseThrow(() -> new PaymentNotFoundException("No Payment found with id=" + id))
                        ));
    }

    public List<Long> findNonCancelledPaymentIds(BigDecimal minAmount, BigDecimal maxAmount) {
        List<Long> ids = new ArrayList<>();

        if (minAmount == null && maxAmount == null) {
            ids = payment1Repository.findNonCancelledIds();
            ids.addAll(payment2Repository.findNonCancelledIds());
            ids.addAll(payment3Repository.findNonCancelledIds());
        } else if (minAmount == null && maxAmount != null) {
            ids = payment1Repository.findNonCancelledIdsWithMaxAmount(maxAmount);
            ids.addAll(payment2Repository.findNonCancelledIdsWithMaxAmount(maxAmount));
            ids.addAll(payment3Repository.findNonCancelledIdsWithMaxAmount(maxAmount));
        } else if (minAmount != null && maxAmount == null) {
            ids = payment1Repository.findNonCancelledIdsWithMinAmount(minAmount);
            ids.addAll(payment2Repository.findNonCancelledIdsWithMinAmount(minAmount));
            ids.addAll(payment3Repository.findNonCancelledIdsWithMinAmount(minAmount));
        } else if (minAmount != null && maxAmount != null) {
            ids = payment1Repository.findNonCancelledIdsWithAmountInRange(minAmount, maxAmount);
            ids.addAll(payment2Repository.findNonCancelledIdsWithAmountInRange(minAmount, maxAmount));
            ids.addAll(payment3Repository.findNonCancelledIdsWithAmountInRange(minAmount, maxAmount));
        }

        return ids;
    }

    public Payment cancelPayment(Long id) throws PaymentNotFoundException, PaymentProcessingException {
        Payment payment = findPaymentById(id);

        if (calculator.isValidForCancellation(payment)) {
            calculator.cancel(payment);
            payment = savePayment(payment);
        } else {
            throw new PaymentProcessingException("Payment id=" + id + " is not valid for cancellation");
        }

        return payment;
    }

    public Payment savePayment(Payment payment) {
        Payment result = null;
        if (payment instanceof Payment1) {
            result = payment1Repository.save((Payment1) payment);
        } else if (payment instanceof Payment2) {
            result = payment2Repository.save((Payment2) payment);
        } else if (payment instanceof Payment3) {
            result = payment3Repository.save((Payment3) payment);
        }

        return result;
    }
}
