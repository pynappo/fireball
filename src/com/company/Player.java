package com.company;

// Maybe have a Wizard class that extends the Player with a certain moveset and move some of these there
public class Player {
    private int health = 1;
    private int consecutiveShieldCount = 0;
    private int ammo = 0;
    private String name;
    public static Move[] moves;
    public Player (String name, Move[] moves) {
        this.name = name;
    }
    public void decreaseHealth (int amount) { health -= amount; }
    public boolean canShield() { return consecutiveShieldCount >= 3; }
    public boolean canFireball () { return ammo > 0; }
    public int getHealth() { return health; }
    public int getAmmo() { return ammo; }
}
