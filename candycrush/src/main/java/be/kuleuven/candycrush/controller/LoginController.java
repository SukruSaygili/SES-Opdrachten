package be.kuleuven.candycrush.controller;

import be.kuleuven.candycrush.model.Player;
import be.kuleuven.candycrush.view.LoginView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane background;
    @FXML
    private Button startButton;
    @FXML
    private TextField name;

    private LoginView view;
    private Player player;

    //scenewissel
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void initialize() {
        assert background != null : "fx:id=\"background\" was not injected: check your FXML file 'login-view.fxml'.";
        assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'login-view.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'login-view.fxml'.";
        player = new Player("");
        view = new LoginView();
        background.getChildren().addAll(view.getBeginPane());   //achtergrond GIF
        startButton.setOnAction(this::switchToGame);
    }

    public void switchToGame(ActionEvent event) {
        if(name.getText().trim().isEmpty()) {
            name.setPromptText("Please enter a name!");
            System.err.println("Please enter a name before starting the game!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/kuleuven/candycrush/candycrush-view.fxml"));
            Parent root = loader.load();
            CandycrushController controller = loader.getController();
            controller.setPlayer(player);       //spelernaam ingevuld op het loginscherm naar de game doorgeven

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(false);
            stage.show();
            player.setName(name.getText());

            // stop intro liedje
            // view.Stopliedje();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
