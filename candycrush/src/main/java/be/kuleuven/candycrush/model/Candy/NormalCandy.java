package be.kuleuven.candycrush.model.Candy;

public record NormalCandy(int color) implements Candy {
    public NormalCandy{
        if(!(color >= 0 && color <= 3)) throw new IllegalArgumentException("A normal candy can only have 4 different color options!");
    }

    @Override
    public String getName() {
        return "NormalCandy";
    }
}
