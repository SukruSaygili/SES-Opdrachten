package be.kuleuven.candycrush;

import be.kuleuven.candycrush.model.CandycrushModel;
import be.kuleuven.candycrush.model.Player;
import be.kuleuven.candycrush.view.CandycrushView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class CandycrushController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane motherPanel;
    @FXML
    private AnchorPane gamePanel;
    @FXML
    private Text scoreText;
    @FXML
    private Text resetText;
    @FXML
    private Text helloText;
    @FXML
    private Button resetButton;

    private CandycrushModel model;
    private Player player;
    private CandycrushView view;

    @FXML
    void initialize() {
        assert motherPanel != null : "fx:id=\"motherPanel\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert gamePanel != null : "fx:id=\"gamePanel\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert scoreText != null : "fx:id=\"scoreText\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert resetText != null : "fx:id=\"resetText\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert helloText != null : "fx:id=\"helloText\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert resetButton != null : "fx:id=\"resetButton\" was not injected: check your FXML file 'candycrush-view.fxml'.";

        player = new Player("test");
        model = new CandycrushModel(player);
        view = new CandycrushView(model);
        gamePanel.getChildren().add(view);
        view.setOnMouseClicked(this::candyTouch);
    }

    public void update() {
        view.update();
    }

    public void candyTouch(MouseEvent me) {
        int candyIndex = view.getIndexOfClicked(me);
        model.updateCandySelected(candyIndex);
        update();
    }
}