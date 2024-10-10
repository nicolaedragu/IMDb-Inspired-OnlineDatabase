import javax.management.Notification;
import java.util.List;
import java.util.SortedSet;

public class Regular extends User implements RequestsManager {
    public Regular(String username, AccountType RegularType, Information information, int experience, List<String> favorites, SortedSet<String> productionsContributionList) {
        super(username, RegularType, information, experience, favorites, productionsContributionList);
    }


    @Override
    public void createRequest(Request r) {
        if (r.getUsername_req_resolver().equals("ADMIN")) {
            RequestsHolder.addRequest(r);
        } else {
            List<User> users = Input.loadUsersFromJson();
            for (User u : users) {
                if (u.getUsername().equals(r.getUsername_req_resolver())) {
                    u.getRequests().add(r.getProblemDescription());
                }
            }
        }
        r.notifyRequestReceiver();
    }

    @Override
    public void removeRequest(Request r) {

    }
}
