package org.shoebob.library;

import org.shoebob.library.enums.Tags;
import org.shoebob.library.registry_items.Book;
import org.shoebob.library.registry_items.user.Admin;
import org.shoebob.library.registry_items.user.Patron;

import java.io.IOException;

public class Main {
    public static final boolean DEBUG = true;

    public static void main(String[] args) {
        FirestoreDataFactory firestore = null;
        try {
            firestore = new FirestoreDataFactory();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Registry<Book> bookRegistry = new Registry<>();
        Registry<Patron> patronRegistry = new Registry<>();
        Registry<Admin> adminRegistry = new Registry<>();



    }
}