import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Series extends Production {
    Integer releaseYear;
    Integer numSeasons;
    private Map<String, List<Episode>> episodes;
    private List<User> users = Input.loadUsersFromJson();

    public Series(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String plot, Integer releaseYear, Integer numSeasons, Map<String, List<Episode>> episodes) {
        super(title, directors, actors, genres, ratings, plot);
        if (releaseYear == null || numSeasons == null || episodes == null) {
            this.canBePrinted = false;
            throw new IllegalArgumentException("There are not enough details about the series.");
        }

        this.canBePrinted = true;
        this.type = "SERIES";
        this.releaseYear = releaseYear;
        this.numSeasons = numSeasons;
        this.episodes = episodes;
    }

    @Override
    public void addReview(String username, Integer rating, String review) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setStrategy(new AddReviewStrategy());
                break;
            }
        }

        this.ratings.add(new Rating(username, rating, review));
        System.out.println("Review added successfully.");
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
    public void setNumSeasons(Integer numSeasons) {
        this.numSeasons = numSeasons;
    }
    public void setEpisodes(Map<String, List<Episode>> episodes) {
        this.episodes = episodes;
    }

    @Override
    public boolean deleteReview(String username) {
        boolean done = false;
        for (Rating rating : ratings) {
            if (rating.getUsername().equals(username)) {
                ratings.remove(rating);
                done = true;
            }
        }
        return done;
    }

    @Override
    public void displayInfo() {
        System.out.println(title + " details: ");
        System.out.println("Directors: " + directors + "\n" + "Actors: " + actors + "\n" + "Genres: " + genres + "\n" + "Ratings: " + ratings + "\n" + "Plot: " + plot + "\n" + "Release year: " + releaseYear + "\n" + "Number of seasons: " + numSeasons + "\n" + "Episodes: " + episodes);
    }
}
