package pl.kwidz.cgr.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.kwidz.cgr.common.BaseEntity;
import pl.kwidz.cgr.feedback.Feedback;
import pl.kwidz.cgr.history.GameTransactionHistory;
import pl.kwidz.cgr.user.User;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Game extends BaseEntity {

    private String title;
    private String platform;
    private String genre;
    private String description;
    private String gameCover;
    private String language;
    private boolean archived;
    private boolean shareable;


    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "game")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "game")
    private List<GameTransactionHistory> histories;
}
