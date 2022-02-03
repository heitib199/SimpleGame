package at.htlkaindorf.simpleballgame;

import android.util.ArrayMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class SimpleLevelReader {
    // attribute
    private static ArrayMap<Integer,ArrayList<Obstacle>> levels;
    // methods
    public static ArrayList<Obstacle> getLevel(int levelId){
        return levels.get(levelId);
    }
    public static int getNextLevelId(int levelId){
        int index = levels.indexOfKey(levelId);
        return levels.keyAt(index+1>=levels.size()?0:index+1);
    }
    public static void readFile(PaintArea paintArea, InputStream inputStream){
        levels = new ArrayMap<>();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine())!=null){
                System.out.println("#"+line);
                line = line.replaceAll(" ","");
                String[] splittedLine = line.split(";");
                int id = Integer.parseInt(splittedLine[0]);
                ArrayList<Obstacle> obstacles = new ArrayList<>();
                for(int i = 1;i<splittedLine.length;++i){
                    System.out.println("    #"+splittedLine[i]);
                    String[] splittedObstacleLine = splittedLine[i].split(",");
                    Obstacle obstacle = new Obstacle(paintArea,Integer.parseInt(splittedObstacleLine[0]),
                            Integer.parseInt(splittedObstacleLine[1]), Integer.parseInt(splittedObstacleLine[2]),
                            Integer.parseInt(splittedObstacleLine[3]),i==1?true:false);
                    obstacles.add(obstacle);
                }
                levels.put(id,obstacles);
            }

        } catch(IOException ex){
            System.out.println("IOException in SimpleLevelReader");
        }
    }
}
