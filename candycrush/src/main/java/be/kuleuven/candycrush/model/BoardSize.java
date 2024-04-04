package be.kuleuven.candycrush.model;

import java.util.ArrayList;

public record BoardSize(int width, int height) {
    public BoardSize{
        if(!(width > 0 && height > 0)) throw new IllegalArgumentException("Number of rows and/or columns cannot be negative!");
    }

    public Iterable<Position> positions() {
        ArrayList<Position> result = new ArrayList<>();
        int currentIndex = 0;
        while(currentIndex < width*height) {
            result.add(Position.fromIndex(currentIndex,this));
            currentIndex++;
        }
        return result;
    }
}
