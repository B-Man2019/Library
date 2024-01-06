package org.shoebob.library.entities;

import org.shoebob.library.interfaces.RegistryItem;

public class Admin implements RegistryItem {
    String firstName, lastName;
    String username, password;
    public Admin(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public Admin() {}

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
}
