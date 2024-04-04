package be.kuleuven.candycrush.model;

public record OnderVolledig() implements Candy {
    @Override
    public String getName() {
        return "OnderVolledig";
    }
}
