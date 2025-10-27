package com.technicalchallenge.repository;

import com.technicalchallenge.model.Trade;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    // Existing methods
    List<Trade> findByTradeId(Long tradeId);

    @Query("SELECT MAX(t.tradeId) FROM Trade t")
    Optional<Long> findMaxTradeId();

    @Query("SELECT MAX(t.version) FROM Trade t WHERE t.tradeId = :tradeId")
    Optional<Integer> findMaxVersionByTradeId(@Param("tradeId") Long tradeId);

    // NEW METHODS for service layer compatibility
    Optional<Trade> findByTradeIdAndActiveTrue(Long tradeId);

    List<Trade> findByActiveTrueOrderByTradeIdDesc();

    @Query("SELECT t FROM Trade t WHERE t.tradeId = :tradeId AND t.active = true ORDER BY t.version DESC")
    Optional<Trade> findLatestActiveVersionByTradeId(@Param("tradeId") Long tradeId);

    // Enhancement 1: Simple search methods following existing patterns
    
    // Find by counterparty name (basic search)
    @Query("SELECT t FROM Trade t WHERE t.active = true AND LOWER(t.counterparty.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY t.tradeDate DESC")
    List<Trade> findByCounterpartyNameContaining(@Param("name") String counterpartyName);
    
    // Find by book name (basic search)
    @Query("SELECT t FROM Trade t WHERE t.active = true AND LOWER(t.book.bookName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY t.tradeDate DESC")
    List<Trade> findByBookNameContaining(@Param("name") String bookName);
    
    // Find by status
    @Query("SELECT t FROM Trade t WHERE t.active = true AND t.tradeStatus.tradeStatus = :status ORDER BY t.tradeDate DESC")
    List<Trade> findByTradeStatus(@Param("status") String status);
    
    // Find by date range
    @Query("SELECT t FROM Trade t WHERE t.active = true AND t.tradeDate BETWEEN :startDate AND :endDate ORDER BY t.tradeDate DESC")
    List<Trade> findByTradeDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find by trader user ID for dashboard
    @Query("SELECT t FROM Trade t WHERE t.active = true AND t.traderUser.id = :userId ORDER BY t.tradeDate DESC")
    List<Trade> findByTraderUserId(@Param("userId") Long userId);
    
    // Find with pagination (basic)
    @Query("SELECT t FROM Trade t WHERE t.active = true ORDER BY t.tradeDate DESC")
    Page<Trade> findAllActivePaginated(Pageable pageable);
}
