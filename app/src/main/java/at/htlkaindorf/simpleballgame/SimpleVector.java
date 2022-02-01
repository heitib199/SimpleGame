package at.htlkaindorf.simpleballgame;

public class SimpleVector {
    private double x;
    private double y;

    public SimpleVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(SimpleVector other){
        this.x = this.x + other.getX();
        this.y = this.y + other.getY();
    }

    public void multi(float value){
        this.x *= value;
        this.y *= value;
    }

    public float distanceTo(SimpleVector other){
        float d_x = (float) (this.x - other.getX());
        float d_y = (float) (this.y - other.getY());

        float result = (float) Math.sqrt(d_x*d_x + d_y*d_y);
        return result;
    }

    public static SimpleVector substract(SimpleVector u, SimpleVector v){
        return new SimpleVector(u.getX() - v.getX(), u.getY() - v.getY());
    }

    public float dotProduct(SimpleVector u){
        return (float) (this.x * u.getX() + this.getY() * u.getY());
    }

    public void normalize(float value){
        this.x /= value;
        this.y /= value;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
