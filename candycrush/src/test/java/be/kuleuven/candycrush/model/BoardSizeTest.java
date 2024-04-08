package be.kuleuven.candycrush.model;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class BoardSizeTest {
    @Test
    void gegevenEenBordGeeftAllePositiesVolgensStijgendeIndexTerugZonderFouten() {
        //Arrange
        var bs = new BoardSize(3, 3);

        //Act
        Iterable<Position> positions = bs.positions();
        ArrayList<Position> verwachtteResultaat = new ArrayList<>(List.of
                (new Position(0,0,bs),new Position(0,1,bs), new Position(0,2,bs),
                 new Position(1,0,bs), new Position(1,1,bs), new Position(1,2,bs),
                 new Position(2,0,bs), new Position(2,1,bs), new Position(2,2,bs)));

        //Assert
        assertThat(positions).isEqualTo(verwachtteResultaat);
    }

    @Test
    void positiesEnBoardsizeGeeftEenExceptionTerugWanneerBoardSizeOngeldigIs() {
        //Act-Assert
        assertThrows(IllegalArgumentException.class, () -> new BoardSize(-1, 5).positions());
        assertThrows(IllegalArgumentException.class, () -> new BoardSize(5, -1).positions());
        assertThrows(IllegalArgumentException.class, () -> new BoardSize(0, 5).positions());
        assertThrows(IllegalArgumentException.class, () -> new BoardSize(5, 0).positions());
    }
}
