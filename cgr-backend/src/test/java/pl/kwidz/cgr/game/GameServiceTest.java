package pl.kwidz.cgr.game;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import pl.kwidz.cgr.common.PageResponse;
import pl.kwidz.cgr.exception.OperationNotPermittedException;
import pl.kwidz.cgr.feedback.Feedback;
import pl.kwidz.cgr.file.FileStorageService;
import pl.kwidz.cgr.history.GameTransactionHistory;
import pl.kwidz.cgr.history.GameTransactionHistoryRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.kwidz.cgr.game.GameSpecification.withOwnerId;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private GameTransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private GameService gameService;

    private Game game;
    private GameRequest gameRequest;
    private GameResponse gameResponse;
    private GameTransactionHistory gameTransactionHistory;
    private BorrowedGameResponse borrowedGameResponse;
    private Pageable pageable;
    private Page<Game> gamesPage;
    private Page<GameTransactionHistory> historyPage;

    @BeforeEach
    void setUp() {
        game = Game.builder()
                .id(1)
                .title("Test Game")
                .platform("PS5")
                .genre("Action")
                .description("Test Description")
                .studio("Test Studio")
                .language("English")
                .archived(false)
                .shareable(true)
                .build();

        gameRequest = new GameRequest(
                1,
                "Test Game",
                "PS5",
                "Action",
                "Test Description",
                "Test Studio",
                "English",
                "Test User",
                true
        );

        gameResponse = GameResponse.builder()
                .id(1)
                .title("Test Game")
                .platform("PS5")
                .genre("Action")
                .description("Test Description")
                .studio("Test Studio")
                .language("English")
                .archived(false)
                .shareable(true)
                .build();

        gameTransactionHistory = GameTransactionHistory.builder()
                .id(1)
                .userId("borrower")
                .game(game)
                .returned(false)
                .returnApproved(false)
                .build();

        borrowedGameResponse = new BorrowedGameResponse();
        borrowedGameResponse.setId(1);

        pageable = PageRequest.of(0, 10);
        gamesPage = new PageImpl<>(Collections.singletonList(game));
        historyPage = new PageImpl<>(Collections.singletonList(gameTransactionHistory));
    }

    @Test
    void shouldSaveGame() {
        // given + when
        when(gameMapper.toGame(gameRequest)).thenReturn(game);
        when(gameRepository.save(game)).thenReturn(game);

        Integer result = gameService.save(gameRequest, authentication);
        // then
        assertEquals(1, result);
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    void shouldFindGameById() {
        // given + when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(gameMapper.toGameResponse(game)).thenReturn(gameResponse);

        GameResponse result = gameService.findById(1);
        // then
        assertNotNull(result);
        assertEquals(gameResponse, result);
    }

    @Test
    void shouldThrowExceptionWhenGameNotFound() {
        // given + when
        when(gameRepository.findById(999)).thenReturn(Optional.empty());
        // then
        assertThrows(EntityNotFoundException.class, () ->
                gameService.findById(999)
        );
    }

    @Test
    void shouldFindAllGames_WhenUserIsLogged() {
        // given
        pageable = PageRequest.of(0, 10, Sort.by("createdDate").descending());
        // when
        when(authentication.getName()).thenReturn("testUser");
        when(gameRepository.findAllDisplayableGames(eq(pageable), eq("testUser"))).thenReturn(gamesPage);
        when(gameMapper.toGameResponse(any(Game.class))).thenReturn(gameResponse);

        PageResponse<GameResponse> result = gameService.findAllGames(0, 10, authentication);
        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(gameRepository).findAllDisplayableGames(pageable, "testUser");
        verify(gameMapper).toGameResponse(any(Game.class));
    }

    @Test
    void shouldFindGamesByOwner() {
        // given
        pageable = PageRequest.of(0, 10, Sort.by("createdDate").descending());
        // when
        when(gameRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(gamesPage);
        when(gameMapper.toGameResponse(game)).thenReturn(gameResponse);
        when(authentication.getName()).thenReturn("testUser");

        PageResponse<GameResponse> result = gameService.findGamesByOwner(0, 10, authentication);
        // then
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void shouldFindAllBorrowedGames() {
        // given
        game.setCreatedBy("owner");
        pageable = PageRequest.of(0, 10, Sort.by("createdDate").descending());
        // when
        when(transactionHistoryRepository.findAllBorrowedGames(pageable,"owner")).thenReturn(historyPage);
        when(authentication.getName()).thenReturn("owner");
        when(gameMapper.toBorrowedGameResponse(gameTransactionHistory)).thenReturn(borrowedGameResponse);

        PageResponse<BorrowedGameResponse> result = gameService.findAllBorrowedGames(0, 10, authentication);
        // then
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void shouldFindAllReturnedGames() {
        // given
        game.setCreatedBy("testUser");
        pageable = PageRequest.of(0, 10, Sort.by("createdDate").descending());
        // when
        when(transactionHistoryRepository.findAllReturnedGames(pageable,"testUser")).thenReturn(historyPage);
        when(authentication.getName()).thenReturn("testUser");
        when(gameMapper.toBorrowedGameResponse(gameTransactionHistory)).thenReturn(borrowedGameResponse);

        PageResponse<BorrowedGameResponse> result = gameService.findAllReturnedGames(0, 10, authentication);
        // then
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }
    @Test
    void shouldUpdateShareableStatus() {
        // given
        game.setCreatedBy("testUser");
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("testUser");

        Integer updatedGameId = gameService.updateShareableStatus(1, authentication);
        // then
        assertThat(updatedGameId).isEqualTo(1);
        assertThat(game.isShareable()).isFalse();
        verify(gameRepository).save(game);
    }

    @Test
    void shouldThrowException_WhenUpdatingShareableStatusOfOthersGame() {
        // given
        game.setCreatedBy("testUser");
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("otherUser");
        // then
        assertThrows(OperationNotPermittedException.class, () ->
                gameService.updateShareableStatus(1, authentication)
        );
    }

    @Test
    void shouldUpdateArchiveStatus() {
        // given
        game.setCreatedBy("testUser");
        game.setArchived(false);
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("testUser");

        Integer updatedGameId = gameService.updateArchiveStatus(1, authentication);
        // then
        assertThat(updatedGameId).isEqualTo(1);
        assertThat(game.isArchived()).isTrue();
        verify(gameRepository).save(game);
    }

    @Test
    void shouldThrowException_WhenUpdatingArchiveStatusOfOthersGame() {
        // given
        game.setCreatedBy("testUser");
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("otherUser");
        // then
        assertThrows(OperationNotPermittedException.class, () ->
                gameService.updateArchiveStatus(1, authentication));
    }

    @Test
    void shouldBorrowGame() {
        // given
        game.setCreatedBy("owner");
        game.setShareable(true);
        game.setArchived(false);
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("borrower");
        when(transactionHistoryRepository.isAlreadyBorrowedByUser(1, "borrower")).thenReturn(false);
        when(transactionHistoryRepository.save(any(GameTransactionHistory.class))).thenReturn(gameTransactionHistory);

        Integer transactionId = gameService.borrowGame(1, authentication);
        // then
        assertThat(transactionId).isEqualTo(1);
        verify(transactionHistoryRepository).save(any(GameTransactionHistory.class));
    }

    @Test
    void shouldThrowException_whenBorrowingGameIsArchiveOrNotShareable() {
        // given
        game.setCreatedBy("owner");
        game.setShareable(false);
        game.setArchived(true);
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        // then
        assertThrows(OperationNotPermittedException.class, () -> gameService.borrowGame(1,authentication));
    }

    @Test
    void shouldThrowException_whenBorrowOwnGame() {
        // given
        game.setCreatedBy("owner");
        game.setShareable(true);
        game.setArchived(false);
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("owner");
        // then
        assertThrows(OperationNotPermittedException.class, () -> gameService.borrowGame(1,authentication));
    }

    @Test
    void shouldThrowException_WhenBorrowingAlreadyBorrowedGame() {
        // given
        game.setCreatedBy("owner");
        game.setShareable(true);
        game.setArchived(false);
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("borrower");
        when(transactionHistoryRepository.isAlreadyBorrowedByUser(1, "borrower")).thenReturn(true);
        // then
        assertThrows(OperationNotPermittedException.class, () ->
                gameService.borrowGame(1, authentication)
        );
    }

    @Test
    void shouldReturnBorrowedGame() {
        // given
        game.setCreatedBy("owner");
        game.setShareable(true);
        game.setArchived(false);
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("borrower");
        when(transactionHistoryRepository.findByGameIdAndUserId(1, "borrower")).thenReturn(Optional.of(gameTransactionHistory));
        when(transactionHistoryRepository.save(any(GameTransactionHistory.class))).thenReturn(gameTransactionHistory);

        Integer transactionId = gameService.returnBorrowedGame(1, authentication);
        // then
        assertThat(transactionId).isEqualTo(1);
        assertThat(gameTransactionHistory.isReturned()).isTrue();
        verify(transactionHistoryRepository).save(gameTransactionHistory);
    }

    @Test
    void shouldThrowException_WhenReturningGameIsArchiveOrNotShareable() {
        // given
        game.setCreatedBy("owner");
        game.setShareable(false);
        game.setArchived(true);
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        // then
        assertThrows(OperationNotPermittedException.class, () ->
                gameService.returnBorrowedGame(1, authentication)
        );
    }

    @Test
    void shouldThrowException_WhenOwnerReturningGame() {
        // given
        game.setCreatedBy("owner");
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("owner");
        // then
        assertThrows(OperationNotPermittedException.class, () ->
                gameService.returnBorrowedGame(1, authentication)
        );
    }

    @Test
    void shouldApproveReturnedGame() {
        // given
        game.setCreatedBy("owner");
        game.setShareable(true);
        game.setArchived(false);
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("owner");
        when(transactionHistoryRepository.findByGameIdAndOwnerId(1, "owner")).thenReturn(Optional.of(gameTransactionHistory));
        when(transactionHistoryRepository.save(any(GameTransactionHistory.class))).thenReturn(gameTransactionHistory);

        Integer transactionId = gameService.approveReturnBorrowedGame(1, authentication);
        // then
        assertThat(transactionId).isEqualTo(1);
        assertThat(gameTransactionHistory.isReturnApproved()).isTrue();
        verify(transactionHistoryRepository).save(gameTransactionHistory);
    }

    @Test
    void shouldThrowException_WhenApproveReturningGameIsArchiveOrNotShareable() {
        // given
        game.setCreatedBy("owner");
        game.setShareable(false);
        game.setArchived(true);
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        // then
        assertThrows(OperationNotPermittedException.class, () ->
                gameService.approveReturnBorrowedGame(1, authentication)
        );
    }

    @Test
    void shouldThrowException_WhenApproveNotOwnGame() {
        // given
        game.setCreatedBy("owner");
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("borrower");
        // then
        assertThrows(OperationNotPermittedException.class, () ->
                gameService.approveReturnBorrowedGame(1, authentication)
        );
    }

    @Test
    void shouldCalculateCorrectGameRate() {
        // given
        List<Feedback> feedbacks = List.of(
                Feedback.builder().note(4.0).build(),
                Feedback.builder().note(5.0).build()
        );
        game.setFeedbacks(feedbacks);
        // when
        double rate = game.getRate();
        // then
        assertThat(rate).isEqualTo(4.5);
    }

    @Test
    void shouldReturnZeroRateWhenNoFeedbacks() {
        // given
        game.setFeedbacks(Collections.emptyList());
        // when
        double rate = game.getRate();
        // then
        assertThat(rate).isEqualTo(0.0);
    }

    @Test
    void shouldUploadGameCoverPicture() {
        // given
        MultipartFile mockFile = mock(MultipartFile.class);
        // when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(authentication.getName()).thenReturn("owner");
        when(fileStorageService.saveFile(any(MultipartFile.class), eq("owner"))).thenReturn("cover.jpg");

        gameService.uploadGameCoverPicture(mockFile, authentication, 1);
        // then
        assertThat(game.getGameCover()).isEqualTo("cover.jpg");
        verify(gameRepository).save(game);
        verify(fileStorageService).saveFile(mockFile, "owner");
    }

}