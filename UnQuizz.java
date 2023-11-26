import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class UnQuizz implements Modele {
    private Vector<String> questions;
    private Vector<String> answers;
    private String difficulty;
    private int score;
    private List<String> correctAnswers;


    public UnQuizz() {
        questions = new Vector<>();
        answers = new Vector<>();
        difficulty = "";
        score = 0;
        correctAnswers = new ArrayList<>();
    }

    @Override
    public Vector<String> getQuestions() {
        return questions;
    }

    @Override
    public Vector<String> getAnswers() {
        return answers;
    }

    @Override
    public boolean getReponse(String selectedAnswer) {
        return false;
    }

    @Override
    public int getNbrQuestions() {
        return questions.size();
    }

    @Override
    public String getDifficulty() {
        return difficulty;
    }

    @Override
    public void setAnswers(Vector<String> answers) {
        this.answers = answers;
    }

    @Override
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int getScore() {
        return score;
    }



    @Override
    public void loadQuestionsAndAnswers(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                setDifficulty("facile");
                questions.add(reader.readLine());
                answers.add(reader.readLine());
                answers.add(reader.readLine());
                answers.add(reader.readLine());
                answers.add(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ... (existing methods)
}
