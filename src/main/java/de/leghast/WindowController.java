package de.leghast;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.*;

public class WindowController implements Initializable {

    @FXML
    private TableView<Kunde> table;
    @FXML
    private TableColumn<Kunde, String> tableFirstName;
    @FXML
    private TableColumn<Kunde, String> tableLastName;
    @FXML
    private TableColumn<Kunde, String> tablePhone;
    @FXML
    private TableColumn<Kunde, String> tableMail;
    @FXML
    private TableColumn<Kunde, String> tableDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}