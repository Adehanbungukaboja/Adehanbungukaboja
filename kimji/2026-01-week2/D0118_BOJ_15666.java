import java.io.*;
import java.util.*;

public class D0118_BOJ_15666 {

    static BufferedReader br;
    static StringTokenizer st;
    static StringBuilder sb = new StringBuilder();

    static int N, M;
    static int[] arr;
    // 선택된 수열
    static int[] selected;

    static void input() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        // N과 M입력 받기
        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        arr = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        selected = new int[M];

        // 비내림차순이니 정렬
        Arrays.sort(arr);
    }

    static void backtracking(int depth, int index) {
        if (depth == M) {
            for (int num : selected) {
                sb.append(num).append(' ');
            }
            sb.append('\n');
            return;
        }

        // 중복 순열은 안되니, 동일한 숫자 스타트는 안하게 설정
        int previousNum = -1;
        // 인덱스도 현재 수 이전 인덱스는 포함안하게.. 정렬된 상태라서 현재 수 이전은 패스 해야함
        for (int i = index; i < N; i++) {
            // 현재 숫자가 이전 숫자랑 같으면 컨티뉴 -> 중복 수열 제거 위함
            if (arr[i] == previousNum)  continue;

            // 현재 숫자 담기
            selected[depth] = arr[i];
            previousNum = arr[i];
            backtracking(depth + 1, i);
        }
    }

    public static void main(String[] args) throws Exception {

        /*
        N개의 자연수 중에서 M개를 고른 수열
        같은 수를 여러 번 골라도 됨
        고른 수열은 비내림차순

        같은 수를 여러번 골라도 되지만, 중복된 수열은 안됨
         */

        // 입력
        input();

        // 백트래킹, 깊이랑 비내림차순이기 때문에 현재의 인덱스 이전은 보면 안되기에 시작 인덱스 지정
        backtracking(0, 0);

        // 출력
        System.out.print(sb);
    }
}
