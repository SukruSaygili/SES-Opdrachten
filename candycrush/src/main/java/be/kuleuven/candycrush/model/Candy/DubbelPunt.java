package be.kuleuven.candycrush.model.Candy;

public record DubbelPunt() implements Candy {
    @Override
    public String getName() {
        return "DubbelPunt";
    }
}
