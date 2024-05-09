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
        updateBoard();
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

    private boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions) {
        List<Position> firstTwoPositions = positions.limit(2).toList();

        if (firstTwoPositions.size() < 2) {
            return false;
        }

        return firstTwoPositions.stream().allMatch(position -> board.getCellAt(position).equals(candy));
    }
    private Stream<Position> horizontalStartingPositions() {
        return board.getBs().positions().stream()
                .filter(position -> !firstTwoHaveCandy(board.getCellAt(position),position.walkLeft())
                        && !board.getCellAt(position).equals(new LegeCandy()));
                        //LegeCandys niet meetellen in matches
    }
    private Stream<Position> verticalStartingPositions() {
        return board.getBs().positions().stream()
                .filter(position -> !firstTwoHaveCandy(board.getCellAt(position),position.walkUp())
                        && !board.getCellAt(position).equals(new LegeCandy()));
                        //LegeCandys niet meetellen in matches
    }
    private List<Position> longestMatchToRight(Position pos) {
        return pos.walkRight()
                .takeWhile(position -> board.getCellAt(position).equals(board.getCellAt(pos)))
                .toList();
    }
    private List<Position> longestMatchDown(Position pos) {
        return pos.walkDown()
                .takeWhile(position -> board.getCellAt(position).equals(board.getCellAt(pos)))
                .toList();
    }

    public Set<List<Position>> findAllMatches() {
        var horizontalMatchPos = horizontalStartingPositions()
                .map(this::longestMatchToRight);        //geeft een stream van lists van positions (Stream<list<Position>>)

        var verticalMatchPos = verticalStartingPositions()
                .map(this::longestMatchDown);

        return Stream.concat(horizontalMatchPos, verticalMatchPos)
                .filter(l -> l.size() >= 3).collect(Collectors.toSet());
    }

    public void clearMatch(List<Position> match) {
        if(match.isEmpty()) return;

        board.replaceCellAt(match.getFirst(),new LegeCandy());      //LegeCandy => stelt een lege candy voor
                                                                    //een muisklik erop zorgt voor GEEN reactie
        clearMatch(match.subList(1,match.size()));
    }

    public void fallDownTo(Position pos) {
        var positionsAbovePos = pos.walkUp().toList();
        if(pos.rowNr() == 0) return;

        if(board.getCellAt(pos).equals(new LegeCandy())) {
            for(var position : positionsAbovePos) {
                if(position.rowNr() == 0) break;
                copyCandyFromTo(positionsAbovePos.get((positionsAbovePos.indexOf(position)+1)), position);
            }
        }

        fallDownTo(positionsAbovePos.get(1));
    }
    //Hulpfunctie voor de fallDownTo()-methode
    private void copyCandyFromTo(Position from, Position to) {
        Candy candy = board.getCellAt(to);
        board.replaceCellAt(to,board.getCellAt(from));
        board.replaceCellAt(from,candy);
    }

    //Enkel deze methode mag opgeroepen worden, want de counter-parameter mag niet gekozen worden.
    public boolean updateBoard() {
        return updateBoard(0);
    }
    private boolean updateBoard(int counter) {
        var allMatches = findAllMatches();
        if(allMatches.isEmpty()) {
            boolean verwijderd = counter > 0;
            System.out.println("#verwijderd:"+counter);
            System.out.println("MEER DAN 1 MATCH VERWIJDERD: "+verwijderd);
            return verwijderd;
        }

        for(var match : allMatches) {
            clearMatch(match);
            counter++;
        }

        board.getBs().positions().forEach(this::fallDownTo);

        return updateBoard(counter);
    }
}