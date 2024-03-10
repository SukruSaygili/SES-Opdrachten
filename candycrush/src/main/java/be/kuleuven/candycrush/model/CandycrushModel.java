package be.kuleuven.candycrush.model;
import be.kuleuven.CheckNeighboursInGrid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CandycrushModel {
    /*variables*/
    private int width, height;
    private final int lowerLimitAmountOfcandys = 3;
    private final int upperBoundTypesOfCandys = 5, lowerBoundTypesOfCandys = 1;
    private Player player;
    private ArrayList<Integer> playground;


    /*constructors*/
    //customizable constructor
    public CandycrushModel(Player player, int width, int height) {
        this.player = player;
        this.playground = new ArrayList<>();
        this.width = width;
        this.height = height;

        int amountOfCandys = this.width * this.height;
        for (int i = 0; i < amountOfCandys; i++) {
            Random rand = new Random();
            int randomNr = rand.nextInt(upperBoundTypesOfCandys) + lowerBoundTypesOfCandys;
            playground.add(randomNr);
        }
    }

    //default for a standard game (overload)
    public CandycrushModel(Player player) {
        this(player, 9, 9);
    }

    /*main method*/
    public static void main(String[] args) {
        Player player1 = new Player("sukru");
        CandycrushModel model = new CandycrushModel(player1);
        int i = 1;
        Iterator<Integer> iter = model.getPlayground().iterator();
        while (iter.hasNext()) {

            int candy = iter.next();
            System.out.print(candy);

            if ((i % model.getWidth()) == 0) {
                System.out.print("\n");
                i = 1;
            }
            i++;
        }
        System.out.print("\n");
    }


    /*getters*/
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Integer> getPlayground() {
        return playground;
    }

    /*setters*/
    public void setPlayground(ArrayList<Integer> nPlayground) {     //to test the updateCandySelected Method
        this.playground = nPlayground;
    }


    /*other methods*/
    public void updateCandySelected(int index) {
        if (index < 0 || index >= playground.size()) {
            System.out.println("model:candyWithIndexSelected:indexOutOfRange");
            return;
        }

        Iterable<Integer> neighboursIterable = CheckNeighboursInGrid.getSameNeighboursIds(this.playground, this.width, this.height, index);

        Iterator<Integer> neighbours = neighboursIterable.iterator();
        int amountOfNeighbours = 0;
        while (neighbours.hasNext()) {
            neighbours.next();
            amountOfNeighbours++;
        }

        if (amountOfNeighbours == 0) {
            System.out.println("There are no neighbour candys!");
            return;
        }

        if (amountOfNeighbours >= lowerLimitAmountOfcandys) {
            //de index zelf updaten
            Random random = new Random();
            int randomGetal = random.nextInt(upperBoundTypesOfCandys) + lowerBoundTypesOfCandys;
            playground.set(index, randomGetal);

            //buren updaten
            neighbours = neighboursIterable.iterator();
            Random rand = new Random();
            while (neighbours.hasNext()) {
                int randomNr = rand.nextInt(upperBoundTypesOfCandys) + lowerBoundTypesOfCandys;
                int neighbourIndex = neighbours.next();

                if (neighbourIndex >= 0 && neighbourIndex < playground.size()) {
                    playground.set(neighbourIndex, randomNr);
                    player.setScore(player.getScore() + 1);
                }
                else {
                    System.out.println("model:candyWithIndexSelected:neighbourIndexOutOfRange");
                }
            }
            player.setScore(player.getScore() + 1);     //+1, het snoepje zelf dat de buren heeft
        }
        else {
            System.out.println("There are not enough neighbour candys!");
        }
    }

    public int getIndexFromRowColumn(int column, int row) {     //row en column: beginnen vanaf 0
        if (column < this.width && row < this.height) {
            return column + row*width;
        }
        else {
            System.out.println("Column or row, or both are out of range!");
            return -1;
        }
    }
}

