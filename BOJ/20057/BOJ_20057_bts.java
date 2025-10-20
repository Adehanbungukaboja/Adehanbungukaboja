import java.io.*;
import java.util.*;

public class Main {
    static int size;
    static int[][] map;
    static int[] dx = {0,1,0,-1};
    static int[] dy = {-1,0,1,0};
    static int[][] sandX = {{1,-1,2,-2,1,-1,1,-1,0,0},{-1,-1,0,0,0,0,1,1,2,1},{1,-1,2,-2,1,-1,1,-1,0,0},{1,1,0,0,0,0,-1,-1,-2,-1}};
    static int[][] sandY = {{1,1,0,0,0,0,-1,-1,-2,-1},{1,-1,2,-2,1,-1,1,-1,0,0},{-1,-1,0,0,0,0,1,1,2,1},{1,-1,2,-2,1,-1,1,-1,0,0}};
    static double[] ratio = {0.01,0.01,0.02,0.02,0.07,0.07,0.1,0.1,0.05};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] st;
        size = Integer.parseInt(br.readLine());
        map = new int[size][size];
        for(int i=0;i<size;i++){
            st = br.readLine().split(" ");
            for(int j=0;j<size;j++){
                map[i][j]=Integer.parseInt(st[j]);
            }
        }
        int row = size/2;
        int col = size/2;
        int dir=0;
        int moveSize=1;
        int sum=0;
        int sandRow;
        int sandCol;
        int restSandRow;
        int restSandCol;
        out:
        while(true){
            for(int i=0;i<moveSize;i++){
                row+=dx[dir];
                col+=dy[dir];
                int sand = map[row][col];
                int restSand = sand;
                map[row][col]=0;
                for(int d=0;d<9;d++){
                    sandRow = row+sandX[dir][d];
                    sandCol = col+sandY[dir][d];
                    // System.out.println(sandRow+" "+sandCol);
                    int goneSand = (int)(sand*ratio[d]);
                    // System.out.println(sand+" "+ratio[d]+" "+goneSand);
                    if(valid(sandRow,sandCol)){
                        map[sandRow][sandCol]+=goneSand;
                    }
                    else{
                        sum+=goneSand;
                    }
                    restSand-=goneSand;
                }
                restSandRow=row+sandX[dir][9];
                restSandCol=col+sandY[dir][9];
                if(valid(restSandRow,restSandCol)){
                    map[restSandRow][restSandCol]+=restSand;
                }
                else{
                    sum+=restSand;
                }
                
                if(row==0&&col==0){
                    break out;
                }
            }
            if(dir==0){
                dir++;
            }
            else if(dir==1){
                dir++;
                moveSize++;
            }
            else if(dir==2){
                dir++;
            }
            else{
                dir=0;
                moveSize++;
            }
        }
        System.out.println(sum);
    }
    static boolean valid(int row, int col){
        return row>=0&&row<size&&col>=0&&col<size;
    }
}
