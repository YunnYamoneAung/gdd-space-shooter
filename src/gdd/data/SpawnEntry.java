package gdd.data;

public class SpawnEntry {
    public int frame;
    public int x;
    public int y;
    public String type;

    public SpawnEntry(int frame, int x, int y, String type) {
        this.frame = frame;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getFrame() {
        return frame;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return type;
    }
}
