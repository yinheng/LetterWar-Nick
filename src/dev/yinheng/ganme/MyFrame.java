package dev.yinheng.ganme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created @2017/6/11 16:27
 */
public class MyFrame {

    private LetterCreate letterCreate = new LetterCreate();
    private JTextPane jTextPaneTips = new JTextPane();
    private JButton startBtn = new JButton("start");
    private JTextPane jTextPane = new JTextPane();
    private JTextPane scoreText = new JTextPane();

    private boolean inputOK = false;

    private JFrame jFrame = new JFrame("Letter War");

    private int score = 0;

    public void show() {

        jFrame.setSize(800, 600);
        jFrame.setResizable(true);

        jTextPane.setSize(500, 500);
        jTextPane.setEditable(false);
        jTextPane.setFont(new Font("serif", 0, 180));

        jTextPaneTips.setSize(500, 500);
        jTextPaneTips.setEditable(false);
        jTextPaneTips.setFont(new Font("serif", 0, 90));
        jTextPaneTips.setText("---");

        scoreText.setSize(500, 500);
        scoreText.setEditable(false);
        scoreText.setFont(new Font("serif", 0, 45));
        scoreText.setText("0");


        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startCheck();
                    }
                }).start();
            }
        });


        jFrame.add(jTextPane, BorderLayout.CENTER);
        jFrame.add(jTextPaneTips, BorderLayout.EAST);
        jFrame.add(startBtn, BorderLayout.SOUTH);
        jFrame.add(scoreText, BorderLayout.NORTH);

        jFrame.setVisible(true);
    }

    public void startCheck() {
        jTextPane.setText(letterCreate.letterCreate().name().toUpperCase());

        boolean userInput = waitForInput(jTextPane.getText());

        if (userInput) {
            score++;
            scoreText.setText(String.format("Score: %d", score));
            jTextPane.setBackground(Color.BLUE);

        } else {
            score--;
            scoreText.setText(String.format("Score: %d", score));
            jTextPane.setBackground(Color.RED);
        }

        startCheck();
    }

    public boolean waitForInput(String key) {

        inputOK = false;

        CountDownLatch latch = new CountDownLatch(1);

        KeyAdapter adapter = new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                MyLogger.log("keyReleased----" + e.getKeyChar());

                jTextPaneTips.setText(String.valueOf(e.getKeyChar()));

                String kc = String.valueOf(e.getKeyChar());

                if (kc.equalsIgnoreCase(key)) {
                    inputOK = true;
                }

                latch.countDown();

                startBtn.removeKeyListener(this);
            }
        };

        startBtn.addKeyListener(adapter);

        try {
            return latch.await(1000, TimeUnit.MILLISECONDS) && inputOK;
        } catch (InterruptedException e) {
            return false;
        }
    }
}
