package Entity;

public class level3Question {
    private String content;
    private String video_Url;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideo_Url() {
        return video_Url;
    }

    public void setVideo_Url(String video_Url) {
        this.video_Url = video_Url;
    }

    @Override
    public String toString() {
        return "level3Question{" +
                "content='" + content + '\'' +
                ", video_Url='" + video_Url + '\'' +
                '}';
    }
}
