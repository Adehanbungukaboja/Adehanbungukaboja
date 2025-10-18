import java.io.*;
import java.util.*;

public class Main {
    static int maxRow;
    static int maxCol;
    static int limit;
    static int[][] map;
    static class Heater{
        int row;
        int col;
        int dir;//동 서 북 남
        Heater(int row, int col, int dir){
            this.row=row;
            this.col=col;
            this.dir=dir;
        }
    }
    static class NeedCheck{
        int row;
        int col;
        NeedCheck(int row, int col){
            this.row=row;
            this.col=col;
        }
    }
    static int[] dx = {0,0,-1,1};
    static int[] dy = {1,-1,0,0};
    static boolean[][] heated;
    static int[][] tempMap;
    static List<NeedCheck> checkList;
    static int[][] walls;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] st = br.readLine().split(" ");
        maxRow = Integer.parseInt(st[0]);
        maxCol = Integer.parseInt(st[1]);
        limit = Integer.parseInt(st[2]);
        map = new int[maxRow][maxCol];
        tempMap = new int[maxRow][maxCol];
        int cur;
        List<Heater>heaterList = new ArrayList<>();
        checkList = new ArrayList<>();
        for(int i=0;i<maxRow;i++){
            st = br.readLine().split(" ");
            for(int j=0;j<maxCol;j++){
                cur = st[j].charAt(0)-'0';
                if(cur>0&&cur<5){
                    heaterList.add(new Heater(i, j, cur-1));
                }
                if(cur==5){
                    checkList.add(new NeedCheck(i, j));
                }
            }
        }
        int wallCount = Integer.parseInt(br.readLine());
        walls = new int[maxRow][maxCol]; // 인덱스 벗어나는거 나중에처리하니까 1더줌
        for(int i=0;i<wallCount;i++){
            st = br.readLine().split(" ");
            int row = Integer.parseInt(st[0])-1;
            int col = Integer.parseInt(st[1])-1;
            int dir = Integer.parseInt(st[2]);
            if(dir==0){
                walls[row][col]|=1<<2;
                walls[row-1][col]|=1<<3;
            }
            else{
                walls[row][col]|=1<<0;
                walls[row][col+1]|=1<<1;
            }
        }
        int count=0;
        while(true){
            for(Heater h : heaterList){
                heaterOn(h);
            }
            
            // System.out.println("온풍기==================="+count);
            // for(int i=0;i<maxRow;i++){
            //     for(int j=0;j<maxCol;j++){
            //         System.out.print(map[i][j]+" ");
            //     }
            //     System.out.println();
            // }
            tempMap = new int[maxRow][maxCol];
            tempCalc(0,0);
            
            // System.out.println("바뀔 온도==================="+count);
            
            // for(int i=0;i<maxRow;i++){
            //     for(int j=0;j<maxCol;j++){
            //         System.out.print(tempMap[i][j]+" ");
            //     }
            //     System.out.println();
            // }
            tempSum();
            
            // System.out.println("온도교환==================="+count);
            // for(int i=0;i<maxRow;i++){
            //     for(int j=0;j<maxCol;j++){
            //         System.out.print(map[i][j]+" ");
            //     }
            //     System.out.println();
            // }
            tempMinus();
            
            // System.out.println("1빼기==================="+count);
            // for(int i=0;i<maxRow;i++){
            //     for(int j=0;j<maxCol;j++){
            //         System.out.print(map[i][j]+" ");
            //     }
            //     System.out.println();
            // }
            count++;
            if(count>100){
                count=101;
                break; 
            }
            if(tempCheck()){
                break;
            }
        }
        System.out.println(count);
    }
    static void tempMinus(){
        if(map[0][0]>0) map[0][0]++;
        if(map[0][maxCol-1]>0) map[0][maxCol-1]++;
        if(map[maxRow-1][0]>0) map[maxRow-1][0]++;
        if(map[maxRow-1][maxCol-1]>0) map[maxRow-1][maxCol-1]++;
        for(int i=0;i<maxRow;i++){
            if(map[i][0]>0){
                map[i][0]--;
            }
            if(map[i][maxCol-1]>0){
                map[i][maxCol-1]--;
            }
        }
        for(int i=0;i<maxCol;i++){
            if(map[0][i]>0){
                map[0][i]--;
            }
            if(map[maxRow-1][i]>0){
                map[maxRow-1][i]--;
            }
        }
    }
    static boolean tempCheck(){
        for(NeedCheck check : checkList){
            int row = check.row;
            int col = check.col;
            if(map[row][col]<limit){
                return false;
            }
        }
        return true;
    }

    static void tempSum(){
        for(int i=0;i<maxRow;i++){
            for(int j=0;j<maxCol;j++){
                map[i][j]+=tempMap[i][j];
            }
        }
    }
    static void tempCalc(int row, int col){
        if(row==maxRow){
            return;
        }
        if(col==maxCol){
            tempCalc(row+1, 0);
            return;
        }
        int bit = walls[row][col];
        if(valid(row,col+1)){
            if((bit&1)==0){ // 오른쪽이 안막힐때
                int curTemp = map[row][col];
                int rightTemp = map[row][col+1];
                int diff = Math.abs(curTemp-rightTemp);
                int divide = diff/4;
                if(curTemp>rightTemp){
                    tempMap[row][col]-=divide;
                    tempMap[row][col+1]+=divide;
                }
                else{
                    tempMap[row][col]+=divide;
                    tempMap[row][col+1]-=divide;
                }
            }
        }
        if(valid(row+1,col)){
            if((bit&8)==0){ //아래가 안막힐때
                int curTemp = map[row][col];
                int rightTemp = map[row+1][col];
                int diff = Math.abs(curTemp-rightTemp);
                int divide = diff/4;
                if(curTemp>rightTemp){
                    tempMap[row][col]-=divide;
                    tempMap[row+1][col]+=divide;
                }
                else{
                    tempMap[row][col]+=divide;
                    tempMap[row+1][col]-=divide;
                }
            }
        }
        tempCalc(row, col+1);
    }
    static void heaterOn(Heater heater){
        int row = heater.row;
        int col = heater.col;
        int dir = heater.dir;
        heated = new boolean[maxRow][maxCol];
        windGo(row+dx[dir],col+dy[dir],dir,5);
    }
    static void windGo(int row, int col, int dir ,int power){
        if(!valid(row,col)) return;
        if(!heated[row][col]){
            map[row][col]+=power;
            heated[row][col]=true;
        }
        if(power==1){
            return;
        }
        int bit=0;
        if(dir==0){ //동
            if(row-1>=0) bit = walls[row-1][col];
            if((bit&9)==0){ // 아래랑 오른쪽이 막혀야함 오른쪽 1 아래 8
                windGo(row-1,col+1,dir,power-1);
            }
            bit = walls[row][col];
            if((bit&1)==0){
                windGo(row,col+1,dir,power-1);
            }
            if(row+1<maxRow) bit = walls[row+1][col];
            if((bit&5)==0){ // 위랑 오른쪽이 막혀야함
                windGo(row+1,col+1,dir,power-1);
            }
        }
        else if(dir==1){ //서
            if(row-1>=0) bit = walls[row-1][col];
            if((bit&10)==0){ // 아래랑 왼쪽이 막혀야함 왼쪽 2 아래 8
                windGo(row-1,col-1,dir,power-1);
            }
            bit = walls[row][col];
            if((bit&2)==0){ // 왼쪽 2
                windGo(row,col-1,dir,power-1);
            }
            if(row+1<maxRow) bit = walls[row+1][col];
            if((bit&6)==0){ // 위랑 왼쪽이 막혀야함 왼쪽 2 위 4
                windGo(row+1,col-1,dir,power-1);
            }
        }
        else if(dir==2){ // 북
            if(col-1>=0) bit = walls[row][col-1];
            if((bit&5)==0){// 왼쪽에선 위랑 오른쪽이 막혀야함 위 4 오른쪽 1
                windGo(row-1,col-1,dir,power-1);
            } 
            bit = walls[row][col];
            if((bit&4)==0){ // 위가 막혀야함
                windGo(row-1,col,dir,power-1);
            }
            if(col+1<maxCol) bit = walls[row][col+1];
            if((bit&6)==0){ // 왼쪽이랑 위가 막혀야함 왼쪽 2 위 4
                windGo(row-1, col+1, dir, power-1);
            }
        }
        else{ // 남
            if(col+1<maxCol) bit = walls[row][col+1];
            if((bit&10)==0){// 오른쪽에선 왼쪽이랑 아래가 막혀야함 왼쪽 2 아래 8
                windGo(row+1,col+1,dir,power-1);
            } 
            bit = walls[row][col];
            if((bit&8)==0){ // 아래가 막혀야함
                windGo(row+1,col,dir,power-1);
            }
            if(col-1>=0) bit = walls[row][col-1];
            if((bit&9)==0){ // 오른쪽이랑 아래가 막혀야함 아래 8 오른쪽 1 
                windGo(row+1, col-1, dir, power-1);
            }
        }
    }
    static boolean valid(int row, int col){
        return row>=0&&row<maxRow&&col>=0&&col<maxCol;
    }
}


