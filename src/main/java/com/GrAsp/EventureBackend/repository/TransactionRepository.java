package com.GrAsp.EventureBackend.repository;

import com.GrAsp.EventureBackend.model.Event;
import com.GrAsp.EventureBackend.model.Transaction;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<Transaction> findById(int id);

    List<Transaction> findTransactionsByTicketId(int ticketId);

    List<Transaction> findTransactionsByUserId(int userId);

    Optional<Transaction> findTransactionByTicketIdAndUserId(int ticketId, int userId);

    Optional<Transaction> findTransactionByUserIdAndTicket_Event_Id(int userId, Integer eventId);

    @Query("""
    SELECT t 
    FROM Transaction t
    JOIN t.ticket tk
    JOIN tk.event e
    WHERE t.user.id = :userId
""")
    Page<Transaction> findAllTransactionsWithUserId(@Param("userId") int userId, Pageable pageable);

    @Query("""
    SELECT t 
    FROM Transaction t
    JOIN t.ticket tk
    JOIN tk.event e
    WHERE e.user.id = :userId
    AND e.id = :eventId
""")
    Page<Transaction> findAllTransactionsWithOrganizerId(@Param("userId") int userId,@Param("eventId") int eventId, Pageable pageable);

    @Query(value = "SELECT " +
            "SUM(t.total_price) AS dailyRevenue, " +
            "COUNT(t) AS count, " +
            "TO_CHAR(t.created_at, 'YYYY-MM-DD') AS date " +
            "FROM transaction t " +
            "JOIN ticket tk ON t.ticket_id = tk.id " +
            "JOIN event e ON tk.event_id = e.id " +
            "WHERE e.user_id = :userId " +
            "AND t.created_at BETWEEN :start AND :end " +
            "GROUP BY TO_CHAR(t.created_at, 'YYYY-MM-DD')", nativeQuery = true)
    List<Object[]> getDailyRevenue(@Param("userId") Integer userId,
                                   @Param("start") OffsetDateTime start,
                                   @Param("end") OffsetDateTime end);

    @Query(value = "SELECT " +
            "SUM(t.total_price) AS monthlyRevenue, " +  // Sum of the amount per month
            "COUNT(t) AS count, " +
            "TO_CHAR(t.created_at, 'YYYY-MM') AS month " +  // Group by month
            "FROM transaction t " +
            "JOIN ticket tk ON t.ticket_id = tk.id " +
            "JOIN event e ON tk.event_id = e.id " +
            "WHERE e.user_id = :userId " +  // Use correct parameter name
            "AND t.created_at BETWEEN :start AND :end " +
            "GROUP BY TO_CHAR(t.created_at, 'YYYY-MM')", nativeQuery = true)
    List<Object[]> getMonthlyRevenue(@Param("userId") Integer userId,
                                     @Param("start") OffsetDateTime start,
                                     @Param("end") OffsetDateTime end);

}
