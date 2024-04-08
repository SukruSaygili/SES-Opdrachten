package be.kuleuven.candycrush.model;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class BoardTests {

    @Test
    public void gegevenEenLegePlaygroundVoorStrings_VultElkePositieMetDeLetterAEnCheckedDit() {
        //Arrange
        BoardSize bs = new BoardSize(3, 3);
        HashMap<Position,String> playgroundMAP = new HashMap<>();
        Board<String> board = new Board<>(bs, playgroundMAP);

        //Act
        Function<Position, String> cellCreator = position -> "A";
        board.fill(cellCreator);

        //Assert
        for (Position position : bs.positions()) {
            assertThat(board.getCellAt(position)).isEqualTo("A");
        }
    }

    @Test
    public void gegevenTweeBordenMetEentjeGevuld_KopieertDezeNaarHetAnderBord() {
        //Arrange
        BoardSize bs = new BoardSize(3, 3);
        HashMap<Position,String> playgroundMAP1 = new HashMap<>();;
        HashMap<Position,String> playgroundMAP2 = new HashMap<>();
        Board<String> board1 = new Board<>(bs, playgroundMAP1);
        Board<String> board2 = new Board<>(bs, playgroundMAP2);

        //Act
        board1.fill(position -> "A");
        board2.fill(position -> "r");
        board1.copyTo(board2);

        //Assert
        for (Position position : bs.positions()) {
            assertThat(board2.getCellAt(position)).isEqualTo("A");
        }
    }
}
