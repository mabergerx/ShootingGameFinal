package hk.ust.cse.comp107x.shootinggamefinal;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;


public class Score {

    private Paint paint;
    private int score;
    private int highscore;

    // Constructor
    public Score(int color) {
        paint = new Paint();
        // Set the font face and size of drawing text
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(24);
        paint.setColor(color);

        score = 0;
        highscore =0;
    }

    public void incrementScore() {
        score++;
    }

    public void decrementScore() {
        score--;
    }

    public int getScore() { return score; }

    public int getHighscore() { return highscore; }

    public void draw(Canvas canvas) {

        // draw text on the canvas. Position the text at (10,30).
        canvas.drawText("Score: " + score, 10, 30, paint);
        canvas.drawText("Highscore: " + highscore, 30, 50, paint);

    }
}
