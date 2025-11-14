import java.io.*;
import java.util.*;

public class BOJ_3197_ohji {

  static BufferedReader br;
  static int R, C;
  static char[][] board = new char[1502][1502];
  static int[] dx = {-1, 0, 1, 0};
  static int[] dy = {0, 1, 0, -1};
  static int[] S1, S2;
  static boolean[][] swanVis = new boolean[1502][1502];
  static boolean[][] iceVis = new boolean[1502][1502];
  static Queue<int[]> iceQ = new ArrayDeque<>();
  static Queue<int[]> iceNextQ = new ArrayDeque<>();
  static Queue<int[]> swanQ = new ArrayDeque<>();
  static Queue<int[]> swanNextQ = new ArrayDeque<>();

  static boolean OOB(int x, int y) {
    return x < 0 || x >= R || y < 0 || y >= C;
  }

  static void melt() {
    iceNextQ = new ArrayDeque<>();
    while (!iceQ.isEmpty()) {
      int[] cur = iceQ.poll();

      board[cur[0]][cur[1]] = '.';
      for (int d = 0; d < 4; ++d) {
        int nx = cur[0] + dx[d], ny = cur[1] + dy[d];
        if (OOB(nx, ny) || iceVis[nx][ny]) continue;

        if (board[nx][ny] == 'X') {
          iceVis[nx][ny] = true;
          iceNextQ.offer(new int[]{nx, ny});
        }
      }
    }
    iceQ = iceNextQ;
  }

  static boolean canMeet() {
    swanNextQ = new ArrayDeque<>();
    while (!swanQ.isEmpty()) {
      int[] cur = swanQ.poll();

      for (int d = 0; d < 4; ++d) {
        int nx = cur[0] + dx[d], ny = cur[1] + dy[d];
        if (OOB(nx, ny) || swanVis[nx][ny]) continue;

        if (nx == S2[0] && ny == S2[1]) return true;

        swanVis[nx][ny] = true;
        if (board[nx][ny] == '.') { // 지금 바로 갈 수 있음
          swanQ.offer(new int[]{nx, ny});
        } else { // 얼음이면 다음 후보. swanQ에 넣지 않으니까 업데이트 안 됨. 지금 가다가 마주친 얼음들
          swanNextQ.offer(new int[]{nx, ny});
        }
      }
    }
    // 이제 오늘은 더 못 감.
    swanQ = swanNextQ;
    return false;
  }

  static int run() {
    swanQ.offer(new int[]{S1[0], S1[1]});
    swanVis[S1[0]][S1[1]] = true;

    int day = 0;
    while (true) {
      if (canMeet()) {
        return day;
      }

      melt();
      ++day;
    }
  }

  public static void main(String args[]) throws IOException {
    br = new BufferedReader(new InputStreamReader(System.in));

    // 입력
    StringTokenizer st = new StringTokenizer(br.readLine());
    R = Integer.parseInt(st.nextToken());
    C = Integer.parseInt(st.nextToken());

    for (int i = 0; i < R; ++i) {
      board[i] = br.readLine().toCharArray();
      for (int j = 0; j < C; ++j) {
        if (board[i][j] == 'L') {
          if (S1 == null) S1 = new int[]{i, j};
          else S2 = new int[]{i, j};
          board[i][j] = '.';
        }
      }
    }

    for (int i = 0; i < R; ++i) {
      for (int j = 0; j < C; ++j) {
        if (board[i][j] == 'X') {
          for (int d = 0; d < 4; ++d) {
            int nx = i + dx[d], ny = j + dy[d];
            if (!OOB(nx, ny) && board[nx][ny] == '.') {
              iceQ.offer(new int[]{i, j});
              iceVis[i][j] = true;
              break;
            }
          }
        }
      }
    }
    
    // 실행 & 출력
    System.out.print(run());
  }
}
