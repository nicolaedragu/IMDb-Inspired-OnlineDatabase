import java.util.ArrayList;
import java.util.List;

public class Actor {
    private String actorName;
    private List<ProductionDetails> filmography;
    private String bibliography;

    public Actor(String actorName, List<ProductionDetails> filmography, String bibliography) {
        this.actorName = actorName;
        this.filmography = filmography;
        this.bibliography = bibliography;
    }

    public String getActorName() {
        return actorName;
    }

    public List<ProductionDetails> getFilmography() {
        return filmography;
    }

    public String getBibliography() {
        return bibliography;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public void setBiography(String bibliography) {
        this.bibliography = bibliography;
    }

    public void setFilmography(List<ProductionDetails> filmography) {
        this.filmography = filmography;
    }

    public void addProduction(String title, String type) {
        ProductionDetails production = new ProductionDetails(title, type);
        filmography.add(production);
    }

    public String toString() {
        return actorName + " details:" + "\n" + "Bibliography: " + bibliography + "\n" + actorName + " has played in: " + filmography;
    }
}
