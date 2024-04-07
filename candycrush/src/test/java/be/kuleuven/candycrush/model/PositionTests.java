package be.kuleuven.candycrush.model;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class PositionTests {
    @Test
    public void gegevenEenRijEnKolomnummerBinnenHetBereik_geeft_OvereenkomstigIndexTerug() {
        //Arrange
        var p = new Position(1,3,new BoardSize(4,2));

        //Act-Assert
        assertThat(p.toIndex()).isEqualTo(7);
    }

    @Test
    public void gegevenEenIndexBinnenHetBereik_geeftEenPositieTerug() {
        //Arrange
        var p = new Position(1,3,new BoardSize(4,2));

        //Act-Assert
        assertThat(Position.fromIndex(5,p.bs())).isEqualTo(new Position(1,1,p.bs()));
    }

    @Test
    public void gegevenEenOngeldigeIndex_geeftException() {
        //Arrange
        var p = new Position(1,3,new BoardSize(4,2));

        //Act-Assert
        assertThatThrownBy(() -> Position.fromIndex(10,p.bs())).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid index!");
    }

    @Test
    public void gegevenEenBoardSizeEnPositie_InHetMidden_GeeftJuisteDirecteBurenTerug() {
        //Arrange
        var bs = new BoardSize(4,4);
        var p = new Position(2,2,bs);

        ArrayList<Position> verwachtteResultaat = new ArrayList<>(List.of
                (new Position(1,1,bs),new Position(1,2,bs), new Position(1,3,bs),
                 new Position(2,1,bs), new Position(2,3,bs), new Position(3,1,bs),
                 new Position(3,2,bs), new Position(3,3,bs)));

        //Act-Assert
        assertThat(p.neighbourPositions()).containsOnlyOnceElementsOf(verwachtteResultaat);
        //containsOnlyOnceElementsOf checked enkel of het element erin zit zonder naar de volgorde te kijken
    }

    @Test
    public void gegevenEenBoardSizeEnPositie_InHetLinkerhoek_GeeftJuisteDirecteBurenTerug() {
        //Arrange
        var bs = new BoardSize(4,4);
        var p = new Position(0,0,bs);

        ArrayList<Position> verwachtteResultaat = new ArrayList<>(List.of
                (new Position(0,1,bs),new Position(1,1,bs), new Position(1,0,bs)));

        //Act-Assert
        assertThat(p.neighbourPositions()).containsOnlyOnceElementsOf(verwachtteResultaat);
        //containsOnlyOnceElementsOf checked enkel of het element erin zit zonder naar de volgorde te kijken
    }

    @Test
    public void gegevenEenBoardSizeEnPositie_RechterKantInHetMidden_GeeftJuisteDirecteBurenTerug() {
        //Arrange
        var bs = new BoardSize(4,4);
        var p = new Position(1,3,bs);

        ArrayList<Position> verwachtteResultaat = new ArrayList<>(List.of
                (new Position(0,2,bs),new Position(0,3,bs), new Position(1,2,bs),
                 new Position(2,2,bs), new Position(2,3,bs)));

        //Act-Assert
        assertThat(p.neighbourPositions()).containsOnlyOnceElementsOf(verwachtteResultaat);
        //containsOnlyOnceElementsOf checked enkel of het element erin zit zonder naar de volgorde te kijken
    }

    @Test
    public void gegevenEenPositieDieHetLaatsteElementInDeRijIs_GeeftTrue() {
        //Arrange
        var p = new Position(1,3,new BoardSize(4,4));

        //Act-Assert
        assertThat(p.isLastColumn()).isEqualTo(true);
    }
}
