package be.kuleuven.candycrush.controller;

import be.kuleuven.candycrush.model.CandycrushModel;
import be.kuleuven.candycrush.model.Player;
import be.kuleuven.candycrush.view.CandycrushView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CandycrushController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane motherPanel, gamePanel, backgroundImage;
    @FXML
    private Text resetText;
    @FXML
    private Text helloText;
    @FXML
    private Text nameText;
    @FXML
    private Text scoreText;
    @FXML
    private Button resetButton;

    private CandycrushModel model;
    private Player player;
    private CandycrushView view;

    //scenewissel
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void initialize() {
        assert motherPanel != null : "fx:id=\"motherPanel\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert gamePanel != null : "fx:id=\"gamePanel\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert scoreText != null : "fx:id=\"scoreText\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert resetText != null : "fx:id=\"resetText\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert helloText != null : "fx:id=\"helloText\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert resetButton != null : "fx:id=\"resetButton\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert backgroundImage != null : "fx:id=\"backgroundImage\" was not injected: check your FXML file 'candycrush-view.fxml'.";

        model = new CandycrushModel(player);
        view = new CandycrushView(model);

        backgroundImage.getChildren().addAll(view.getGameBackground());
        gamePanel.getChildren().addAll(view);

        scoreText.setText(""+ 0);
        view.setOnMouseClicked(this::candyTouch);

        resetButton.setOnAction(this::switchToBegin);
    }

    public void update() {
        view.update();
        nameText.setText(player.getName());
        scoreText.setText(""+ player.getScore());
    }

    public void candyTouch(MouseEvent me) {
        int candyIndex = view.getIndexOfClicked(me);
        model.updateCandySelected(candyIndex);
        update();
        System.out.println(player.getName());
        System.out.println(model.getPlayer().getScore());
    }

    public void switchToBegin(ActionEvent event) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/be/kuleuven/candycrush/login-view.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(false);
            stage.show();
            //stop intro liedje
            //view.Stopliedje();
        }
        catch (IOException e) {
            // er gebeurt niks, error notification is ook overbodig
        }
    }

    //om de spelernaam ingevuld in het loginscherm,  in de candycrushcontroller te krijgen
    public void setPlayer(Player player) {
        this.player = player;
        model.setPlayer(player);
    }
}