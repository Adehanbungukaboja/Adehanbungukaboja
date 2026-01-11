import java.io.*;
import java.util.*;

public class D0111_BOJ_11053 {

    static BufferedReader br;
    static StringTokenizer st;

    // 수열 A의 크기 N
    static int N;
    // 수열 A
    static int[] A;
    // 최대 길이를 위한 dp 배열
    static int[] dp;

    static void input() throws  IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        // N 입력
        N = Integer.parseInt(br.readLine());

        // 수열 A 입력
        A = new int[N + 1];
        // dp 배열 초기화
        dp = new int[N + 1]; // 이전 값에서 증가하는 형식..
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= N; i++) {
            A[i] = Integer.parseInt(st.nextToken());
            dp[i] = 1; // 기본길이는 1이니깐..
        }
    }

    public static void main(String[] args)  throws Exception {

        // 가장 긴 부분을 구하는 것
        // dp를 이용해서 이전보다 수가 크면 증가 그렇지 않으면 유지하는 형식으로 가면 될 듯 함
        // LIS 문제임

        // 입력하기
        input();

        // 최대 길이 기본은 1
        int maxLength = 1;
        // 현재 값과 이전까지의 수 비교하면서 이어지는 지 볼 것
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j < i; j++) {
                // 현재(i)의 값이 이전(j) 값보다 크면
                if (A[i] > A[j]) {
                    // 현재의 길이가 긴지, 이전까지의 누적보다 긴 지 확인
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            // 최대 길이 업데이트
            maxLength = Math.max(maxLength, dp[i]);
        }

        System.out.println(maxLength);
    }
}
