package be.kuleuven.candycrush.model;
import be.kuleuven.CheckNeighboursInGrid;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void gegevenNegatieveIndex_GeeftEenPrintEnStoptDeMethodeupdateCandySelected() {
        //Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1);
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        //Act
        cm.updateCandySelected(-5);

        //Assert
        Assertions.assertThat(outputStreamCaptor.toString().trim()).isEqualTo("model:candyWithIndexSelected:indexOutOfRange");
        //trim() => alle whitespace voor en achter weg te halen, zodat enkel de desbetreffende output wordt getest
    }

    @Test
    public void gegevenEenIndexEnGeenNEIGHBOURS_GeeftEenPrintEnStoptDeMethodeCandySelected() {
        // Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1);
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Act
        ArrayList<Integer> nPlayground = new ArrayList<>();     //zorgen voor geen neighbours
        for (int i = 0; i < (cm.getWidth() * cm.getHeight()); i++) {
            nPlayground.add(i);
        }
        cm.setPlayground(nPlayground);
        cm.updateCandySelected(2);

        // Assert
        String actual = outputStreamCaptor.toString().trim().replace("\r\n", "\n"); //dit heeft te maken met de whitespace tussening beide printstatements
        assertThat(actual).isEqualTo("[]\nThere are no neighbour candys!");      //de haken komen van de checkNeighboursIds methode en daarom zet ik die er ook bij
    }

    @Test
    public void gegevenEenJuisteIndexCheckOfDeScoreJuistWordtGeupdatet() {
        // Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1,3,3);

        //Act
        ArrayList<Integer> playgroundWithNeighbours = new ArrayList<>();
        Collections.addAll(playgroundWithNeighbours, 1,3,5,
                                                               4,3,6,
                                                               3,3,3);
        cm.setPlayground(playgroundWithNeighbours);
        cm.updateCandySelected(4);

        //Assert
        assertThat(cm.getPlayer().getScore()).isEqualTo(5);
    }

    @Test
    public void gegevenEenJuisteIndexMetMinderDan3Neighbours_printThereAreNotEnoughNeighbourCandys() {
        // Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1,3,3);
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        //Act
        ArrayList<Integer> playgroundWithNeighbours = new ArrayList<>();
        Collections.addAll(playgroundWithNeighbours, 1,3,5,
                                                               4,3,6,
                                                               3,2,1);
        cm.setPlayground(playgroundWithNeighbours);
        cm.updateCandySelected(4);

        //Assert
        String actual = outputStreamCaptor.toString().trim().replace("\r\n", "\n"); //dit heeft te maken met de whitespace tussening beide printstatements
        assertThat(actual).isEqualTo("[6, 1]\nThere are not enough neighbour candys!");      //de haken met de indexen komen van de checkNeighboursIds methode en daarom zet ik die er ook bij
    }

    @Test
    public void gegevenEenRijEnKolomAllebeiEenRandomWaardeBinneninHetBereik_BerekenDeIndex() {
        // Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1);

        //Act
        int index = cm.getIndexFromRowColumn(2,3);

        //Assert
        assertThat(index).isEqualTo(29);
    }

    @Test
    public void gegevenEenRijEnKolomAllebeiEenRandomWaardeBuitenHetBereik_BerekenDeIndex_GeeftNulEnPrintOutOfRange() {
        // Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1);
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        //Act
        int index = cm.getIndexFromRowColumn(200,215);

        //Assert
        assertThat(outputStreamCaptor.toString().trim()).isEqualTo("Column or row, or both are out of range!");
        assertThat(index).isEqualTo(-1);
    }

    @Test
    public void gegevenEenRijEnKolomEenVanDeTweeBuitenHetBereik_BerekenDeIndex_GeeftNulEnPrintOutOfRange() {
        // Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1);
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        //Act
        int index = cm.getIndexFromRowColumn(2,215);

        //Assert
        assertThat(outputStreamCaptor.toString().trim()).isEqualTo("Column or row, or both are out of range!");
        assertThat(index).isEqualTo(-1);
    }

    @Test
    public void gegevenEenModelMetDeAanpasbareConstructorDusWidhtEnHeightOpgegeven_GettersGevenHetJuisteResultaatTerug() {
        //Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1,200,60);

        //Act
        int actualWidth = cm.getWidth();
        int actualHeight = cm.getHeight();

        //Assert
        assertThat(actualWidth).isEqualTo(200);
        assertThat(actualHeight).isEqualTo(60);

    }
}








