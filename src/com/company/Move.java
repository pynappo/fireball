package com.company;

public class Move {
    private String name;
    private String[] weakerMoveNames;
    public Move (String name) {
      this.name = name;
    }
    public int compareTo (Move other) {
        if (name.equals(other.name)) return 0;
        else for (String name : other.weakerMoveNames) {
            if (name.equals(name)) return -1 ;
        }
        return 1;
    }
}
