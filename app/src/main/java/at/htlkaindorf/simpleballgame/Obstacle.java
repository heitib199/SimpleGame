package at.htlkaindorf.simpleballgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;

import java.util.ArrayList;

public class Obstacle {
    private int x;
    private int y;
    private int width = 0;
    private int height = 0;
    private Paint paint;
    private Rect rect;
    private boolean isTarget;
    private PaintArea paintArea;

    public Obstacle(PaintArea paintArea, int x, int y,int width, int height, boolean isTarget) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.paint = new Paint();
        this.isTarget = isTarget;
        this.paintArea = paintArea;
        if(isTarget){
            paint.setColor(Color.RED);
        }else {
            paint.setColor(Color.YELLOW);
        }
        rect = new Rect(x, y, x+width, y+height);
    }

    public void collision(SimpleGameObject gameObject) {
        double objX = gameObject.getLocation().getX();
        double objY = gameObject.getLocation().getY();
        double radius = gameObject.getRadius();
        SimpleVector ballVec = gameObject.getSpeed();
        SimpleVector ballLoc = gameObject.getLocation();

        boolean isColl = false;

        if(objX + radius >= x && objX - radius <= x + width && objY + radius >= y && objY - radius <= y + height){
            isColl = true;
            if(objX < x && objX + radius > 0){
                ballVec.multi(gameObject.getDampingRate());
                ballVec.setX(-ballVec.getX());
                ballLoc.setX(x - radius);
                gameObject.setSpeed(ballVec);
                gameObject.setLocation(ballLoc);
            }else if(objX - radius < x + width && objX > x + width) {
                ballVec.multi(gameObject.getDampingRate());
                ballVec.setX(-ballVec.getX());
                ballLoc.setX(x + width + radius);
                gameObject.setLocation(ballLoc);
                gameObject.setSpeed(ballVec);
            }else if(objY + radius >= y) {
                ballVec.multi(gameObject.getDampingRate());
                ballVec.setY(-ballVec.getY());
                gameObject.setSpeed(ballVec);
                if(objY < y){
                    ballLoc.setY(y - radius);
                }else{
                    ballLoc.setY(y + height + radius);
                }
                gameObject.setLocation(ballLoc);
            }
        }

        if(isColl && isTarget){
            ArrayList<SimpleGameObject> list = paintArea.getBalls();
            list.remove(gameObject);
            paintArea.setBalls(list);
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }
}
