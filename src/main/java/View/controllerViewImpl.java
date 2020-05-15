package View;


import Entity.*;
import fileUtils.questionXmlHandler;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

public class controllerViewImpl implements controllerView {
    private Stage stage;
    private user user;
    private File errorFile ;

    private final String XMLPATH_1 = "src/main/resources/QuestionLevel/level1.xml";
    private final String XMLPATH_2 = "src/main/resources/QuestionLevel/level2.xml";
    private final String XMLPATH_3 = "src/main/resources/QuestionLevel/level3.xml";
    private final int LEVEL1_QUESTION_COUNT = questionXmlHandler.level1QuestionsGet(XMLPATH_1).size();
    private final int LEVEL2_QUESTION_COUNT = questionXmlHandler.level2QuestionsNumGet(XMLPATH_2);
    private final int LEVEL3_QUESTION_COUNT = questionXmlHandler.level3QuestionsGet(XMLPATH_3).size();

    public controllerViewImpl(Stage stage, user user,File errorFile) {
        this.stage = stage;
        this.user = user;
        this.errorFile = errorFile;
    }


    @Override
    public void getHomepage() {

        Pane stackPane = new Pane();
        stackPane.setBackground(new Background(new BackgroundImage(new Image("img/indexbackground.jpg"), BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
        Label version = new Label("version: 0.0002");
        Button start = new Button("开始游戏");
        ObservableList<String> choices = FXCollections.observableArrayList("选择关卡","知识问答","部位对接","虚拟实验","敬请期待...");
        ChoiceBox select = new ChoiceBox(choices);
        select.getSelectionModel().select(0);
        select.setTooltip(new Tooltip("上一关卡未通过的话可是不能跳关的哦~"));
        ImageView title = new ImageView(new Image("img/jiepo.jpg"));
        title.setFitHeight(100);title.setFitWidth(200);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        ArrayList<system> system_list = questionXmlHandler.level1SystemGet(XMLPATH_1);
        ArrayList<String> systemName_list = new ArrayList<>();
        for (system system : system_list){
            systemName_list.add(system.getSystem());
        }
        start.setOnAction((event -> {
            if (select.getValue().equals("") || select.getValue().equals("选择关卡")){
                alert.setTitle("错误");
                alert.setContentText("请先选择关卡！");
                alert.showAndWait();
            }
            if (select.getValue().equals("知识问答")){
                if (user.getLevel1Score()>0){
                    alert.setTitle("注意");
                    alert.setContentText("第一关已经挑战成功，请继续下一关卡！");
                    alert.showAndWait();
                    return;
                }
                getLevel1Select(systemName_list);
            }
            if (select.getValue().equals("部位对接") ){
                if (user.getLevel2Score()>0){
                    alert.setTitle("注意");
                    alert.setContentText("第二关已经挑战成功，请继续下一关卡！");
                    alert.showAndWait();
                    return;
                }
                if(user.getLevel1Score()<60){
                    alert.setTitle("错误");
                    alert.setContentText("你第一关还没及格，切勿急于求成！");
                    alert.showAndWait();
                }else
                    getLevel2(0);

            }
            if(select.getValue().equals("虚拟实验")){
                if(user.getLevel2Score()<60){
                    alert.setTitle("错误");
                    alert.setContentText("你第二关还没及格，心急吃不了热豆腐！");
                    alert.showAndWait();
                }else
                    getLevel3(0);

            }

        }));

        stackPane.getChildren().addAll(version,start,title,select);
        title.setLayoutX(250);title.setLayoutY(400);
        version.setLayoutX(0);version.setLayoutY(0);
        start.setLayoutX(300);start.setLayoutY(600);
        select.setLayoutX(300);select.setLayoutY(630);
        stage.setScene(new Scene(stackPane));
    }

    private void fillLevel1(ArrayList<level1Question> questions , int questionNum , ArrayList<String> sysName_List) {


        Pane pane = new Pane();
        pane.setBackground(new Background(new BackgroundImage(new Image("img/Part1background.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        level1Question question = questions.get(questionNum);
        System.out.println(question.getCorrectAns());
        ArrayList<String> questionAns = question.getAns();
        /////////////////////////////////////////////////////////////
        Label questionHeading = new Label(question.getHeading());
        questionHeading.setFont(new Font("宋体",25));
        questionHeading.setLayoutX(100);
        questionHeading.setLayoutY(50);
        Button back2Select = new Button("重新开始");
        back2Select.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("注意");
                alert.setHeaderText("请确认：");
                alert.setContentText("请问您是否要重新开始[知识问答](重新开始后会清空当前关卡已得分数！)");
                Optional result = alert.showAndWait();
                if (result.get().equals(ButtonType.OK)) {
                    user.setLevel1Score(0);
                    getHomepage();
                }
            }
        });
        back2Select.setLayoutX(700);
        ChoiceBox choice = new ChoiceBox(FXCollections.<String>observableArrayList(questionAns));
        choice.getSelectionModel().select(0);
        choice.setLayoutX(500);
        choice.setLayoutY(500);
        Button sub = new Button();
        sub.setText("下一题");
        sub.setLayoutX(550);
        sub.setLayoutY(550);
        if (questionNum != questions.size()) {
            sub.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (question.getCorrectAns().equals(choice.getValue().toString())) {
                        user.setLevel1Score(user.getLevel1Score() + 1);
                    }
                    else{
                        try {
                            FileWriter fileWriter = new FileWriter(errorFile,true);
                            fileWriter.write("["+question.getHeading()+"] 回答错误，正确答案为"+question.getCorrectAns()+"\n");
//                            fileWriter.write("1");
                            fileWriter.flush();
                            fileWriter.close();
                            System.out.println("写入错题成功");
                            FileReader reader = new FileReader(errorFile);
                            char[] chars = new char[50];
                            reader.read(chars);
                            System.out.println(chars);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (questionNum+1 < questions.size()) {
                        fillLevel1(questions, questionNum + 1,sysName_List);
                    } else {

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("确认");
                        alert.setContentText("本系统题目已完成，点击确定继续完成下一系统!");
                        alert.showAndWait();
                        sysName_List.remove(question.getSystem());
                        getLevel1Select(sysName_List);
                    }


                }
            });
        }
        pane.getChildren().addAll(questionHeading, back2Select, choice, sub);
        ////////////////////////////////////////////////////////////////////
        for (int ansNum = 0; ansNum < questionAns.size(); ansNum++) {
            Label quesionAnsLabel = new Label(questionAns.get(ansNum));
            quesionAnsLabel.setFont(new Font("Dialog",18));
            quesionAnsLabel.setLayoutX(150);
            quesionAnsLabel.setLayoutY(150 + ansNum * 50);
            pane.getChildren().add(quesionAnsLabel);
        }
        stage.setScene(new Scene(pane));
    }
    private void getLevel1(String sysName , ArrayList<String> sysName_List){
        stage.setScene(new Scene(new Pane()));
        ArrayList<level1Question> questions = questionXmlHandler.level1QuestionsGet(XMLPATH_1);
        for (Iterator<level1Question> iterator = questions.iterator();iterator.hasNext();){
            if(!iterator.next().getSystem().equals(sysName)){
                iterator.remove();
            }
        }

        fillLevel1(questions,0,sysName_List);
    }
    @Override
    public void getLevel1Select(ArrayList<String> systemName_list){
        if (systemName_list.size() == 0){
            getLevel1End();
            return;
        }
        Pane pane = new Pane();
        pane.setBackground(new Background(new BackgroundImage(new Image("img/Part1background.jpg"), BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
        ObservableList<String> system = FXCollections.observableArrayList(systemName_list);
        ListView<String> listView = new ListView<>(system);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                systemName_list.remove(newValue.toString());
                getLevel1(newValue.toString() ,systemName_list);
            }
        });
        listView.setPrefSize(800,800);
        pane.getChildren().addAll(listView);
        stage.setScene(new Scene(pane));
    }

    @Override
    public void getLevel1End() {
        user.setLevel1Score(Float.parseFloat(String.format("%.2f",(user.getLevel1Score()/LEVEL1_QUESTION_COUNT)*100)));
        System.out.println(user.getLevel1Score());
        System.out.println("成绩"+user.getLevel1Score());
        getHomepage();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("成绩");
        if (user.getLevel1Score()>=60)
            alert.setContentText("你的[知识问答]关卡得分为"+user.getLevel1Score()+"分，成功闯入下一关！");
        else {

            alert.setContentText("你的[知识问答]关卡得分为" + user.getLevel1Score() + "分，闯关失败...不过不用气馁，已为你当前关卡清空分数，再次挑战吧~");
            user.setLevel1Score(0);
        }
        alert.showAndWait();
        System.out.println(user.getLevel1Score());

    }

    @Override
    public void getLevel2(int questionNum) {
        if (questionNum == questionXmlHandler.level2QuestionsGet(XMLPATH_2).size()){
            user.setLevel2Score(Float.parseFloat(String.format("%.2f",(user.getLevel2Score()/LEVEL2_QUESTION_COUNT)*100)));
            getHomepage();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("成绩");
            if (user.getLevel2Score()>=60)
                alert.setContentText("你的[部位对接]关卡得分为"+user.getLevel2Score()+"分，成功闯入下一关！");
            else {

                alert.setContentText("你的[部位对接]关卡得分为" + user.getLevel2Score() + "分，闯关失败...不过不用气馁，已为你清空当前关卡分数，再次挑战吧~");
                user.setLevel2Score(0);
            }
            alert.showAndWait();
            return;
        }
        Pane pane = new Pane();
        pane.setBackground(new Background(new BackgroundImage(new Image("img/Part2background.gif"), BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
        level2Question question = questionXmlHandler.level2QuestionsGet(XMLPATH_2).get(questionNum);
            System.out.println(question.getTitle());
        Button back2HomePage = new Button("重新开始");
        back2HomePage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("注意");
                alert.setHeaderText("请确认：");
                alert.setContentText("请问您是否要重新开始[部位对接](重新开始后会清空当前关卡已得分数！)");
                Optional result = alert.showAndWait();
                if (result.get().equals(ButtonType.OK)) {
                    user.setLevel2Score(0);
                    getHomepage();
                }
            }
        });
        back2HomePage.setLayoutX(700);back2HomePage.setLayoutY(20);
            ArrayList<String> ansList = question.getAnsList();
            ArrayList<String> list = new ArrayList<>();
            for(int i =0;i<question.getAnsList().size();i++){
                list.add(i+1+"号部位");
            }
            ObservableList observableList = FXCollections.observableArrayList(list);
            ListView<String> listView = new ListView<String>(observableList);
            listView.setPrefWidth(400);listView.setPrefHeight(650);listView.setLayoutX(400);listView.setLayoutY(70);
            listView.setCellFactory(TextFieldListCell.forListView());
            listView.setEditable(true);
            listView.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
                @Override
                public void handle(ListView.EditEvent<String> event) {
                    observableList.set(event.getIndex(),event.getNewValue());
                }
            });
            Label head = new Label(question.getTitle());
            head.setFont(new Font("Dialog",25));
            head.setPrefWidth(400);head.setPrefHeight(70);head.setLayoutX(350);head.setLayoutY(5);
            ImageView img = new ImageView(question.getImg());
            img.setFitWidth(400);img.setFitHeight(730);img.setLayoutX(0);img.setLayoutY(70);
            Button sub = new Button("提交答案");
            sub.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("请确认");
                    alert.setHeaderText("确认");
                    alert.setContentText("请确认你的答案，提交后无法更改!");
                    Optional optional = alert.showAndWait();
                    if (optional.get() == ButtonType.OK){
                        for (int i = 0;i<question.getAnsList().size();i++){
                            if (listView.getItems().get(i).equals(question.getAnsList().get(i))){
                                user.setLevel2Score(user.getLevel2Score()+1);
                            }else {
                                try {
                                    FileWriter fileWriter = new FileWriter(errorFile,true);
                                    fileWriter.write("["+question.getTitle()+"] 的第"+(i+1)+"个部位回答错误，正确答案为"+question.getAnsList().get(i)+"\n");
                                    fileWriter.flush();
                                    fileWriter.close();
                                    System.out.println("写入错题成功");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        getLevel2(questionNum+1);
                    }
                }
            });
            sub.setPrefWidth(200);sub.setPrefHeight(60);sub.setLayoutY(720); sub.setLayoutX(500);
            pane.getChildren().addAll(img,listView,head,sub,back2HomePage);
        stage.setScene(new Scene(pane));
        if (questionNum == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");alert.setHeaderText("提示");
            alert.setContentText("注意哦，输入答案后请用Enter键确认录入");
            alert.showAndWait();
        }

    }

    @Override
    public void getLevel3(int questionNum) {
        if (questionNum == LEVEL3_QUESTION_COUNT){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("成功");
            alert.setContentText("你的所有答案和成绩已提交，前往查看你的闯关情况吧！");
            alert.showAndWait();
            getEnd();
            return;
        }
        Pane pane = new Pane();
        ArrayList<level3Question> questions_list = questionXmlHandler.level3QuestionsGet(XMLPATH_3);
        level3Question question = questions_list.get(questionNum);
        Media media = new Media(question.getVideo_Url());
        MediaPlayer player = new MediaPlayer(media);
        MediaView view = new MediaView(player);
        System.out.println(media.getSource());
        view.setFitWidth(800);view.setFitHeight(800);view.setLayoutX(0);view.setLayoutY(0);
        Button back2Homepage = new Button("重新开始");
        back2Homepage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("注意");
                alert.setHeaderText("请确认：");
                alert.setContentText("请问您是否要重新开始[虚拟实验](重新开始后会清空当前关卡已得分数！)");
                Optional result = alert.showAndWait();
                if (result.get().equals(ButtonType.OK)) {
                    user.setLevel3Score(0);
                    getHomepage();
                }
            }
        });
        back2Homepage.setPrefWidth(100);back2Homepage.setPrefHeight(25);back2Homepage.setLayoutX(700);back2Homepage.setLayoutY(630);
        Button play = new Button("开始播放");
        play.setPrefWidth(100);play.setPrefHeight(25);play.setLayoutX(600);play.setLayoutY(630);
        Label content = new Label(question.getContent());
        content.setFont(new Font("Dialog",25));
        content.setPrefWidth(600);content.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
        content.setLayoutX(0);content.setLayoutY(600);
        TextArea ans = new TextArea();
        ans.setPrefWidth(600);ans.setPrefHeight(160);ans.setLayoutX(0);ans.setLayoutY(630);
        Button sub = new Button("提交答案\n回答下一题");
        sub.setPrefWidth(85);sub.setPrefHeight(70);sub.setLayoutX(707.5);sub.setLayoutY(700);
        sub.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("确认");
                alert.setContentText("请确认你的答案，提交后无法修改！");
                Optional optional = alert.showAndWait();
                if (optional.get() == ButtonType.OK){
                    try {
                        FileWriter fileWriter = new FileWriter(errorFile,true);
                        fileWriter.write("你对["+question.getContent()+"]给出的答案是(开放性试题):\n"+ans.getText()+"\n");
                        System.out.println("你对["+question.getContent()+"]给出的答案是(开放性试题):\n"+ans.getText()+"\n");
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    getLevel3(questionNum+1);
                }
            }
        });
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                player.play();
            }
        });


        pane.getChildren().addAll(view,content,back2Homepage,play,ans,sub);
        stage.setScene(new Scene(pane));
    }
    private void getEnd(){
        Pane pane = new Pane();
        String score = "\n你的第一关得分:"+user.getLevel1Score()+"\n你的第二关得分:"+user.getLevel2Score()+"\n你的平均得分:"+(user.getLevel2Score()+user.getLevel1Score())/2;
        try {
            FileReader reader = new FileReader(errorFile);
            char[] errorResultChar = new char[(int)errorFile.length()];
            reader.read(errorResultChar);
            System.out.println(String.valueOf(errorResultChar));
            Label result= new Label(String.valueOf(errorResultChar)+score);
            System.out.println(String.valueOf(errorResultChar));
//            result.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
            result.setLayoutX(0);result.setLayoutY(0);result.setPrefWidth(800);result.setPrefHeight(700);result.setFont(new Font("宋体",15));
            pane.getChildren().add(result);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Button replay = new Button("重新挑战");
        replay.setPrefWidth(300);replay.setPrefHeight(70);
        replay.setLayoutX(250);replay.setLayoutY(715);
        replay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("你要清空分数，重新挑战吗？");
                Optional optional = alert.showAndWait();
                if (optional.get() == ButtonType.OK){
                    try {
                        if (!errorFile.exists()){
                            errorFile.createNewFile();
                        }
                        FileWriter fileWriter = new FileWriter(errorFile);
                        fileWriter.write("");
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    user = new user();
                    getHomepage();
                }

            }
        });
        pane.getChildren().addAll(replay);
        stage.setScene(new Scene(pane));
    }


}
