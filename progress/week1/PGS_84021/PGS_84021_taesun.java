import java.util.*;
import java.io.*;

class Solution {
    static class Point implements Comparable<Point>{
        int row;
        int col;
        Point(int row, int col){
            this.row=row;
            this.col=col;
        }
        @Override
        public int compareTo(Point o){
            if(this.row==o.row){
                return this.col-o.col;
            }
            return this.row-o.row;
        }
        @Override
        public boolean equals(Object o) {
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return this.row == p.row && this.col == p.col;
        }
        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static boolean[][] visited;
    static int tableSize;
    static int[][] sBoard;
    static int[][] sTable;
    public int solution(int[][] game_board, int[][] table) {
        sBoard=game_board;
        sTable=table;
        tableSize = table[0].length;
        visited = new boolean[tableSize][tableSize];
        List<List<TreeSet<Point>>>fragList = new ArrayList<>(); //회전수,조각번호
        for(int i=0;i<4;i++){
            fragList.add(new ArrayList<>());
        }
        int fragCount=0;
        //조각들 찾기
        for(int i=0;i<tableSize;i++){
            for(int j=0;j<tableSize;j++){
                if(table[i][j]==1&&!visited[i][j]){
                    fragCount++;
                    TreeSet<Point>frag = getFrag(i,j);
                    fragList.get(0).add(frag);
                }
            }
        }
        //회전
        for(int i=0;i<3;i++){
            for(TreeSet<Point> frag : fragList.get(i)){
                TreeSet<Point> newFrag = rotate(frag);
                fragList.get(i+1).add(newFrag);
            }
        }
        //빈칸이랑 모양이 같으면 크기더하기
        visited = new boolean[tableSize][tableSize];
        boolean[] used = new boolean[fragCount];
        int answer=0;
        for(int i=0;i<tableSize;i++){
            for(int j=0;j<tableSize;j++){
                if(game_board[i][j]==0&&!visited[i][j]){
                    TreeSet<Point>blank = getBlank(i,j);
                    out:
                    for(int f=0;f<fragCount;f++){
                        if(!used[f]){
                            for(int d=0;d<4;d++){
                                TreeSet<Point>curFrag = fragList.get(d).get(f);
                                if(curFrag.size()!=blank.size()) break;
                                if(curFrag.equals(blank)){
                                    used[f]=true;
                                    answer+=curFrag.size();
                                    break out;
                                }
                            }
                        }
                    }
                }
            }
        }
        return answer;
    }
    static TreeSet<Point> getBlank(int i,int j){
        Queue<Point> q = new ArrayDeque<>();
        q.add(new Point(i,j));
        visited[i][j]=true;
        TreeSet<Point> blank = new TreeSet<>();
        int minRow=100;
        int minCol=100;
        while(!q.isEmpty()){
            Point cur = q.poll();
            blank.add(new Point(cur.row,cur.col));
            minRow=Math.min(minRow,cur.row);
            minCol=Math.min(minCol,cur.col);
            for(int d=0;d<4;d++){
                int curRow = cur.row+dx[d];
                int curCol = cur.col+dy[d];
                if(valid(curRow,curCol)){
                    if(sBoard[curRow][curCol]==0&&!visited[curRow][curCol]){
                        visited[curRow][curCol]=true;
                        q.add(new Point(curRow,curCol));
                    }
                }
            }
        }
        for(Point p : blank){
            p.row-=minRow;
            p.col-=minCol;
        }
        return blank;
    }
    static TreeSet<Point> getFrag(int i,int j){
        Queue<Point> q = new ArrayDeque<>();
        q.add(new Point(i,j));
        visited[i][j]=true;
        TreeSet<Point> frag = new TreeSet<>();
        int minRow=100;
        int minCol=100;
        while(!q.isEmpty()){
            Point cur = q.poll();
            frag.add(new Point(cur.row,cur.col));
            minRow=Math.min(minRow,cur.row);
            minCol=Math.min(minCol,cur.col);
            for(int d=0;d<4;d++){
                int curRow = cur.row+dx[d];
                int curCol = cur.col+dy[d];
                if(valid(curRow,curCol)){
                    if(sTable[curRow][curCol]==1&&!visited[curRow][curCol]){
                        visited[curRow][curCol]=true;
                        q.add(new Point(curRow,curCol));
                    }
                }
            }
        }
        for(Point p : frag){
            p.row-=minRow;
            p.col-=minCol;
        }
        return frag;
    }
    static boolean valid(int row, int col){
        return row>=0&&row<tableSize&&col>=0&&col<tableSize;
    }
    
    static TreeSet<Point> rotate(TreeSet<Point> frag){
        int maxRow=0;
        for(Point p : frag){
            maxRow = Math.max(maxRow, p.row);
        }
        TreeSet<Point> newFrag = new TreeSet<>();
        int minRow=Integer.MAX_VALUE, minCol=Integer.MAX_VALUE;
        for(Point p : frag){
            int nr = p.col;
            int nc = maxRow - p.row;
            newFrag.add(new Point(nr, nc));
            minRow = Math.min(minRow, nr);
            minCol = Math.min(minCol, nc);
        }
        TreeSet<Point> normalized = new TreeSet<>();
        for(Point p : newFrag){
            normalized.add(new Point(p.row - minRow, p.col - minCol));
        }
        return normalized;
    }
}