package pl.kwidz.cgr.feedback;

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
import org.springframework.security.core.Authentication;
import pl.kwidz.cgr.common.PageResponse;
import pl.kwidz.cgr.exception.OperationNotPermittedException;
import pl.kwidz.cgr.game.Game;
import pl.kwidz.cgr.game.GameRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private FeedbackMapper feedbackMapper;

    @Mock
    private Authentication connectedUser;

    @InjectMocks
    private FeedbackService feedbackService;

    private FeedbackRequest feedbackRequest;
    private Game game;
    private Feedback feedback;
    private FeedbackResponse feedbackResponse;

    @BeforeEach
    public void setUp() {
        feedbackRequest = new FeedbackRequest(4.5, "Great game!", 1);
        game = new Game();
        game.setId(1);
        game.setArchived(false);
        game.setShareable(true);
        game.setCreatedBy("user1");

        feedback = new Feedback();
        feedback.setId(1);
        feedback.setNote(4.5);
        feedback.setComment("Great game!");
        feedback.setGame(game);

        feedbackResponse = new FeedbackResponse(4.5, "Great game!", false);
    }

    @Test
    public void saveFeedback_whenUserIsLogged_shouldSaveFeedback() {
        // given + when
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(feedbackMapper.toFeedback(feedbackRequest)).thenReturn(feedback);
        when(feedbackRepository.save(feedback)).thenReturn(feedback);
        when(connectedUser.getName()).thenReturn("user2");

        Integer feedbackId = feedbackService.save(feedbackRequest, connectedUser);
        // then
        assertEquals(1, feedbackId);
        verify(gameRepository, times(1)).findById(1);
        verify(feedbackMapper, times(1)).toFeedback(feedbackRequest);
        verify(feedbackRepository, times(1)).save(feedback);
    }

    @Test
    public void saveFeedback_whenGameNotFound_shouldThrowEntityNotFoundException() {
        // given + when
        when(gameRepository.findById(1)).thenReturn(Optional.empty());
        // then
        assertThrows(EntityNotFoundException.class, () -> feedbackService.save(feedbackRequest, connectedUser));
        verify(gameRepository, times(1)).findById(1);
    }

    @Test
    public void saveFeedback_whenGameIsArchived_shouldThrowOperationNotPermittedException() {
        // given + when
        game.setArchived(true);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        // then
        assertThrows(OperationNotPermittedException.class, () -> feedbackService.save(feedbackRequest, connectedUser));
        verify(gameRepository, times(1)).findById(1);
    }

    @Test
    public void saveFeedback_whenGameIsNotShareable_shouldThrowOperationNotPermittedException() {
        // given + when
        game.setShareable(false);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        // then
        assertThrows(OperationNotPermittedException.class, () -> feedbackService.save(feedbackRequest, connectedUser));
        verify(gameRepository, times(1)).findById(1);
    }

    @Test
    public void saveFeedback_whenFeedbackIsForOwnGame_shouldThrowOperationNotPermittedException() {
        // given + when
        game.setCreatedBy("user2");
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(connectedUser.getName()).thenReturn("user2");
        // then
        assertThrows(OperationNotPermittedException.class, () -> feedbackService.save(feedbackRequest, connectedUser));
        verify(gameRepository, times(1)).findById(1);
    }

    @Test
    public void findAllFeedbacksByGame_whenUserIsLogged_shouldReturnPaginatedFeedbackResponses() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Feedback> feedbackPage = new PageImpl<>(List.of(feedback), pageable, 1);
        // when
        when(feedbackRepository.findAllByGameId(1, pageable)).thenReturn(feedbackPage);
        when(feedbackMapper.toFeedbackResponse(feedback, "user2")).thenReturn(feedbackResponse);
        when(connectedUser.getName()).thenReturn("user2");

        PageResponse<FeedbackResponse> response = feedbackService.findAllFeedbacksByGame(1, 0, 10, connectedUser);
        // then
        assertEquals(1, response.getContent().size());
        assertEquals(feedbackResponse, response.getContent().getFirst());
        verify(feedbackRepository, times(1)).findAllByGameId(1, pageable);
        verify(feedbackMapper, times(1)).toFeedbackResponse(feedback, "user2");
    }

}