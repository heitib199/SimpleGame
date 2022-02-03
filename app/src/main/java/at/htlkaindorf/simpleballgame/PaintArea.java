package at.htlkaindorf.simpleballgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class PaintArea extends SurfaceView implements SurfaceHolder.Callback {

    private SimpleGameThread thread;
    private static final String TAG = "PaintArea";
    private ArrayList<SimpleGameObject> balls;
    private ArrayList<Obstacle> obstacles;
    private Obstacle obstacle;
    private boolean mousedown = false;
    private float startX = 0;
    private float startY = 0;
    int actualLevel = 1;

    public PaintArea(Context context) {
        super(context);
        SimpleLevelReader.readFile(this,getResources().openRawResource(R.raw.levels));
        getHolder().addCallback(this);
    }

    public void update(){
        for(SimpleGameObject ball : balls){
            ball.setColor(Color.CYAN);
        }
        for(int i = 0; i < balls.size() - 1; ++i){
            SimpleGameObject ball = balls.get(i);
            for (int j = i + 1; j < balls.size(); ++j){
                SimpleGameObject other = balls.get(j);
                ball.coll(other);
            }
        }
        for(SimpleGameObject ball : balls){
            ball.update(obstacles);
        }
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        for(Obstacle obst : obstacles){
            obst.draw(canvas);
        }
        for(SimpleGameObject ball : balls){
            ball.draw(canvas);
        }
    }
    public void nextLevel(){
        obstacles = SimpleLevelReader.getLevel(actualLevel);
        balls.clear();
        actualLevel = SimpleLevelReader.getNextLevelId(actualLevel);
    }
    public ArrayList<SimpleGameObject> getBalls() {
        return balls;
    }

    public void setBalls(ArrayList<SimpleGameObject> balls) {
        this.balls = balls;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated");
        balls = new ArrayList<>();
        nextLevel();
        thread = new SimpleGameThread(this, surfaceHolder);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        try {
            thread.setRunning(false);
            thread.join();
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if(event.getY() > getHeight() - 200) {
                startX = event.getX();
                startY = event.getY();
                mousedown = true;
            }
        }

        if((event.getAction() == MotionEvent.ACTION_UP) && mousedown){
            float x = event.getX();
            float y = event.getY();
            SimpleVector speed = new SimpleVector((x-startX)*0.1, (y-startY)*0.1);
            SimpleGameObject ball1 = new SimpleGameObject(this, startX, startY);
            ball1.setSpeed(speed);
            balls.add(ball1);
            mousedown = false;
        }

        return true;
    }
}