package pl.kwidz.cgr.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TakenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);
}
