import java.io.*;
import java.util.*;

public class D0110_BOJ_26006 {

    static BufferedReader br;
    static StringTokenizer st;

    // 체스판의 크기 NxN, 체스판은 만들지 않습니다. 만들어서 하는 방식은 메모리가 터지게 되어있어요.
    static int N;
    // 흑색 퀸의 수
    static int K;

    // 백색 킹의 위치
    static int whiteKingR, whiteKingC;
    // 흑색 퀸의 위치들
    static int[] blackQueenR;
    static int[] blackQueenC;

    static void input() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        // 보드판의 크기, 흑색킹의 개수 입력
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        // 백색 킹의 위치 입력 -> 1부터 시작
        st = new StringTokenizer(br.readLine());
        whiteKingR = Integer.parseInt(st.nextToken()) - 1;
        whiteKingC = Integer.parseInt(st.nextToken()) - 1;

        // 흑색 퀸의 개수 만큼 좌표 저장 초기화
        blackQueenR = new int[K];
        blackQueenC = new int[K];

        // 흑색 퀸의 위치 입력받기 -> 1부터 시작
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            blackQueenR[i] = Integer.parseInt(st.nextToken()) - 1;
            blackQueenC[i] = Integer.parseInt(st.nextToken()) - 1;
        }
    }

    // 킹 위치를 이동했을 때 공격 당하는 지 확인
    static boolean isAttacked(int row, int col) {
        for (int i = 0; i < K; i++) {
            if (blackQueenR[i] == row)  return true;
            if (blackQueenC[i] == col)  return true;
            if (Math.abs(blackQueenR[i] - row) == Math.abs(blackQueenC[i] - col))   return true;
        }

        return false;
    }

    static boolean avoid() {
        // 8방향 확인
        int[] deltaRow = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] deltaCol = {-1, 0, 1, 1, 1, 0, -1, -1};

        for (int dir = 0; dir < 8; dir++) {
            int newRow = whiteKingR+deltaRow[dir];
            int newCol = whiteKingC+deltaCol[dir];

            // 범위 벗어나면 패스
            if (newRow < 0 || newRow >= N || newCol < 0 || newCol >= N) continue;

            // 이동했을 때 공격을 안당하면 피할 수 있는 것
            if (!isAttacked(newRow, newCol)) return true;
        }

        // 다 돌았는데 안되면 못피한거지 뭐
        return false;
    }

    public static void main(String[] args) throws Exception {
        // 백색 킹의 체크(CHECK), 체크메이트(CHECKMATE), 스테일메이트(STALEMATE) 판단하기, 그냥 안전하다면 NONE
        // 백색 킹은 8방향 한칸씩만 이동가능함

        // 흑색 퀸은 같은 행, 열, 대각선으로 거리제한없이 이동가능
        // 그렇다면 흑색 퀸 입력받으면 보드에 공격가능 위치 모두 체크해두기

        // 칠하는 방식으로 간단하게 하려 했더니 메모리 초과가 남
        // 공격당하는 조건
        //      1. 흑색 퀸과 같은 행일 경우
        //      2. 흑색 퀸과 같은 열일 경우
        //      3. 흑색 퀸의 대각선에 있을 경우

        // 입력과 초기값 세팅
        input();

        // 백색 킹이 현재 공격범위안에 있으면, CHECK 또는 CHECKMATE
        if (isAttacked(whiteKingR, whiteKingC)) {
            // 피할 수 있으면
            if (avoid())    System.out.println("CHECK");
            // 피할 수 없으면
            else            System.out.println("CHECKMATE");
        }
        // 백색 킹이 현재 공격범위안에 없으면 STALEMATE 또는 NONE
        else {
            // 피할 수 있으면
            if (avoid())    System.out.println("NONE");
            // 피할 수 없으면
            else            System.out.println("STALEMATE");
        }
    }
}
