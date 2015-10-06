package hk.ust.cse.comp107x.shootinggamefinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Mark on 02/09/2015.
 */
public class Lives {
    float x;
    float y;
    public int numberOfLives = 3;
    public boolean gameOver;

    public static Bitmap life;

    private Context mContext;

    public Lives(Context c, float posx, float posy) {

        mContext = c;
        x = posx;
        y = posy;


        life = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.life), 50, 50, false);
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
