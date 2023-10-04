package com.csabapro.core.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ship {
    private Vec2 startPos;
    private Orientation orientation;
    private int size;
    private List<Vec2> positions;

    private static List<Vec2> generatePositions(Vec2 startPos, Orientation orientation, int size) {
        ArrayList<Vec2> positions = new ArrayList<>();
        int start = orientation == Orientation.Horizontal ? startPos.x : startPos.y;
        int end = start + size;
        BiFunction<Vec2, Integer, Void> inc = orientation == Orientation.Horizontal ? (Vec2 in, Integer s) -> {in.x = s; return null;} : (Vec2 in, Integer s) -> {in.y = s; return null;};
        for (Vec2 v = new Vec2(startPos); start < end; inc.apply(v, start), v = new Vec2(startPos), start++)
            positions.add(v);
        return positions;
    }

    @JsonCreator
    public Ship(@JsonProperty("startPos") @JsonAlias({"pos", "location"}) Vec2 startPos, @JsonProperty("orientation") Orientation orientation, @JsonProperty("size") int size) {
        this.startPos = startPos;
        this.orientation = orientation;
        this.size = size;
        this.positions = generatePositions(startPos, orientation, size);
    }

    public Vec2 getStartPos() {
        return startPos;
    }

    public void setStartPos(Vec2 startPos) {
        this.startPos = startPos;
        this.positions = generatePositions(startPos, orientation, size);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        this.positions = generatePositions(startPos, orientation, size);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        this.positions = generatePositions(startPos, orientation, size);
    }

    public List<Vec2> getPositions() {
        return positions;
    }

    public List<Vec2> getOutOfBoundsPositions(Vec2 upperBounds) {
        List<Vec2> outOfBoundsPositions = new ArrayList<>();
        for (Vec2 pos : positions) {
            if (pos.x > upperBounds.x || pos.y > upperBounds.y || pos.x < 0 || pos.y < 0)
                outOfBoundsPositions.add(pos);
        }
        return outOfBoundsPositions;
    }

    /**
     * @param otherShipPositions a flat list of the positions of other ships
     * @return a list of overlapping positions
     */
    public List<Vec2> getOverlappingPositions(List<Vec2> otherShipPositions) {
        List<Vec2> overlapping = new ArrayList<>(otherShipPositions); // make a copy because retainAll modifies the array in place
        overlapping.retainAll(positions);
        return overlapping;
    }

    /**
     * Rotates the ship to the other other orientation and recalculates the positions
     */
    public void rotate() {
        orientation = orientation == Orientation.Horizontal ? Orientation.Vertical : Orientation.Horizontal; 
        positions = generatePositions(startPos, orientation, size);
    }
}
