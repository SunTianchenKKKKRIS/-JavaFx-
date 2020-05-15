package Entity;

import java.util.ArrayList;
import java.util.Arrays;

public class level1Question extends system {
    private String heading;
    private ArrayList<String> ans;
    private String correctAns;

    public static double score;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public ArrayList<String> getAns() {
        return ans;
    }

    public void setAns(ArrayList<String> ans) {
        this.ans = ans;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }

    @Override
    public String toString() {
        return "level1Question{" +
                "heading='" + heading + '\'' +
                ", ans=" + ans +
                ", correctAns='" + correctAns + '\'' +
                '}';
    }
}
