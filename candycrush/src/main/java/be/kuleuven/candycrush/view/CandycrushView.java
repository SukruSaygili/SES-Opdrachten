package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.*;
import be.kuleuven.candycrush.model.Candy.*;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class CandycrushView extends Region {
    /*variables*/
    private CandycrushModel model;
    private final int widthCandyPlate = 50, heigthCandyPlate = 50, radiusCandy = 15;
    private AnchorPane gameBackground;


    /*constructor*/
    public CandycrushView(CandycrushModel model) {
        this.model = model;
        this.update();
        this.setGameBackground();
    }

    /*getters*/
    public AnchorPane getGameBackground() {
        return gameBackground;
    }
    public Position getPosOfClicked(MouseEvent me){
        int row = (int) me.getY()/ heigthCandyPlate;
        int column = (int) me.getX()/ widthCandyPlate;
        Position posOfClicked = new Position(row,column,model.getBoard().getBs());

        System.out.println("x: "+me.getX()+"\ny: "+me.getY()+"\nrow: "+row+"\ncolumn: "+column);

        System.out.println("position of clicked: " + posOfClicked);

        return posOfClicked;
    }

    /*setters*/
    public void setGameBackground() {
        gameBackground = new AnchorPane();
        Rectangle achtergrond = new Rectangle(0,0,1600,1000);
        achtergrond.setStroke(new Color(1f,0,0,0));
        gameBackground.getChildren().addAll(achtergrond);
        URL imgURL = getClass().getResource("/Afbeeldingen/GameAchtergrond.gif");
        assert imgURL != null;
        Image picture = new Image(imgURL.toString());
        ImagePattern pattern = new ImagePattern(picture);
        achtergrond.setFill(pattern);
    }

    /*other methods*/
    public void update() {
        getChildren().clear();

        Set<Candy> uniqueCandies = new HashSet<>(model.getBoard().getPlaygroundMAP().values());

        for (Candy c : uniqueCandies) {
            Set<Position> positions = model.getBoard().getPositionsOfElement(c);
            for (Position pos : positions) {
                Rectangle candyPlate = new Rectangle(pos.columnNr() * widthCandyPlate, pos.rowNr() * heigthCandyPlate,
                        widthCandyPlate, heigthCandyPlate);

                candyPlate.setFill(Color.TRANSPARENT);
                candyPlate.setStroke(Color.LIGHTGRAY);

                Node candyShape = makeCandyShape(pos, c);

                getChildren().addAll(candyPlate, candyShape);
            }
        }
    }

    //methode die een cirkel creert afhankelijk van het nummer van de 'candy' op een bepaalde x en y coordinaat
    private Circle candyCircle(double x, double y, int number) {
        Circle c = new Circle(x, y, radiusCandy);

        switch (number) {
            case 0:
                c.setFill(Color.RED);
                break;
            case 1:
                c.setFill(Color.ORANGE);
                break;
            case 2:
                c.setFill(Color.YELLOW);
                break;
            case 3:
                c.setFill(Color.GREEN);
                break;
            default:
                throw new IllegalArgumentException("Unknow colornumber!");
        }
        return c;
    }
    private Node makeCandyShape(Position position, Candy candy) {
        double xCircle = position.columnNr() * widthCandyPlate + ((double) widthCandyPlate /2);
        double yCircle = position.rowNr() * heigthCandyPlate + ((double) heigthCandyPlate /2);
        double xRectangle = xCircle - (double) radiusCandy;     //linkerbovenhoek vierkant
        double yRectangle = yCircle - (double) radiusCandy;     //linkerbovenhoek vierkant
        Rectangle specialCandy = new Rectangle(xRectangle, yRectangle, radiusCandy*2,radiusCandy*2);
        //yield: blokcode voor switch-expressie, daarom
        return switch(candy) {
            case NormalCandy(int color) -> candyCircle(xCircle,yCircle,color);
            case OnderVolledig() -> {
                specialCandy.setFill(Color.CYAN);
                yield specialCandy;
            }
            case AllesGrezend() -> {
                specialCandy.setFill(Color.BLUEVIOLET);
                yield specialCandy;
            }
            case DubbelPunt() -> {
                specialCandy.setFill(Color.DARKGOLDENROD);
                yield specialCandy;
            }
            case RandomBom() -> {
                specialCandy.setFill(Color.FUCHSIA);
                yield specialCandy;
            }
            default -> throw new IllegalArgumentException("Unknown candy!");
        };
    }
}
