package org.shoebob.library.entities;

import org.shoebob.library.interfaces.RegistryItem;

import java.util.ArrayList;

public class Patron implements RegistryItem {
    String firstName, lastName;
    String username, password;

    String cardNum;
    ArrayList<String> checkedOutBooks = new ArrayList<>();

    public Patron() {};

    public Patron(String cardNum, String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;

        this.cardNum = cardNum;
    }

    // getter methods

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

    public String getCardNum() {
        return cardNum;
    }

    // setter methods


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

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    // book-related methods

    public ArrayList<String> getCheckedOutBooks() {
        return checkedOutBooks;
    }

    public String getCheckedOutBook(int index) {
        return checkedOutBooks.get(index);
    }

    public void addCheckedOutBook(Book book) {
        checkedOutBooks.add(book.getIsbn());
    }

    public void removeCheckedOutBook(int index) {
        checkedOutBooks.remove(index);
    }

    public String toString() {
        return "Name: " + firstName + ", " + lastName + "\nUsername: " + username + "\nPassword: " + password
                + "\nCard num: " + cardNum;
    }
}
