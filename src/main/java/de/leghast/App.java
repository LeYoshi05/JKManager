package de.leghast;

import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * JavaFX App
 */
public class App extends Application {

    private static PasswordCheck passwordTest;
    private static Button button;
    private static Scene scene;
    private static PasswordField passwordField;
    private static ProgressIndicator progressIndicator;
    private static Label loadingLabel;
    private static Label falseLabel;
    private static Button newAccButton;
    private int really;
    private DatabaseDisplay databaseDisplay;
    String jarPath;

    Boolean startOK = true;

    @Override

    public void start(Stage stage) throws IOException {

        try {
            jarPath = getClass()
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        startGO();

        if (!startOK) { // Unterscheidung zwischem erstem Durchlauf und folgenden
                        // Nutzungsversuchen
            firstStart(stage, false);
        } else {
            start2(stage);
        }

    }

    public void start2(Stage stage) throws IOException { // Login-Maske
        databaseDisplay = new DatabaseDisplay();
        really = 0;
        scene = new Scene(loadFXML("login"));
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();

        AnchorPane anchorPane = (AnchorPane) scene.lookup("#anchorPane"); // Import der Elemente
        button = (Button) anchorPane.lookup("#loginButton");
        progressIndicator = (ProgressIndicator) anchorPane.lookup("#progress");
        loadingLabel = (Label) anchorPane.lookup("#loadingLabel");
        falseLabel = (Label) anchorPane.lookup("#falsePass");
        loadingLabel.setVisible(false);
        progressIndicator.setVisible(false);
        passwordField = (PasswordField) anchorPane.lookup("#passwordField");

        newAccButton = (Button) anchorPane.lookup("#buttonNewAcc");

        newAccButton.addEventHandler(MouseEvent.MOUSE_CLICKED, deleteEventHandler(stage, newAccButton));

        button.addEventHandler(MouseEvent.MOUSE_CLICKED, loginEventHandler(stage));
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    checkPassword(passwordField.getText(), stage);
                }
            }
        });
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws IOException {
        launch(); // nur zur Ausführbarkeit notwendig

    }

    public EventHandler<Event> loginEventHandler(Stage stage) {
        EventHandler<Event> handler = new EventHandler<Event>() {

            @Override
            public void handle(Event event) { // Wenn LOGIN-Knopf gedrückt: Passwort-Check!
                checkPassword(passwordField.getText(), stage);
            }
        };
        return handler;
    }

    public EventHandler<Event> deleteEventHandler(Stage stage, Button button) {
        EventHandler<Event> handler = new EventHandler<Event>() {

            @Override
            public void handle(Event event) { // Wenn LOGIN-Knopf gedrückt: Passwort-Check!

                if (really <= 0) {
                    button.setText("Account löschen?");
                    really = 1;
                } else {

                    File f = new File("./saves/databaseConn.42");
                    File g = new File("./saves/firstRun.42");
                    if (f.exists())
                        f.delete();
                    if (g.exists())
                        g.delete();
                    try {
                        firstStart(stage, false);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        return handler;
    }

    public EventHandler<Event> firstStartHandler(Stage stage, TextField webaddress, TextField username,
            PasswordField databasePass, PasswordField newPass, TextField portField, TextField nameField) { // HAHAHA,
                                                                                                           // das sind
                                                                                                           // viele
        EventHandler<Event> handler = new EventHandler<Event>() {

            @Override
            public void handle(Event event) {

                passwordTest = new PasswordCheck();
                if (webaddress.getText().equals("") | username.getText().equals("") | databasePass.getText().equals("")
                        | newPass.getText().equals("")) { // Falls Nutzer Eingabe vergessen hat
                    try {
                        firstStart(stage, true); // Lade Maske mit Fehlermeldung
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    try {
                        BufferedWriter writer;
                        writer = new BufferedWriter( // schreibe Passwort
                                new FileWriter("./saves/firstRun.42"));
                        writer.append(passwordTest.toHexString(newPass.getText()));
                        writer.close();

                        writer = new BufferedWriter( // schreibe Datenbankdetails
                                new FileWriter("./saves/databaseConn.42"));
                        String address = webaddress.getText() + ":" + portField.getText() + "/" + nameField.getText();
                        writer.write(address + "\n");
                        writer.append(username.getText() + "\n");
                        writer.append(databasePass.getText());

                        writer.close();
                        start2(stage); // Fahre mit Login fort.
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        return handler;
    }

    private void checkPassword(String password, Stage stage) {

        try {
            if (passwordTest.checkPassword(password)) { // Joa, wenn wahr bzw. falsch halt unterschiedliche Funktionen
                passwordTrue(stage);
                falseLabel.setVisible(false);
            } else {
                falseLabel.setVisible(true);
                passwordField.setText("");
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

    }

    private void passwordTrue(Stage stage) {
        passwordField.setDisable(true); // Beende Passworteingabe
        passwordField.setText("");
        passwordField.setPromptText("Korrektes Passwort.");
        button.setDisable(true);

        try {
            databaseDisplay.run(stage);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void firstStart(Stage stage, Boolean showError) throws IOException {

        String dirName = "./";

        Files.list(new File(dirName).toPath())
                .limit(10)
                .forEach(path -> {
                    System.out.println(path);
                });

        scene = new Scene(loadFXML("firstLogin")); // Setup der Eingabemaske
        stage.setScene(scene);
        stage.setTitle("Konto erstellen");
        stage.show();

        if (showError) {
            Label errLabel = (Label) scene.lookup("#labelErr"); // Anzeigen der FEhlermeldung
            errLabel.setVisible(true);
        }

        Button startButton = (Button) scene.lookup("#button"); // Import der Elemente
        TextField address = (TextField) scene.lookup("#webaddress");
        TextField name = (TextField) scene.lookup("#username");
        PasswordField dataPass = (PasswordField) scene.lookup("#passwordDatabase");
        PasswordField newPass = (PasswordField) scene.lookup("#newPass");
        TextField portField = (TextField) scene.lookup("#port");
        TextField nameField = (TextField) scene.lookup("#databaseName");
        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                firstStartHandler(stage, address, name, dataPass, newPass, portField, nameField));
    }

    private void startGO() {
        File f = new File("./saves/firstRun.42"); // Datei wird gesucht

        if (f.exists() && !f.isDirectory()) { // Wenn existiert:
            passwordTest = new PasswordCheck();
        } else { // sonst Datei erstellen und First-Time-Setup starten.
            startOK = false;// <---- sorgt für Setup
        }
    }
}