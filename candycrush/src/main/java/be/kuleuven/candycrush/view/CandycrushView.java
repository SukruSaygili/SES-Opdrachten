package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.CandycrushModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Iterator;

public class CandycrushView extends Region {
    private CandycrushModel model;
    private int widthCandy, heigthCandy;

    /*constructor*/
    public CandycrushView(CandycrushModel model) {
        this.model = model;
        widthCandy = 50;
        heigthCandy = 50;
        this.update();
    }

    public void update() {
        getChildren().clear();
        int xCoordinateITER = 0, yCoordinateITER = 0;       //positive direction of y toward the bottom
        Iterator<Integer> candys = model.getPlayground().iterator();
        while(candys.hasNext()) {
            int candy = candys.next();
            Rectangle candyPlate = new Rectangle(xCoordinateITER*widthCandy, yCoordinateITER*heigthCandy,widthCandy,heigthCandy);
            candyPlate.setFill(Color.TRANSPARENT);
            candyPlate.setStroke(Color.LIGHTGRAY);

            Text candyNumber = new Text("" + candy);
            double xCoordinateCandy = candyPlate.getX() + (candyPlate.getWidth() - candyNumber.getBoundsInLocal().getWidth()) / 2;
            double yCoordinateCandy = candyPlate.getY() + (candyPlate.getHeight() + candyNumber.getBoundsInLocal().getHeight()) / 2;
            candyNumber.setX(xCoordinateCandy); candyNumber.setY(yCoordinateCandy);

            getChildren().addAll(candyPlate,candyNumber);

            if(xCoordinateITER == model.getWidth() - 1) {
                xCoordinateITER  = 0;
                yCoordinateITER++;
            }
            else {
                xCoordinateITER ++;
            }
        }
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
}
