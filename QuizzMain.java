import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class QuizzMain {
    private Timer questionTimer;
    private int timerCount;

    private JButton jouerButton;
    private JButton validerLaReponseButton;
    private JPanel mainPanel;
    private JLabel scoreLabel;
    private JLabel difficulteLabel;
    private JLabel nbrQuestionsLabel;
    private JTextArea questionTextArea;
    private JRadioButton reponseARadioButton;
    private JRadioButton reponseBRadioButton;
    private JRadioButton reponseCRadioButton;
    private JRadioButton reponseDRadioButton;
    private ButtonGroup buttonGroup;
    private JLabel questNLabel;

    private Modele modele;
    private int currentQuestion;
    private int score;

    public QuizzMain() {
        modele = new UnQuizz();
        currentQuestion = 0;
        score = 0;
        timerCount = 30; // Initial timer count (30 seconds)
        createUI();
    }

    private void createUI() {
        jouerButton = new JButton("Jouer");
        validerLaReponseButton = new JButton("Valider la Réponse");

        difficulteLabel = new JLabel("Difficulté: " + modele.getDifficulty());
        nbrQuestionsLabel = new JLabel("Questions: 0/0");
        scoreLabel = new JLabel("Score: 0/0");
        questionTextArea = new JTextArea();
        reponseARadioButton = new JRadioButton();
        reponseBRadioButton = new JRadioButton();
        reponseCRadioButton = new JRadioButton();
        reponseDRadioButton = new JRadioButton();
        questNLabel = new JLabel();

        jouerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jouerButton.setEnabled(false);
                validerLaReponseButton.setEnabled(true);
                modele.loadQuestionsAndAnswers("C:/dev/quizProj/src/quizz.txt");
                displayQuestion();
            }
        });

        validerLaReponseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });

        questionTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerCount--;
                if (timerCount <= 0) {
                    // Timer reached 0, move to the next question
                    timerCount = 30; // Reset timer for the next question
                    displayQuestion();
                }
                updateTimerLabel();
            }
        });

        mainPanel = new JPanel();
        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(reponseARadioButton);
        buttonGroup.add(reponseBRadioButton);
        buttonGroup.add(reponseCRadioButton);
        buttonGroup.add(reponseDRadioButton);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                                .addComponent(difficulteLabel)
                                .addComponent(scoreLabel)
                                .addComponent(nbrQuestionsLabel)
                                .addComponent(questionTextArea)
                                .addComponent(reponseARadioButton)
                                .addComponent(reponseBRadioButton)
                                .addComponent(reponseCRadioButton)
                                .addComponent(reponseDRadioButton)
                                .addComponent(questNLabel)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(jouerButton)
                                        .addComponent(validerLaReponseButton)
                                )
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(difficulteLabel)
                        .addComponent(scoreLabel)
                        .addComponent(nbrQuestionsLabel)
                        .addComponent(questionTextArea)
                        .addComponent(reponseARadioButton)
                        .addComponent(reponseBRadioButton)
                        .addComponent(reponseCRadioButton)
                        .addComponent(reponseDRadioButton)
                        .addComponent(questNLabel)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(jouerButton)
                                .addComponent(validerLaReponseButton)
                        )
        );

        layout.linkSize(SwingConstants.HORIZONTAL, jouerButton, validerLaReponseButton);
        layout.linkSize(SwingConstants.VERTICAL, jouerButton, validerLaReponseButton);
    }

    private void displayQuestion() {
        if (currentQuestion < modele.getNbrQuestions()) {
            questionTextArea.setText(modele.getQuestions().get(currentQuestion));
            reponseARadioButton.setText("Réponse A: " + modele.getAnswers().get(currentQuestion * 4));
            reponseBRadioButton.setText("Réponse B: " + modele.getAnswers().get(currentQuestion * 4 + 1));
            reponseCRadioButton.setText("Réponse C: " + modele.getAnswers().get(currentQuestion * 4 + 2));
            reponseDRadioButton.setText("Réponse D: " + modele.getAnswers().get(currentQuestion * 4 + 3));
            questNLabel.setText("Quest. n°" + (currentQuestion + 1));
            difficulteLabel.setText("Difficulté: " + modele.getDifficulty());

        } else {
            endGame();
            return;
        }

        // Reset timer for the next question
        timerCount = 30;
        updateTimerLabel();
        questionTimer.restart();

        // Increment currentQuestion count after updating labels
        currentQuestion++;
    }

    private void updateTimerLabel() {
        questNLabel.setText("Quest. n°" + (currentQuestion + 1) + " - Time: " + timerCount + "s");
    }

    private void checkAnswer() {
        if (reponseARadioButton.isSelected() || reponseBRadioButton.isSelected() ||
                reponseCRadioButton.isSelected() || reponseDRadioButton.isSelected()) {
            String selectedAnswer = "";
            if (reponseARadioButton.isSelected()) {
                selectedAnswer = "A";
            } else if (reponseBRadioButton.isSelected()) {
                selectedAnswer = "B";
            } else if (reponseCRadioButton.isSelected()) {
                selectedAnswer = "C";
            } else if (reponseDRadioButton.isSelected()) {
                selectedAnswer = "D";
            }
            if (checkCorrectAnswer(selectedAnswer)) {
                // Correct answer
                score++;
                // Change background color to green for 2 seconds
                mainPanel.setBackground(Color.GREEN);
                // Show a green mark for correct answer
                scoreLabel.setText("Score: " + modele.getScore() + "/" + modele.getNbrQuestions() + " ✓");
            } else {
                // Wrong answer
                // Change background color to red for 2 seconds
                mainPanel.setBackground(Color.RED);
                // Show a red mark for wrong answer
                scoreLabel.setText("Score: " + modele.getScore() + "/" + modele.getNbrQuestions() + " ✗");
            }

            modele.setScore(score);
            questionTimer.stop(); // Stop the timer
            clearSelection();
            displayQuestion();
            updateLabels();
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Veuillez sélectionner une réponse.");
        }
    }

    private void resetBackgroundColor() {
        mainPanel.setBackground(null); // Reset to default background color
        scoreLabel.setText("Score: " + modele.getScore() + "/" + modele.getNbrQuestions());
    }

    private boolean checkCorrectAnswer(String selectedAnswer) {
        try (BufferedReader correctAnswerReader = new BufferedReader(new FileReader("C:/dev/quizProj/src/CorrectAnswers.txt"))) {
            for (int i = 0; i < currentQuestion - 1; i++) {
                correctAnswerReader.readLine();
            }

            String correctAnswer = correctAnswerReader.readLine();
            return correctAnswer != null && correctAnswer.equalsIgnoreCase(selectedAnswer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clearSelection() {
        buttonGroup.clearSelection();
    }

    private void endGame() {
        questionTimer.stop(); // Stop the timer
        JOptionPane.showMessageDialog(mainPanel, "Fin du jeu. Votre score est : " + modele.getScore());
        jouerButton.setEnabled(true);
        validerLaReponseButton.setEnabled(false);
        currentQuestion = 0;
        score = 0;
        modele.setScore(0);
        updateLabels();
    }

    private void updateLabels() {
        scoreLabel.setText("Score: " + modele.getScore() + "/" + modele.getNbrQuestions());
        nbrQuestionsLabel.setText("Questions: " + currentQuestion + "/" + modele.getNbrQuestions());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("QuizzMain");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new QuizzMain().mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
