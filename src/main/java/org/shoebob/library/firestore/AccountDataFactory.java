package org.shoebob.library.firestore;

import org.shoebob.library.registry.items.user.Patron;
import org.shoebob.library.registry.items.user.Admin;

import java.io.IOException;


public class AccountDataFactory {

    // register methods
    public static void registerPatronAccount(Patron patron, FirestoreDataFactory firestore) {
        firestore.addObject(patron, "patrons", patron.getUsername());
    }

    public static void registerAdminAccount(Admin admin, FirestoreDataFactory firestore) {
        firestore.addObject(admin, "admins", admin.getUsername());
    }

    // get account methods

    public static Patron getPatronAccount(String username, FirestoreDataFactory firestore) {
        return firestore.getObject("patrons", username, Patron.class);
    }

    public static Admin getAdminAccount(String username, FirestoreDataFactory firestore) {
        return firestore.getObject("admins", username, Admin.class);
    }

    // logic methods
    public static boolean validLogin(String username, String password, FirestoreDataFactory firestore) {
        Patron patron = getPatronAccount(username, firestore);
        if (patron != null) {
            return patron.getPassword().equals(password);
        } else {
            return false;
        }
    }
}
