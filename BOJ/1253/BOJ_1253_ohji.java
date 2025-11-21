import java.io.*;
import java.util.*;

public class BOJ_1253_ohji {
  
  static BufferedReader br;
  static int N;
  static int[] arr = new int[2002];

  static boolean isGood(int idx) {
    int l = 0, r = N - 1;
    while (l < r) {
      if (l == idx) {
        ++l; continue;
      } else if (r == idx) {
        --r; continue;
      }
      int s = arr[l] + arr[r];
      if (s == arr[idx]) return true;

      if (s < arr[idx]) ++l;
      else --r;
    }

    return false;
  }

  public static void main(String args[]) throws IOException {
    br = new BufferedReader(new InputStreamReader(System.in));

    N = Integer.parseInt(br.readLine());
    StringTokenizer st = new StringTokenizer(br.readLine());
    for (int i = 0; i < N; ++i) {
      arr[i] = Integer.parseInt(st.nextToken());
    }

    Arrays.sort(arr, 0, N);

    int cnt = 0;
    for (int i = 0; i < N; ++i) {
      if (isGood(i)) ++cnt;
    }
    System.out.print(cnt);
  }
}
