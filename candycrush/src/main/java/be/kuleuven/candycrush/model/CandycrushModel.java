package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.Candy.*;
import javafx.geometry.Pos;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CandycrushModel {
    private final int lowerLimitAmountOfNeighbourcandys = 3;
    private Player player;
    private Board<Candy> board;


    /*constructors*/
    //customizable constructor
    public CandycrushModel(Player player, BoardSize bs) {
        this.player = player;
        board = new Board<>(bs,new ConcurrentHashMap<>());
        board.fill(position -> generateRandomCandy());
        board.updateBoard();
    }
    //default for a standard game (overload)
    public CandycrushModel(Player player) {
        this(player, new BoardSize(9,9));
    }

    //create model from ASCII
    public static CandycrushModel createBoardFromString(String configuration) {
        var lines = configuration.toLowerCase().lines().toList();
        BoardSize size = new BoardSize(lines.size(), lines.getFirst().length());
        var model = new CandycrushModel(new Player("default"), size); // Create model using the constructor
        for (int row = 0; row < lines.size(); row++) {
            var line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                model.getBoard().replaceCellAt(new Position(row, col, size), characterToCandy(line.charAt(col)));
            }
        }
        return model;
    }
    private static Candy characterToCandy(char c) {
        return switch(c) {
            case '.' -> null;
            case 'o' -> new NormalCandy(0);
            case '*' -> new NormalCandy(1);
            case '#' -> new NormalCandy(2);
            case '@' -> new NormalCandy(3);
            default -> throw new IllegalArgumentException("Unexpected value: " + c);
        };
    }
    public static void printBoard(CandycrushModel model) {
        Board<Candy> board = model.getBoard();
        BoardSize size = board.getBs();

        for (int row = 0; row < size.height(); row++) {
            for (int col = 0; col < size.width(); col++) {
                Position position = new Position(row, col, size);
                Candy candy = board.getCellAt(position);
                System.out.print(candy == null ? "." : candy.toString());
            }
            System.out.println();
        }
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
            board.replaceCellAt(pos,new LegeCandy());      //LegeCandy => lege plaats

            //buren updaten
            sameNeighbours = neighboursIterable.iterator();
            while (sameNeighbours.hasNext()) {
                board.replaceCellAt(sameNeighbours.next(),new LegeCandy());       //LegeCandy => lege plaats
                player.setScore(player.getScore() + 1);
            }
            player.setScore(player.getScore() + 1);     //+1, het snoepje zelf dat de buren heeft
        }
        else {
            System.out.println("There are not enough neighbour candys!");
        }
    }
    public Iterable<Position> getSameNeighbourPositions(Position position) {
        var neighboursPos = position.neighbourPositions().iterator();
        var sameNeighboursPos = new ArrayList<Position>();

        var candyOnPosition = board.getCellAt(position);
        if(candyOnPosition.equals(new LegeCandy())) {
            System.out.println("EMPTY SPACE!");
            return sameNeighboursPos;                         //LegeCandy == 'lege plaats' => niet toevoegen => geen score
        }

        while(neighboursPos.hasNext()) {
            var neighbourPos = neighboursPos.next();
            var candyOnNeighbourPos = board.getCellAt(neighbourPos);

            if(candyOnPosition.equals(candyOnNeighbourPos)) {
                sameNeighboursPos.add(neighbourPos);
            }
        }
        return sameNeighboursPos;
    }
    public static Candy generateRandomCandy() {     //statisch gemaakt om in threadclient gemakkelijk te kunnen gebruiken
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