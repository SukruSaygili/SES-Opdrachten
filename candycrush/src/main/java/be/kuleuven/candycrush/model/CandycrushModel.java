package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.Candy.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CandycrushModel {
    /*variables*/
    private final int lowerLimitAmountOfNeighbourcandys = 3;
    private Player player;
    private Board<Candy> board;


    /*constructors*/
    //customizable constructor
    public CandycrushModel(Player player, BoardSize bs) {
        this.player = player;
        board = new Board<>(bs,new HashMap<>());
        board.fill(position -> generateRandomCandy());
    }
    //default for a standard game (overload)
    public CandycrushModel(Player player) {
        this(player, new BoardSize(9,9));
    }

    /*main method*/
    public static void main(String[] args) {
        Player player1 = new Player("sukru");
        CandycrushModel model = new CandycrushModel(player1);

        for (Position p : model.getBoard().getBs().positions()) {
            System.out.print(model.getBoard().getPlaygroundMAP().get(p));

            if (p.isLastColumn()) {
                System.out.print("\n");
            }
        }
        System.out.print("\n");
    }

    /*getters*/
    public Board<Candy> getBoard() {
        return board;
    }
    public Player getPlayer() {
        return player;
    }

    /*setters*/
    public void setPlayer(Player player) {
        this.player = player;
    }

    /*other methods*/
    public void updateCandySelected(Position pos) {
        var neighboursIterable = getSameNeighbourPositions(pos);
        var sameNeighbours = neighboursIterable.iterator();

        int amountOfSameNeighbours = 0;
        while (sameNeighbours.hasNext()) {
            sameNeighbours.next();
            amountOfSameNeighbours++;
        }

        if (amountOfSameNeighbours == 0) {
            System.out.println("There are no neighbour candys!");
            return;
        }

        if (amountOfSameNeighbours >= lowerLimitAmountOfNeighbourcandys) {
            //de index zelf updaten
            board.replaceCellAt(pos,generateRandomCandy());

            //buren updaten
            sameNeighbours = neighboursIterable.iterator();
            while (sameNeighbours.hasNext()) {
                board.replaceCellAt(sameNeighbours.next(),generateRandomCandy());
                player.setScore(player.getScore() + 1);
            }
            player.setScore(player.getScore() + 1);     //+1, het snoepje zelf dat de buren heeft
            System.out.println(player.getName());
        }
        else {
            System.out.println("There are not enough neighbour candys!");
        }
    }
    public Iterable<Position> getSameNeighbourPositions(Position position) {
        var neighboursPos = position.neighbourPositions().iterator();
        var sameNeighboursPos = new ArrayList<Position>();

        var candyOnPosition = board.getCellAt(position);

        while(neighboursPos.hasNext()) {
            var neighbourPos = neighboursPos.next();
            var candyOnNeighbourPos = board.getCellAt(neighbourPos);

            if(candyOnPosition.equals(candyOnNeighbourPos)) {
                sameNeighboursPos.add(neighbourPos);
            }
        }
        return sameNeighboursPos;
    }
    public Candy generateRandomCandy() {
        Random random = new Random();
        int candy = random.nextInt(10);
        //Indien zonder kansen, een normale candy komt vrij weinig voor wat niet logisch is
        return switch (candy) {
            case 0, 1, 2, 3, 4, 5 -> new NormalCandy(random.nextInt(4)); //60% kans voor NormalCandy
            case 6 -> new AllesGrezend();                                       //10% kans voor AllesGrezend
            case 7 -> new OnderVolledig();                                      //10% kans voor OnderVolledig
            case 8 -> new DubbelPunt();                                         //10% kans voor DubbelPunt
            case 9 -> new RandomBom();                                          //10% kans voor RandomBom
            default -> null;
        };
    }
}