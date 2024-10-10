import java.util.List;

public class Movie extends Production {
    String duration;
    Integer releaseYear;
    private List<User> users = Input.loadUsersFromJson();

    public Movie(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String plot, String duration, Integer releaseYear) {
        super(title, directors, actors, genres, ratings, plot);

        if (duration == null || releaseYear == null) {
            this.canBePrinted = false;
            throw new IllegalArgumentException("There are not enough details about the movie.");
        }

        this.canBePrinted = true;
        this.type = "MOVIE";
        this.duration = duration;
        this.releaseYear = releaseYear;
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

    public void setDuration(String duration) {
        this.duration = duration;
    }
    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public boolean deleteReview(String username) {
        boolean done = false;
        for (Rating rating : ratings) {
            if (rating.getUsername().equals(username)) {
                ratings.remove(rating);
                done = true;
                break;
            }
        }
        return done;
    }

    @Override
    public void displayInfo() {
        System.out.println(title + " details: ");
        System.out.println("Directors: " + directors + "\n" + "Actors: " + actors + "\n" + "Genres: " + genres + "\n" + "Ratings: " + ratings + "\n" + "Plot: " + plot + "\n" + "Duration: " + duration + "\n" + "Release year: " + releaseYear);
    }
}
