package be.kuleuven.candycrush.model;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.function.Function;

public class BoardTests {

    @Test
    public void gegevenEenLegePlaygroundVoorStrings_VultElkePositieMetDeLetterAEnCheckedDit() {
        //Arrange
        BoardSize bs = new BoardSize(3, 3);
        ArrayList<String> playground = new ArrayList<>();
        Board<String> board = new Board<>(bs, playground);

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
        ArrayList<String> playground1 = new ArrayList<>();
        ArrayList<String> playground2 = new ArrayList<>();
        Board<String> board1 = new Board<>(bs, playground1);
        Board<String> board2 = new Board<>(bs, playground2);

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
