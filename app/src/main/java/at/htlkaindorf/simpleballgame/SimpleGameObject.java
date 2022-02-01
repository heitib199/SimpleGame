package at.htlkaindorf.simpleballgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceView;

import java.util.ArrayList;

public class SimpleGameObject {
    private SimpleVector acceleration;
    private SimpleVector location;
    private SimpleVector speed;
    private SurfaceView paintArea;
    private int color;
    private int radius = 25;
    private float dampingRate = 0.9f;

    public SimpleGameObject(SurfaceView paintArea, float x, float y){
        //this.speed = new SimpleVector(10, 10);
        this.speed = speed;
        this.acceleration = new SimpleVector(0 ,0.2);
        //this.location = new SimpleVector(x, y);   SC
        this.location = new SimpleVector(x, y);
        this.paintArea = paintArea;
    }

    public void update(ArrayList<Obstacle> obstacles){
        if(location.getX() + radius > paintArea.getWidth()){
            speed.multi(dampingRate);
            speed.setX(-speed.getX());
            location.setX(paintArea.getWidth() - radius);
        }
        if(location.getX() - radius < 0){
            speed.multi(dampingRate);
            speed.setX(-speed.getX());
            location.setX(radius);
        }
        if(location.getY() - radius < 0){
            speed.multi(dampingRate);
            location.setY(radius);
            speed.setY(-speed.getY());
        }
        if (location.getY() + radius > paintArea.getHeight()){
            speed.multi(dampingRate);
            speed.setY(-speed.getY());
            location.setY(paintArea.getHeight() - radius);
        }
        speed.add(acceleration);
        location.add(speed);

        for (Obstacle obst : obstacles) {
            obst.collision(this);
        }
    }

    public SimpleVector getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }

    public void setLocation(SimpleVector location) {
        this.location = location;
    }

    public SimpleVector getSpeed() {
        return speed;
    }


    public void setSpeed(SimpleVector speed) {
        this.speed = speed;
    }

    public float getDampingRate() {
        return dampingRate;
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle((int)location.getX(), (int)location.getY(), radius, paint);
    }

    public void coll(SimpleGameObject other){
        float distance = this.getLocation().distanceTo(other.getLocation());
        if(this.getRadius() + other.getRadius() > distance){
            this.setColor(Color.RED);
            other.setColor(Color.RED);

            SimpleVector vColl = SimpleVector.substract(this.getLocation(), other.getLocation());
            vColl.normalize(distance);
            SimpleVector speedResult = SimpleVector.substract(this.getSpeed(), other.getSpeed());
            float speed = speedResult.dotProduct(vColl);
            if(speed < 0){
                this.getSpeed().setX(this.getSpeed().getX() - speed * vColl.getX());
                this.getSpeed().setY(this.getSpeed().getY() - speed * vColl.getY());
                this.getSpeed().multi(dampingRate);
                other.getSpeed().setX(other.getSpeed().getX() + speed * vColl.getX());
                other.getSpeed().setY(other.getSpeed().getY() + speed * vColl.getY());
                other.getSpeed().multi(dampingRate);
            }
        }
    }

    public void setColor(int color) {
        this.color = color;
    }
}
