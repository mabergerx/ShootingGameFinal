package hk.ust.cse.comp107x.shootinggamefinal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


public class Cannon {

    private static final String TAG = Cannon.class.getSimpleName();

    float x = -1; // Cannon's center (x,y)
    float y = -1;
    float stepX; // Cannon's step in  x direction

    public static int xOffset, yOffset;

    int lowerX, lowerY, upperX, upperY;
    private Paint paint; // The paint style, color used for drawing


    // Constructor
    public Cannon(int color, Context c) {
        paint = new Paint();
        paint.setColor(color);

       Context mContext = c;



        stepX = (DrawView.widthPixels/100)*8;

        xOffset = (DrawView.widthPixels / 6) / 2;
        yOffset = DrawView.heightPixels / 15;

    }

    public void setBounds(int lx, int ly, int ux, int uy) {
        lowerX = lx;
        lowerY = ly;
        upperX = ux;
        upperY = uy;

        if (x < 0 && y < 0) {
            x = ux/2;
            y = uy;
        }
    }

    public void moveLeft() {
        // Get new (x,y) position of the canvas by moving it left
        // when the left button is clicked. Ensure that it does not
        // move off the screen.
        if (x - 30 > 0) {
            x -= stepX;
        }
    }

    public void moveRight() {
        // Get new (x,y) position of the canvas by moving it right
        // when the right button is clicked. Ensure that it does not
        // move off the screen.
        if (x + 30 < upperX) {
            x += stepX;
        }
    }

    public float getPosition() {
        return x;
    }

    // Draw the cannon on the canvas
    public void draw(Canvas canvas) {
        canvas.drawLine(x, y - (yOffset * 1.5f), x, y, paint);
        canvas.drawRect(x - xOffset, y - (yOffset / 4), x + xOffset, y, paint); // onderste blokje
        canvas.drawRect(x - (xOffset / 3), y - yOffset, x + (xOffset / 3), y, paint); // bovenste blokje
    }
}
