package hk.ust.cse.comp107x.shootinggamefinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class AndroidGuy {
    float x; // Guy's top left corner (x,y)
    float y;
    float wid;
    float hei;
    float stepX = 10; // Guy's step in (x,y) direction
    float stepY = 20; // gives speed of motion, larger means faster speed
    int lowerX, lowerY, upperX, upperY; // boundaries
    public boolean guyOutOfBounds = false;
    private static final String TAG = AndroidGuy.class.getSimpleName();


    Bitmap android_guy;


    private Context mContext;


    // Constructor
    public AndroidGuy(int color, Context c) {

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

        Log.i(TAG, "width: " + widthPixels);
        Log.i(TAG, "height: " + heightPixels);
        Log.i(TAG, "width/: " + (widthPixels/100)*10);
        Log.i(TAG, "height/: " + (heightPixels/100)*10);
        // create a bitmap from the supplied image (the image is the icon that is part of the app)
        android_guy = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.ic_launcher),(widthPixels/100)*10,(heightPixels/100)*5, false);

        wid = widthPixels;
        hei = heightPixels;
    }

    public void setBounds(int lx, int ly, int ux, int uy) {
        lowerX = lx;
        lowerY = ly;
        upperX = ux;
        upperY = uy;

        x = (float) ((upperX-50)*Math.random());
        y = 0;
    }

    public boolean move() {
        // Get new (x,y) position. Movement is always in vertical direction downwards
        y += stepY;
        // Detect when the guy reaches the bottom of the screen
        // restart at a random location at the top of the screen
        if (y + 50 > upperY) {
            guyOutOfBounds = true;
            reset();
            // Make the sound corresponding to the Android Guy falling down the bottom of screen
            SoundEffects.INSTANCE.playSound(SoundEffects.SOUND_GUY);
            return true;
        }
        else
            return true;
    }

    public void guyInBounds() {
        guyOutOfBounds = false;
    }


    // When you reset, starts the Android Guy from a random X co-ordinate location
    // at the top of the screen again
    public void reset() {
        x = (float) ((upperX-50)*Math.random());
        y = 0;
    }

    // Returns the rectangle enclosing the Guy. Used for collision detection
    public RectF getRect() {
        return new RectF(x,y,x+50,y+50);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(android_guy, x, y, null);
    }
}
