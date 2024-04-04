package be.kuleuven.candycrush.model;

public record DubbelPunt() implements Candy {
    @Override
    public String getName() {
        return "DubbelPunt";
    }
}
