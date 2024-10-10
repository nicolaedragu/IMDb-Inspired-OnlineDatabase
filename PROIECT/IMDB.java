import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class IMDB {
    private static IMDB instance = null;

    private IMDB() {
        users = new ArrayList<>();
        productions = new ArrayList<>();
        actors = new ArrayList<>();
    }

    private static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    public int login(List<User> users) {
        int indexOfUser;
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print("    email: ");
                String email = scanner.nextLine();
                System.out.print("    password: ");
                String password = scanner.nextLine();
                if (users.stream().anyMatch(user -> user.getInformation().getCredentials().getEmail().equals(email) && user.getInformation().getCredentials().getPassword().equals(password))) {
                    System.out.println("You have successfully logged in as " + email + "!");
                    indexOfUser = users.indexOf(users.stream().filter(user -> user.getInformation().getCredentials().getEmail().equals(email) && user.getInformation().getCredentials().getPassword().equals(password)).findFirst().get());
                    break;
                } else {
                    System.out.println("Invalid credentials. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid email and password.");
            }
        }
        System.out.println("Username: " + users.get(indexOfUser).getUsername());
        return indexOfUser;
    }

    private void exit(List<User> users) {
        System.out.println("Do you want to exit the app or log in with another account?");
        System.out.println("    1) Exit");
        System.out.println("    2) Log in with another account");
        int option = 0;
        boolean ret = true;
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                option = scanner.nextInt();
                scanner.nextLine();
                if (option == 1) {
                    ret = true;
                } else if (option == 2) {
                    System.out.println("You have chosen to log in with another account.");
                    ret = false;
                } else {
                    throw new InvalidInputException("Invalid option. Please enter 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number, either 1 or 2.");
                scanner.nextLine();
                option = 0;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
                option = 0;
            }
        } while (option != 1 && option != 2);
        if (ret) {
            System.exit(0);
        } else {
            IMDB.getInstance().mainMenu(users);
        }
    }

    private void mainMenu(List<User> users) {
        int option = 0;
        Scanner scanner = new Scanner(System.in);
        int indexOfUser = IMDB.getInstance().login(users);
        System.out.println();
        if (users.get(indexOfUser).getAccountType() == AccountType.REGULAR) {
            do {

                if (users.get(indexOfUser).getExperience() == 0) {
                    System.out.println("User experience: -");
                } else {
                    if (users.get(indexOfUser).getExperience() == 999) {
                        System.out.println("User experience: " + users.get(indexOfUser).getExperience() + " (admin)");
                    } else {
                        System.out.print("User experience: " + users.get(indexOfUser).getExperience());
                    }
                }
                System.out.println();

                System.out.println("Choose your preferred action: ");
                System.out.println("    1) View productions details");
                System.out.println("    2) View actors details");
                System.out.println("    3) View your notifications");
                System.out.println("    4) Search for actor/movie/series");
                System.out.println("    5) Add/Delete actor/movie/series to/from your favorites");
                System.out.println("    6) Create/Delete a request");
                System.out.println("    7) Add/Delete a review");
                System.out.println("    8) Logout");
                try {
                    option = scanner.nextInt();
                    if (option == 1) {
                        System.out.println("You have chosen to view productions details.\n");
                        AllUsersActions.getInstance().viewProductionDetails(productions);
                    } else if (option == 2) {
                        System.out.println("You have chosen to view actors details.\n");
                        AllUsersActions.getInstance().viewActorDetails(actors);
                    } else if (option == 3) {
                        System.out.println("You have chosen to view your notifications.\n");
                        AllUsersActions.getInstance().viewNotifications(users.get(indexOfUser));
                    } else if (option == 4) {
                        System.out.println("You have chosen to search for actor/movie/series.\n");
                        AllUsersActions.getInstance().searchProductionOrActor();
                    } else if (option == 5) {
                        System.out.println("You have chosen to add/delete actor/movie/series to/from your favorites.\n");
                        AllUsersActions.getInstance().addOrDeleteProductionOrActorFromFavorites(users.get(indexOfUser));
                    } else if (option == 6) {
                        System.out.println("You have chosen to create/delete a request.\n");
                        AllUsersActions.getInstance().createOrDeleteRequest(users.get(indexOfUser));
                    } else if (option == 7) {
                        System.out.println("You have chosen to add/delete a review.\n");
                        AllUsersActions.getInstance().addOrDeleteReview(users.get(indexOfUser));
                    } else if (option == 8) {
                        System.out.println("You have chosen to logout.\n");
                        users.get(indexOfUser).logout();
                        IMDB.getInstance().exit(users);
                    } else {
                        System.out.println("Invalid option. Please enter a number between 1 and 8.\n");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 8.\n");
                }
            } while (option != 8);
        }

        if (users.get(indexOfUser).getAccountType() == AccountType.CONTRIBUTOR) {
            do {
                System.out.println("    1) View productions details");
                System.out.println("    2) View actors details");
                System.out.println("    3) View your notifications");
                System.out.println("    4) Search for actor/movie/series");
                System.out.println("    5) Add/Delete actor/movie/series to/from your favorites");
                System.out.println("    6) Create/Delete a request");
                System.out.println("    7) Add/Delete actor/movie/series from the database");
                System.out.println("    8) View and solve requests");
                System.out.println("    9) Update Production details");
                System.out.println("    10) Update Actor details");
                System.out.println("    11) Logout");
                try {
                    option = scanner.nextInt();
                    if (option == 1) {
                        System.out.println("You have chosen to view productions details.\n");
                        AllUsersActions.getInstance().viewProductionDetails(productions);
                    } else if (option == 2) {
                        System.out.println("You have chosen to view actors details.\n");
                        AllUsersActions.getInstance().viewActorDetails(actors);
                    } else if (option == 3) {
                        System.out.println("You have chosen to view your notifications.\n");
                        AllUsersActions.getInstance().viewNotifications(users.get(indexOfUser));
                    } else if (option == 4) {
                        System.out.println("You have chosen to search for actor/movie/series.\n");
                        AllUsersActions.getInstance().searchProductionOrActor();
                    } else if (option == 5) {
                        System.out.println("You have chosen to add/delete actor/movie/series to/from your favorites.\n");
                        AllUsersActions.getInstance().addOrDeleteProductionOrActorFromFavorites(users.get(indexOfUser));
                    } else if (option == 6) {
                        System.out.println("You have chosen to create/delete a request.\n");
                        AllUsersActions.getInstance().createOrDeleteRequest(users.get(indexOfUser));
                    } else if (option == 7) {
                        System.out.println("You have chosen to add/delete actor/movie/series from the database.\n");
                        Staff staff = (Staff) users.get(indexOfUser);
                        System.out.println("Choose an option:");
                        System.out.println("    1) Add production");
                        System.out.println("    2) Add actor");
                        System.out.println("    3) Delete production");
                        System.out.println("    4) Delete actor");
                        int option2 = scanner.nextInt();
                        scanner.nextLine();
                        switch (option2) {
                            case 1:
                                productions = AllUsersActions.getInstance().addProduction(staff, productions);
                                users.get(indexOfUser).setStrategy(new AddProductStrategy());
                                break;
                            case 2:
                                actors = AllUsersActions.getInstance().addActor(staff, actors);
                                users.get(indexOfUser).setStrategy(new AddActorStrategy());
                                break;
                            case 3:
                                System.out.println("Productions: ");
                                for (Production production : productions) {
                                    System.out.print(production.getTitle() + ", ");
                                }
                                System.out.println();
                                System.out.print("Enter production name to delete: ");
                                String productionName = scanner.nextLine();

                                productions = AllUsersActions.getInstance().deleteProduction(productions, productionName);
                                break;
                            case 4:
                                System.out.println("Actors: ");
                                for (Actor actor : actors) {
                                    System.out.print(actor.getActorName() + ", ");
                                }
                                System.out.println();
                                System.out.print("Enter actor name to delete: ");
                                String actorName = scanner.nextLine();

                                actors = AllUsersActions.getInstance().deleteActor(actors, actorName);
                                break;
                            default:
                                System.out.println("Invalid option. Please enter a number between 1 and 4.");
                                break;
                        }
                    } else if (option == 8) {
                        System.out.println("You have chosen to view and solve requests.\n");
                        AllUsersActions.getInstance().viewRequests(users.get(indexOfUser));
                    } else if (option == 9) {
                        System.out.println("You have chosen to update Production details.\n");
                        AllUsersActions.getInstance().updateProductionDetails();
                    } else if (option == 10) {
                        System.out.println("You have chosen to update Actor details.");
                        AllUsersActions.getInstance().updateActor(indexOfUser);
                    } else if (option == 11) {
                        System.out.println("You have chosen to logout.\n");
                        users.get(indexOfUser).logout();
                        IMDB.getInstance().exit(users);
                    } else {
                        System.out.println("Invalid option. Please enter a number between 1 and 11.\n");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 11.\n");
                }
            } while (option != 11);
        }

        if (users.get(indexOfUser).getAccountType() == AccountType.ADMIN) {
            do {
                System.out.println("    1) View productions details");
                System.out.println("    2) View actors details");
                System.out.println("    3) View your notifications");
                System.out.println("    4) Search for actor/movie/series");
                System.out.println("    5) Add/Delete actor/movie/series to/from your favorites");
                System.out.println("    6) Add/Delete actor/movie/series from the database");
                System.out.println("    7) View and solve requests");
                System.out.println("    8) Update Production details");
                System.out.println("    9) Update Actor details");
                System.out.println("    10) Add or delete an user");
                System.out.println("    11) Logout");
                try {
                    option = scanner.nextInt();
                    if (option == 1) {
                        System.out.println("You have chosen to view productions details.\n");
                        AllUsersActions.getInstance().viewProductionDetails(productions);
                    } else if (option == 2) {
                        System.out.println("You have chosen to view actors details.\n");
                        AllUsersActions.getInstance().viewActorDetails(actors);
                    } else if (option == 3) {
                        System.out.println("You have chosen to view your notifications.\n");
                        AllUsersActions.getInstance().viewNotifications(users.get(indexOfUser));
                    } else if (option == 4) {
                        System.out.println("You have chosen to search for actor/movie/series.\n");
                        AllUsersActions.getInstance().searchProductionOrActor();
                    } else if (option == 5) {
                        System.out.println("You have chosen to add/delete actor/movie/series to/from your favorites.\n");
                        AllUsersActions.getInstance().addOrDeleteProductionOrActorFromFavorites(users.get(indexOfUser));
                    } else if (option == 6) {
                        System.out.println("You have chosen to add/delete actor/movie/series from the database.\n");
                        Staff staff = (Staff) users.get(indexOfUser);
                        System.out.println("Choose an option:");
                        System.out.println("    1) Add production");
                        System.out.println("    2) Add actor");
                        System.out.println("    3) Delete production");
                        System.out.println("    4) Delete actor");
                        int option2 = scanner.nextInt();
                        scanner.nextLine();
                        switch (option2) {
                            case 1:
                                productions = AllUsersActions.getInstance().addProduction(staff, productions);
                                break;
                            case 2:
                                actors = AllUsersActions.getInstance().addActor(staff, actors);
                                break;
                            case 3:
                                System.out.println("Productions: ");
                                for (Production production : productions) {
                                    System.out.print(production.getTitle() + ", ");
                                }
                                System.out.println();
                                System.out.print("Enter production name to delete: ");
                                String productionName = scanner.nextLine();

                                productions = AllUsersActions.getInstance().deleteProduction(productions, productionName);
                                break;
                            case 4:
                                System.out.println("Actors: ");
                                for (Actor actor : actors) {
                                    System.out.print(actor.getActorName() + ", ");
                                }
                                System.out.println();
                                System.out.print("Enter actor name to delete: ");
                                String actorName = scanner.nextLine();

                                actors = AllUsersActions.getInstance().deleteActor(actors, actorName);
                                break;
                            default:
                                System.out.println("Invalid option. Please enter a number between 1 and 4.");
                                break;
                        }
                    } else if (option == 7) {
                        System.out.println("You have chosen to view and solve requests.\n");
                        AllUsersActions.getInstance().viewRequests(users.get(indexOfUser));
                    } else if (option == 8) {
                        System.out.println("You have chosen to update Production details.\n");
                        AllUsersActions.getInstance().updateProductionDetails();
                    } else if (option == 9) {
                        System.out.println("You have chosen to update Actor details.\n");
                        AllUsersActions.getInstance().updateActor(indexOfUser);
                    } else if (option == 10) {
                        System.out.println("You have chosen to add/delete user.\n");
                        users = AllUsersActions.getInstance().addOrDeleteUser(users);
                    } else if (option == 11) {
                        System.out.println("You have chosen to logout.\n");
                        users.get(indexOfUser).logout();
                        IMDB.getInstance().exit(users);
                    } else {
                        System.out.println("Invalid option. Please enter a number between 1 and 11.\n");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 11.\n");
                } catch (InformationIncompleteException e) {
                    System.err.println(e.getMessage());
                    throw new RuntimeException(e);
                }
            } while (option != 11);
        }
    }

    public void run() {
        getInstance().users = Input.loadUsersFromJson();
        getInstance().productions = Input.loadProductionsFromJson();
        getInstance().actors = Input.loadActorsFromJson();
        System.out.println("Welcome to IMDB!");
        System.out.println("Please choose your preferred option of using the app: ");
        System.out.println("    1) Terminal");
        System.out.println("    2) GUI");
        int option;
        Scanner scanner = new Scanner(System.in);

        // alegeri posibile: 1 sau 2 pentru Terminal sau GUI
        do {
            try {
                option = scanner.nextInt();
                scanner.nextLine();
                if (option == 1) {
                    System.out.println("You have chosen to use the app in Terminal mode.");
                } else if (option == 2) {
                    System.out.println("You have chosen to use the app in GUI mode.");
                } else {
                    throw new InvalidInputException("Invalid option. Please enter 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number, either 1 or 2.");
                scanner.nextLine();
                option = 0;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
                option = 0;
            }
        } while (option != 1 && option != 2);
        // inceputul programului
        System.out.println("Welcome! Please enter your credentials to log in: ");
        System.out.println();

        // login + main menu
        getInstance().mainMenu(users);
    }

    private List<User> users;
    private List<Production> productions;
    private List<Actor> actors;


    public static void main(String[] args) {
        IMDB.getInstance().run();
    }
}
