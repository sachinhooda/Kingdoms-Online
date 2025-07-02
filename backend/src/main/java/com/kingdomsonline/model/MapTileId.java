package com.kingdomsonline.model;

import java.io.Serializable;
import java.util.Objects;

public class MapTileId implements Serializable {
    private int x;
    private int y;

    public MapTileId() {}

    public MapTileId(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapTileId that)) return false;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
