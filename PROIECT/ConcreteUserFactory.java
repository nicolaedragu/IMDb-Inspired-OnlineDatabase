import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;

public class ConcreteUserFactory implements UserFactory {
    @Override
    public User createUser(String username, AccountType accountType, String name, int experience, String email, String password, String country, int age, String gender, LocalDateTime birthDate, List<String> favorites, SortedSet<String> productionsContributionList) throws InformationIncompleteException {
        User.Information.informationBuilder informationBuilder = new User.Information.informationBuilder();
        informationBuilder.setCredentials(new Credentials(email, password));
        informationBuilder.setName(name);
        informationBuilder.setCountry(country);
        informationBuilder.setAge(age);
        informationBuilder.setGender(gender);
        informationBuilder.setBirthDate(birthDate);
        switch (accountType) {
            case REGULAR:
                return new Regular(username, AccountType.REGULAR, informationBuilder.build(), experience, favorites, productionsContributionList);
            case CONTRIBUTOR:
                return new Contributor(username, AccountType.CONTRIBUTOR, informationBuilder.build(), experience, favorites, productionsContributionList);
            case ADMIN:
                return new Admin(username, AccountType.ADMIN, informationBuilder.build(), experience, favorites, productionsContributionList);
            default:
                throw new InformationIncompleteException("Invalid account type!");
        }
    }
}
