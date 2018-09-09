import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import net.sourceforge.tess4j.*;

public class Bot{

    private final int coordinates[] = new int[]{677, 157, 1065, 516};
    private final String ext = "png";
    private final String file = "screen." + ext;

    private String question;
    private String answers[];
    private String bestAns;
    private int points[];
    private int index = 0;

    private JFrame frame;
    private JButton capture;
    private JButton clear;
    private JTextArea questionArea;
    private JTextField ans1;
    private JTextField ans2;
    private JTextField ans3;
    private JTextField finalAns;
    private JLabel questionlabel;
    private JLabel answerslabel;
    private JLabel bestAnsLabel;

    public Bot(){
        frame = new JFrame("HQ Trivia Bot");
        questionlabel = new JLabel("Question:");
        answerslabel = new JLabel("Answers:");
        bestAnsLabel = new JLabel("Best Answer:");
        frame.setSize(500, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.add(questionlabel);
        questionArea = new JTextArea(5, 50);
        questionArea.setEditable(false);
        frame.add(questionArea);
        frame.add(answerslabel);
        ans1 = new JTextField(50);
        ans1.setEditable(false);
        ans2 = new JTextField(50);
        ans2.setEditable(false);
        ans3 = new JTextField(50);
        ans3.setEditable(false);
        frame.add(ans1);
        frame.add(ans2);
        frame.add(ans3);
        frame.add(bestAnsLabel);
        finalAns = new JTextField(50);
        finalAns.setEditable(false);
        frame.add(finalAns);

        question = new String();
        answers = new String[3];
        bestAns = new String();
        points = new int[3];

        capture = new JButton("New Question");
        capture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //screenShot();
                parseScreen();
                parseQuestionAndAnswers();
                search();
                maxIndex();
                displayResults();
                index = 0;
            }
        });
        frame.add(capture);

        clear = new JButton("clear");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ans1.setText("");
                ans2.setText("");
                ans3.setText("");
                questionArea.setText("");
                finalAns.setText("");
            }
        });
        frame.add(clear);

        frame.setVisible(true);
    }

    private void screenShot(){

        try {
            Rectangle captureRect = new Rectangle(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
            BufferedImage screenFullImage = (new Robot()).createScreenCapture(captureRect);
            ImageIO.write(screenFullImage, ext, new File(file));
            System.out.println("Screenshot saved as " + file);
        }catch (Exception e) {
            e.printStackTrace();
        }
        convertToBlackAndWhite();
    }

    private void parseQuestionAndAnswers() {
        question = "";
        String split[] = parseScreen().split("\n");
        answers[0] = split[split.length - 3];
        answers[1] = split[split.length - 2];
        answers[2] = split[split.length - 1];

        for (int i = 0; i < split.length - 3; i++) {
            question += split[i];
        }
    }

    private void search(){

    }

    private void displayResults(){
        questionArea.setText(question);
        ans1.setText(answers[0] + " -- " + points[0]);
        ans2.setText(answers[1] + " -- " + points[1]);
        ans3.setText(answers[2] + " -- " + points[2]);
        finalAns.setText(bestAns + " -- " + points[index]);
    }

    private void maxIndex(){

        for(int i = 0; i < 3; i++){
            if(points[index] < points[i]){
                index = i;
            }
        }

        bestAns = answers[index];
    }

    private void convertToBlackAndWhite(){
        try {

            File img = new File(file);
            BufferedImage image = ImageIO.read(img);

            BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D graphic = result.createGraphics();
            graphic.drawImage(image, 0, 0, Color.WHITE, null);
            graphic.dispose();
            ImageIO.write(result, ext, img);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String parseScreen(){
        File imageFile = new File(file);
        ITesseract instance = new Tesseract();
        String result = new String();
        try {
            result = instance.doOCR(imageFile);
        }catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        //System.out.println(result);
        return result;
    }

    public static void main(String args[]){
        new Bot();
        //getMouseCoordinates();
    }

    private static void getMouseCoordinates(){

        while(true){
            Point p = MouseInfo.getPointerInfo().getLocation();
            System.out.println(p.x + " -- " + p.y);
            try{
                Thread.sleep(1000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}