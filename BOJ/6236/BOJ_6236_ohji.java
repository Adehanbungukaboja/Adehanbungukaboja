import java.io.*;
import java.util.*;

public class BOJ_6236_ohji {
  
  static BufferedReader br;
  static int N, M;
  static int[] money = new int[100002];

  static int minDay(int K) {
    int budget = 0, cnt = 0;
    for (int i = 0; i < N; ++i) {
      if (budget < money[i]) {
        ++cnt;
        budget = K;
      }
      budget -= money[i];
    }
    return cnt;
  }

  public static void main(String args[]) throws IOException {
    br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());

    int l = 0, r = 0;
    for (int i = 0; i < N; ++i) {
      money[i] = Integer.parseInt(br.readLine());
      l = Math.max(l, money[i]); // 최소 예산은 하루 예산 중 맥스값. 그것보단 이상이어야 모든 날짜를 살아갈 수 있음.
      r += money[i]; // 최대 예산은 모두를 합친 값. 그러면 한 번만 인출하면 됨.
    }

    // M번 이하로 뽑을 수 있으면 됨. 그럼 나머지 일자에서 임의로 더 뽑아서 M번 맞출 수 있음.
    int K = 0;
    while (l <= r) {
      K = (l + r) / 2; // K
      
      int m = minDay(K); // K라면 최소 몇 번 인출하면 되는지
      if (m <= M) { 
        // 더 금액을 아래로 내려보기
        r = K - 1;
      } else {
        // 돈을 더 많이 뽑아야했었던 거니까 예산을 늘려야한다.
        l = K + 1;
      }
    }
    System.out.print(K);
  }
}
