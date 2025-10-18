import java.util.*;
import java.io.*;

public class Main {
    static int[][][] boomerang;
    static int[][] dx = {{0,1},{0,-1},{0,-1},{0,1}};
    static int[][] dy = {{-1,0},{-1,0},{1,0},{1,0}};
    static int maxRow;
    static int maxCol;
    static int[][] map;
    static int max;
    public static void main(String[] args) throws IOException {
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        String[] st = br.readLine().split(" ");
        maxRow = Integer.parseInt(st[0]);
        maxCol = Integer.parseInt(st[1]);
        map = new int[maxRow][maxCol];
        for(int i=0;i<maxRow;i++){
            st=br.readLine().split(" ");
            for(int j=0;j<maxCol;j++){
                map[i][j]=Integer.parseInt(st[j]);
            }
        }
        if(maxRow==1||maxCol==1){
            System.out.println("0");
            return;
        }
        boomerang = new int[maxRow][maxCol][4];
        for(int i=0;i<maxRow;i++){
            for(int j=0;j<maxCol;j++){
                makeBoomerang(i,j);
            }
        }
        
        boolean[]used=new boolean[maxRow*maxCol];
        dfs(0,used,0);
        System.out.println(max);
    }
    static void dfs(int idx, boolean[] used,int sum){
        if(idx==maxRow*maxCol){
            max=Math.max(max,sum);
            return;
        }
        if(used[idx]){
            dfs(idx+1,used,sum);
            return;
        }
        int row = idx/maxCol;
        int col = idx%maxCol;
        for(int i=0;i<4;i++){
            if(boomerang[row][col][i]!=0){
                boolean check = true;
                for(int d=0;d<2;d++){
                    int curRow = row+dx[i][d];
                    int curCol = col+dy[i][d];
                    if(used[curRow*maxCol+curCol]){
                        check=false;
                        break;
                    }
                }
                if(check){
                    for(int d=0;d<2;d++){
                        int curRow = row+dx[i][d];
                        int curCol = col+dy[i][d];
                        used[curRow*maxCol+curCol]=true;
                    }
                    used[idx]=true;
                    dfs(idx+1,used,sum+boomerang[row][col][i]);
                    for(int d=0;d<2;d++){
                        int curRow = row+dx[i][d];
                        int curCol = col+dy[i][d];
                        used[curRow*maxCol+curCol]=false;
                    }
                    used[idx]=false;
                }
            }
        }
        dfs(idx+1,used,sum);
    }
    static void makeBoomerang(int row, int col){
        int core = map[row][col]*2;
        for(int i=0;i<4;i++){
            int sum=core;
            boolean canMake=true;
            for(int d=0;d<2;d++){
                int curRow = row+dx[i][d];
                int curCol = col+dy[i][d];
                if(!valid(curRow,curCol)){
                    canMake=false;
                    break;
                }
                sum+=map[curRow][curCol];
            }
            if(canMake){
                boomerang[row][col][i]=sum;
            }
        }
    }
    static boolean valid(int row, int col){
        return row>=0&&row<maxRow&&col>=0&&col<maxCol;
    }
}