/**
 1. dfs로 퍼즐 조각 구분
 2. 찾은 퍼즐 조각의 모양을 0,0 좌표로 저장 후, 돌린 모양도 리스트로 저장

 3. 게임 보드에 채운 칸 수 리턴(퍼즐들의 크기 저장해놓으면 될듯)
 */
import java.util.*;
import java.io.*;

class Solution {
    static int arrSize;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = { 0,-1, 0, 1};

    static final int PUZZLE = 0;
    static final int BLANK = 1;
    // 좌표 저장 클래스
    static class Pair{
        int x;
        int y;

        public Pair(int x, int y){
            this.x = x;
            this.y = y;
        }

        public String toString(){
            return "[" + this.x + ", " + this.y + "]";
        }
    }

    // 좌표 정렬 용 클래스
    static class PairComparator implements Comparator<Pair>{
        // x 기준으로 정렬 후, x가 같다면 y기준으로
        @Override
        public int compare(Pair o1, Pair o2) {
            if(o1.x == o2.x)    return o1.y - o2.y;
            return o1.x - o2.x;
        }
    }

    // 테이블의 퍼즐 조각 정보 저장할 클래스: rotate 필요, 정규화 필요
    static class PuzzleInfo{
        List<Pair> degree0;
        List<List<Pair>> rotates = new ArrayList<>();   //초기화 필수
        int size;
        boolean isUsed = false;
        // 퍼즐용. rotate 시켜야함
        public PuzzleInfo(List<Pair> origin){
            // 클래스 필드를 정규화한 값으로 저장
            this.degree0 = normalize(origin);
            size = origin.size();
            // 정규화된 좌표를 이용해 rotate진행 -> 90,180,270
            // 현재 좌표
            rotates.add(new ArrayList<>(degree0));
            List<Pair> curRotate = this.degree0;

            // 돌리고, 정규화 된 좌표를 rotates 좌표에 저장
            for(int i = 0; i < 3; i++){
                curRotate = rotate(curRotate);
                rotates.add(new ArrayList<>(curRotate));
            }
            //-> 한 퍼즐 당  0,90,180,270 된 좌표가 rotates에 저장됨
        }

        private List<Pair> normalize(List<Pair> origin){
            //정규화된 좌표리스트
            List<Pair> normalize = new ArrayList<>();

            // 시작좌표가 될 가장 작은 x,y 찾기
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            for(Pair p : origin){
                minX = Math.min(minX, p.x);
                minY = Math.min(minY, p.y);
            }

            // origin 배열에 있는 모든 좌표를 minX, minY로 빼서 정규화
            // 정규화된 좌표 normalize 배열에 저장
            for(Pair p : origin){
                normalize.add(new Pair(p.x - minX, p.y - minY));
            }

            // 좌표리스트의 순서가 같아야 비교가 가능하기 때문에 정렬 필수
            normalize.sort(new PairComparator());

            return normalize;
        }

        private List<Pair> rotate(List<Pair> curDegree){
            // 돌린 좌표 저장할 List
            List<Pair> rotatePair = new ArrayList<>();

            for(Pair p : curDegree){
                rotatePair.add(new Pair(p.y, -p.x));
            }

            //돌린 위치 정규화
            return normalize(rotatePair);
        }

    }

    // 테이블의 퍼즐 조각 정보 저장할 클래스: rotate 불필요 정규화 필요
    static class BlankInfo{
        List<Pair> origin;
        int size;
        public BlankInfo(List<Pair> origin){
            this.origin = normalize(origin);
            this.size = origin.size();
        }
        private List<Pair> normalize(List<Pair> origin){
            //정규화된 좌표리스트
            List<Pair> normalize = new ArrayList<>();

            // 시작좌표가 될 가장 작은 x,y 찾기
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            for(Pair p : origin){
                minX = Math.min(minX, p.x);
                minY = Math.min(minY, p.y);
            }

            // origin 배열에 있는 모든 좌표를 minX, minY로 빼서 정규화
            // 정규화된 좌표 normalize 배열에 저장
            for(Pair p : origin){
                normalize.add(new Pair(p.x - minX, p.y - minY));
            }

            // 좌표리스트의 순서가 같아야 비교가 가능하기 때문에 정렬 필수
            normalize.sort(new PairComparator());

            return normalize;
        }
    }


