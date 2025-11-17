import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{
    static Queue<Integer>iceQueue = new ArrayDeque<>();
    static Queue<Integer>swan = new ArrayDeque<>();
    static Queue<Integer> nextQueue;
    static boolean[][] visited;
    static char[][] map;
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static int maxRow;
    static int maxCol;
    static boolean[][] visitedIce;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] st = br.readLine().split(" ");
        maxRow = Integer.parseInt(st[0]);
        maxCol = Integer.parseInt(st[1]);
        map = new char[maxRow][maxCol];
        visited = new boolean[maxRow][maxCol];
        visitedIce = new boolean[maxRow][maxCol];
        String s;
        int row;
        int col;
        for(int i=0;i<maxRow;i++){
            s = br.readLine();
            for(int j=0;j<maxCol;j++){
                map[i][j]=s.charAt(j);
                if(map[i][j]=='L'&&swan.size()==0){
                    map[i][j]='.';
                    swan.add(i*maxCol+j);
                    visited[i][j]=true;
                }
            }
        }
        for(int i=0;i<maxRow;i++){
            for(int j=0;j<maxCol;j++){
                if(map[i][j]=='X'){
                    for(int d=0;d<4;d++){
                        row = i+dx[d];
                        col = j+dy[d];
                        if(valid(row,col)){
                            if(map[row][col]=='.'||map[row][col]=='L'){
                                iceQueue.add(i*maxCol+j);
                                break;
                            }
                        }
                    }
                }
            }
        }

        int count=0;
        while(true){
            boolean canMeet = move();
            if(canMeet){
                System.out.println(count);
                return;
            }
            meltIce();
            count++;
            // System.out.println("===========================");
            // for(int i=0;i<maxRow;i++){
            //     for(int j=0;j<maxCol;j++){
            //         System.out.print(map[i][j]+" ");
            //     }
            //     System.out.println();
            // }
        }
    }
    static boolean move(){
        nextQueue = new ArrayDeque<>();
        int cur;
        int row;
        int col;
        int curRow;
        int curCol;
        while(!swan.isEmpty()){
            cur = swan.poll();
            row = cur/maxCol;
            col = cur%maxCol;
            for(int d=0;d<4;d++){
                curRow=row+dx[d];
                curCol=col+dy[d];
                if(valid(curRow,curCol)){
                    visited[curRow][curCol]=true;
                    if(map[curRow][curCol]=='L'){
                        return true;
                    }
                    else if(map[curRow][curCol]=='X'){
                        nextQueue.add(curRow*maxCol+curCol);
                    }
                    else{
                        swan.add(curRow*maxCol+curCol);
                    }
                }
            }
        }
        swan.addAll(nextQueue);
        return false;
    }
    static void meltIce(){
        nextQueue = new ArrayDeque<>();
        int cur;
        int row;
        int col;
        int curRow;
        int curCol;
        while(!iceQueue.isEmpty()){
            cur = iceQueue.poll();
            row = cur/maxCol;
            col = cur%maxCol;
            map[row][col]='.';
            for(int d=0;d<4;d++){
                curRow = row+dx[d];
                curCol = col+dy[d];
                if(valid(curRow,curCol)){
                    if(map[curRow][curCol]=='X'&&!visitedIce[curRow][curCol]){
                        visitedIce[curRow][curCol]=true;
                        nextQueue.add(curRow*maxCol+curCol);
                    }
                }
            }
        }
        iceQueue.addAll(nextQueue);
    }
    static boolean valid(int row, int col){
        return row>=0&&row<maxRow&&col>=0&&col<maxCol&&!visited[row][col];
    }
}