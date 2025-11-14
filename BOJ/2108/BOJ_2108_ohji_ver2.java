import java.io.*;
import java.util.*;

public class BOJ_2108_ohji_ver2 {
  
  static BufferedReader br;
  static StringBuilder sb = new StringBuilder();
  static int N;
  static int[] nums = new int[500002];
  static Map<Integer, Integer> freq = new HashMap<>();

  public static void main(String args[]) throws IOException {
    br = new BufferedReader(new InputStreamReader(System.in));
    N = Integer.parseInt(br.readLine());

    int total = 0;
    for (int i = 0; i < N; ++i) {
      nums[i]= Integer.parseInt(br.readLine());
      total += nums[i];

      freq.put(nums[i], freq.getOrDefault(nums[i], 0) + 1);
    }

    // 평균
    sb.append(Math.round(total * 1.0 / N)).append('\n');

    // 중앙값
    Arrays.sort(nums, 0, N);
    sb.append(nums[N / 2]).append('\n');

    // 최빈값
    int maxFreq = 0;
    for (int cnt : freq.values()) {
      if (cnt > maxFreq) {
        maxFreq = cnt;
      }
    }

    PriorityQueue<Integer> pq = new PriorityQueue<>();
    for (Map.Entry<Integer, Integer> e : freq.entrySet()) {
      if (e.getValue() == maxFreq) pq.offer(e.getKey());
    }

    if (pq.size() == 1) {
      sb.append(pq.poll()).append('\n');
    } else {
      pq.poll(); sb.append(pq.poll()).append('\n');
    }

    // 범위
    sb.append(nums[N - 1] - nums[0]).append('\n');

    System.out.print(sb);
  }
}
