package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.Candy.Candy;
import be.kuleuven.candycrush.model.Candy.LegeCandy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board <T> {
    private BoardSize bs;
    private ConcurrentHashMap<Position,T> playgroundMAP;
    private ConcurrentHashMap<T, Set<Position>> playgroundMAPREV;
    private int amountOfCandiesDeleted = 0;


    /*Constructor*/
    public Board(BoardSize bs, ConcurrentHashMap<Position,T> playgroundMAP) {
        this.bs = bs;
        this.playgroundMAP = playgroundMAP;
        this.playgroundMAPREV = new ConcurrentHashMap<>();

        for(Map.Entry<Position,T> entry : playgroundMAP.entrySet()) {
            T element = entry.getValue();
            Position position = entry.getKey();
            playgroundMAPREV.computeIfAbsent(element, k -> ConcurrentHashMap.newKeySet()).add(position);
        }
    }

    /*getters*/
    public BoardSize getBs() {
        return bs;
    }
    public ConcurrentHashMap<Position, T> getPlaygroundMAP() {
        return playgroundMAP;
    }
    public int getAmountOfCandiesDeleted() {
        return amountOfCandiesDeleted;
    }
    public T getCellAt(Position position) {
        if (!isValidPosition(position)) {
            System.out.println("Invalid position! Position could not be found!");
            return null;
        }
        return playgroundMAP.get(position);
    }
    public Set<Position> getPositionsOfElement(T element) {
        return Collections.unmodifiableSet(playgroundMAPREV.getOrDefault(element,
                Collections.emptySet()));
    }

    /*setters*/
    public void setPlaygroundMAP(ConcurrentHashMap<Position, T> playgroundMAP) {
        this.playgroundMAP = playgroundMAP;
    }

    /*Other methods*/
    public void replaceCellAt(Position p, T newCell) {
        if (!isValidPosition(p)) {
            System.out.println("Invalid position! Position could not be found and the cell could not be replaced!");
            return;
        }

        T oldCell = playgroundMAP.put(p,newCell);

        if(oldCell != null) {
            Set<Position> pos = playgroundMAPREV.get(oldCell);
            if(pos != null) {
                pos.remove(p);
                if(pos.isEmpty()) {
                    playgroundMAPREV.remove(oldCell);
                }
            }
        }
        playgroundMAPREV.computeIfAbsent(newCell,k-> ConcurrentHashMap.newKeySet()).add(p);
    }
    public void fill(Function<Position, T> cellCreator) {
        for (Position position : bs.positions()) {
            T newCell = cellCreator.apply(position);
            playgroundMAP.put(position,newCell);
            playgroundMAPREV.computeIfAbsent(newCell, k -> ConcurrentHashMap.newKeySet()).add(position);
        }
    }
    public void copyTo(Board<T> otherBoard) throws IllegalArgumentException {
        if (!this.bs.equals(otherBoard.bs)) {
            throw new IllegalArgumentException("Boards have different sizes!");
        }

        otherBoard.playgroundMAP.clear();
        otherBoard.playgroundMAPREV.clear();

        for(Map.Entry<Position, T> entry : playgroundMAP.entrySet()) {
            Position pos = entry.getKey();
            T element = entry.getValue();
            otherBoard.playgroundMAP.put(pos,element);
            otherBoard.playgroundMAPREV.computeIfAbsent(element, k-> ConcurrentHashMap.newKeySet()).add(pos);
        }

    }

    private boolean isValidPosition(Position position) {
        return position != null && playgroundMAP.containsKey(position);
    }

    private boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions) {
        List<Position> firstTwoPositions = positions.limit(2).toList();

        if (firstTwoPositions.size() < 2) {
            return false;
        }

        return firstTwoPositions.stream().allMatch(position -> this.getCellAt(position).equals(candy));
    }
    private Stream<Position> horizontalStartingPositions() {
        return this.getBs().positions().stream()
                .filter(position -> !firstTwoHaveCandy((Candy)this.getCellAt(position),position.walkLeft())
                        && !this.getCellAt(position).equals(new LegeCandy()));
        //LegeCandys niet meetellen in matches
    }
    private Stream<Position> verticalStartingPositions() {
        return this.getBs().positions().stream()
                .filter(position -> !firstTwoHaveCandy((Candy)this.getCellAt(position),position.walkUp())
                        && !this.getCellAt(position).equals(new LegeCandy()));
        //LegeCandys niet meetellen in matches
    }
    private List<Position> longestMatchToRight(Position pos) {
        return pos.walkRight()
                .takeWhile(position -> this.getCellAt(position).equals(this.getCellAt(pos)))
                .toList();
    }
    private List<Position> longestMatchDown(Position pos) {
        return pos.walkDown()
                .takeWhile(position -> this.getCellAt(position).equals(this.getCellAt(pos)))
                .toList();
    }
    public Set<List<Position>> findAllMatches() {
        var horizontalMatchPos = horizontalStartingPositions()
                .map(this::longestMatchToRight);        //geeft een stream van lists van positions (Stream<list<Position>>)

        var verticalMatchPos = verticalStartingPositions()
                .map(this::longestMatchDown);

        return Stream.concat(horizontalMatchPos, verticalMatchPos)
                .filter(l -> l.size() >= 3).collect(Collectors.toSet());
    }

    public void clearMatch(List<Position> match) {
        if(match.isEmpty()) return;

        this.replaceCellAt(match.getFirst(), (T) new LegeCandy());      //LegeCandy => stelt een lege candy voor
        //een muisklik erop zorgt voor GEEN reactie
        clearMatch(match.subList(1,match.size()));
    }
    private void copyCandyFromTo(Position from, Position to) {
        T candy = this.getCellAt(to);
        this.replaceCellAt(to,this.getCellAt(from));
        this.replaceCellAt(from,candy);
    }
    public void fallDownTo(Position pos) {
        var positionsAbovePos = pos.walkUp().toList();
        if(pos.rowNr() == 0) return;

        if(this.getCellAt(pos).equals(new LegeCandy())) {
            for(var position : positionsAbovePos) {
                if(position.rowNr() == 0) break;
                copyCandyFromTo(positionsAbovePos.get((positionsAbovePos.indexOf(position)+1)), position);
            }
        }

        fallDownTo(positionsAbovePos.get(1));
    }

    public boolean updateBoard() {
        return updateBoard(0);
    }
    private boolean updateBoard(int counter) {
        var allMatches = findAllMatches();
        if(allMatches.isEmpty()) {
            return counter > 0;
        }
        List<Position> clearedPositions = new ArrayList<>();       //voorkomen dat een verwijderde candy dubbel wordt geteld

        for(var match : allMatches) {
            clearMatch(match);
            counter++;
            for(var p : match) {
                if(!clearedPositions.contains(p)) {
                    clearedPositions.add(p);
                    this.amountOfCandiesDeleted++;
                }
            }
        }

        this.getBs().positions().forEach(this::fallDownTo);

        return updateBoard(counter);
    }

    private boolean matchAfterSwitch(Position p1,Position p2) {
        T cell1 = getCellAt(p1); T cell2 = getCellAt(p2);
        replaceCellAt(p1, cell2); replaceCellAt(p2, cell1);
        boolean p1ZitErin = false;
        boolean p2ZitErin = false;

        Set<List<Position>> allMatches = findAllMatches();

        for (List<Position> match : allMatches) {
            if (match.contains(p1)) {
                p1ZitErin = true;
            }
            if (match.contains(p2)) {
                p2ZitErin = true;
            }
        }
        T cell1After = getCellAt(p1); T cell2After = getCellAt(p2);
        replaceCellAt(p1, cell2After); replaceCellAt(p2, cell1After);

        return p1ZitErin || p2ZitErin;
    }
    public boolean switchCells(Position p1, Position p2) {
        if(!isValidPosition(p1) || !isValidPosition(p2) || !p1.isLocatedNextTo(p2) ||
                getCellAt(p1) instanceof LegeCandy|| getCellAt(p2) instanceof LegeCandy) {
            return false;
        }

        if(matchAfterSwitch(p1,p2)) {
            T cell1 = getCellAt(p1); T cell2 = getCellAt(p2);
            replaceCellAt(p1, cell2); replaceCellAt(p2, cell1);
            return true;
        }
        return false;
    }

    public int calculateScore(List<List<Position>> switchesToExecute) {
        Board<T> copyOfOriginalBoard = new Board<>(this.bs, new ConcurrentHashMap<>());
        this.copyTo(copyOfOriginalBoard);

        for(var switchToExecute : switchesToExecute) {
            copyOfOriginalBoard.switchCells(switchToExecute.getFirst(), switchToExecute.getLast());
            copyOfOriginalBoard.updateBoard();
        }
        return copyOfOriginalBoard.amountOfCandiesDeleted;
    }
}