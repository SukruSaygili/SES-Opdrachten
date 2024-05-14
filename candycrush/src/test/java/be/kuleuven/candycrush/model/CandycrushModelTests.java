package be.kuleuven.candycrush.model;
import be.kuleuven.candycrush.model.Candy.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static be.kuleuven.candycrush.model.CandycrushModel.createBoardFromString;
import static be.kuleuven.candycrush.model.CandycrushModel.printBoard;
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
        ArrayList<Candy> candies = new ArrayList<>(
                List.of(new NormalCandy(2), new DubbelPunt(), new OnderVolledig(), new RandomBom()));

        ConcurrentHashMap<Position,Candy> playgroundMAP = new ConcurrentHashMap<>();
        var positions = cm.getBoard().getBs().positions().iterator();
        for (Candy candy : candies) {
            playgroundMAP.put(positions.next(), candy);
        }

        cm.getBoard().setPlaygroundMAP(playgroundMAP);
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
        ArrayList<Candy> candiesWithNeighbours = new ArrayList<>();
        Collections.addAll(candiesWithNeighbours,
                new NormalCandy(1),new NormalCandy(3), new NormalCandy(0),
                new NormalCandy(2),new NormalCandy(3),new NormalCandy(0),
                new NormalCandy(3),new NormalCandy(3),new NormalCandy(3));

        ConcurrentHashMap<Position,Candy> playgroundMAP = new ConcurrentHashMap<>();
        var positions = cm.getBoard().getBs().positions().iterator();
        for(Candy c : candiesWithNeighbours) {
            playgroundMAP.put(positions.next(),c);
        }

        cm.getBoard().setPlaygroundMAP(playgroundMAP);
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
        ArrayList<Candy> playgroundWithNotEnoughNeighbours = new ArrayList<>();
        Collections.addAll(playgroundWithNotEnoughNeighbours,
                new NormalCandy(1),new NormalCandy(1), new NormalCandy(0),
                new NormalCandy(2),new NormalCandy(3),new NormalCandy(0),
                new NormalCandy(3),new NormalCandy(0),new NormalCandy(3));

        ConcurrentHashMap<Position,Candy> playgroundMAP = new ConcurrentHashMap<>();
        var positions = cm.getBoard().getBs().positions().iterator();
        for(Candy c : playgroundWithNotEnoughNeighbours) {
            playgroundMAP.put(positions.next(),c);
        }

        cm.getBoard().setPlaygroundMAP(playgroundMAP);
        cm.updateCandySelected(new Position(1,1,cm.getBoard().getBs()));

        //Assert
        String actual = outputStreamCaptor.toString().trim().replace("\r\n", "\n"); //dit heeft te maken met de whitespace tussening beide printstatements
        assertThat(actual).isEqualTo("There are not enough neighbour candys!");      //de haken met de indexen komen van de checkNeighboursIds methode en daarom zet ik die er ook bij
    }

    @Test
    public void gegevenEenModelMetDeAanpasbareConstructorDusWidhtEnHeightOpgegeven_GettersGevenHetJuisteResultaatTerug() {
        //Arrange
        Player player1 = new Player("sukru");
        CandycrushModel cm = new CandycrushModel(player1,new BoardSize(2,2));

        //Act
        int actualWidth = cm.getBoard().getBs().width();
        int actualHeight = cm.getBoard().getBs().height();

        //Assert
        assertThat(actualWidth).isEqualTo(2);
        assertThat(actualHeight).isEqualTo(2);

    }

    @Test
    public void gegevenEenBordMetMatches_TestOfAlleMatchesGevondenWorden() {
        //Arrange
        CandycrushModel model = createBoardFromString("""
           *o@#
           o#@#
           @@@#
           *ooo""");
        Board<Candy> board = model.getBoard();
        BoardSize size = board.getBs();

        //Act
        Set<List<Position>> allMatches = board.findAllMatches();

        //Assert
        Set<List<Position>> expectedMatches = new HashSet<>();
        expectedMatches.add(List.of(new Position(2,0,size),
                                    new Position(2,1,size),
                                    new Position(2,2,size)));
        expectedMatches.add(List.of(new Position(0,2,size),
                                    new Position(1,2,size),
                                    new Position(2,2,size)));
        expectedMatches.add(List.of(new Position(0,3,size),
                                    new Position(1,3,size),
                                    new Position(2,3,size)));
        expectedMatches.add(List.of(new Position(3,1,size),
                                    new Position(3,2,size),
                                    new Position(3,3,size)));
        assertThat(allMatches).containsExactlyInAnyOrderElementsOf(expectedMatches);
    }

    @Test
    public void gegevenEenbordMetMatches_TestOfAlleMatchesVerwijderdWorden() {
        //Arrange
        CandycrushModel model = createBoardFromString("""
           *o@#
           o#@#
           @@@#
           *ooo""");
        Board<Candy> board = model.getBoard();
        BoardSize size = board.getBs();

        //Act
        var allMatches = board.findAllMatches();
        allMatches.forEach(board::clearMatch);

        //Assert
        allMatches.stream()
                .flatMap(List::stream)
                .map(board::getCellAt)
                .forEach(cell -> assertThat(cell).isEqualTo(new LegeCandy()));
    }

    @Test
    public void gegevenEenbordMetMatchesIn_L_Vorm_Na1Switch_GaatNaOfDezeNietDubbelGeteldWord() {
        //Arrange
        CandycrushModel model = createBoardFromString("""
           *o@#
           o#@o
           @@*o
           *o@#""");
        printBoard(model);

        //Act
        Board<Candy> board = model.getBoard();
        BoardSize size = board.getBs();
        board.switchCells(new Position(2,2, size), new Position(3,2,size));
        System.out.println("\nSWITCHED BOARD");
        printBoard(model);
        board.updateBoard();
        System.out.println("\nUPDATED BOARD");
        printBoard(model);

        //Assert
        assertThat(board.getAmountOfCandiesDeleted()).isEqualTo(5);
    }

    @Test
    public void gegevenEenBordMetMatchesNaSwitches_CheckOfAlleParenTeruggegevenWorden() {
        //Arrange
        CandycrushModel model1 = createBoardFromString("""
               @@o#
               o*#o
               @@**
               *#@@""");
        Board<Candy> board = model1.getBoard();
        BoardSize size = board.getBs();

        //Act
        var pairs = board.matchAfterSwitchPairs();

        //Assert
        List<List<Position>> expectedPairs = new ArrayList<>();
        expectedPairs.add(List.of(new Position(1,1,size), new Position(2, 1,size)));
        expectedPairs.add(List.of(new Position(2,2,size), new Position(3,2,size)));
        expectedPairs.add(List.of(new Position(2,1,size), new Position(3,1,size)));


        // Assert
        assertThat(pairs).containsExactlyInAnyOrderElementsOf(expectedPairs);
    }

    @Test
    public void gegevenEenBordMetMatchesNaSwitches_CheckOfAlleParenTeruggegevenWorden2() {
        //Arrange
        CandycrushModel model2 = createBoardFromString("""
                #oo##
                #@o@@
                *##o@
                @@*@o
                **#*o""");
        Board<Candy> board = model2.getBoard();
        BoardSize size = board.getBs();

        //Act
        var pairs = board.matchAfterSwitchPairs();

        //Assert
        List<List<Position>> expectedPairs = new ArrayList<>();
        expectedPairs.add(List.of(new Position(1,1,size), new Position(1,2,size)));
        expectedPairs.add(List.of(new Position(1,0,size), new Position(2,0,size)));
        expectedPairs.add(List.of(new Position(3,2,size), new Position(3,3,size)));
        expectedPairs.add(List.of(new Position(2,3,size), new Position(2,4,size)));
        expectedPairs.add(List.of(new Position(4,2,size), new Position(4,3,size)));
        expectedPairs.add(List.of(new Position(3,3,size), new Position(3,4,size)));
        expectedPairs.add(List.of(new Position(2,0,size), new Position(2,1,size)));
        expectedPairs.add(List.of(new Position(3,2,size), new Position(4,2,size)));
        expectedPairs.add(List.of(new Position(2,2,size), new Position(2,3,size)));


        // Assert
        assertThat(pairs).containsExactlyInAnyOrderElementsOf(expectedPairs);
    }

    @Test
    public void gegevenEenBordZonderMatchesNaSwitches_CheckOfLijstLeegIs() {
        //Arrange
        CandycrushModel model = createBoardFromString("""
                #o*#
                #*o@
                *@#*
                @@**""");
        Board<Candy> board = model.getBoard();
        BoardSize size = board.getBs();

        //Act
        var pairs = board.matchAfterSwitchPairs();

        // Assert
        assertThat(pairs).isEmpty();
    }

    @Test
    public void gegevenEenBordMEtMatches_EnEenSequentieVanWissels_CheckOfDeScoreJuistWordtGeteld() {
        //Arrange
        CandycrushModel model1 = createBoardFromString("""
           @@o#
           o*#o
           @@**
           *#@@""");

        Board<Candy> board = model1.getBoard();
        BoardSize size = board.getBs();

        //Act
        List<List<Position>> switchesToExectute = new ArrayList<>();
        switchesToExectute.add(List.of(new Position(1,1, size), new Position(2,1,size)));
        switchesToExectute.add(List.of(new Position(1,0, size), new Position(1,1,size)));
        switchesToExectute.add(List.of(new Position(1,3, size), new Position(2,3,size)));
        switchesToExectute.add(List.of(new Position(2,1, size), new Position(3,1,size)));
        int score = board.calculateScore(switchesToExectute);

        //Assert
        assertThat(score).isEqualTo(15);
    }

    @Test
    public void gegevenEenBordMEtMatches_EnEenSequentieVanWissels_CheckOfDeScoreJuistWordtGeteld2() {
        //Arrange
        CandycrushModel model1 = createBoardFromString("""
           @@o#
           o*#o
           @@**
           *#@@""");

        Board<Candy> board = model1.getBoard();
        BoardSize size = board.getBs();

        //Act
        List<List<Position>> switchesToExectute = new ArrayList<>();
        switchesToExectute.add(List.of(new Position(1,1, size), new Position(2,1,size)));
        int score = board.calculateScore(switchesToExectute);

        //Assert
        assertThat(score).isEqualTo(3);
    }

    @Test
    public void gegevenEenBordMEtMatches_EnEenSequentieVanWisselsInLVrom_CheckOfDeScoreJuistWordtGeteld() {
        //Arrange
        CandycrushModel model = createBoardFromString("""
                *o@#
                o#@o
                @@*o
                *o@#""");

        Board<Candy> board = model.getBoard();
        BoardSize size = board.getBs();

        //Act
        List<List<Position>> switchesToExectute = new ArrayList<>();
        switchesToExectute.add(List.of(new Position(2,2, size), new Position(3,2,size)));
        int score = board.calculateScore(switchesToExectute);
        printBoard(model);

        //Assert
        assertThat(score).isEqualTo(5);
    }

    @Test
    public void backtrackingTestModel1() {
        //Arrange
        CandycrushModel model1 = createBoardFromString("""
           @@o#
           o*#o
           @@**
           *#@@""");
        Board<Candy> board = model1.getBoard();

        //Act
        var switches = board.maximizeScore();
        System.out.println(switches);
        System.out.println(board.calculateScore(switches));

        printBoard(model1);

        //Assert


    }

    @Test
    public void backtrackingTestModel2() {
        //Arrange
        CandycrushModel model2 = createBoardFromString("""
           #oo##
           #@o@@
           *##o@
           @@*@o
           **#*o""");
        Board<Candy> board = model2.getBoard();

        //Act
        var switches = board.maximizeScore();
        System.out.println(switches);
        System.out.println(board.calculateScore(switches));

        printBoard(model2);

        //Assert


        //Assert
    }

    @Test
    public void backtrackingTestModel3() {
        //Arrange
        CandycrushModel model3 = createBoardFromString("""
           #@#oo@
           @**@**
           o##@#o
           @#oo#@
           @*@**@
           *#@##*""");
        Board<Candy> board = model3.getBoard();

        //Act
        var switches = board.maximizeScore();
        System.out.println(switches);
        System.out.println(board.calculateScore(switches));

        printBoard(model3);

        //Assert
    }


}








