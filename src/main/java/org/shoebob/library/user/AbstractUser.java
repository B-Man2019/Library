package org.shoebob.library.user;

import java.util.UUID;

// TODO: Change user info to be in a database
public abstract class AbstractUser {
    private UUID uuid;
    private String firstName, lastName;
    private String username, password; // TODO: insecure, change later

    public AbstractUser(String firstName, String lastName, String username, String password) {
        this.uuid = UUID.randomUUID();

        this.firstName = firstName;
        this.lastName = lastName;

        this.username = username;
        this.password = password;
    }

    // Accessor Methods

    public UUID getUUID() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setter methods
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "UUID: " + uuid.toString() + "\nLast, first: " + lastName + ", "  + firstName + "\nUsername: "
                + username + "\nPassword: " + password;
    }
}
