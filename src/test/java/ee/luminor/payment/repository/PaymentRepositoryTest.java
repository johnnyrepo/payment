package ee.luminor.payment.repository;

import ee.luminor.payment.model.Currency;
import ee.luminor.payment.model.Payment1;
import ee.luminor.payment.model.Payment2;
import ee.luminor.payment.model.Payment3;
import ee.luminor.payment.util.PaymentCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PaymentRepositoryTest {

    @Autowired
    private Payment1Repository payment1Repository;
    @Autowired
    private Payment2Repository payment2Repository;
    @Autowired
    private Payment3Repository payment3Repository;

    @BeforeEach
    public void setUp() {
        payment1Repository.save(PaymentCreator.createPayment1(1L,"Details 1 for Payment1", "EE1234567890123", "DE9876543210987", BigDecimal.valueOf(123.45), LocalDateTime.now()));
        payment1Repository.save(PaymentCreator.createPayment1(2L,"Details 2 for Payment1", "LV1234567890123", "DE9876543210987", BigDecimal.valueOf(456.78), LocalDateTime.now()));
        payment1Repository.save(PaymentCreator.createPayment1(3L,"Details 3 for Payment1", "LT1234567890123", "DE9876543210987", BigDecimal.valueOf(789.01), LocalDateTime.now()));

        payment2Repository.save(PaymentCreator.createPayment2(4L,"Details 1 for Payment2", "EE9876543210987", "US1233214566549", BigDecimal.valueOf(1001.01), LocalDateTime.now()));
        payment2Repository.save(PaymentCreator.createPayment2(5L,"Details 2 for Payment2", "LV9876543210987", "US1233214566549", BigDecimal.valueOf(2002.02), LocalDateTime.now()));
        payment2Repository.save(PaymentCreator.createPayment2(6L,"Details 3 for Payment2", "LT9876543210987", "US1233214566549", BigDecimal.valueOf(3003.03), LocalDateTime.now()));

        payment3Repository.save(PaymentCreator.createPayment3(7L,"REVOGB21", Currency.EUR, "EE4561237891234", "GB1001200230034", BigDecimal.valueOf(400004.44), LocalDateTime.now()));
        payment3Repository.save(PaymentCreator.createPayment3(8L,"TRWSCA15", Currency.USD, "LV4561237891234", "US1233214566549", BigDecimal.valueOf(500005.55), LocalDateTime.now()));
        payment3Repository.save(PaymentCreator.createPayment3(9L,"SBRBNK77", Currency.EUR, "LT4561237891234", "DE9876543210987", BigDecimal.valueOf(700007.77), LocalDateTime.now()));
    }

    @AfterEach
    public void tearDown() {
        payment1Repository.deleteAll();
        payment1Repository.flush();

        payment2Repository.deleteAll();
        payment2Repository.flush();

        payment3Repository.deleteAll();
        payment3Repository.flush();
    }

    @Test
    public void findPayment1Test() {
        List<Payment1> payments = payment1Repository.findAll();

        assertThat(payments).hasSize(3);
    }

    @Test
    public void findPayment2Test() {
        List<Payment2> payments = payment2Repository.findAll();

        assertThat(payments).hasSize(3);
    }

    @Test
    public void findPayment3Test() {
        List<Payment3> payments = payment3Repository.findAll();

        assertThat(payments).hasSize(3);
    }

}
