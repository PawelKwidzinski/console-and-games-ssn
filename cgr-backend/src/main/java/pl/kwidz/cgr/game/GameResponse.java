package pl.kwidz.cgr.game;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameResponse {

    private Integer id;
    private String title;
    private String platform;
    private String genre;
    private String description;
    private String studio;
    private String language;
    private String owner;
    private byte[] cover;
    private double rate;
    private boolean archived;
    private boolean shareable;

}
