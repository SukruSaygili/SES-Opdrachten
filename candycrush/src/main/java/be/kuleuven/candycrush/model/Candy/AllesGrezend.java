package be.kuleuven.candycrush.model.Candy;


public record AllesGrezend() implements Candy {
    @Override
    public String getName() {
        return "AllesGrezend";
    }
}
