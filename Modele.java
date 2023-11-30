import java.util.Vector;

public interface Modele {
    void setScore(int score);
    int getScore();
    void setDifficulty(String difficulty);
    String getDifficulty();
    int getNbrQuestions();
    void setAnswers(Vector<String> answers);
    Vector<String> getAnswers();
    boolean getReponse(String selectedAnswer);

    // Add these methods to the interface
    Vector<String> getQuestions();

    void loadQuestionsAndAnswers(String questionsFileName);

    void clearQuestionsAndAnswers();
}
