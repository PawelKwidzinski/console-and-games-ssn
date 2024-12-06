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
    private String studio;
    private String gameCover;
    private String language;
    private boolean archived;
    private boolean shareable;

    @OneToMany(mappedBy = "game")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "game")
    private List<GameTransactionHistory> histories;


    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
        double roundedRate = Math.round(rate * 10.0) / 10.0;
        // Return 4.0 if roundedRate is less than 4.5, otherwise return 4.5
        return roundedRate;
    }
}
