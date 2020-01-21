package ee.luminor.payment.repository;

import ee.luminor.payment.model.Payment3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface Payment3Repository extends JpaRepository<Payment3, Long> {

    @Query("select p.id from Payment3 p where p.cancellationFee is null and p.amount >= :minAmount and p.amount <= :maxAmount")
    List<Long> findNonCancelledIdsWithAmountInRange(@Param("minAmount") BigDecimal minAmount,
                                                    @Param("maxAmount") BigDecimal maxAmount);

    @Query("select p.id from Payment3 p where p.cancellationFee is null and p.amount >= :minAmount")
    List<Long> findNonCancelledIdsWithMinAmount(@Param("minAmount") BigDecimal minAmount);

    @Query("select p.id from Payment3 p where p.cancellationFee is null and p.amount <= :maxAmount")
    List<Long> findNonCancelledIdsWithMaxAmount(@Param("maxAmount") BigDecimal maxAmount);

    @Query("select p.id from Payment3 p where p.cancellationFee is null")
    List<Long> findNonCancelledIds();

}
