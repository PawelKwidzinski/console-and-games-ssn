package pl.kwidz.cgr.game;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.kwidz.cgr.common.PageResponse;

@RestController
@RequestMapping("games")
@RequiredArgsConstructor
@Tag(name = "Game")
public class GameController {

    private final GameService gameService;

    @PostMapping
    public ResponseEntity<Integer> saveGame(@Valid @RequestBody GameRequest gameRequest, Authentication connectedUser) {
        return ResponseEntity.ok(gameService.save(gameRequest, connectedUser));
    }

    @GetMapping("/{game-id}")
    public ResponseEntity<GameResponse> findGameById(@PathVariable("game-id") Integer gameId) {
        return ResponseEntity.ok(gameService.findById(gameId));
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<GameResponse>> findAllGames(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(gameService.findAllGames(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<GameResponse>> findAllGamesByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(gameService.findGamesByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedGameResponse>> findAllBorrowedGames(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(gameService.findAllBorrowedGames(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedGameResponse>> findAllReturnedGames(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser) {
        return ResponseEntity.ok(gameService.findAllReturnedGames(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{game-id}")
    public ResponseEntity<Integer> updateSharableStatus(@PathVariable("game-id") Integer gameId,
                                                             Authentication connectedUser) {
        return ResponseEntity.ok(gameService.updateShareableStatus(gameId, connectedUser));
    }

    @PatchMapping("/archive/{game-id}")
    public ResponseEntity<Integer> updateArchiveStatus(@PathVariable("game-id") Integer gameId,
                                                        Authentication connectedUser) {
        return ResponseEntity.ok(gameService.updateArchiveStatus(gameId, connectedUser));
    }

    @PostMapping("/borrow/{game-id}")
    public ResponseEntity<Integer> borrowGame(@PathVariable("game-id") Integer gameId,
                                              Authentication connectedUser) {
        return ResponseEntity.ok(gameService.borrowGame(gameId, connectedUser));
    }

    @PatchMapping("/borrow/return/{game-id}")
    public ResponseEntity<Integer> returnBorrowedGame(@PathVariable("game-id") Integer gameId,
                                                       Authentication connectedUser) {
        return ResponseEntity.ok(gameService.returnBorrowedGame(gameId, connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{game-id}")
    public ResponseEntity<Integer> approveReturnBorrowedGame(@PathVariable("game-id") Integer gameId,
                                                      Authentication connectedUser) {
        return ResponseEntity.ok(gameService.approveReturnBorrowedGame(gameId, connectedUser));
    }

    @PostMapping(value = "/cover/{game-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadGameCoverPicture(@PathVariable("game-id") Integer gameId,
                                                    @Parameter()
                                                    @RequestPart("file") MultipartFile file,
                                                    Authentication connectedUser) {
        gameService.uploadBookCoverPicture(file, connectedUser, gameId);
        return ResponseEntity.accepted().build();
    }








}
