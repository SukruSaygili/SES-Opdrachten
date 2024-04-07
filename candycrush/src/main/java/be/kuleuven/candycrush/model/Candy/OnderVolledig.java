package be.kuleuven.candycrush.model.Candy;

public record OnderVolledig() implements Candy {
    @Override
    public String getName() {
        return "OnderVolledig";
    }
}
