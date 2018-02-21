package pinkpanthers.pinkshelters;

import java.util.*;

/**
 * Created by hdang on 2/19/18.
 */

public class MockDB implements DBI {
    private static int id = 0;
    private static Map<String, Account> accounts = new HashMap<>(); //stores username and account
    private String username;
    private String password;
    private String database;

    public MockDB(String username, String password, String database) {
        this.username = username;
        this.password = password;
        this.database = database;
    }

    private int idGenerator() {
        return id++;
    }

    @Override
    public Account getAccountByUsername(String username) throws NoSuchUserException {
        Account user = accounts.get(username);
        if (user == null) {
            throw new NoSuchUserException("User " + username + " does not exist.");
        }
        return user;
    }

    @Override
    public Account create(String userType, String username, String password, String name, String email) throws UniqueKeyError {
        if (accounts.keySet().contains(username)) {
            throw new UniqueKeyError("Username already exists: " + username);
        }
        int id = this.idGenerator();
        Account newUser;
        switch (userType) {
            case "Homeless":
                newUser = new Homeless(username, password, name, "active", email, id);
                break;
            case "Volunteer":
                newUser = new Volunteer(username, password, name, "active", email, id);
                break;
            case "Admin":
                newUser = new Admin(username, password, name, "active", email, id);
                break;
            default:
                throw new RuntimeException("You have attempted to create an invalid user type. " +
                        "This should not be possible if the UI is designed correctly.");
        }
        accounts.put(username, newUser);
        return newUser;
    }

    @Override
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }
}
