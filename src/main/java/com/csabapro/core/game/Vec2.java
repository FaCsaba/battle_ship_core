package com.csabapro.core.game;

import java.text.ParseException;
import java.util.List;

public class Vec2 {
    public int x;
    public int y;

    public Vec2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(Vec2 c) {
        this.x = c.x;
        this.y = c.y;
    }

    public Vec2() {}

    public static Vec2 fromString(String s) throws ParseException {
        String[] vec2Split = s.split(",");
        if (vec2Split.length != 2) {
            throw new ParseException("Could not parse Vec2. Incorrect number of elements", 0);
        }

        int x = 0;
        int y = 0;
        try {
            x = Integer.parseInt(vec2Split[0]);
            y = Integer.parseInt(vec2Split[1]);
        } catch (NumberFormatException e) {
            throw new ParseException("Could not parse Vec2. Elements are not integer: "+vec2Split[0]+" "+vec2Split[1], 0);
        }

        return new Vec2(x, y);
    }

    /**
     * Is this vector < upperBounds and this vector > 0,0
     * @param upperBounds exclusive upper bounds
     * @return
     */
    public boolean isInBounds(Vec2 upperBounds) {
        return x < upperBounds.x || y < upperBounds.y || x > 0 || y > 0;
    }

    public boolean isOverlapping(List<Vec2> otherPositions) {
        return otherPositions.contains(this);
    }

    @Override
    public String toString() {
        return ""+x+","+y;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (this.getClass() != other.getClass()) return false;

        Vec2 casted = (Vec2) other;
        return this.x == casted.x && this.y == casted.y;
    }
}
