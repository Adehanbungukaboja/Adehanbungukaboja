import java.io.*;
import java.util.*;

class BOJ_2108_ohji {

  static BufferedReader br;
  static StringBuilder sb = new StringBuilder();
  static int N, mode;
  static int[] nums = new int[500002];
  static int[] cnt = new int[8002];

  public static void main(String[] args) throws IOException {
    br = new BufferedReader(new InputStreamReader(System.in));
    N = Integer.parseInt(br.readLine());

    int total = 0, maxx = Integer.MIN_VALUE, minn = Integer.MAX_VALUE;
    for (int i = 0; i < N; ++i) {
      nums[i] = Integer.parseInt(br.readLine());
      maxx = Math.max(maxx, nums[i]);
      minn = Math.min(minn, nums[i]);
      ++cnt[nums[i] + 4000];
      total += nums[i];
    }
    // 평균
    if (total >= 0) sb.append((int)((double)total / N + 0.5)).append('\n');
    else sb.append((int)((double)total / N - 0.5)).append('\n');

    // 중앙값
    Arrays.sort(nums, 0, N);
    sb.append(nums[N / 2]).append('\n');

    // 최빈값
    int max_cnt = 0, second = 12345678;
    for (int i = 0; i <= 8000; ++i) {
      if (cnt[i] > max_cnt) {
        max_cnt = cnt[i];
        mode = i - 4000;
        second = 12345678;
      } else if (cnt[i] == max_cnt && second == 12345678) {
        second = i - 4000;
      }
    }
    if (second == 12345678) sb.append(mode).append('\n');
    else sb.append(second).append('\n');

    // 범위
    sb.append(maxx - minn);

    System.out.print(sb);
  }
}
