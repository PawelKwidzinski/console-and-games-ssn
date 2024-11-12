package pl.kwidz.cgr.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameTransactionHistoryRepository extends JpaRepository<GameTransactionHistory, Integer> {

    @Query("""
            SELECT gameTransactionHistory FROM GameTransactionHistory gameTransactionHistory
            WHERE gameTransactionHistory.user.id = :userId
            """)
    Page<GameTransactionHistory> findAllBorrowedGames(Pageable pageable, Integer userId);

    @Query("""
            SELECT gameTransactionHistory FROM GameTransactionHistory gameTransactionHistory
            WHERE gameTransactionHistory.game.owner.id = :userId
            """)
    Page<GameTransactionHistory> findAllReturnedGames(Pageable pageable, Integer userId);

    @Query("""
            SELECT (COUNT(*) > 0) AS isBorrowed
            FROM GameTransactionHistory gameTransactionHistory
            WHERE gameTransactionHistory.user.id = :userId
            AND gameTransactionHistory.game.id = :gameId
            AND gameTransactionHistory.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(Integer gameId, Integer userId);

    @Query("""
        SELECT gameTransactionHistory FROM GameTransactionHistory gameTransactionHistory
        WHERE gameTransactionHistory.user.id = :userId
        AND gameTransactionHistory.game.id = :gameId
        AND gameTransactionHistory.returned = false
        AND gameTransactionHistory.returnApproved = false
        """)
    Optional<GameTransactionHistory> findByGameIdAndUserId(Integer gameId, Integer userId);

    @Query(
            """
        SELECT gameTransactionHistory FROM GameTransactionHistory gameTransactionHistory
        WHERE gameTransactionHistory.game.owner.id = :ownerId
        AND gameTransactionHistory.game.id = :gameId
        AND gameTransactionHistory.returned = true 
        AND gameTransactionHistory.returnApproved = false                    
            """
    )
    Optional<GameTransactionHistory> findByGameIdAndOwnerId(Integer gameId, Integer ownerId);
}
