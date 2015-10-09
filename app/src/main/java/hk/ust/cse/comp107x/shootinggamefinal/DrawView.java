package hk.ust.cse.comp107x.shootinggamefinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
//import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int sleeptime = 30;

    public static int widthPixels, heightPixels;

    private boolean gameOver_ = false;
    private int width, height;
    private DrawViewThread drawviewthread;

    Context mContext;

    // We can have multiple bullets and explosions
    // keep track of them in ArrayList
    ArrayList<Bullet> bullets;
    ArrayList<Explosion> explosions;
    ArrayList<Lives> lives;
    Cannon cannon;
    AndroidGuy androidGuy;
    Score score;


    public DrawView(Context context, AttributeSet attrs) {

        super(context, attrs);

        mContext = context;


        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
// since SDK_INT = 1;
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
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

        getHolder().addCallback(this);

        setFocusable(true);
        this.requestFocus();

        // create a cannon object
        cannon = new Cannon(Color.BLUE,mContext);

        // create arraylists to keep track of bullets and explosions
        bullets = new ArrayList<Bullet> ();
        explosions = new ArrayList<Explosion>();

        // create the falling Android Guy
        androidGuy = new AndroidGuy(Color.RED, mContext);
        score = new Score(Color.BLACK);

        lives = new ArrayList<Lives>();


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        drawviewthread = new DrawViewThread(holder);
        drawviewthread.setRunning(true);
        drawviewthread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        drawviewthread.setRunning(false);

        while (retry){
            try {
                drawviewthread.join();
                retry = false;
            }
            catch (InterruptedException e){

            }
        }

    }

    public class DrawViewThread extends Thread{
        private SurfaceHolder surfaceHolder;
        public boolean threadIsRunning = true;

        public DrawViewThread(SurfaceHolder holder){
            surfaceHolder = holder;
            setName("DrawViewThread");
        }

        public void setRunning (boolean running){
            threadIsRunning = running;
        }

        public void run() {
            Canvas canvas = null;

            while (threadIsRunning) {

                try {
                    canvas = surfaceHolder.lockCanvas(null);

                    synchronized(surfaceHolder){
                        drawGameBoard(canvas);
                    }
                    sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;


        cannon.setBounds(0,0,width, height);
        androidGuy.setBounds(0, 0, width, height);
        for (int i = 0; i < bullets.size(); i++ ) {
            bullets.get(i).setBounds(0,0,width,height);
        }

    }


    public void drawGameBoard(Canvas canvas) {
        canvas.drawColor(getResources().getColor(R.color.background_surface));
        //if you want another background color
        // Draw the cannon
        cannon.draw(canvas);

        // Draw all the bullets
        for (int i = 0; i < bullets.size(); i++ ) {
            if (bullets.get(i) != null) {
                bullets.get(i).draw(canvas);

                if (bullets.get(i).move() == false) {
                    bullets.remove(i);
                }
            }
        }

        // Draw all the explosions, at those locations where the bullet
        // hits the Android Guy
        for (int i = 0; i < explosions.size(); i++ ) {
            if (explosions.get(i) != null) {
                if (explosions.get(i).draw(canvas) == false) {
                    explosions.remove(i);
                }
            }
        }

        // Handle all the lives. Draw them first, and remove them if Android Guy
        // falls out of bounds.

        Log.i("Drawview", "Livescount:" + lives.size());
        if(lives.size() == 0) {
            Log.i("Drawview", "Empty lives");
            gameOver_ = true;
            Log.i("over", "Game over!");
            gameIsOver();

        }

        for (int i = lives.size()-1; i >= 0; i-- ) {
           // Log.i("Drawview", "lives: " + lives.size());
            if (lives.get(i) != null) {
                lives.get(i).draw(canvas);
                if (androidGuy.guyOutOfBounds) {
                    //lives.get(i).removeLife();
                    if (lives.size() > 0) {
                        lives.remove(i);
                        androidGuy.guyInBounds();
                        Log.i("DrawView", "Life removed." + "lives: " + lives.size());
                    }

                }
            }
        }

        // If the Android Guy is falling, check to see if any of the bullets
        // hit the Guy
        if (androidGuy != null) {
            androidGuy.draw(canvas);

            RectF guyRect = androidGuy.getRect();

            for (int i = 0; i < bullets.size(); i++ ) {

                // The rectangle surrounding the Guy and Bullet intersect, then it's a collision
                // Generate an explosion at that location and delete the Guy and bullet. Generate
                // a new Android Guy to fall from the top.
                if (RectF.intersects(guyRect, bullets.get(i).getRect())) {
                    explosions.add(new Explosion(Color.RED,mContext, androidGuy.getX(), androidGuy.getY()));
                    androidGuy.reset();
                    bullets.remove(i);
                    // Play the explosion sound by calling the SoundEffects class
                    if (!ShootingGame.no_beeps) {
                        SoundEffects.INSTANCE.playSound(SoundEffects.SOUND_EXPLOSION);
                    }
                    score.incrementScore();
                    break;
                }

            }



            if (androidGuy.move() == false) {
                androidGuy = null;
            }
        }
        score.draw(canvas);

    }



    // Move the cannon left or right
    public void moveCannonLeft() {
        cannon.moveLeft();
    }

    public void moveCannonRight() {
        cannon.moveRight();
    }

    // Whenever the user shoots a bullet, create a new bullet moving upwards
    public void shootCannon() {

        bullets.add(new Bullet(Color.RED, mContext, cannon.getPosition(), (height - Cannon.yOffset * 1.5f)));

    }

    public void addLife(float pos_x, float pos_y) {

        lives.add(new Lives(mContext, pos_x, pos_y));
    }

    public void stopGame(){
        if (drawviewthread != null){
            drawviewthread.setRunning(false);
        }
    }

    public void gameIsOver() {
        stopGame();
        Handler handler = new Handler(Looper.getMainLooper());

        Runnable r = new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Your score is: " + score.getScore() + R.string.dialog_message);
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(mContext,"user clicked yes", Toast.LENGTH_LONG).show();
                        ((Activity) getContext()).recreate();

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(mContext,"user clicked no", Toast.LENGTH_LONG).show();
                        ((Activity) getContext()).finish();

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };

        handler.post(r);

    }

    public void resumeGame(){
        if (drawviewthread != null){
            drawviewthread.setRunning(true);
        }
    }

    public void releaseResources(){

    }

}
