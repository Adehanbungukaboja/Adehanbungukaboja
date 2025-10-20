import java.io.*;
import java.util.*;

public class Main {
    static class Star{
        int x;
        int y;
        Star(int x, int y){
            this.x=x;
            this.y=y;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] st = br.readLine().split(" ");
        int maxCol = Integer.parseInt(st[0]);
        int maxRow = Integer.parseInt(st[1]);
        int tLength = Integer.parseInt(st[2]);
        int starCount = Integer.parseInt(st[3]);
        List<Star>list = new ArrayList<>();
        for(int i=0;i<starCount;i++){
            st = br.readLine().split(" ");
            int x = Integer.parseInt(st[0]);
            int y = Integer.parseInt(st[1]);
            list.add(new Star(x,y));
        }
        int startX;
        int startY;
        int endX;
        int endY;
        int min = starCount;
        int count;
        int minX;
        int minY;
        int maxX;
        int maxY;
        for(int i=0;i<starCount;i++){
            for(int j=0;j<starCount;j++){
                startX = list.get(i).x;
                startY = list.get(j).y;
                count=starCount;
                endX = startX+tLength;
                endY = startY+tLength;
                minX = Math.min(startX,endX);
                minY = Math.min(startY,endY);
                maxX = Math.max(startX,endX);
                maxY = Math.max(startY,endY);
                for(Star s : list){
                    if(s.x>=minX&&s.x<=maxX&&s.y>=minY&&s.y<=maxY){
                        count--;
                    }
                }
                min = Math.min(min,count);
            }
        }
        System.out.println(min);
    }
}