package org.shoebob.library.user;

import org.shoebob.library.Book;

import java.util.ArrayList;

public class Patron extends AbstractUser {
    // TODO: Find valid data type for card #
    int cardNum;
    ArrayList<Book> checkedOutBooks = new ArrayList<Book>();

    public Patron(int cardNum, String firstName, String lastName, String username, String password) {
        super(firstName, lastName, username, password);
        this.cardNum = cardNum;
    }


}
