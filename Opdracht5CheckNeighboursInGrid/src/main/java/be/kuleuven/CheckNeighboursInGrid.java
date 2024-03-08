package be.kuleuven;

import java.util.ArrayList;
import java.util.Iterator;

public class CheckNeighboursInGrid {
    //constructor
    public CheckNeighboursInGrid() {
    }

    public static Iterable<Integer> getSameNeighboursIds(Iterable<Integer> grid, int width, int height, int indexToCheck){
        /* check whether the specified index is in the grid */
        if(indexToCheck < 0 || indexToCheck >= width * height) {
            throw new IllegalArgumentException("Index is out of bound");
        }

        /*calculating the place of the given index and making an arraylist for the result*/
        int col = indexToCheck % width;
        int row = indexToCheck/width;
        ArrayList<Integer> result = new ArrayList<>();

        /* places to check (left, right, top, bottom and diagonal) */
        int left = (indexToCheck - 1), topLeftCorner = (indexToCheck-width-1), bottomLeftCorner = (indexToCheck+width-1);
        int right = (indexToCheck + 1), topRightCorner = (indexToCheck-width+1), bottomRightCorner = (indexToCheck+width+1);
        int top = (indexToCheck-width);
        int bottom = (indexToCheck+width);


        /* upper left neighbour */
        if(col > 0 && row > 0 && isEqual(grid,indexToCheck,topLeftCorner)) {

            result.add(topLeftCorner);
        }
        /* left neighbour */
        if(col > 0 && isEqual(grid,indexToCheck,left)) {
            result.add(left);
        }
        /* bottom left neighbour */
        if(col > 0 && row < (height-1) && isEqual(grid,indexToCheck,bottomLeftCorner)) {
            result.add(bottomLeftCorner);
        }

        /* upper right neighbour */
        if(col < (width-1) && row > 0 && isEqual(grid,indexToCheck,topRightCorner)) {
            result.add(topRightCorner);
        }
        /* right neighbour */
        if(col < (width-1) && isEqual(grid,indexToCheck,right)) {
            result.add(right);
        }
        /* bottom right neighbour */
        if(col > 0 && row < (height-1) && isEqual(grid,indexToCheck,bottomRightCorner)) {
            result.add(bottomRightCorner);
        }

        /* upper neighbour */
        if(row > 0 && isEqual(grid,indexToCheck,top)) {
            result.add(top);
        }
        /* bottom neighbour */
        if(row < (height-1) && isEqual(grid,indexToCheck,bottom)) {
            result.add(bottom);
        }

        System.out.print("[");
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");

        /*returning an iterator*/
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return result.iterator();
            }
        };
    }

    /*checking the equality of the neighbour element with the given element on the index (help method)*/
    private static boolean isEqual(Iterable<Integer> grid, int indexToCheck, int neighbourIndex) {
        int elementToCheck = getElement(grid,indexToCheck);
        int elementNeighbour = getElement(grid,neighbourIndex);
        return (elementToCheck == elementNeighbour);
    }

    /*getting the element out of an iterable with a given index*/
    private static int getElement(Iterable<Integer> grid, int index) {      //in het begin staat de cursor voor de iterator
        int currentIndex = 0;                                               //dus niet op het eerste element

        Iterator<Integer> gridIterator = grid.iterator();

        while(gridIterator.hasNext() && currentIndex < index) {
            gridIterator.next();
            currentIndex++;
        }
        if(currentIndex != index) {
            throw new IndexOutOfBoundsException("Index out of bound!");
        }
        else {
            return gridIterator.next();                                     //we stoppen voor de gevraagde element, dus nog een keer
        }                                                                   //next() uitvoeren
    }
}
