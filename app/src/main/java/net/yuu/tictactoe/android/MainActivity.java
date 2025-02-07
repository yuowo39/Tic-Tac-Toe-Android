package net.yuu.tictactoe.android;

import android.app.AlertDialog;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private char[] gameBoard;
    private char currentPlayer;

    private TextView tvGameInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvGameInfo = findViewById(R.id.tv_game_info);

        initGame();
    }

    private void initGame() {
        // Init
        gameBoard = new char[9];
        Arrays.fill(gameBoard, '?');
        currentPlayer = 'X';
        tvGameInfo.setText(R.string.x_turn);

        // Clear the cells
        GridLayout glGameBoard = findViewById(R.id.gl_game_board);
        ArrayList<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < glGameBoard.getChildCount(); i++) {
            View child = glGameBoard.getChildAt(i);
            if (child instanceof ImageView) {
                imageViews.add((ImageView) child);
            }
        }
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setImageDrawable(null);
        }
    }

    public void OnCellClicked(android.view.View view) {
        int index = Integer.parseInt(view.getTag().toString());
        if (gameBoard[index] == '?') {
            gameBoard[index] = currentPlayer;
            drawCell((ImageView)view);
        } else {
            Toast.makeText(this, R.string.cell_already_taken, Toast.LENGTH_SHORT).show();
        }
    }

    private void drawCell(ImageView imageView) {
        int w = imageView.getWidth();
        int h = imageView.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(currentPlayer == 'X' ? Color.RED : Color.BLUE);
        paint.setStrokeWidth((float) Math.min(w, h) / 10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        int centerX = w / 2;
        int centerY = h / 2;
        int size = Math.min(w, h) / 3;

        if (currentPlayer == 'X') {
            canvas.drawLine(centerX - size, centerY - size, centerX + size, centerY + size, paint);
            canvas.drawLine(centerX + size, centerY - size, centerX - size, centerY + size, paint);
        } else {
            canvas.drawCircle(centerX, centerY, size, paint);
        }

        imageView.setImageBitmap(bitmap);
    }

    public void OnResetMatchButtonClicked(android.view.View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.reset_match);

        builder.setMessage(R.string.reset_match_question);

        builder.setPositiveButton(R.string.question_yes, (dialog, which) -> resetMatch());
        builder.setNegativeButton(R.string.question_no, (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);

        builder.show();
    }

    private void resetMatch() {
        initGame();
        Toast.makeText(this, R.string.reset_match_successfully, Toast.LENGTH_SHORT).show();
    }
}