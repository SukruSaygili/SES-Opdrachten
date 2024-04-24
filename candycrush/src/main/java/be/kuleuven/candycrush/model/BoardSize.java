package be.kuleuven.candycrush.model;

import java.util.ArrayList;
import java.util.Collection;

public record BoardSize(int width, int height) {
    /*Constructor*/
    public BoardSize{
        if(!(width > 0 && height > 0)) throw new IllegalArgumentException("Number of rows and/or columns cannot be negative!");
    }

    /*Other methods*/
    public Collection<Position> positions() {
        ArrayList<Position> result = new ArrayList<>();
        int currentIndex = 0;
        while(currentIndex < width*height) {
            result.add(Position.fromIndex(currentIndex,this));
            currentIndex++;
        }
        return result;
    }
}
