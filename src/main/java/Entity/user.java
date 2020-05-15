package Entity;

public class user {
    private float level1Score;
    private float level2Score;
    private float level3Score;

    public float getLevel1Score() {
        return level1Score;
    }

    public void setLevel1Score(float level1Score) {
        this.level1Score = level1Score;
    }

    public float getLevel2Score() {
        return level2Score;
    }

    public void setLevel2Score(float level2Score) {
        this.level2Score = level2Score;
    }

    public float getLevel3Score() {
        return level3Score;
    }

    public void setLevel3Score(float level3Score) {
        this.level3Score = level3Score;
    }

    @Override
    public String toString() {
        return "user{" +
                "level1Score=" + level1Score +
                ", level2Score=" + level2Score +
                ", level3Score=" + level3Score +
                '}';
    }
}
