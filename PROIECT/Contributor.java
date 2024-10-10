import java.util.List;
import java.util.SortedSet;

public class Contributor extends Staff implements RequestsManager {
    public Contributor(String username, AccountType ContributorType, Information information, int experience, List<String> favorites, SortedSet<String> productionsContributionList) {
        super(username, ContributorType, information, experience, favorites, productionsContributionList);
    }


    @Override
    public void createRequest(Request r) {
        try {
            for (User user : Input.loadUsersFromJson()) {
                if (user.getProductionsContributionList().contains(r.getTitleOrActorName())) {
                    r.setUsername_req_resolver(user.getUsername());
                    break;
                }
            }
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
        } catch (Exception e) {
            System.out.println("The request was not sent.");
        }
        r.notifyRequestReceiver();
    }

    @Override
    public void removeRequest(Request r) {
    }

}
