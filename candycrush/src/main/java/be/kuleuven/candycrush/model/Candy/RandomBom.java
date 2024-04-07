package be.kuleuven.candycrush.model.Candy;

public record RandomBom() implements Candy {
    @Override
    public String getName() {
        return "RandomBom";
    }
}
