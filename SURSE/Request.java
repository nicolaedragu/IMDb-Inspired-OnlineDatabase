import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Request {
    public enum RequestType {
        DELETE_ACCOUNT,
        ACTOR_ISSUE,
        MOVIE_ISSUE,
        OTHERS
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private RequestType requestType;
    private LocalDateTime creationDate;
    private String titleOrActorName;
    private String problemDescription;
    private String Username_req_creator;
    private String Username_req_resolver;
    private boolean isResolved;

    public Request(RequestType requestType, String titleOrActorName, String problemDescription, String Username_req_creator) {
        this.requestType = requestType;
        this.creationDate = LocalDateTime.now();
        this.titleOrActorName = titleOrActorName;
        this.problemDescription = problemDescription;
        this.Username_req_creator = Username_req_creator;
        this.Username_req_resolver = null;
        this.isResolved = false;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public String getTitleOrActorName() {
        return titleOrActorName;
    }
    public String getProblemDescription() {
        return problemDescription;
    }
    public String getUsername_req_creator() {
        return Username_req_creator;
    }
    public String getUsername_req_resolver() {
        if (Username_req_resolver == null)
            return "ADMIN";
        else
            return Username_req_resolver;
    }
    public boolean getIsResolved() {
        return isResolved;
    }
    public void setUsername_req_creator(String username_req_creator) {
        Username_req_creator = username_req_creator;
    }
    public void setUsername_req_resolver(String username_req_resolver) {
        Username_req_resolver = username_req_resolver;
    }

//    Daca cererea e DELETE_ACCOUNT sau OTHERS, cererea -> lista tuturor adminilor, Username_req_resolver = "ADMIN",
//    Daca cererea e ACTOR_ISSUE sau MOVIE_ISSUE, cererea -> lista de cereri a utilizatorului care a introdus prodctia/actorul respectiv
//    Un Admin / Contributor poate rezolva sau respinge o cerere. Daca o rezolva, utilizatorul care a creat-o va primi experienta
//    Se introduce manual DOAR tipul de cerere (requestType) si descrierea problemei (problemDescription)
//    Se introduce automat data crearii cererii (creationDate), titlul prodctiei/actorului (titleOrActorName), (Username_req_creator)


    private List<User> observersRegular = new ArrayList<>();
    private List<User> observersContributor = new ArrayList<>();
    private List<User> observersAdmin = new ArrayList<>();

    //    Daca CONTRIBUTOR / ADMIN primesc o cerere destinata lor, vor primi o notificare
    public void notifyRequestReceiver() {
        if (this.requestType == RequestType.DELETE_ACCOUNT || this.requestType == RequestType.OTHERS) {
            this.Username_req_resolver = "ADMIN";
            for (User observer : observersAdmin) {
                observer.update(this);
                observer.notification("You have received a request " + requestType + " from " + Username_req_creator + "!");
            }
        } else {
            if (this.requestType == RequestType.ACTOR_ISSUE || this.requestType == RequestType.MOVIE_ISSUE) {
                // gasirea username-ului utilizatorului care a adaugat productia in sistem
                // daca este Contributor, va primi o notificare
                for (User user : Input.loadUsersFromJson()) {
                    if (user.getProductionsContributionList().contains(this.titleOrActorName)) {
                        this.Username_req_resolver = user.getUsername();
                        observersContributor.add(user);
                    }
                }
                for (User user : observersContributor) {
                    if (user.getUsername().equals(this.Username_req_resolver)) {
                        user.update(this);
                        user.notification("You have received a " + requestType + " request from " + Username_req_creator + "!");
                    }
                }
            }
        }
    }


    //    Daca o (rezolva || respinge) creatorul cererii va fi notificat REGULAR / CONTRIBUTOR
//    metoda ce trimite notificarile catre utilizatorii REGULAR
    public void notifyRegularObservers() {
        for (User observer : observersRegular) {
            observer.update(this);
            if (this.isResolved)
                observer.notification("Cererea " + titleOrActorName + " a fost rezolvata!");
            else
                observer.notification("Cererea " + titleOrActorName + " a fost respinsa!");
        }
    }

    //    metoda ce trimite notificarile catre utilizatorii Contributor
    public void notifyContributorObservers() {
        for (User observer : observersContributor) {
            observer.update(this);
            if (this.isResolved)
                observer.notification("Cererea " + titleOrActorName + " a fost rezolvata!");
            else
                observer.notification("Cererea " + titleOrActorName + " a fost respinsa!");
        }
    }


    public String getFormattedCreationDate() {
        return creationDate.format(formatter);
    }
}
