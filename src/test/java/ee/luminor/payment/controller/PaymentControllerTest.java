package ee.luminor.payment.controller;

import ee.luminor.payment.model.Currency;
import ee.luminor.payment.model.Payment1;
import ee.luminor.payment.model.Payment2;
import ee.luminor.payment.model.Payment3;
import ee.luminor.payment.service.PaymentService;
import ee.luminor.payment.util.PaymentCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PaymentController.class)
@ExtendWith(RestDocumentationExtension.class)
public class PaymentControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    public void testGetAllPayments() throws Exception {
        Payment1 payment1 = PaymentCreator.createPayment1(1L, "Details 1 for Payment1", "EE1234567890123", "DE9876543210987", BigDecimal.valueOf(123.45), LocalDateTime.of(2020, 1, 15, 15, 0));
        Payment2 payment2 = PaymentCreator.createPayment2(2L, "Details 1 for Payment2", "EE9876543210987", "US1233214566549", BigDecimal.valueOf(1001.01), LocalDateTime.of(2020, 1, 16, 16, 0));
        Payment3 payment3 = PaymentCreator.createPayment3(3L, "TRWSCA15", Currency.USD, "LV4561237891234", "US1233214566549", BigDecimal.valueOf(500005.55), LocalDateTime.of(2020, 1, 17, 17, 0));
        var payments = List.of(payment1, payment2, payment3);

        when(paymentService.findAllPayments()).thenReturn(payments);

        this.mockMvc.perform(get("/payments"))
                .andDo(print())
                .andDo(document("payments/get-all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].details", is("Details 1 for Payment1")))
                .andExpect(jsonPath("$[0].debtorIban", is("EE1234567890123")))
                .andExpect(jsonPath("$[0].creditorIban", is("DE9876543210987")))
                .andExpect(jsonPath("$[0].currency", is("EUR")))
                .andExpect(jsonPath("$[0].amount", is(123.45)))
                .andExpect(jsonPath("$[0].paymentDate", is("2020-01-15T15:00:00")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].details", is("Details 1 for Payment2")))
                .andExpect(jsonPath("$[1].debtorIban", is("EE9876543210987")))
                .andExpect(jsonPath("$[1].creditorIban", is("US1233214566549")))
                .andExpect(jsonPath("$[1].currency", is("USD")))
                .andExpect(jsonPath("$[1].amount", is(1001.01)))
                .andExpect(jsonPath("$[1].paymentDate", is("2020-01-16T16:00:00")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].debtorIban", is("LV4561237891234")))
                .andExpect(jsonPath("$[2].creditorIban", is("US1233214566549")))
                .andExpect(jsonPath("$[2].creditorBic", is("TRWSCA15")))
                .andExpect(jsonPath("$[2].currency", is("USD")))
                .andExpect(jsonPath("$[2].amount", is(500005.55)))
                .andExpect(jsonPath("$[2].paymentDate", is("2020-01-17T17:00:00")));

        verify(paymentService).findAllPayments();
    }

    @Test
    public void testGetPaymentCancellationFeeById() throws Exception {
        Payment1 payment1 = (Payment1) PaymentCreator.createPayment1(1L, "Details 1 for Payment1", "EE1234567890123", "DE9876543210987", BigDecimal.valueOf(123.45), LocalDateTime.of(2020, 1, 15, 15, 0))
                .setCancellationFee(BigDecimal.valueOf(1.05));

        when(paymentService.findPaymentById(1L)).thenReturn(payment1);

        this.mockMvc.perform(get("/payments/{id}", 1L))
                .andDo(print())
                .andDo(document("payments/get-id-and-cancellation-fee",
                        pathParameters(parameterWithName("id").description("Payment id"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cancellationFee", is(1.05)));

        verify(paymentService).findPaymentById(1L);
    }

    @Test
    public void testGetNonCancelledPaymentIds() throws Exception {
        var paymentIds = List.of(123L, 456L, 789L);

        when(paymentService.findNonCancelledPaymentIds(BigDecimal.valueOf(20.02), BigDecimal.valueOf(30.03))).thenReturn(paymentIds);

        this.mockMvc.perform(get("/payments/non-cancelled?minAmount={minAmount}&maxAmount={maxAmount}", BigDecimal.valueOf(20.02), BigDecimal.valueOf(30.03)))
                .andDo(print())
                .andDo(document("payments/non-cancelled",
                        requestParameters(parameterWithName("minAmount").description("Amount should be >= of this value (Optional)"),
                                parameterWithName("maxAmount").description("Amount should be <= of this value (Optional)"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", is(123)))
                .andExpect(jsonPath("$[1]", is(456)))
                .andExpect(jsonPath("$[2]", is(789)));

        verify(paymentService).findNonCancelledPaymentIds(BigDecimal.valueOf(20.02), BigDecimal.valueOf(30.03));
    }

    @Test
    public void testCancelPayment() throws Exception {
        Payment1 payment = (Payment1) PaymentCreator.createPayment1(123L, "Details 1 for Payment1", "EE1234567890123", "DE9876543210987", BigDecimal.valueOf(123.45), LocalDateTime.of(2020, 1, 15, 15, 0))
                .setCancellationFee(BigDecimal.valueOf(0.35));

        when(paymentService.cancelPayment(123L)).thenReturn(payment);

        this.mockMvc.perform(patch("/payments/{id}", 123L))
                .andDo(print())
                .andDo(document("payments/cancel",
                        pathParameters(parameterWithName("id").description("Payment id"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.details", is("Details 1 for Payment1")))
                .andExpect(jsonPath("$.debtorIban", is("EE1234567890123")))
                .andExpect(jsonPath("$.creditorIban", is("DE9876543210987")))
                .andExpect(jsonPath("$.currency", is("EUR")))
                .andExpect(jsonPath("$.amount", is(123.45)))
                .andExpect(jsonPath("$.paymentDate", is("2020-01-15T15:00:00")))
                .andExpect(jsonPath("$.cancellationFee", is(0.35)));

        verify(paymentService).cancelPayment(123L);
    }

//    @Test
//    public void testSavePayment1() {
//
//    }

}
