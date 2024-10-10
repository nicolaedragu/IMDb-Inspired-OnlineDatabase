import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;

public interface UserFactory {
    User createUser(String username, AccountType accountType, String name, int experience, String email, String password, String country, int age, String gender, LocalDateTime birthDate, List<String> favorites, SortedSet<String> productionsContributionList) throws InformationIncompleteException;
}