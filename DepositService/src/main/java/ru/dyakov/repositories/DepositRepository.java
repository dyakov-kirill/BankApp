package ru.dyakov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dyakov.entities.Deposit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Integer> {

    List<Deposit> findByBankAccountId(Integer accountId);

    @Query(value = "INSERT INTO deposits" +
            " (deposit_account_id, deposit_type_id, deposit_refill, deposits_amount, start_date, end_date, " +
            "deposit_rate, type_percent_payment_id, percent_payment_account_id, " +
            "percent_payment_date, capitalization, deposit_refund_account_id) " +
            "VALUES " +
            "(:depositAccountId, :depositTypeId, :depositRefill, :depositsAmount, :startDate, :endDate," +
            ":depositRate, :typePercentPaymentId, :percentPaymentAccountId," +
            ":percentPaymentDate, :capitalization, :depositRefundAccountId) returning *", nativeQuery = true)
    Deposit createDeposit(@Param("depositAccountId") int depositAccountId, @Param("depositTypeId") int depositTypeId, @Param("depositRefill") boolean depositRefill, @Param("depositsAmount") BigDecimal depositsAmount,
                          @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("depositRate") BigDecimal depositRate,
                          @Param("typePercentPaymentId") int typePercentPaymentId, @Param("percentPaymentAccountId") int percentPaymentAccountId,
                          @Param("percentPaymentDate") LocalDate percentPaymentDate, @Param("capitalization") boolean capitalization, @Param("depositRefundAccountId") int depositRefundAccountId);
}
