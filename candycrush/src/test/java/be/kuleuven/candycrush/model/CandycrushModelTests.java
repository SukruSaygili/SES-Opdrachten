package be.kuleuven.candycrush.model;
import be.kuleuven.candycrush.model.Candy.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CandycrushModelTests {

    @Test
    public void gegevenNieuwePlayerMetScore_GeeftScoreTerugInCandycrushmodelKlasse_GelijkAanNul() {
        //Arrange
        Player player1Mocked = mock(Player.class);
        CandycrushModel cm = new CandycrushModel(player1Mocked);

        //Act
        when(player1Mocked.getScore()).thenReturn(0);

        //Assert
        assertThat(cm.getPlayer().getScore()).isEqualTo(0);
    }

    @Test
    public void gegevenNieuwePlayerMetNaam_GeeftNaamTerugInCandycrushmodelKlasse() {
        //Arrange
        Player player2Mocked = mock(Player.class);
        CandycrushModel cm = new CandycrushModel(player2Mocked);

        //Act
        when(player2Mocked.getName()).thenReturn("sukru");

        //Assert
        assertThat(cm.getPlayer().getName()).isEqualTo("sukru");
    }

    @Test
    public void gegevenEenIndexEnGeenNEIGHBOURS_GeeftEenPrintEnStoptDeMethodeCandySelected() {
        // Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1,new BoardSize(2,2));
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Act
        //zorgen voor geen neighbours
        ArrayList<Candy> nPlayground = new ArrayList<>(
                List.of(new NormalCandy(2), new DubbelPunt(), new OnderVolledig(), new RandomBom()));
        cm.getBoard().setPlayground(nPlayground);
        cm.updateCandySelected(new Position(0,0,cm.getBoard().getBs()));

        // Assert
        String actual = outputStreamCaptor.toString().trim().replace("\r\n", "\n"); //dit heeft te maken met de whitespace tussening beide printstatements
        assertThat(actual).isEqualTo("There are no neighbour candys!");      //de haken komen van de checkNeighboursIds methode en daarom zet ik die er ook bij
    }

    @Test
    public void gegevenEenJuisteIndexCheckOfDeScoreJuistWordtGeupdatet() {
        // Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1,new BoardSize(3,3));

        //Act
        ArrayList<Candy> playgroundWithNeighbours = new ArrayList<>();
        Collections.addAll(playgroundWithNeighbours,
                new NormalCandy(1),new NormalCandy(3), new NormalCandy(0),
                new NormalCandy(2),new NormalCandy(3),new NormalCandy(0),
                new NormalCandy(3),new NormalCandy(3),new NormalCandy(3));

        cm.getBoard().setPlayground(playgroundWithNeighbours);
        cm.updateCandySelected(new Position(1,1,cm.getBoard().getBs()));

        //Assert
        assertThat(cm.getPlayer().getScore()).isEqualTo(5);
    }

    @Test
    public void gegevenEenJuisteIndexMetMinderDan3Neighbours_printThereAreNotEnoughNeighbourCandys() {
        // Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1,new BoardSize(3,3));
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        //Act
        ArrayList<Candy> playgroundWithNeighbours = new ArrayList<>();
        Collections.addAll(playgroundWithNeighbours,
                new NormalCandy(1),new NormalCandy(1), new NormalCandy(0),
                new NormalCandy(2),new NormalCandy(3),new NormalCandy(0),
                new NormalCandy(3),new NormalCandy(0),new NormalCandy(3));
        cm.getBoard().setPlayground(playgroundWithNeighbours);
        cm.updateCandySelected(new Position(1,1,cm.getBoard().getBs()));

        //Assert
        String actual = outputStreamCaptor.toString().trim().replace("\r\n", "\n"); //dit heeft te maken met de whitespace tussening beide printstatements
        assertThat(actual).isEqualTo("There are not enough neighbour candys!");      //de haken met de indexen komen van de checkNeighboursIds methode en daarom zet ik die er ook bij
    }

    @Test
    public void gegevenEenModelMetDeAanpasbareConstructorDusWidhtEnHeightOpgegeven_GettersGevenHetJuisteResultaatTerug() {
        //Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1,new BoardSize(200,60));

        //Act
        int actualWidth = cm.getBoard().getBs().width();
        int actualHeight = cm.getBoard().getBs().height();

        //Assert
        assertThat(actualWidth).isEqualTo(200);
        assertThat(actualHeight).isEqualTo(60);

    }
}








