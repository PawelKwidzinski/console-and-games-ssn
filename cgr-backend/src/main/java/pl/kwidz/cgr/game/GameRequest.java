package pl.kwidz.cgr.game;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GameRequest (
        Integer id,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String title,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String platform,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String genre,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String description,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String studio,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String language,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String ownerName,
        boolean shareable
) {
}
