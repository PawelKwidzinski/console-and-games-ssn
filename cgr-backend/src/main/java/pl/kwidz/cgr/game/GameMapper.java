package pl.kwidz.cgr.game;

import org.springframework.stereotype.Service;
import pl.kwidz.cgr.file.FileUtils;
import pl.kwidz.cgr.history.GameTransactionHistory;

@Service
public class GameMapper {

    public Game toGame(GameRequest gameRequest) {
        return Game.builder()
                .id(gameRequest.id())
                .title(gameRequest.title())
                .platform(gameRequest.platform())
                .genre(gameRequest.genre())
                .description(gameRequest.description())
                .studio(gameRequest.studio())
                .language(gameRequest.language())
                .shareable(gameRequest.shareable())
                .archived(false)
                .build();
    }

    public GameResponse toBookResponse(Game game) {
        return GameResponse.builder()
                .id(game.getId())
                .title(game.getTitle())
                .platform(game.getPlatform())
                .genre(game.getGenre())
                .description(game.getDescription())
                .studio(game.getStudio())
                .language(game.getLanguage())
                .rate(game.getRate())
                .shareable(game.isShareable())
                .archived(game.isArchived())
                .owner(game.getCreatedBy())
                .cover(FileUtils.readFileFromLocation(game.getGameCover()))
                .build();
    }

    public BorrowedGameResponse toBorrowedGameResponse(GameTransactionHistory history) {
        return BorrowedGameResponse.builder()
                .id(history.getGame().getId())
                .title(history.getGame().getTitle())
                .platform(history.getGame().getPlatform())
                .genre(history.getGame().getGenre())
                .rate(history.getGame().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
