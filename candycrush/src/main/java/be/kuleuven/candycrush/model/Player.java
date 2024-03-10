package be.kuleuven.candycrush.model;

public class Player {
    /*variables*/
    private String name;
    private int score;

    /*constructors*/
    //default constructor
    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }
    //score constructor: sets the score of the player to zero at the beginning
    public Player(String name) {
        this(name,0);
    }

    /*getters*/
    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }

    /*setters*/
    public void setName(String name) {
        this.name = name;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
