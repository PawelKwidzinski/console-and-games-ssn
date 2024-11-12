package pl.kwidz.cgr.game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer>, JpaSpecificationExecutor<Game> {
    @Query("""
            SELECT game FROM Game game
            WHERE game.archived = false
            AND game.shareable = true
            AND game.createdBy != :userId
            """)
    Page<Game> findAllDisplayableGames(Pageable pageable, Integer userId);
}
