package be.kuleuven.candycrush.model;

import java.util.ArrayList;
import java.util.stream.Stream;

public record Position(int rowNr, int columnNr, BoardSize bs) {
    /*Constructor*/
    public Position {
        if(!(rowNr >= 0 && rowNr < bs.height() && columnNr >= 0 && columnNr < bs.width()))
            throw new IllegalArgumentException("The entered position is not valid!");
    }

    /*Other methods*/
    public int toIndex() {
        return rowNr * bs.width() + columnNr;
    }
    public static Position fromIndex(int index, BoardSize size) {
        if(index < 0 || index >= size.height() * size.width()) {
            throw new IllegalArgumentException("Invalid index!");
        }
        return new Position(index/size.width(),index%size.width(),size);
    }
    public Iterable<Position> neighbourPositions() {
        ArrayList<Position> neighbours = new ArrayList<>();
        int indexToCheck = toIndex();

        int left = indexToCheck - 1, topLeftCorner = indexToCheck - bs.width() - 1, bottomLeftCorner = indexToCheck + bs.width() - 1,
        right = indexToCheck + 1, topRightCorner = indexToCheck - bs.width() + 1, bottomRightCorner = indexToCheck + bs.width() + 1,
        top = indexToCheck - bs.width(), bottom = indexToCheck + bs.width();

        if (columnNr > 0) {
            neighbours.add(fromIndex(left, bs));

            if (rowNr > 0) {
                neighbours.add(fromIndex(topLeftCorner, bs));
            }

            if (rowNr < bs.height() - 1) {
                neighbours.add(fromIndex(bottomLeftCorner, bs));
            }
        }

        if (columnNr < bs.width() - 1) {
            neighbours.add(fromIndex(right, bs));

            if (rowNr > 0) {
                neighbours.add(fromIndex(topRightCorner, bs));
            }

            if (rowNr < bs.height() - 1) {
                neighbours.add(fromIndex(bottomRightCorner, bs));
            }
        }

        if (rowNr > 0) {
            neighbours.add(fromIndex(top, bs));
        }

        if (rowNr < bs.height() - 1) {
            neighbours.add(fromIndex(bottom, bs));
        }

        return neighbours;
    }
    public boolean isLastColumn() {
        return columnNr == (bs.width()-1);
    }

    public Stream<Position> walkLeft() {
        return bs.positions().stream()
                .filter(p ->  p.rowNr() == this.rowNr() && p.columnNr() <= this.columnNr())
                .toList()                                                               //stream omdraaien zodat de teruggegeven stream
                .reversed()                                                             //begint met de positie zelf en daarna die ernaast enz.
                .stream();
    }
    public Stream<Position> walkRight() {
        return bs.positions().stream()
                .filter(p ->  p.rowNr() == this.rowNr() && p.columnNr() >= this.columnNr());
    }
    public Stream<Position> walkUp() {
        return bs.positions().stream()
                 /*vergelijking waaraan een positie moet voldoen om 'boven of op' this-positie te kunnen zitten,
                   bepaalt de index van een candy die boven this-positie zit
                   in de rij waarvan het rijnummer is gegeven aan onderstaande vergelijking*/
                .filter(p -> p.rowNr() <= this.rowNr && p.toIndex() == (this.toIndex() - (this.bs.width() * (this.rowNr - p.rowNr))))
                .toList()
                .reversed()
                .stream();
    }
    public Stream<Position> walkDown() {
        return bs.positions().stream()
                /*vergelijking waaraan een positie moet voldoen om 'onder of op' this-positie te kunnen zitten,
                  bepaalt de index van een candy die onder this-positie zit
                  in de rij waarvan het rijnummer is gegeven aan onderstaande vergelijking*/
                .filter(p -> p.rowNr() >= this.rowNr && p.toIndex() == (this.toIndex() + (this.bs.width() * (p.rowNr - this.rowNr))));
    }
}
