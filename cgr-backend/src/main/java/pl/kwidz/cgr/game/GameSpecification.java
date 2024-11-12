package pl.kwidz.cgr.game;

import org.springframework.data.jpa.domain.Specification;

public class GameSpecification {

    public static Specification<Game> withOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }
}
