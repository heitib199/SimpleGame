package at.htlkaindorf.simpleballgame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class SimpleGameThread extends Thread{

    private boolean isRunning;
    private int duration = 20;
    private PaintArea paintArea;
    private SurfaceHolder surfaceHolder;

    public SimpleGameThread(PaintArea paintArea, SurfaceHolder surfaceHolder){
        this.paintArea = paintArea;
        this.surfaceHolder = surfaceHolder;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while (isRunning){
            Canvas canvas = surfaceHolder.lockCanvas();

            try {
                synchronized (canvas){
                    paintArea.update();
                    paintArea.draw(canvas);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                this.surfaceHolder.unlockCanvasAndPost(canvas);
            }

            long currentTime = System.currentTimeMillis();
            long waitTime = currentTime - startTime;

            if(waitTime < duration) { //FPS
                waitTime = duration;
            }

            try {
                this.sleep(waitTime);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
            System.out.println(String.format("Thread - %d", currentTime));
            startTime = System.currentTimeMillis();
        }
    }
}
