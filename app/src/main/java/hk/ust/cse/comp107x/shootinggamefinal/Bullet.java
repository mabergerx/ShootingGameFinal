package hk.ust.cse.comp107x.shootinggamefinal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class Bullet {
    float radius; // Bullet's radius
    float x; // Bullet's center (x,y)
    float y;
    float stepX = 10; // Bullet's step of motion in (x,y) direction
    float stepY; // gives speed of motion, larger means faster speed
    int lowerX, lowerY, upperX, upperY;
    private Paint paint; // The paint style, color used for drawing

    private Context mContext;

    // Constructor
    public Bullet(int color, Context c, float startx, float starty) {
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

        x = startx;
        y = starty;

        radius = (widthPixels/100);

        stepY = (heightPixels/100)*2;
    }

    public void setBounds(int lx, int ly, int ux, int uy) {
        lowerX = lx;
        lowerY = ly;
        upperX = ux;
        upperY = uy;
    }

    // Rectangle enclosing the bullet. Used for collision detection with Guy
    public RectF getRect() {
        return new RectF(x-radius,y-radius,x+radius,y+radius);
    }

    // Move the bullet upwards by stepY every time. This creates the upward motion.
    public boolean move() {
        // Get new (x,y) position
        y -= stepY;
        // Detect when the bullet reaches the top of the screen
        // then remove the bullet
        if (y - radius < 0) {
            // Make the sound corresponding to the bullet leaving from top of screen
            SoundEffects.INSTANCE.playSound(SoundEffects.SOUND_BULLET);
            return false;
        }
        else
            return true;
    }

    // draw the bullet on the canvas
    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }
}
