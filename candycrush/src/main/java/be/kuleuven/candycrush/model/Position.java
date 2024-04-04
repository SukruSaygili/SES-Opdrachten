package be.kuleuven.candycrush.model;

import java.util.ArrayList;

public record Position(int rowNr, int columnNr, BoardSize bs) {
    public Position {
        if(!(rowNr >= 0 && rowNr < bs.height() && columnNr >= 0 && columnNr < bs.width()))
            throw new IllegalArgumentException("The entered position is not valid!");
    }

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
}
