package be.kuleuven.candycrush.model;

import java.util.*;
import java.util.function.Function;

public class Board <T> {
    private BoardSize bs;
    private HashMap<Position,T> playgroundMAP;
    private HashMap<T, Set<Position>> playgroundMAPREV;


    /*Constructor*/
    public Board(BoardSize bs, HashMap<Position,T> playgroundMAP) {
        this.bs = bs;
        this.playgroundMAP = playgroundMAP;
        this.playgroundMAPREV = new HashMap<>();

        for(Map.Entry<Position,T> entry : playgroundMAP.entrySet()) {
            T element = entry.getValue();
            Position position = entry.getKey();
            playgroundMAPREV.computeIfAbsent(element, k -> new HashSet<>()).add(position);
        }
    }

    /*getters*/
    public BoardSize getBs() {
        return bs;
    }
    public HashMap<Position, T> getPlaygroundMAP() {
        return playgroundMAP;
    }

    /*setters*/
    public void setPlaygroundMAP(HashMap<Position, T> playgroundMAP) {
        this.playgroundMAP = playgroundMAP;
    }

    /*Other methods*/
    public T getCellAt(Position position) {
        if (!isValidPosition(position)) {
            System.out.println("Invalid position! Position could not be found!");
            return null;
        }
        return playgroundMAP.get(position);
    }
    public void replaceCellAt(Position p, T newCell) {
        if (!isValidPosition(p)) {
            System.out.println("Invalid position! Position could not be found and the cell could not be replaced!");
            return;
        }

        T oldCell = playgroundMAP.put(p,newCell);

        if(oldCell != null) {
            Set<Position> pos = playgroundMAPREV.get(oldCell);
            if(pos != null) {
                pos.remove(p);
                if(pos.isEmpty()){
                    playgroundMAPREV.remove(oldCell);
                }
            }
        }
        playgroundMAPREV.computeIfAbsent(newCell,k->new HashSet<>()).add(p);
    }
    public void fill(Function<Position, T> cellCreator) {
        for (Position position : bs.positions()) {
            T newCell = cellCreator.apply(position);
            playgroundMAP.put(position,newCell);
            playgroundMAPREV.computeIfAbsent(newCell, k -> new HashSet<>()).add(position);
        }
    }
    public void copyTo(Board<T> otherBoard) throws IllegalArgumentException {
        if (!this.bs.equals(otherBoard.bs)) {
            throw new IllegalArgumentException("Boards have different sizes!");
        }

        otherBoard.playgroundMAP.clear();
        otherBoard.playgroundMAPREV.clear();

        for(Map.Entry<Position, T> entry : playgroundMAP.entrySet()) {
            Position pos = entry.getKey();
            T element = entry.getValue();
            otherBoard.playgroundMAP.put(pos,element);
            otherBoard.playgroundMAPREV.computeIfAbsent(element, k-> new HashSet<>()).add(pos);
        }

    }
    //geeft een lege set terug indien er geen posities zijn voor het element
    public Set<Position> getPositionsOfElement(T element) {
        return Collections.unmodifiableSet(playgroundMAPREV.getOrDefault(element,
                Collections.emptySet()));
    }
    //help method
    private boolean isValidPosition(Position position) {
        return position != null && playgroundMAP.containsKey(position);
    }
}
