import Entity.user;
import View.controllerView;
import View.controllerViewImpl;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ApplicationMain extends Application {

    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("解剖王者");
        primaryStage.setWidth(800);
        primaryStage.setHeight(800);
        primaryStage.getIcons().add(new Image("img/icon.png"));
        primaryStage.setResizable(false);
        user user = new user();
        File errorFile = new File("src/main/resources/ErrorQuestion.txt");
                if (errorFile.exists()){
            try {
                FileWriter fileWriter = new FileWriter(errorFile);
                fileWriter.write("");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        controllerView pageController = new controllerViewImpl(primaryStage,user,errorFile);
        pageController.getHomepage();


        primaryStage.show();
    }
}
