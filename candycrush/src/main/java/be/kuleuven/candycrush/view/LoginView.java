package be.kuleuven.candycrush.view;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.net.URL;

public class LoginView extends Region {
    /*variables*/
    private AnchorPane backgroundLogin;

    /*constructor*/
    public LoginView() {
        teken();
    }

    /*getters*/
    public AnchorPane getBeginPane(){
        return backgroundLogin;
    }

    /*other methods*/
    public void teken() {
        backgroundLogin = new AnchorPane();

        Rectangle figuur1 = new Rectangle(0,0,1200,675);
        figuur1.setStroke(new Color(1f,0,0,0));

        Rectangle figuur2 = new Rectangle(450,212.5,300,250);
        figuur2.setStroke(new Color(1f,0,0,0));

        backgroundLogin.getChildren().addAll(figuur1,figuur2);

        URL imgURL = getClass().getResource("/Afbeeldingen/LoginAchtergrond.jpg");
        assert imgURL != null;
        Image picture = new Image(imgURL.toString());
        ImagePattern pattern = new ImagePattern(picture);

        figuur1.setFill(pattern);

        URL imgURL2 = getClass().getResource("/Afbeeldingen/NameGIF.gif");
        assert imgURL2 != null;
        Image picture2 = new Image(imgURL2.toString());
        ImagePattern pattern2 = new ImagePattern(picture2);
        figuur2.setFill(pattern2);
    }
}
