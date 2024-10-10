import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class Input {
    public static List<User> loadUsersFromJson() {
        List<User> users = new ArrayList<>();
        JSONParser objParser = new JSONParser();
        try {
            FileReader reader = new FileReader("input" + File.separator + "accounts.json");
            JSONArray input = (JSONArray) objParser.parse(reader);
            for (Object o : input) {
                JSONObject account = (JSONObject) o;
                String username = (String) account.get("username");
                Object experienceObj = account.get("experience");
                int experience = (experienceObj != null) ? Integer.parseInt((String) experienceObj) : 0;

                JSONObject information = (JSONObject) account.get("information");
                JSONObject credentials = (JSONObject) information.get("credentials");

                String email = (String) credentials.get("email");
                String password = (String) credentials.get("password");

                String name = (String) information.get("name");
                String country = (String) information.get("country");
                int age = ((Long) information.get("age")).intValue();
                String gender = (String) information.get("gender");
                String birthDate = (String) information.get("birthDate");
                LocalDateTime userBirthDate = LocalDateTime.parse(birthDate + "T00:00:00");

                String userTypeStr = (String) account.get("userType");
                AccountType userType = AccountType.valueOf(userTypeStr.toUpperCase());
                JSONArray productionsContribution = (JSONArray) account.get("productionsContribution");
                JSONArray actorsContribution = (JSONArray) account.get("actorsContribution");
                JSONArray favoriteProductions = (JSONArray) account.get("favoriteProductions");
                JSONArray favoriteActors = (JSONArray) account.get("favoriteActors");
                SortedSet<String> productionsContributionList = new TreeSet<>();
                if (productionsContribution != null) {
                    for (Object production : productionsContribution) {
                        productionsContributionList.add(production.toString());
                    }
                }
                if (actorsContribution != null) {
                    for (Object actor : actorsContribution) {
                        productionsContributionList.add(actor.toString());
                    }
                }
                List<String> favorites = new ArrayList<>();
                if (favoriteProductions != null) {
                    for (Object production : favoriteProductions) {
                        favorites.add(production.toString());
                    }
                }
                if (favoriteActors != null) {
                    for (Object actor : favoriteActors) {
                        favorites.add(actor.toString());
                    }
                }
                User user = createUser(username, userType, name, experience, email, password, country, age, gender, String.valueOf(userBirthDate), favorites, productionsContributionList);
                users.add(user);
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException | ParseException e) {
            System.err.println("Error reading/parsing JSON: " + e.getMessage());
        } catch (InformationIncompleteException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
        return users;
    }

    public static List<Production> loadProductionsFromJson() {
        List<Production> productions = new ArrayList<>();
        JSONParser objParser = new JSONParser();
        try {
            FileReader reader = new FileReader("input" + File.separator + "production.json");
            JSONArray input = (JSONArray) objParser.parse(reader);
            for (Object o : input) {
                JSONObject jsonObject = (JSONObject) o;
                String type = ((String) jsonObject.get("type")).toUpperCase();
                String title = (String) jsonObject.get("title");
                int releaseYear = jsonObject.get("releaseYear") != null ? ((Long) jsonObject.get("releaseYear")).intValue() : 0;
                JSONArray directors = (JSONArray) jsonObject.get("directors");
                List<String> directorsList = new ArrayList<>();
                for (Object director : directors) {
                    directorsList.add((String) director);
                }
                JSONArray actors = (JSONArray) jsonObject.get("actors");
                List<String> actorsList = new ArrayList<>();
                for (Object actor : actors) {
                    actorsList.add((String) actor);
                }
                JSONArray genres = (JSONArray) jsonObject.get("genres");
                List<Genre> genresList = new ArrayList<>();
                for (Object genre : genres) {
                    String genreString = ((String) genre).toUpperCase();
                    try {
                        genresList.add(Genre.valueOf(genreString));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid genre: " + genreString);
                    }
                }
                JSONArray ratings = (JSONArray) jsonObject.get("ratings");
                List<Rating> ratingsList = new ArrayList<>();
                for (Object rating : ratings) {
                    JSONObject ratingObj = (JSONObject) rating;
                    String username = (String) ratingObj.get("username");
                    int ratingValue = ((Long) ratingObj.get("rating")).intValue();
                    String comment = (String) ratingObj.get("comment");
                    Rating rating1 = new Rating(username, ratingValue, comment);
                    ratingsList.add(rating1);
                }
                String description = (String) jsonObject.get("plot");
                double avgRating;
                if (jsonObject.get("averageRating") == null) {
                    jsonObject.put("averageRating", 0.0);
                } else {
                    avgRating = (Double) jsonObject.get("averageRating");
                }
                if (type.equals("MOVIE")) {
                    String duration = (String) jsonObject.get("duration");
                    Movie movie = new Movie(title, directorsList, actorsList, genresList, ratingsList, description, duration, releaseYear);
                    productions.add(movie);
                } else {
                    int numberSeasons = ((Long) jsonObject.get("numSeasons")).intValue();
                    Map<String, List<Episode>> episodes = parseEpisodes(jsonObject);
                    Series series = new Series(title, directorsList, actorsList, genresList, ratingsList, description, releaseYear, numberSeasons, episodes);
                    productions.add(series);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException | ParseException e) {
            System.err.println("Error reading/parsing JSON: " + e.getMessage());
        }
        return productions;
    }

    public static List<Actor> loadActorsFromJson() {
        List<Actor> actors = new ArrayList<>();
        JSONParser objParser = new JSONParser();
        Set<String> newActors = new HashSet<>();
        try {
            FileReader reader = new FileReader("input" + File.separator + "actors.json");
            JSONArray input = (JSONArray) objParser.parse(reader);
            for (Object o : input) {
                JSONObject actor = (JSONObject) o;
                String name = (String) actor.get("name");
                newActors.add(name);
                JSONArray performancesJson = (JSONArray) actor.get("performances");
                List<ProductionDetails> performancesList = new ArrayList<>();
                if (performancesJson != null) {
                    for (Object performanceObj : performancesJson) {
                        JSONObject performance = (JSONObject) performanceObj;
                        String title = (String) performance.get("title");
                        String type = (String) performance.get("type");
                        ProductionDetails productionDetails = new ProductionDetails(title, type);
                        performancesList.add(productionDetails);
                    }
                }

                Object biographyObj = actor.get("biography");
                String biography = (biographyObj != null) ? (String) biographyObj : "";

                actors.add(new Actor(name, performancesList, biography));
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException | ParseException e) {
            System.err.println("Error reading/parsing JSON: " + e.getMessage());
        }

        List<Production> productions = loadProductionsFromJson();

        for (Production production : productions) {
            for (String actorName : production.getActors()) {
                if (!newActors.contains(actorName)) {
                    List<ProductionDetails> filmography = new ArrayList<>();
                    filmography.add(new ProductionDetails(production.getTitle(), production.getType()));
                    actors.add(new Actor(actorName, filmography, "-"));
                    newActors.add(actorName);
                }
            }
        }

        return actors;
    }

    private static Map<String, List<Episode>> parseEpisodes(JSONObject jsonObject) {
        Map<String, List<Episode>> episodesMap = new HashMap<>();
        JSONObject seasonsObject = (JSONObject) jsonObject.get("seasons");

        for (Object seasonKey : seasonsObject.keySet()) {
            String season = (String) seasonKey;
            JSONArray episodesArray = (JSONArray) seasonsObject.get(season);
            List<Episode> episodeList = new ArrayList<>();

            for (Object episodeObj : episodesArray) {
                JSONObject episode = (JSONObject) episodeObj;
                String episodeName = (String) episode.get("episodeName");
                String duration = (String) episode.get("duration");
                Episode episodeInstance = new Episode(episodeName, duration);
                episodeList.add(episodeInstance);
            }

            episodesMap.put(season, episodeList);
        }
        return episodesMap;
    }


    public static User createUser(String username, AccountType accountType, String name, int experience, String email, String password,
                                  String country, int age, String gender, String birthDate, List<String> favorites, SortedSet<String> productionsContributionList) throws InformationIncompleteException {
        UserFactory userFactory = new ConcreteUserFactory();
        return userFactory.createUser(username, accountType, name, experience, email, password, country, age, gender, LocalDateTime.parse(birthDate), favorites, productionsContributionList);
    }

}
