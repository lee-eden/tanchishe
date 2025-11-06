package com.example.tanchishe;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private SnakeGame snakeGame;
    private SnakeGameView gameView;
    private Handler handler = new Handler();
    private Runnable gameRunnable;
    private Button btnPause, btnRestart;
    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        snakeGame = new SnakeGame(20, 20);
        gameView = findViewById(R.id.snakeGameView);
        btnPause = findViewById(R.id.btnPause);
        btnRestart = findViewById(R.id.btnRestart);
        scoreView = findViewById(R.id.scoreView);

        gameView.setSnakeGame(snakeGame);

        gameRunnable = new Runnable() {
            @Override
            public void run() {
                if (!snakeGame.isGameOver() && !snakeGame.isPaused()) {
                    snakeGame.move();
                    scoreView.setText("分数: " + snakeGame.getScore());
                    gameView.invalidate();
                    handler.postDelayed(this, 250);
                }
            }
        };
        handler.post(gameRunnable);

        btnPause.setOnClickListener(v -> {
            snakeGame.togglePause();
            btnPause.setText(snakeGame.isPaused() ? "继续" : "暂停");
            if (!snakeGame.isPaused()) {
                handler.post(gameRunnable);
            } else {
                handler.removeCallbacks(gameRunnable);
            }
        });

        btnRestart.setOnClickListener(v -> {
            snakeGame.reset();
            btnPause.setText("暂停");
            scoreView.setText("分数: " + snakeGame.getScore());
            gameView.invalidate();
            handler.post(gameRunnable);
        });

        gameView.setOnSwipeListener(direction -> {
            snakeGame.changeDirection(direction);
        });
    }
}