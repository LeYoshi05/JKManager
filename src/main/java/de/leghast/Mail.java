package de.leghast;

import java.io.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Mail {
    private static Scene scene;
    private static Stage stage;

    public void runMailSetup(Stage staget) throws IOException {
        stage = staget;
        scene = new Scene(loadFXML("setupEmail"));
        stage.setScene(scene);
        stage.setTitle("E-Mail Setup");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
