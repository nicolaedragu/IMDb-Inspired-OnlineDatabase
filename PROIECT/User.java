import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public abstract class User {
    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private String gender;
        private LocalDateTime birthDate;

        private Information(Credentials credentials, String name, String country, int age, String gender, LocalDateTime birthDate) {
            this.credentials = credentials;
            this.name = name;
            this.country = country;
            this.age = age;
            this.gender = gender;
            this.birthDate = birthDate;
        }

        static class informationBuilder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String gender;
            private LocalDateTime birthDate;

            public informationBuilder setCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public informationBuilder setName(String name) {
                this.name = name;
                return this;
            }

            public informationBuilder setCountry(String country) {
                this.country = country;
                return this;
            }

            public informationBuilder setAge(int age) {
                this.age = age;
                return this;
            }

            public informationBuilder setGender(String gender) {
                this.gender = gender;
                return this;
            }

            public informationBuilder setBirthDate(LocalDateTime birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information build() throws InformationIncompleteException {
                if (credentials == null || name == null || country == null || age < 0 || gender == null || birthDate == null) {
                    throw new InformationIncompleteException("Error: Information is incomplete!");
                }
                return new Information(credentials, name, country, age, gender, birthDate);
            }
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public String getGender() {
            return gender;
        }

        public LocalDateTime getBirthDate() {
            return birthDate;
        }
    }

    String username;
    Information information;
    AccountType accountType;
    int experience;

    List<String> notifications;
    List<String> favorites; // Productions and Actors
    List<String> requests;
    SortedSet<String> productionsContributionList; // colectie sortata alfabetic care retine ce a adaugat acest utilizator in sistem

    private ExperienceStrategy strategy; // variabila necesara pentru implementarea Strategy Pattern

    // adminul introduce numele persoanei, username-ul (generat!!, si sa fie unic), parola (generata!!, puternica)
    public User(String username, AccountType accountType, Information information, int experience, List<String> favorites, SortedSet<String> productionsContributionList) {
        this.username = username;
        this.accountType = accountType;
        this.information = information;
        setExperience(experience);
        this.notifications = new ArrayList<>();
        this.notifications.add("Welcome to IMDB!");
        this.favorites = favorites;
        this.requests = new ArrayList<>();
        this.productionsContributionList = productionsContributionList;
    }

    public String getUsername() {
        return username;
    }
    public Information getInformation() {
        return information;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public int getExperience() {
        return experience;
    }
    public List<String> getNotifications() {
        return notifications;
    }
    public SortedSet<String> getProductionsContributionList() {
        return productionsContributionList;
    }
    public void showProductionsContributionList() {
        if (this.productionsContributionList.isEmpty())
            System.out.println("Your contribution list is empty.");
        else
            System.out.println("This is your contribution list: " + this.productionsContributionList);
    }
    public void setProductionsContributionList(SortedSet<String> productionsContributionList) {
        this.productionsContributionList = productionsContributionList;
    }
    public List<String> getFavorites() {
        return favorites;
    }
    public void showFavorites() {
        if (this.favorites.isEmpty())
            System.out.println("Your favorites list is empty.");
        else
            System.out.println("This is your favorites list: " + this.favorites);
    }
    public List<String> getRequests() {
        return requests;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
    // asta pentru cand creezi un utilizator nou

    void notification(String notification) {
        notifications.add(notification);
    }

    // updatat utilizatorul in legatura cu outcome-ul requestului
    void update(Request request) {
        if (request.getIsResolved()) {
            notifications.add("Your request has been accepted.");
        } else {
            notifications.add("Your request has been rejected.");
        }
    }

    public void addFavoriteProduction(String production) {
        List<Production> productions = Input.loadProductionsFromJson();
        boolean found = false;
        for (Production p : productions) {
            if (p.getTitle().equalsIgnoreCase(production)) {
                this.favorites.add(p.getTitle());
                System.out.println("Production added successfully.");
                found = true;
                break;
            }
        }
        if (!found)
            System.out.println("Production not found.");
    }

    public void addFavoriteActor(String actor) {
        List<Actor> actors = Input.loadActorsFromJson();
        boolean found = false;
        for (Actor a : actors) {
            if (a.getActorName().equalsIgnoreCase(actor)) {
                this.favorites.add(a.getActorName());
                System.out.println("Actor added successfully.");
                found = true;
                break;
            }
        }
        if (!found)
            System.out.println("Actor not found.");
    }

//    EXPERIENTA creste atunci cand:
//    se actualizeaza atunci cand utilizatorul adauga o recenzie,
//    adauga o productie in sistem sau un actor in sistem

//    uitlizatorii Admin au experienta infinita, asadar lor nu li se modifica experienta

    public void setStrategy(ExperienceStrategy strategy) {
        this.strategy = strategy;
        int experience = getExperiencePoints();
        updateExperience(experience);
    }

    public int getExperiencePoints() {
        if (strategy == null) {
            throw new IllegalStateException("Strategy not set!");
        }
        return strategy.calculateExperience();
    }

    public void updateExperience(int newExperience) {
        experience += newExperience;
    }

    //    dupa logout se revine la pagina de logare
    public void logout() {
        System.out.println("You have been logged out.\nReturning to main page...\n");

    }
}
