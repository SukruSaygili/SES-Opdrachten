package be.kuleuven.candycrush.controller;

import be.kuleuven.candycrush.model.CandycrushModel;
import be.kuleuven.candycrush.model.Player;
import be.kuleuven.candycrush.model.Position;
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
import java.util.Objects;

public class CandycrushController {
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
    private Position firstClickedPosition = null;

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
        view.setOnMouseClicked(this::candyTouchSelect);

        resetButton.setOnAction(this::switchToBegin);
    }

    public void update() {
        model.getBoard().updateBoard();
        model.getPlayer().setScore(model.getBoard().getAmountOfCandiesDeleted());
        view.update();
        nameText.setText(model.getPlayer().getName());
        scoreText.setText(""+ model.getPlayer().getScore());
        System.out.println("Naam: "+ model.getPlayer().getName() + "   Score: " + model.getPlayer().getScore());
    }

    public void candyTouch(MouseEvent me) {
        model.updateCandySelected(view.getPosOfClicked(me));
        update();
    }

    public void candyTouchSelect(MouseEvent me) {
        Position clickedPosition = view.getPosOfClicked(me);
        view.drawSelectionAround(clickedPosition);
        if (firstClickedPosition == null) {
            firstClickedPosition = clickedPosition;
        }
        else {
            model.getBoard().switchCells(firstClickedPosition, clickedPosition);
            firstClickedPosition = null;
            update();
        }
    }

    public void switchToBegin(ActionEvent event) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/be/kuleuven/candycrush/login-view.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(false);
            stage.show();
        }
        catch (IOException ignored) {
        }
    }

    //om de spelernaam ingevuld in het loginscherm,  in de candycrushcontroller te krijgen
    public void setPlayer(Player player) {
        this.player = player;
        model.setPlayer(player);
    }
}