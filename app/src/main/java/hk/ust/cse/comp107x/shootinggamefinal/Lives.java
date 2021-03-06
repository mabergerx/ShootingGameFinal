package hk.ust.cse.comp107x.shootinggamefinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Mark on 02/09/2015.
 */
public class Lives {

    private static final String TAG = Lives.class.getSimpleName();

    public static int lifeWidth;

    float x;
    float y;

    public static Bitmap life;

    Context mContext;

    public Lives(Context c, float posx, float posy) {

        mContext = c;
        x = posx;
        y = posy;


        life = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.life), (int) (DrawView.widthPixels * 0.05), (int) (DrawView.heightPixels * 0.025), false);

        lifeWidth = life.getWidth();

        Log.i(TAG, "life width: " + lifeWidth);
    }

  /*  public boolean removeLife() {

        if (numberOfLives != 0) {
            numberOfLives--;
            return true;
        } else {
            gameOver = true;
            return false;
        }

    }*/


    public void draw(Canvas canvas) {

        canvas.drawBitmap(life, x, y, null);

    }

}
