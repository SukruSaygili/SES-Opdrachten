package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.Candy.Candy;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class MultithreadingClient {
    public static void main(String[] args) {

        BoardSize bs = new BoardSize(9, 9);
        Board<Candy> board = new Board<>(bs, new ConcurrentHashMap<>());
        board.fill(position -> CandycrushModel.generateRandomCandy());

        Thread thread1 = new Thread(() -> continuouslyReplaceCells(board,bs,"thread1"));
        Thread thread2 = new Thread(() -> continuouslyReplaceCells(board,bs,"thread2"));

        thread1.start();
        thread2.start();
    }

    private static void continuouslyReplaceCells(Board<Candy> board, BoardSize bs, String threadName) {
        Random random = new Random();
        while (true) {
            int xRand = random.nextInt(board.getBs().width());
            int yRand = random.nextInt(board.getBs().height());

            Candy candy = CandycrushModel.generateRandomCandy();
            board.replaceCellAt(new Position(yRand,xRand,bs), candy);

            System.out.println(threadName + " replaced cell at position (" + xRand + ", " + yRand + ") with " + candy);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
