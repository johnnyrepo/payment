package ee.luminor.payment.service;

import ee.luminor.payment.exception.PaymentNotFoundException;
import ee.luminor.payment.exception.PaymentProcessingException;
import ee.luminor.payment.model.*;
import ee.luminor.payment.repository.Payment1Repository;
import ee.luminor.payment.repository.Payment2Repository;
import ee.luminor.payment.repository.Payment3Repository;
import ee.luminor.payment.util.PaymentCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private Payment1Repository payment1Repository;
    @Mock
    private Payment2Repository payment2Repository;
    @Mock
    private Payment3Repository payment3Repository;
    @Mock
    private PaymentCancellationCalculator calculator;
    @InjectMocks
    private PaymentService paymentService;

    Payment1 payment1 = PaymentCreator.createPayment1(1L, "Details 1 for Payment1", "EE1234567890123", "DE9876543210987", BigDecimal.valueOf(123.45), LocalDateTime.now());
    Payment2 payment2 = PaymentCreator.createPayment2(2L, "Details 1 for Payment2", "EE9876543210987", "US1233214566549", BigDecimal.valueOf(1001.01), LocalDateTime.now());
    Payment3 payment3 = PaymentCreator.createPayment3(3L, "TRWSCA15", Currency.USD, "LV4561237891234", "US1233214566549", BigDecimal.valueOf(500005.55), LocalDateTime.now());

    @Test
    public void testFindAllPayments() {
        when(payment1Repository.findAll()).thenReturn(List.of(payment1));
        when(payment2Repository.findAll()).thenReturn(List.of(payment2));
        when(payment3Repository.findAll()).thenReturn(List.of(payment3));

        var payments = paymentService.findAllPayments();

        assertEquals(payments.size(), 3);

        verify(payment1Repository).findAll();
        verify(payment2Repository).findAll();
        verify(payment3Repository).findAll();
    }

    @Test
    public void testFindPaymentById() {
        when(payment1Repository.findById(1L)).thenReturn(Optional.of(payment1));

        Payment payment = paymentService.findPaymentById(1L);

        assertEquals(payment1, payment);

        verify(payment1Repository).findById(1L);
    }

    @Test
    public void testFindNonCancelledPaymentIds() {
        when(payment1Repository.findNonCancelledIds()).thenReturn(Stream.of(123L).collect(Collectors.toList()));
        when(payment2Repository.findNonCancelledIds()).thenReturn(Stream.of(456L).collect(Collectors.toList()));
        when(payment3Repository.findNonCancelledIds()).thenReturn(Stream.of(789L).collect(Collectors.toList()));

        List<Long> ids = paymentService.findNonCancelledPaymentIds(null, null);

        assertEquals(ids.size(), 3);
        assertTrue(ids.containsAll(Arrays.asList(123L, 456L, 789L)));
    }

    @Test
    public void testFindNonCancelledPaymentIdsWithMinAmount() {
        var minAmount = BigDecimal.valueOf(1.01);

        when(payment1Repository.findNonCancelledIdsWithMinAmount(minAmount)).thenReturn(Stream.of(123L).collect(Collectors.toList()));
        when(payment2Repository.findNonCancelledIdsWithMinAmount(minAmount)).thenReturn(Stream.of(456L).collect(Collectors.toList()));
        when(payment3Repository.findNonCancelledIdsWithMinAmount(minAmount)).thenReturn(Stream.of(789L).collect(Collectors.toList()));

        List<Long> ids = paymentService.findNonCancelledPaymentIds(minAmount, null);

        assertEquals(ids.size(), 3);
        assertTrue(ids.containsAll(Arrays.asList(123L, 456L, 789L)));
    }

    @Test
    public void testFindNonCancelledPaymentIdsWithMaxAmount() {
        var maxAmount = BigDecimal.valueOf(5.05);

        when(payment1Repository.findNonCancelledIdsWithMaxAmount(maxAmount)).thenReturn(Stream.of(123L).collect(Collectors.toList()));
        when(payment2Repository.findNonCancelledIdsWithMaxAmount(maxAmount)).thenReturn(Stream.of(456L).collect(Collectors.toList()));
        when(payment3Repository.findNonCancelledIdsWithMaxAmount(maxAmount)).thenReturn(Stream.of(789L).collect(Collectors.toList()));

        List<Long> ids = paymentService.findNonCancelledPaymentIds(null, maxAmount);

        assertEquals(ids.size(), 3);
        assertTrue(ids.containsAll(Arrays.asList(123L, 456L, 789L)));
    }

    @Test
    public void testFindNonCancelledPaymentIdsWithAmountInRange() {
        var minAmount = BigDecimal.valueOf(1.01);
        var maxAmount = BigDecimal.valueOf(5.05);

        when(payment1Repository.findNonCancelledIdsWithAmountInRange(minAmount, maxAmount)).thenReturn(Stream.of(123L).collect(Collectors.toList()));
        when(payment2Repository.findNonCancelledIdsWithAmountInRange(minAmount, maxAmount)).thenReturn(Stream.of(456L).collect(Collectors.toList()));
        when(payment3Repository.findNonCancelledIdsWithAmountInRange(minAmount, maxAmount)).thenReturn(Stream.of(789L).collect(Collectors.toList()));

        List<Long> ids = paymentService.findNonCancelledPaymentIds(minAmount, maxAmount);

        assertEquals(ids.size(), 3);
        assertTrue(ids.containsAll(Arrays.asList(123L, 456L, 789L)));
    }

    @Test
    public void testCancelPaymentWhenSuccess() {
        Long id = payment1.getId();
        when(payment1Repository.findById(id)).thenReturn(Optional.of(payment1));
        when(calculator.isValidForCancellation(payment1)).thenReturn(true);
        when(payment1Repository.save(payment1)).thenReturn((Payment1) payment1.setCancellationFee(BigDecimal.valueOf(1.05)));

        Payment result = paymentService.cancelPayment(id);

        assertEquals(result.getCancellationFee(), BigDecimal.valueOf(1.05));

        verify(payment1Repository).findById(id);
        verify(calculator).isValidForCancellation(payment1);
        verify(calculator).cancel(payment1);
    }

    @Test
    public void testCancelPaymentWhenPaymentNotFound() {
        Long id = payment1.getId();
        when(payment1Repository.findById(id)).thenReturn(Optional.empty());
        when(payment2Repository.findById(id)).thenReturn(Optional.empty());
        when(payment3Repository.findById(id)).thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> {
            paymentService.cancelPayment(id);
        });

        assertEquals(exception.getMessage(), "No Payment found with id=1");

        verify(payment1Repository).findById(id);
        verify(payment2Repository).findById(id);
        verify(payment3Repository).findById(id);
    }

    @Test
    public void testCancelPaymentWhenPaymentIsNotValid() {
        Long id = payment1.getId();
        when(payment1Repository.findById(id)).thenReturn(Optional.of(payment1));
        when(calculator.isValidForCancellation(payment1)).thenReturn(false);

        PaymentProcessingException exception = assertThrows(PaymentProcessingException.class, () -> {
            paymentService.cancelPayment(id);
        });

        assertEquals(exception.getMessage(), "Payment id=1 is not valid for cancellation");
    }

    @Test
    public void testSavePayment1() {
        paymentService.savePayment(payment1);

        verify(payment1Repository).save(payment1);
    }

    @Test
    public void testSavePayment2() {
        paymentService.savePayment(payment2);

        verify(payment2Repository).save(payment2);
    }

    @Test
    public void testSavePayment3() {
        paymentService.savePayment(payment3);

        verify(payment3Repository).save(payment3);
    }

}
