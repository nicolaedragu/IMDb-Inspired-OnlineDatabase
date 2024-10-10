import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

public class Admin extends Staff {
    public Admin(String username, AccountType AdminType, Information information, int experience, List<String> favorites, SortedSet<String> productionsContributionList) {
        super(username, AdminType, information, experience, favorites, productionsContributionList);
        setStrategy(new AdminStrategy());
    }

    public static String generateUsername(String name) {
        List<String> parts = Arrays.asList(name.toLowerCase().split(" "));
        parts = new ArrayList<>(parts);
        String username;
        parts.add(String.valueOf((int) (Math.random() * 1000)));
        username = String.join("_", parts);
        return username;
    }

    public static String generatePassword() {
        String passwordChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        int passwordLength = 10;
        char[] password = new char[passwordLength];


        for (int i = 0; i < passwordLength; i++) {
            int index = (int) (Math.random() * passwordChars.length());
            password[i] = passwordChars.charAt(index);
        }

        return new String(password);
    }
}
