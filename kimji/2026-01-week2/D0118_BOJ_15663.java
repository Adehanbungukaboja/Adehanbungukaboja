import java.io.*;
import java.util.*;

public class D0118_BOJ_15663 {

    static BufferedReader br;
    static StringTokenizer st;
    static StringBuilder sb = new StringBuilder();

    // N과 M 선언
    static int N, M;

    // N개의 자연수를 담을 배열
    static int[] arr;
    // 선택한 숫자 담을 배열
    static int[] sequence;
    // 이미 선택된 인덱스인지 확인
    static boolean[] selected;

    static void input() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        // N과 M 입력 받기
        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        // N개의 수 입력 받기
        arr = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        sequence = new int[M];
        selected = new boolean[N];

        // 수열은 사전 순으로 증가하는 순서로 출력
        // 편의를 위해 정렬
        Arrays.sort(arr);
    }

    static void backtracking(int depth) {
        // depth == M이면 만들어진 수열
        if (depth == M) {
            for (int i = 0; i < M; i++) {
                sb.append(sequence[i]).append(' ');
            }
            sb.append('\n');
            return;
        }

        // 중복 제거를 위한 변수
        int previousNum = -1;
        // 전체 숫자 돌며 이미 선택된 숫자인지 확인
        for (int i = 0; i < N; i++) {
            // 이미 선택 되었으면 패스
            if (selected[i])            continue;
            // 동일한 수면 패스
            if (arr[i] == previousNum)  continue;

            // 이제 이 숫자 선택
            selected[i] = true;
            // 수열에 담기
            sequence[depth] = arr[i];
            // 중복 제거를 위해 이 숫자 사용했다 체크
            previousNum = arr[i]; // 정렬해뒀기에 가능
            // 다음 숫자 탐색
            backtracking(depth + 1);
            // 백 트래킹
            selected[i] = false;
        }
    }

    public static void main(String[] args) throws Exception {

        /*
        N은 자연수의 개수
        M은 N개에서 고를 수의 개수 -> 수열의 개수

        N개의 자연수 중에서 M개를 고른 수열
        근데 이제 사전 순으로 증가해야하고, 중복 수열이 없어야 함

        1 3 9 9 면
        1 수열
        3 수열
        9 수열 하고, 또 9 수열이 나오면 안됨

        1 3 하고 만들어 지면 출력에 넣고
        백트래킹하고 1 9 만들고,, 이런식으로 가야겠네 백트래킹 기법 사용

         */

        // 입력 받기
        input();

        // 백트래킹
        backtracking(0);

        // 출력
        System.out.print(sb);
    }
}
