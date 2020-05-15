package Entity;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class level2Question {
    private String title;
    private Image img;
    private ArrayList<String> ansList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public ArrayList<String> getAnsList() {
        return ansList;
    }

    public void setAnsList(ArrayList<String> ansList) {
        this.ansList = ansList;
    }

    @Override
    public String toString() {
        return "level2Question{" +
                "title='" + title + '\'' +
                ", img=" + img +
                ", ansList=" + ansList +
                '}';
    }
}
