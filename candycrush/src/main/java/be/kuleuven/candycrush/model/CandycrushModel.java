package be.kuleuven.candycrush.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CandycrushModel {
    /*variables*/
    private final BoardSize bs;
    private final int lowerLimitAmountOfNeighbourcandys = 3;
    private Player player;
    private ArrayList<Candy> playground;


    /*constructors*/
    //customizable constructor
    public CandycrushModel(Player player, BoardSize bs) {
        this.player = player;
        this.playground = new ArrayList<>();
        this.bs = bs;

        int amountOfCandys = bs.width() * bs.height();
        for (int i = 0; i < amountOfCandys; i++) {
            playground.add(generateRandomCandy());
        }
    }
    //default for a standard game (overload)
    public CandycrushModel(Player player) {
        this(player, new BoardSize(9,9));
    }

    /*main method*/
    public static void main(String[] args) {
        Player player1 = new Player("sukru");
        CandycrushModel model = new CandycrushModel(player1);
        int i = 1;
        Iterator<Candy> iter = model.getPlayground().iterator();
        while (iter.hasNext()) {
            Candy c = iter.next();
            System.out.print(c);

            if ((i % model.getBs().width()) == 0) {
                System.out.print("\n");
                i = 1;
            }
            i++;
        }
        System.out.print("\n");
    }

    /*getters*/
    public BoardSize getBs() {
        return bs;
    }
    public Player getPlayer() {
        return player;
    }
    public ArrayList<Candy> getPlayground() {
        return playground;
    }

    /*setters*/
    public void setPlayground(ArrayList<Candy> nPlayground) {     //to test the updateCandySelected Method
        this.playground = nPlayground;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    /*other methods*/
    public void updateCandySelected(Position pos) {
        Iterable<Position> neighboursIterable = getSameNeighbourPositions(pos);
        Iterator<Position> sameNeighbours = neighboursIterable.iterator();

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
            playground.set(pos.toIndex(),generateRandomCandy());

            //buren updaten
            sameNeighbours = neighboursIterable.iterator();
            while (sameNeighbours.hasNext()) {
                int neighbourIndex = sameNeighbours.next().toIndex();

                if (neighbourIndex >= 0 && neighbourIndex < playground.size()) {
                    playground.set(neighbourIndex, generateRandomCandy());
                    player.setScore(player.getScore() + 1);
                }
                else {
                    System.out.println("model:candyWithIndexSelected:neighbourIndexOutOfRange");
                }
            }
            player.setScore(player.getScore() + 1);     //+1, het snoepje zelf dat de buren heeft
            System.out.println(player.getName());
        }
        else {
            System.out.println("There are not enough neighbour candys!");
        }
    }
    public Iterable<Position> getSameNeighbourPositions(Position position) {
        Iterator<Position> neighboursPos = position.neighbourPositions().iterator();
        ArrayList<Position> sameNeighboursPos = new ArrayList<>();

        var candyOnPosition = playground.get(position.toIndex());

        while(neighboursPos.hasNext()) {
            var neighbourPos = neighboursPos.next();
            var candyOnNeighbourPos = playground.get(neighbourPos.toIndex());

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

