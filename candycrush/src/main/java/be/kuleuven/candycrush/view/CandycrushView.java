package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.CandycrushModel;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.Iterator;

public class CandycrushView extends Region {
    /*variables*/
    private CandycrushModel model;
    private int widthCandy, heigthCandy, radiusCandy;
    private AnchorPane gameBackground;


    /*constructor*/
    public CandycrushView(CandycrushModel model) {
        this.model = model;
        widthCandy = 50;
        heigthCandy = 50;
        radiusCandy = 15;
        this.update();
        this.setGameBackground();
    }

    /*getters*/
    public AnchorPane getGameBackground() {
        return gameBackground;
    }
    public int getIndexOfClicked(MouseEvent me){
        int index = -1;

        int row = (int) me.getY()/heigthCandy;
        int column = (int) me.getX()/widthCandy;

        System.out.println("x: "+me.getX()+"\ny: "+me.getY()+"\nrow: "+row+"\ncolum: "+column);

        index = model.getIndexFromRowColumn(column,row);
        System.out.println("index of clicked: " + index);

        return index;
    }

    /*setters*/
    public void setGameBackground() {
        gameBackground = new AnchorPane();
        Rectangle achtergrond = new Rectangle(0,0,1600,1000);
        achtergrond.setStroke(new Color(1f,0,0,0));
        gameBackground.getChildren().addAll(achtergrond);
        URL imgURL = getClass().getResource("/Afbeeldingen/GameAchtergrond.gif");
        Image picture = new Image(imgURL.toString());
        ImagePattern pattern = new ImagePattern(picture);
        achtergrond.setFill(pattern);
    }

    /*other methods*/
    public void update() {
        getChildren().clear();
        int xCoordinateITER = 0, yCoordinateITER = 0;       //positive direction of y toward the bottom
        Iterator<Integer> candys = model.getPlayground().iterator();
        while(candys.hasNext()) {
            int candy = candys.next();
            Rectangle candyPlate = new Rectangle(xCoordinateITER*widthCandy, yCoordinateITER*heigthCandy,widthCandy,heigthCandy);
            candyPlate.setFill(Color.TRANSPARENT);
            candyPlate.setStroke(Color.LIGHTGRAY);

            Circle c = candyCircle(candy);
            //Text candyNumber = new Text("" + candy);
            //double xCoordinateCandy = candyPlate.getX() + (candyPlate.getWidth() - candyNumber.getBoundsInLocal().getWidth()) / 2;
            //double yCoordinateCandy = candyPlate.getY() + (candyPlate.getHeight() + candyNumber.getBoundsInLocal().getHeight()) / 2;
            //candyNumber.setX(xCoordinateCandy); candyNumber.setY(yCoordinateCandy);
            double xCoordinateCandy = candyPlate.getX() + candyPlate.getWidth() / 2;
            double yCoordinateCandy = candyPlate.getY() + candyPlate.getHeight() / 2;
            c.setCenterX(xCoordinateCandy); c.setCenterY(yCoordinateCandy);

            getChildren().addAll(candyPlate,c);

            if(xCoordinateITER == model.getWidth() - 1) {
                xCoordinateITER  = 0;
                yCoordinateITER++;
            }
            else {
                xCoordinateITER ++;
            }
        }
    }

    //methode die een cirkel creert afhankelijk van het number van de 'candy'
    private Circle candyCircle(int number) {
        Circle c = new Circle(radiusCandy);

        switch (number) {
            case 1:
                c.setFill(Color.RED);
                break;
            case 2:
                c.setFill(Color.ORANGE);
                break;
            case 3:
                c.setFill(Color.YELLOW);
                break;
            case 4:
                c.setFill(Color.GREEN);
                break;
            case 5:
                c.setFill(Color.BLUE);
                break;
        }
        return c;
    }

}
