package hk.ust.cse.comp107x.shootinggamefinal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;


public class Score {

    private Paint paint;
    private int score;
    public static int highscore;
    private static final String TAG = Score.class.getSimpleName();

    // Constructor
    public Score(int color) {
        paint = new Paint();
        // Set the font face and size of drawing text
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(50);
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

    public int newHighscore(int score) {
        if (score > highscore) {
            highscore = score;
        } else {
            return highscore;
        }
        return highscore;
    }


    public void draw(Canvas canvas) {

        // draw text on the canvas. Position the text at (10,30).
        canvas.drawText("Score: " + score, (DrawView.widthPixels)/100, ((DrawView.widthPixels)/100)*3, paint);
        canvas.drawText("Highscore: " + ShootingGame.global_highscore, (DrawView.widthPixels)/100, (DrawView.widthPixels)/100*6, paint);

    }
}
