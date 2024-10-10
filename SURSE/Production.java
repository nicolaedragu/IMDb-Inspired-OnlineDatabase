import java.util.List;

public abstract class Production implements Comparable<Object> {
    String title;
    String type;
    List<String> directors;
    List<String> actors;
    List<Genre> genres;
    List<Rating> ratings;
    String plot;
    Double averageRating;
    boolean canBePrinted;

    public Production(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = description;
        this.averageRating = calculateAverageRating(ratings);
    }
    public String getTitle() {
        return title;
    }
    public String getType() {
        return type;
    }
    public List<String> getDirectors() {
        return directors;
    }
    public List<String> getActors() {
        return actors;
    }
    public List<Genre> getGenres() {
        return genres;
    }
    public List<Rating> getRatings() {
        return ratings;
    }
    public String getPlot() {
        return plot;
    }
    public Double getAverageRating() {
        return averageRating;
    }
    public boolean isCanBePrinted() {
        return canBePrinted;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }
    public void setActors(List<String> actors) {
        this.actors = actors;
    }
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
    public void setPlot(String plot) {
        this.plot = plot;
    }
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
    public void setCanBePrinted(boolean canBePrinted) {
        this.canBePrinted = canBePrinted;
    }

    public Double calculateAverageRating(List<Rating> ratings) {
        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getRating();
        }
        return sum / ratings.size();
    }

    public abstract void addReview(String username, Integer rating, String review);
    public abstract boolean deleteReview(String username);
    public abstract void displayInfo();

    @Override
    public int compareTo(Object o){
        Production p = (Production) o;
        return Integer.compare(this.title.compareTo(p.title), 0);
    }
}
