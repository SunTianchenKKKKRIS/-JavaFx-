package fileUtils;

import Entity.level1Question;

import Entity.level2Question;
import Entity.level3Question;
import Entity.system;
import javafx.scene.image.Image;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

public class questionXmlHandler {
    public static ArrayList<system> level1SystemGet(String xmlPath){


        ArrayList<level1Question> level1Questions = level1QuestionsGet(xmlPath);
        ArrayList<String> system_Namelist = new ArrayList<>();
        ArrayList<system> system_list = new ArrayList<>();
        for (level1Question level1Question:level1Questions){
            if(!system_Namelist.contains(level1Question.getSystem())){
                system_Namelist.add(level1Question.getSystem());
                system_list.add(new system(level1Question.getSystem()));
            }
        }
//        System.out.println(system_list);
        return system_list;

    }

    public static ArrayList<level1Question> level1QuestionsGet(String xmlPath){
        ArrayList<level1Question> level1Questions = new ArrayList<>();
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(xmlPath);
            Element root = doc.getRootElement();
//            System.out.println(root.element("question").elementText("heading"));
            for (Iterator<Element> questions = root.elementIterator();questions.hasNext();){

//                System.out.println(questions.next().elementText("heading"));
                Element questionElement = questions.next();
//                System.out.println(element.elementText("heading"));
                level1Question question = new level1Question();
                question.setHeading(questionElement.elementText("heading"));
                ArrayList<String> ans = new ArrayList<>();

                for(Iterator<Element> anses = questionElement.element("ans").elementIterator();anses.hasNext();){
                    Element ansElement = anses.next();
                    if (ansElement.getName().equals("correctAns")){
                        question.setCorrectAns(ansElement.getTextTrim());
//                        System.out.println(ansElement.getTextTrim());
                        continue;
                    }
                    ans.add(ansElement.getTextTrim());
                    question.setAns(ans);
                }
                question.setSystem(questionElement.elementText("system"));
//                System.out.println(question);
                level1Questions.add(question);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        finally {
            return level1Questions;
        }
    }

    public static ArrayList<level2Question> level2QuestionsGet(String xmlPath){
        ArrayList<level2Question> level2Questions = new ArrayList<>();
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(xmlPath);
            Element root = doc.getRootElement();
            for (Iterator<Element> questions = root.elementIterator();questions.hasNext();){
                Element questionElement = questions.next();
                level2Question question = new level2Question();
                question.setTitle(questionElement.elementText("title"));
                question.setImg(new Image(new FileInputStream(new File(questionElement.elementText("img_url")))));
                ArrayList<String> ansList = new ArrayList<>();
                for (Iterator<Element> anses = questionElement.element("Ans").elementIterator();anses.hasNext();){
                    Element ansElement = anses.next();
                    ansList.add(ansElement.getTextTrim());
                }
                question.setAnsList(ansList);
                level2Questions.add(question);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return level2Questions;
    }

    public static int level2QuestionsNumGet(String xmlPath){
        int num = 0;

        for (level2Question question :level2QuestionsGet(xmlPath)){
            num+= question.getAnsList().size();
        }
        return num;
    }

    public static ArrayList<level3Question> level3QuestionsGet(String xmlPath){
        ArrayList<level3Question> questions_List = new ArrayList<>();
        SAXReader reader = new SAXReader();
        try {
            Document doc  = reader.read(xmlPath);
            Element root = doc.getRootElement();
            for (Iterator<Element> questions = root.elementIterator();questions.hasNext();){
                Element questionElement = questions.next();
                level3Question question = new level3Question();
                question.setContent(questionElement.elementText("content"));
                question.setVideo_Url(new File("src/main/resources/video/"+questionElement.elementText("video_url")).toURI().toString());
                questions_List.add(question);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return questions_List;
    }

    public static void main(String[] args) {
//            File file = new File("ErrorQuestion.txt");
//        System.out.println(file.getName());
            }
}