    public int solution(int[][] game_board, int[][] table) {
        int answer = 0;

        arrSize = game_board.length;

        List<Pair> pairList;
        // 퍼즐 조각들 정보 리스트
        List<PuzzleInfo> puzzles = new ArrayList<>();
        // 빈칸들 정보 리스트
        List<BlankInfo> blanks = new ArrayList<>();

        // 테이블에서 퍼즐 조각 찾기
        for(int row = 0; row < arrSize; row++){
            for(int col = 0; col < arrSize; col++){

                //테이블에서 1찾으면 저장
                if(table[row][col] == 1){
                    // 찾은 퍼즐 조각의 좌표 정보들 저장할 리스트
                    pairList = new ArrayList<>();

                    // 시작점 죄표 넣고
                    pairList.add(new Pair(row, col));

                    // 현재 블록 방문 처리
                    table[row][col] = 2;
                    //퍼즡 조각 찾기
                    find(PUZZLE,table, pairList, row, col);

                    // find 메서드가 종료되면, 현재 위치와 인접한 블록 다 찾음
                    // 해당 블록 정보를 이용해 PuzzleInfo 만든 후, puzzles 리스트에 저장
                    PuzzleInfo puzzleInfo = new PuzzleInfo(pairList);
                    puzzles.add(puzzleInfo);
                }
            }
        }

        // 게임보드에서 빈칸 찾기
        for(int row = 0; row < arrSize; row++){
            for(int col = 0; col < arrSize; col++){

                // 보드에서 빈칸 찾으면 저장
                if(game_board[row][col] == 0){
                    pairList = new ArrayList<>();

                    pairList.add(new Pair(row, col));
                    //현재 위치 방문처리
                    game_board[row][col] = 1;

                    // 인접 빈칸 찾기
                    find(BLANK, game_board, pairList, row, col);

                    BlankInfo blankInfo = new BlankInfo(pairList);
                    blanks.add(blankInfo);
                }
            }
        }

        // blanks와 puzzles를 비교해서 넣을 수 있는 퍼즐 찾기
        for(BlankInfo b : blanks){
            boolean isMatched = false;
            for(PuzzleInfo p : puzzles){
                // 사용되지 않았거나, blank와 사이즈가 같은 퍼즐들만 좌표 비교
                if((!p.isUsed) && (b.size == p.size)){
                    // rotate 좌표와 b의 좌표들 비교
                    for(int idx = 0; idx < 4; idx++){
                        List<Pair> curRotate = p.rotates.get(idx);
                        if(comparePos(b.origin, curRotate)){
                            answer += b.size;
                            p.isUsed = true;
                            isMatched = true;
                            break;
                        }
                    }
                }

                // 해당 빈칸과 맞는 블록을 찾았으면 종료
                if(isMatched){
                    break;
                }

            }
        }

        return answer;
    }

    static boolean comparePos(List<Pair> blank, List<Pair> puzzle){
        for(int idx = 0; idx < blank.size(); idx++){
            if((blank.get(idx).x != puzzle.get(idx).x)||(blank.get(idx).y != puzzle.get(idx).y)) return false;
        }

        return true;
    }

    // 조각 정보 찾는 메서드 - dfs
    // 받은 어레이 정보를 바탕으로 찾은 조각의 좌표 정보를 list에 추가
    static void find(int type, int[][] arr, List<Pair> list, int sx, int sy){
        int noVisited = 0;
        if(type == 0){  //puzzle
            noVisited = 1;
        }

        for(int dir = 0; dir < 4; dir++){
            int mx = sx + dx[dir];
            int my = sy + dy[dir];

            // 범위 벗어나는지 체크
            if(mx < 0 || mx >= arrSize || my < 0 || my >= arrSize)  continue;

            if(arr[mx][my] == noVisited){
                // 블록 찾으면 좌표 리스트에 넣기
                list.add(new Pair(mx, my));
                // 현재 좌표 방문 처리
                arr[mx][my] = 2;
                // 다음 위치 탐색
                find(type, arr, list, mx, my);
            }
        }
    }

}