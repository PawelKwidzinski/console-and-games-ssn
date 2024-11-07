package pl.kwidz.cgr.history;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.kwidz.cgr.common.BaseEntity;
import pl.kwidz.cgr.game.Game;
import pl.kwidz.cgr.user.User;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GameTransactionHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private boolean returned;
    private boolean returnApproved;
}
