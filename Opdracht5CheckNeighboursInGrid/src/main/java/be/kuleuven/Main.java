package be.kuleuven;

import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TEST
        ArrayList<Integer> grid = new ArrayList<>();
        grid.add(0); grid.add(0); grid.add(1); grid.add(0);
        grid.add(1); grid.add(1); grid.add(0); grid.add(2);
        grid.add(2); grid.add(0); grid.add(1); grid.add(3);
        grid.add(0); grid.add(1); grid.add(1); grid.add(1);

        CheckNeighboursInGrid.getSameNeighboursIds(grid,4,4,5);

    }
}