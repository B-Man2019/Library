package org.shoebob.library.data;

import org.shoebob.library.registry.items.Patron;
import org.shoebob.library.registry.items.Admin;

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
    public static boolean validPatronLogin(String username, String password, FirestoreDataFactory firestore) {
        Patron patron = getPatronAccount(username, firestore);
        if (patron != null) {
            return patron.getPassword().equals(password);
        } else {
            return false;
        }
    }

    public static boolean validAdminLogin(String username, String password, FirestoreDataFactory firestore) {
        Admin admin = getAdminAccount(username, firestore);
        if (admin != null) {
            return admin.getPassword().equals(password);
        } else {
            return false;
        }
    }
}
