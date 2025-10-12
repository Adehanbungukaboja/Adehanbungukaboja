import java.util.*;
import java.io.*;

public class Main {
    static class Point implements Comparable<Point>{
        int map;
        int row;
        int col;
        int dist;
        Point(int map, int row, int col, int dist){
            this.map =map;
            this.row = row;
            this.col = col;
            this. dist= dist;
        }
        @Override
        public int compareTo(Point o){
            return this.dist-o.dist;
        }
    }
    static int[][][][] maps = new int[5][5][5][4];
    static final int INF = 1_000_000_000;
    static int[] dx = {1,-1,0,0,0,0};//map 위 아래 왼쪽 오른쪽  상 하
    static int[] dy = {0,0,0,0,-1,1};//row
    static int[] dz = {0,0,-1,1,0,0};//col
    static int[][] order = new int[5][2];
    static int ans = INF;
    static boolean[] used = new boolean[5];
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] st;
        for(int map=0;map<5;map++){
            for(int row=0;row<5;row++){
                st = br.readLine().split(" ");
                for(int col=0;col<5;col++){
                    maps[map][row][col][0]=st[col].charAt(0)-'0';
                }
            }
        }
        for(int map=0;map<5;map++){
            for(int turn=1;turn<=3;turn++){
                int[][] newMap = turn(map,turn-1);
                for(int row=0;row<5;row++){
                    for(int col=0;col<5;col++){
                        maps[map][row][col][turn]=newMap[row][col];
                    }
                }
            }
        }
        dfs(0);
        if(ans==INF){
            ans=-1;
        }
        System.out.println(ans);
    }
    static void dfs(int depth){
        if(depth==5){
            int dist = goOut(order);
            ans = Math.min(ans,dist);
            return;
        }
        for(int i=0;i<5;i++){
            if(used[i]) continue;
            for(int j=0;j<4;j++){
                if(depth==0&&maps[i][0][0][j]==0) continue;
                if(depth==4&&maps[i][4][4][j]==0) continue;
                order[depth][0]=i;
                order[depth][1]=j;
                used[i]=true;
                dfs(depth+1);
                used[i]=false;
            }
        }
    }

    static int goOut(int[][] mapOrder){
        int[][][] dist = new int[5][5][5];
        PriorityQueue<Point> pq = new PriorityQueue<>();
        for(int[][] arr1 : dist){
            for(int [] arr2 : arr1){
                Arrays.fill(arr2, INF);
            }
        }
        pq.add(new Point(0,0,0,0));
        dist[0][0][0]=0;
        while(!pq.isEmpty()){
            Point cur = pq.poll();
            if(dist[cur.map][cur.row][cur.col]<cur.dist) continue;
            if(cur.map==4&&cur.row==4&&cur.col==4){
                return cur.dist;
            }
            for(int d=0;d<6;d++){
                int curMap= cur.map+dx[d];
                int curRow = cur.row+dy[d];
                int curCol = cur.col+dz[d];
                if(valid(curMap,curRow,curCol)&&maps[mapOrder[curMap][0]][curRow][curCol][mapOrder[curMap][1]]==1){
                    if(dist[curMap][curRow][curCol]<=cur.dist+1) continue;
                    dist[curMap][curRow][curCol]=cur.dist+1;
                    pq.add(new Point(curMap, curRow, curCol, cur.dist+1));
                }
            }
        }
        return INF;
    }
    static boolean valid(int map, int row, int col){
        return map>=0&&row>=0&&col>=0&&map<5&&row<5&&col<5;
    }

    static int[][] turn(int mapId, int turn){
        int[][] newMap = new int[5][5];
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                newMap[i][j]=maps[mapId][4-j][i][turn];
            }
        }
        return newMap;
    }
    
}
