import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;

public class Staff extends User implements StaffInterface {
    List<Request> requestsToDo;  // doar acest utilizator trebuie sa le rezolve
    SortedSet<String> ProductionsAndActors; // colectie sortata alfabetic care retine ce a adaugat acest utilizator in sistem


    public Staff(String username, AccountType accountType, Information information, int experience, List<String> favorites, SortedSet<String> productionsContributionList) {
        super(username, accountType, information, experience, favorites, productionsContributionList);
    }

    @Override
    public void addProductionSystem(Production p, List<Production> productions) {
        productions.add(p);
        AllUsersActions.getInstance().updateProductions(productions);
    }

    @Override
    public void addActorSystem(Actor a, List<Actor> firstActors) {
        firstActors.add(a);
        AllUsersActions.getInstance().updateActorsClass(firstActors);
    }


    @Override
    public void removeProductionSystem(String name) {
        // located in AllUsersActions
    }

    @Override
    public void removeActorSystem(String name) {
        // located in AllUsersActions
    }

    @Override
    public void updateProduction(Production p) {
        // located in AllUsersActions class because of the possibility of updating either a movie or a series
    }

    @Override
    public List<Actor> updateActor(Actor a, List<Actor> actors) {
        String name = a.getActorName();
        System.out.println("What do you want to update?");
        System.out.println("1. Name");
        System.out.println("2. Filmography");
        System.out.println("3. Biography");
        String newName = null;
        List<ProductionDetails> newFilmography = null;
        String newBiography = null;
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                System.out.println("Enter the new name: ");
                newName = scanner.nextLine();
                a.setActorName(newName);
                break;
            case 2:
                newFilmography = new ArrayList<>();
                String answer;
                do {
                    System.out.print("Enter the type of the production: ");
                    String type = scanner.nextLine();
                    if (type.equalsIgnoreCase("movie")) {
                        type = "Movie";
                        System.out.print("Enter the name of the " + type + ": ");
                    }
                    else if (type.equalsIgnoreCase("series")) {
                        type = "Series";
                        System.out.print("Enter the name of the " + type + ": ");
                    }
                    String name1 = scanner.nextLine();
                    newFilmography.add(new ProductionDetails(name1, type));
                    System.out.println("Do you want to add another production? (y/n)");
                    answer = scanner.nextLine();
                } while (answer.equalsIgnoreCase("y"));
                a.setFilmography(newFilmography);
                break;
            case 3:
                System.out.println("Enter the new biography: ");
                newBiography = scanner.nextLine();
                a.setBiography(newBiography);
                break;
            default:
                System.out.println("Invalid choice.");
        }

        for (int i = 0; i < actors.size(); i++) {
            if (actors.get(i).getActorName().equals(name)) {
                actors.set(i, a);
                break;
            }
        }
        System.out.println("The page of " + name + " has been updated.\n");
        return actors;
    }
}
