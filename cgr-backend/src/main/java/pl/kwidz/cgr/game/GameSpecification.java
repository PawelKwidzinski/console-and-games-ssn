package pl.kwidz.cgr.game;

import org.springframework.data.jpa.domain.Specification;

public class GameSpecification {

    public static Specification<Game> withOwnerId(String ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("createdBy"), ownerId);
    }
}
