package be.kuleuven.candycrush.model;


public record AllesGrezend() implements Candy {
    @Override
    public String getName() {
        return "AllesGrezend";
    }
}
