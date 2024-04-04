package be.kuleuven.candycrush.model;

public sealed interface Candy permits NormalCandy, OnderVolledig, AllesGrezend, DubbelPunt, RandomBom {
    String getName();
}
