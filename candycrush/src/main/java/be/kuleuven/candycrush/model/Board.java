package be.kuleuven.candycrush.model;

import java.util.ArrayList;
import java.util.function.Function;

public class Board <T> {
    private BoardSize bs;
    private ArrayList<T> playground;

    /*Constructor*/
    public Board(BoardSize bs, ArrayList<T> playground) {
        this.bs = bs;
        this.playground = playground;
    }

    /*getters*/
    public BoardSize getBs() {
        return bs;
    }
    public ArrayList<T> getPlayground() {
        return playground;
    }

    /*setters*/
    public void setPlayground(ArrayList<T> playground) {
        this.playground = playground;
    }

    /*Other methods*/
    public T getCellAt(Position position) {
        if (!isValidPosition(position)) {
            System.out.println("Invalid position! Position could not be found!");
            return null;
        }

        return playground.get(position.toIndex());
    }
    public void replaceCellAt(Position p, T newCell) {
        if (!isValidPosition(p)) {
            System.out.println("Invalid position! Position could not be found and the cell could not be replaced!");
            return;
        }
        playground.set(p.toIndex(), newCell);
    }
    public void fill(Function<Position, T> cellCreator) {
        for (Position position : bs.positions()) {
            T newCell = cellCreator.apply(position);
            playground.add(newCell);
        }
    }
    public void copyTo(Board<T> otherBoard) throws IllegalArgumentException {
        if (!this.bs.equals(otherBoard.bs)) {
            throw new IllegalArgumentException("Boards have different sizes!");
        }

        for (int i = 0; i < playground.size(); i++) {
            otherBoard.playground.set(i, playground.get(i));
        }
    }
    //help method
    private boolean isValidPosition(Position position) {
        return position != null && position.toIndex() >= 0 && position.toIndex() < playground.size();
    }
}
