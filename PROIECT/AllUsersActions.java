import javax.sound.sampled.Port;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AllUsersActions {
    private AllUsersActions() {
    }

    private static AllUsersActions instance = null;

    public static AllUsersActions getInstance() {
        if (instance == null) {
            instance = new AllUsersActions();
        }
        return instance;
    }

    private List<User> users = Input.loadUsersFromJson();
    private List<Production> productions = Input.loadProductionsFromJson();
    private List<Actor> actors = Input.loadActorsFromJson();

    public void sortActors() {
        actors.sort(new ActorComparator());
    }

    public void sortProductionAlphabetically() {
        productions.sort(new ProductionComparator());
    }

    public void sortProductionsByRatings() {
        productions.sort(new RatingComparator());
    }

    public void sortProductionsByGenre() {
        productions.sort(new GenreComparator());
    }

    // 1) View productions details
    public void viewProductionDetails(List<Production> productions) {
        int ok = 0;
        System.out.println("Production list: ");
        for (Production production : productions) {
            System.out.print(production.getTitle() + ", ");
        }
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Filter by name? (y/n)");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("y")) {
            getInstance().sortProductionAlphabetically();
            System.out.println("Production list: ");
            for (Production production : productions) {
                System.out.print(production.getTitle() + ", ");
            }
        } else if (answer.equalsIgnoreCase("n")) {
            System.out.println("Filter by rating? (y/n)");
            answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("y")) {
                getInstance().sortProductionsByRatings();
                System.out.println("Production list: ");
                for (Production production : productions) {
                    System.out.print(production.getTitle() + " (" + production.getRatings().size() + " ratings), ");
                }
            } else if (answer.equalsIgnoreCase("n")) {
                System.out.println("Filter by genre? (y/n)");
                answer = scanner.nextLine();
                if (answer.equalsIgnoreCase("y")) {
                    getInstance().sortProductionsByGenre();
                    System.out.println("Production list: ");
                    for (Production production : productions) {
                        System.out.print(production.getTitle() + "(" + production.getGenres() + "), ");
                    }
                }
            }
        }
        do {
            System.out.println();
            System.out.print("Choose a production: ");
            scanner = new Scanner(System.in);
            String productionName = scanner.nextLine();
            for (Production production : productions) {
                if (production.getTitle().equalsIgnoreCase(productionName)) {
                    production.displayInfo();
                    ok = 1;
                }
            }
            if (ok == 0) {
                System.out.println("Production not found. Please try again.");
            }
        } while (ok == 0);
    }

    // 2) View actor details
    public void viewActorDetails(List<Actor> actors) {
        int ok = 0;

        System.out.println("Actor list: ");
        for (Actor actor : actors) {
            System.out.print(actor.getActorName() + ", ");
        }
        System.out.println();

        System.out.println("Filter by name? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("y")) {
            System.out.println("Sorted actor list: ");
            getInstance().sortActors();
            for (Actor actor : actors) {
                System.out.print(actor.getActorName() + ", ");
            }
            System.out.println();
        }

        System.out.println("Choose an actor to see more details.");
        do {
            System.out.print("Actor name: ");
            String actorName = scanner.nextLine();
            for (Actor actor : actors) {
                if (actor.getActorName().equalsIgnoreCase(actorName)) {
                    System.out.println(actor);
                    ok = 1;
                }
            }
            if (ok == 0) {
                System.out.println("Actor not found. Please try again.");
            }
        } while (ok == 0);
    }

    // 3) View your notifications
    public void viewNotifications(User user) {
        System.out.print("Username: ");
        System.out.println(user.username);
        if (user.getNotifications().isEmpty()) {
            System.out.println("You have no notifications.");
        } else {
            System.out.println("Your notifications: ");
            for (String notification : user.getNotifications()) {
                System.out.print(" - ");
                System.out.println(notification);
            }
        }
        System.out.println();
    }

    // 4) Search for actor/movie/series
    public void searchProductionOrActor() {
        System.out.println("What do you want to search for?");
        System.out.println("1. Movie");
        System.out.println("2. Series");
        System.out.println("3. Actor");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                System.out.print("Movie name: ");
                String MovieName = scanner.nextLine();
                if (!searchForMovie(MovieName)) {
                    System.out.println("Movie not found.");
                }
                break;
            case 2:
                System.out.print("Series name: ");
                String seriesName = scanner.nextLine();
                if (!searchForSeries(seriesName)) {
                    System.out.println("Series not found.");
                }
                break;
            case 3:
                System.out.print("Actor name: ");
                String actorName = scanner.nextLine();
                if (!searchForActor(actorName)) {
                    System.out.println("Actor not found.");
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public boolean searchForSeries(String productionName) {
        for (Production production : productions) {
            if (production.getTitle().equalsIgnoreCase(productionName) && production.getType().equalsIgnoreCase("series")) {
                System.out.println("Series found!");
                production.displayInfo();
                return true;
            }
        }
        return false;
    }

    public boolean searchForMovie(String productionName) {
        for (Production production : productions) {
            if (production.getTitle().equalsIgnoreCase(productionName) && production.getType().equalsIgnoreCase("movie")) {
                System.out.println("Movie found!");
                production.displayInfo();
                return true;
            }
        }
        return false;
    }

    public boolean searchForActor(String actorName) {
        for (Actor actor : actors) {
            if (actor.getActorName().equalsIgnoreCase(actorName)) {
                System.out.println("Actor found!");
                System.out.println(actor);
                return true;
            }
        }
        return false;
    }

    // 5) Add/Delete actor/movie/series to/from your favorites
    public void addOrDeleteProductionOrActorFromFavorites(User user) {
        user.showFavorites();
        List<String> favorites = user.getFavorites();
        System.out.println("Do you want to add or delete a production from your favorites?");
        System.out.println("1. Add");
        System.out.println("2. Delete");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                addSomethingToFavorites(user);
                break;
            case 2:
                if (favorites.isEmpty()) {
                    System.out.println("You can't delete anything from a list that is empty.");
                    break;
                }
                deleteSomethingFromFavorites(user);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public void addSomethingToFavorites(User user) {
        System.out.println("What do you want to add to your favorites?");
        System.out.println("1. Production");
        System.out.println("2. Actor");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                System.out.print("Production name: ");
                String productionName = scanner.nextLine();
                user.addFavoriteProduction(productionName);
                break;
            case 2:
                System.out.print("Actor name: ");
                String actorName = scanner.nextLine();
                user.addFavoriteActor(actorName);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public void deleteSomethingFromFavorites(User user) {
        System.out.println("What do you want to delete from favorites?");
        System.out.print("Your choice: ");
        Scanner scanner = new Scanner(System.in);
        List<String> favorites = user.getFavorites();
        String productionOrActorName = scanner.nextLine();
        boolean found = false;
        for (String favorite : favorites) {
            if (favorite.equalsIgnoreCase(productionOrActorName)) {
                Object object = favorite;
                favorites.remove(object);
                System.out.println("Production/Actor deleted successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Production/Actor not found in favorites. Try again.\n");
            deleteSomethingFromFavorites(user);
        }
    }

    // 6) Create/Delete a request
    public void createOrDeleteRequest(User user) {
        System.out.println("What do you want to do?");
        System.out.println("1. Create a request");
        System.out.println("2. Delete a request");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                createRequest(user);
                break;
            case 2:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void createRequest(User user) {
        System.out.println("What type of request do you want to create?");
        System.out.println("1. DELETE_ACCOUNT");
        System.out.println("2. ACTOR_ISSUE");
        System.out.println("3. MOVIE_ISSUE");
        System.out.println("4. OTHERS");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the title of the production or the name of the actor: ");
        String titleOrActorName = scanner.nextLine();
        System.out.print("Enter the problem description: ");
        String problemDescription = scanner.nextLine();
        Request request = new Request(Request.RequestType.values()[choice - 1], titleOrActorName, problemDescription, user.getUsername());
        if (user.getAccountType().toString().equalsIgnoreCase("Regular")) {
            Regular regular = (Regular) user;
            regular.createRequest(request);
        } else {
            Contributor contributor = (Contributor) user;
            contributor.createRequest(request);
        }
        System.out.println("Request created successfully.");
    }

    // 7) Add/Delete a review
    public void addOrDeleteReview(User user) {
        Scanner scanner = new Scanner(System.in);
        String productionName;
        boolean productionFound;
        boolean reviewAlreadyAdded = false;
        int prodNumber = 0;
        do {
            productionFound = false;
            System.out.println("Choose a production to add/delete a review: ");
            for (Production production : productions) {
                System.out.print(production.getTitle() + ", ");
            }
            System.out.println();
            productionName = scanner.nextLine();
            for (Production production : productions) {
                if (production.getTitle().equalsIgnoreCase(productionName)) {
                    productionFound = true;
                    prodNumber = productions.indexOf(production);
                    System.out.println("Here are the reviews for this production: ");
                    for (Rating rating : production.getRatings()) {
                        System.out.println(rating);
                    }
                    break;
                }
            }
            if (!productionFound) {
                System.out.println("Production not found. Please try again.");
            }
        } while (!productionFound);

        System.out.println("Do you want to add or delete a review?");
        System.out.println("1. Add");
        System.out.println("2. Delete");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                for (Rating ratingParser : productions.get(prodNumber).getRatings()) {
                    if (ratingParser.getUsername().equals(user.getUsername())) {
                        reviewAlreadyAdded = true;
                    }
                }
                if (reviewAlreadyAdded) {
                    System.out.println("User already added a review for this movie. Please delete the previous review and try again.");
                    break;
                }
                System.out.println("Write your review: ");
                String review = scanner.nextLine();
                System.out.println("Choose a rating from 1 to 10: ");
                int rating = scanner.nextInt();
                scanner.nextLine();
                for (Production production : productions) {
                    if (production.getTitle().equalsIgnoreCase(productionName)) {
                        production.addReview(user.getUsername(), rating, review);
                        user.setStrategy(new AddReviewStrategy());
                    }
                }
                break;
            case 2:
                for (Production production : productions) {
                    if (production.getTitle().equalsIgnoreCase(productionName)) {
                        if (production.deleteReview(user.getUsername())) {
                            System.out.println("Review deleted successfully.");
                        } else {
                            System.out.println("User did not add a review for this movie.");
                        }
                    }
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    // --------- CONTRIBUTOR ACTIONS ------------------------------------
    // sunt in STAFF

    public List<Production> deleteProduction(List<Production> productions, String productionName) {
        Scanner scanner = new Scanner(System.in);
        boolean found = false;
        for (Production production : productions) {
            if (production.getTitle().equalsIgnoreCase(productionName)) {
                Object object = production;
                productions.remove(object);
                System.out.println("Production deleted successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Production not found.\n");
        }
        return productions;
    }

    public List<Actor> deleteActor(List<Actor> actors, String actorName) {
        Scanner scanner = new Scanner(System.in);
        boolean found = false;
        for (Actor actor : actors) {
            if (actor.getActorName().equalsIgnoreCase(actorName)) {
                Object object = actor;
                actors.remove(object);
                System.out.println("Actor deleted successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Actor not found.\n");
        }
        return actors;
    }

    // 7) Add/Delete actor/movie/series from the database (view the requests)
    public List<Production> addProduction(Staff staff, List<Production> productions) {
        System.out.println("What do you want to add?");
        System.out.println("1. Movie");
        System.out.println("2. Series");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        Production newProduction;
        switch (choice) {
            case 1:
                System.out.print("Movie name: ");
                String movieName = scanner.nextLine();
                System.out.println("Enter the directors' names. When you are done, type 'done'.");
                List<String> directors = new ArrayList<>();
                String directorName;
                while (!(directorName = scanner.nextLine()).equalsIgnoreCase("done")) {
                    directors.add(directorName);
                }
                System.out.println("Enter the actors' names. When you are done, type 'done'.");
                List<String> actors = new ArrayList<>();
                String actorName;
                while (!(actorName = scanner.nextLine()).equalsIgnoreCase("done")) {
                    actors.add(actorName);
                }
                System.out.println("Enter the genres. When you are done, type 'done'.");
                List<Genre> genres = new ArrayList<>();
                String genreName;
                boolean valid = false;
                try {
                    while (!(genreName = scanner.nextLine()).equalsIgnoreCase("done")) {
                        valid = false;
                        for (Genre genre : Genre.values()) {
                            if (genre.name().equalsIgnoreCase(genreName)) {
                                valid = true;
                                break;
                            }
                        }
                        if (valid)
                            genres.add(Genre.valueOf(genreName.toUpperCase()));
                        else {
                            System.err.println("Invalid genre. Please try again.");
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid genre. Please try again.");
                }
                System.out.print("Plot: ");
                String plot = scanner.nextLine();
                System.out.print("Duration: ");
                String duration = scanner.nextLine();
                System.out.print("Release year: ");
                Integer releaseYear = scanner.nextInt();
                scanner.nextLine();
                List<Rating> ratings = new ArrayList<>();
                newProduction = new Movie(movieName, directors, actors, genres, ratings, plot, duration, releaseYear);
                staff.addProductionSystem(newProduction, productions);

                System.out.println("Movie added successfully.");
                break;
            case 2:
                System.out.print("Series name: ");
                String seriesName = scanner.nextLine();
                System.out.println("Enter the directors' names. When you are done, type 'done'.");
                List<String> directors2 = new ArrayList<>();
                String directorName2;
                while (!(directorName2 = scanner.nextLine()).equalsIgnoreCase("done")) {
                    directors2.add(directorName2);
                }
                System.out.println("Enter the actors' names. When you are done, type 'done'.");
                List<String> actors2 = new ArrayList<>();
                String actorName2;
                while (!(actorName2 = scanner.nextLine()).equalsIgnoreCase("done")) {
                    actors2.add(actorName2);
                }
                System.out.println("Enter the genres. When you are done, type 'done'.");
                List<Genre> genres2 = new ArrayList<>();
                String genreName2;
                while (!(genreName2 = scanner.nextLine()).equalsIgnoreCase("done")) {
                    genres2.add(Genre.valueOf(genreName2.toUpperCase()));
                }
                System.out.print("Plot: ");
                String plot2 = scanner.nextLine();
                List<Rating> ratings2 = new ArrayList<>();
                System.out.print("Release year: ");
                Integer releaseYear2 = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Number of seasons: ");
                Integer numSeasons = scanner.nextInt();
                scanner.nextLine();
                Map<String, List<Episode>> episodes = new HashMap<>();
                List<Episode> episodeList = new ArrayList<>();
                System.out.println("Enter the episodes. When you are done, type 'done episodes'.");
                System.out.print("Enter the season number (ex. Season 1). If you want to exit, type 'done': ");
                String seasonName = scanner.nextLine();
                while (!seasonName.equalsIgnoreCase("done")) {
                    while (true) {
                        System.out.print("Episode name: ");
                        String episodeName = scanner.nextLine();
                        if (episodeName.equalsIgnoreCase("done episodes")) {
                            break;
                        }
                        System.out.print("Episode duration: ");
                        String episodeDuration = scanner.nextLine();
                        Episode episode = new Episode(episodeName, episodeDuration);
                        episodeList.add(episode);
                    }
                    episodes.put(seasonName, episodeList);
                    System.out.print("Enter the next season number (ex. Season 2). If you want to exit, type 'done': ");
                    seasonName = scanner.nextLine();
                }
                newProduction = new Series(seriesName, directors2, actors2, genres2, ratings2, plot2, releaseYear2, numSeasons, episodes);
                staff.addProductionSystem(newProduction, productions);
                System.out.println("Series added successfully.");
                break;
            default:
                System.out.println("Invalid choice.");
        }

        return productions;
    }

    public List<Actor> addActor(Staff staff, List<Actor> actors) {
        System.out.print("Actor name: ");
        Scanner scanner = new Scanner(System.in);
        String actorName = scanner.nextLine();
        System.out.println("Enter the movies/series the actor played in. When you are done, type 'done'.");
        List<ProductionDetails> productions = new ArrayList<>();
        String productionName;
        String productionType;
        String answer = "";
        System.out.println("Possible types: Movie, Series");
        do {
            System.out.print("Production type: ");
            productionType = scanner.nextLine();
            System.out.print("Production name: ");
            productionName = scanner.nextLine();
            if (productionType.equalsIgnoreCase("movie")) {
                productionType = "Movie";
            } else {
                productionType = "Series";
            }
            productions.add(new ProductionDetails(productionName, productionType));
            System.out.println("Do you want to add another production? (y/n)");
            answer = scanner.nextLine();
        } while (answer.equalsIgnoreCase("y"));

        System.out.println("Enter his/her bibliography.");
        String description = scanner.nextLine();
        Actor newActor = new Actor(actorName, productions, description);
        staff.addActorSystem(newActor, actors);
        System.out.println("Actor added successfully.");
        System.out.println();
        return actors;
    }

    public void updateActorsClass(List<Actor> newActorList) {
        actors = newActorList;
    }

    public void updateProductions(List<Production> newProductionList) {
        productions = newProductionList;
    }

    // 8) Update Production details
    public void updateProductionDetails() {
        System.out.println("What type of production do you want to update?");
        System.out.println("1. Movie");
        System.out.println("2. Series");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                System.out.print("Movie name: ");
                String movieName = scanner.nextLine();
                Production productionArgument = null;
                for (Production production : productions) {
                    if (production.getTitle().equalsIgnoreCase(movieName) && production.getType().equalsIgnoreCase("movie")) {
                        productionArgument = production;
                    }
                }
                updateMovieDetails((Movie) productionArgument);
                break;
            case 2:
                System.out.print("Series name: ");
                String seriesName = scanner.nextLine();
                Production productionArgument2 = null;
                for (Production production : productions) {
                    if (production.getTitle().equalsIgnoreCase(seriesName) && production.getType().equalsIgnoreCase("series")) {
                        productionArgument2 = production;
                    }
                }
                updateSeriesDetails((Series) productionArgument2);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public void updateMovieDetails(Movie movie) {
        System.out.println("What do you want to update?");
        System.out.println("1. Title");
        System.out.println("2. Directors");
        System.out.println("3. Actors");
        System.out.println("4. Genres");
        System.out.println("5. Ratings");
        System.out.println("6. Plot");
        System.out.println("7. Duration");
        System.out.println("8. Release year");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                System.out.print("New title: ");
                String newTitle = scanner.nextLine();
                movie.setTitle(newTitle);
                System.out.println("Title updated successfully.");
                break;
            case 2:
                System.out.println("Enter the new directors' names. When you are done, type 'done'.");
                List<String> directors = new ArrayList<>();
                String directorName;
                while (!(directorName = scanner.nextLine()).equalsIgnoreCase("done")) {
                    directors.add(directorName);
                }
                movie.setDirectors(directors);
                System.out.println("Directors updated successfully.");
                break;
            case 3:
                System.out.println("Enter the new actors' names. When you are done, type 'done'.");
                List<String> actors = new ArrayList<>();
                String actorName;
                while (!(actorName = scanner.nextLine()).equalsIgnoreCase("done")) {
                    actors.add(actorName);
                }
                movie.setActors(actors);
                System.out.println("Actors updated successfully.");
                break;
            case 4:
                System.out.println("Enter the new genres. When you are done, type 'done'.");
                List<Genre> genres = new ArrayList<>();
                String genreName;
                while (!(genreName = scanner.nextLine()).equalsIgnoreCase("done")) {
                    genres.add(Genre.valueOf(genreName.toUpperCase()));
                }
                movie.setGenres(genres);
                System.out.println("Genres updated successfully.");
                break;
            case 5:
                System.out.println("Enter the new ratings. When you are done, type 'done'.");
                List<Rating> ratings = new ArrayList<>();
                String username;
                Integer rating;
                String review;
                while (true) {
                    System.out.print("Username: ");
                    username = scanner.nextLine();
                    if (username.equalsIgnoreCase("done")) {
                        break;
                    }
                    System.out.print("Rating: ");
                    rating = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Review: ");
                    review = scanner.nextLine();
                    ratings.add(new Rating(username, rating, review));
                }
                movie.setRatings(ratings);
                System.out.println("Ratings updated successfully.");
                break;
            case 6:
                System.out.print("New plot: ");
                String newPlot = scanner.nextLine();
                movie.setPlot(newPlot);
                System.out.println("Plot updated successfully.");
                break;
            case 7:
                System.out.print("New duration: ");
                String newDuration = scanner.nextLine();
                movie.setDuration(newDuration);
                System.out.println("Duration updated successfully.");
                break;
            case 8:
                System.out.print("New release year: ");
                Integer newReleaseYear = scanner.nextInt();
                scanner.nextLine();
                movie.setReleaseYear(newReleaseYear);
                System.out.println("Release year updated successfully.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
        System.out.println("Movie updated successfully.");
    }

    public void updateSeriesDetails(Series series) {
        System.out.println("What do you want to update?");
        System.out.println("1. Title");
        System.out.println("2. Directors");
        System.out.println("3. Actors");
        System.out.println("4. Genres");
        System.out.println("5. Ratings");
        System.out.println("6. Plot");
        System.out.println("7. Release year");
        System.out.println("8. Number of seasons");
        System.out.println("9. Episodes");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                System.out.print("New title: ");
                String newTitle = scanner.nextLine();
                series.setTitle(newTitle);
                System.out.println("Title updated successfully.");
                break;
            case 2:
                System.out.println("Enter the new directors' names. When you are done, type 'done'.");
                List<String> directors = new ArrayList<>();
                String directorName;
                while (!(directorName = scanner.nextLine()).equalsIgnoreCase("done")) {
                    directors.add(directorName);
                }
                series.setDirectors(directors);
                System.out.println("Directors updated successfully.");
                break;
            case 3:
                System.out.println("Enter the new actors' names. When you are done, type 'done'.");
                List<String> actors = new ArrayList<>();
                String actorName;
                while (!(actorName = scanner.nextLine()).equalsIgnoreCase("done")) {
                    actors.add(actorName);
                }
                series.setActors(actors);
                System.out.println("Actors updated successfully.");
                break;
            case 4:
                System.out.println("Enter the new genres. When you are done, type 'done'.");
                List<Genre> genres = new ArrayList<>();
                String genreName;
                while (!(genreName = scanner.nextLine()).equalsIgnoreCase("done")) {
                    genres.add(Genre.valueOf(genreName.toUpperCase()));
                }
                series.setGenres(genres);
                System.out.println("Genres updated successfully.");
                break;
            case 5:
                System.out.println("Enter the new ratings. When you are done, type 'done'.");
                List<Rating> ratings = new ArrayList<>();
                String username;
                Integer rating;
                String review;
                while (true) {
                    System.out.print("Username: ");
                    username = scanner.nextLine();
                    if (username.equalsIgnoreCase("done")) {
                        break;
                    }
                    System.out.print("Rating: ");
                    rating = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Review: ");
                    review = scanner.nextLine();
                    ratings.add(new Rating(username, rating, review));
                }
                series.setRatings(ratings);
                System.out.println("Ratings updated successfully.");
                break;
            case 6:
                System.out.print("New plot: ");
                String newPlot = scanner.nextLine();
                series.setPlot(newPlot);
                System.out.println("Plot updated successfully.");
                break;
            case 7:
                System.out.println("New release year: ");
                Integer newReleaseYear = scanner.nextInt();
                scanner.nextLine();
                series.setReleaseYear(newReleaseYear);
                System.out.println("Release year updated successfully.");
                break;
            case 8:
                System.out.println("New number of seasons: ");
                Integer newNumSeasons = scanner.nextInt();
                scanner.nextLine();
                series.setNumSeasons(newNumSeasons);
                System.out.println("Number of seasons updated successfully.");
                break;
            case 9:
                Map<String, List<Episode>> episodes = new HashMap<>();
                List<Episode> episodeList = new ArrayList<>();
                System.out.print("Enter the season number (ex. Season 1): ");
                String seasonName = scanner.nextLine();
                while (!seasonName.equalsIgnoreCase("done")) {
                    System.out.println("Enter the episodes. When you are done, type 'done episodes'.");
                    while (true) {
                        System.out.print("Episode name: ");
                        String episodeName = scanner.nextLine();
                        if (episodeName.equalsIgnoreCase("done episodes")) {
                            break;
                        }
                        System.out.print("Episode duration: ");
                        String episodeDuration = scanner.nextLine();
                        Episode episode = new Episode(episodeName, episodeDuration);
                        episodeList.add(episode);
                    }
                    episodes.put(seasonName, episodeList);
                    System.out.println("Enter the next season number (ex. Season 2). If you want to exit, type 'done'.");
                    seasonName = scanner.nextLine();
                }
                series.setEpisodes(episodes);
                System.out.println("Episodes updated successfully.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
        for (Production production : productions) {
            if (production.getTitle().equalsIgnoreCase(series.getTitle())) {
                production = series;
            }
        }
        System.out.println("Series updated successfully.");
    }

    // 9) Update Actor details
    public void updateActor(int indexOfUser) {
        System.out.print("Please enter the name of the actor you want to update: ");
        Scanner scanner = new Scanner(System.in);
        String actorName = scanner.nextLine();
        Staff admin = (Staff) users.get(indexOfUser);
        for (Actor actor : actors) {
            if (actor.getActorName().equalsIgnoreCase(actorName)) {
                actors = admin.updateActor(actor, actors);
                break;
            }
        }
    }

    public void viewRequests(User user) {
        List<String> req = new ArrayList<>();
        if (user.getRequests().isEmpty()) {
            System.out.println("You have no requests.");
        } else {
            System.out.println("Your requests: ");
            System.out.println(user.getRequests());
        }
    }


    // --------- ADMIN ACTIONS -------------------------------------------
    // e in ADMIN

    public List<User> addOrDeleteUser(List<User> users) throws InformationIncompleteException {
        System.out.println("What do you want to do?");
        System.out.println("1. Add user");
        System.out.println("2. Delete user");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                users = addUser(users);
                break;
            case 2:
                users = deleteUser(users);
                break;
            default:
                System.out.println("Invalid choice.");
        }
        return users;
    }

    private List<User> deleteUser(List<User> users) {
        System.out.println("Enter the username of the user you want to delete: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        boolean found = false;
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                Object object = user;
                for (Production production : productions) {
                    if (production.getRatings().removeIf(rating -> rating.getUsername().equals(user.getUsername()))) {
                        production.deleteReview(username);
                    }
                }
                users.remove(object);
                System.out.println("User deleted successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("User not found. Please try again.");
            deleteUser(users);
        }
        return users;
    }

    public List<User> addUser(List<User> users) throws InformationIncompleteException {
        System.out.println("What type of user do you want to add?");
        System.out.println("Type 'r' for Regular, 'c' for Contributor or 'a' for Admin.");
        Scanner scanner = new Scanner(System.in);
        String typeString = scanner.nextLine();
        AccountType type = null;
        switch (typeString.toLowerCase()) {
            case "r":
                type = AccountType.valueOf(AccountType.REGULAR.toString());
                break;
            case "c":
                type = AccountType.valueOf(AccountType.CONTRIBUTOR.toString());
                break;
            case "a":
                type = AccountType.valueOf(AccountType.ADMIN.toString());
                break;
            default:
                System.out.println("Invalid choice.");
        }
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        String password = Admin.generatePassword();
        System.out.println("Enter full name: ");
        String name = scanner.nextLine();
        String username = Admin.generateUsername(name);
        System.out.println("Enter country: ");
        String country = scanner.nextLine();
        System.out.println("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter gender (Male or Female): ");
        String gender = scanner.nextLine();
        switch (gender.toLowerCase()) {
            case "male":
                gender = "Male";
                break;
            case "female":
                gender = "Female";
                break;
            default:
                System.out.println("Invalid choice.");
        }

        LocalDate birthDate = null;
        try {
            System.out.println("Enter birth date: ");
            System.out.print("Year: ");
            int year = scanner.nextInt();
            System.out.print("Month: ");
            int month = scanner.nextInt();
            System.out.print("Day: ");
            int day = scanner.nextInt();
            scanner.nextLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = String.format("%04d-%02d-%02d", year, month, day);

            birthDate = LocalDate.parse(date, formatter);
        } catch (DateTimeException e) {
            System.err.println("Invalid date. Please try again.");
            e.printStackTrace();
            addUser(users);
        }
        LocalDateTime birthDate2 = birthDate.atStartOfDay();

        List<String> favorites = new ArrayList<>();
        SortedSet<String> productionsContributionList = new TreeSet<>();
        UserFactory userFactory = new ConcreteUserFactory();
        System.out.println("\nYou want to add a new user with the following details: ");
        System.out.print("Type: ");
        System.out.println(type);
        System.out.print("Email: ");
        System.out.println(email);
        System.out.print("Password: ");
        System.out.println(password);
        System.out.print("Name: ");
        System.out.println(name);
        System.out.print("Username: ");
        System.out.println(username);
        System.out.print("Country: ");
        System.out.println(country);
        System.out.print("Age: ");
        System.out.println(age);
        System.out.print("Gender: ");
        System.out.println(gender);
        System.out.print("Birth date: ");
        System.out.println(birthDate);
        System.out.println("Confirm? (y/n)");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("n")) {
            System.out.println("User not added. Try again.");
            addUser(users);
        }
        User newUser = userFactory.createUser(username, type, name, 0, email, password, country, age, gender, birthDate2, favorites, productionsContributionList);
        System.out.println("User added successfully.");

        users.add(newUser);
        return users;
    }


}
