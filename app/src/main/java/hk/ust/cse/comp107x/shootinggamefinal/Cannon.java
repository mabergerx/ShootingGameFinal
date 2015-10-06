package hk.ust.cse.comp107x.shootinggamefinal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


public class Cannon {
    float x = -1; // Cannon's center (x,y)
    float y = -1;
    float stepX; // Cannon's step in  x direction
    int lowerX, lowerY, upperX, upperY;
    private Paint paint; // The paint style, color used for drawing

    private Context mContext;

    // Constructor
    public Cannon(int color, Context c) {
        paint = new Paint();
        paint.setColor(color);

        mContext = c;

        WindowManager w = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
// since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }

        stepX = (widthPixels/100)*8;

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
        canvas.drawLine(x, y - 100, x, y, paint);
        canvas.drawRect(x - 30, y - 10, x + 30, y, paint);
        canvas.drawRect(x - 10, y - 40, x + 10, y, paint);
    }
}
