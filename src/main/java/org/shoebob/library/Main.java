package org.shoebob.library;

import org.shoebob.library.firestore.AccountDataFactory;
import org.shoebob.library.firestore.FirestoreDataFactory;
import org.shoebob.library.io.Menu;
import org.shoebob.library.io.SimpleInput;
import org.shoebob.library.registry.items.Book;
import org.shoebob.library.registry.Registry;
import org.shoebob.library.registry.items.user.Admin;
import org.shoebob.library.registry.items.user.Patron;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner scan = new Scanner(System.in);

    public static final boolean DEBUG = true;
    public static accountType accountType = null;

    public static final Registry<Book> bookRegistry = new Registry<>();
    public static final Registry<Patron> patronRegistry = new Registry<>();
    public static final Registry<Admin> adminRegistry = new Registry<>();

    public static FirestoreDataFactory firestore = null;
    static {
        System.out.println("Connecting to Firestore...\n");
        try {
            firestore = new FirestoreDataFactory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public enum accountType {
        PATRON,
        ADMIN
    }

    public static void main(String[] args) {
        System.out.println("Creating test patron...");
        firestore.addObject(new Patron(
                "50", "Test", "User", "TestUser", "test"),
                "patrons",
                "TestUser"
        );
        System.out.println("Test patron created!");

        Menu menu = new Menu("Select account type");

        menu.addAction(() -> {
            accountType = accountType.PATRON;
        }, "Patron");

        menu.addAction(() -> {
            accountType = accountType.ADMIN;
        }, "Admin");

        menu.addAction(() -> {
            System.exit(0);
        }, "Exit");

        System.out.println(menu);
        menu.getInput();

        if (accountType == accountType.PATRON) {
            Menu patronMenu = new Menu("Welcome Patron!");
            patronMenu.addAction(() -> {
                startPatronLogin();
            }, "Login");
            patronMenu.addAction(() -> {
                startPatronSignup();
            }, "Signup");

            System.out.println(patronMenu);
            patronMenu.getInput();
        } else if (accountType == accountType.ADMIN) {
            startAdminLogin();
        }
    }

    private static void startPatronLogin() {
        SimpleInput usernameInput = new SimpleInput("Enter username");
        String username = usernameInput.getStringInput();

        SimpleInput passwordInput = new SimpleInput("Enter password");
        String password = passwordInput.getStringInput();

        boolean valid = AccountDataFactory.validLogin(username, password, firestore);
        if (valid) {
            System.out.println("Good login!");
        } else {
            System.out.println("Bad login.");
        }
    }

    private static void startPatronSignup() {

    }

    private static void startAdminLogin() {

    }
}