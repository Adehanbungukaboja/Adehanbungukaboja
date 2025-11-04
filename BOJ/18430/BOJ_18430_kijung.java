package BOJ_18430;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int N, M, answer = 0;
    static int[][] map;
    static boolean[][] used;
    // 상우하좌 (시계방향)
    static int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[N][M];
        used = new boolean[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        find(0, 0);

        System.out.println(answer);
    }

    static void find(int idx, int sum) {
        answer = Math.max(answer, sum);
        if (idx == N * M) {
            return;
        }

        int r = idx / M;
        int c = idx % M;

        if (used[r][c]) {
            // 사용된 경우 다음 칸으로 이동
            find(idx + 1, sum);
            return;
        }

        find(idx + 1, sum);

        for (int k = 0; k < 4; k++) {

            int nR1 = r + dir[k][0];
            int nC1 = c + dir[k][1];

            int dIDx = (k + 1) % 4;
            int nR2 = r + dir[dIDx][0];
            int nC2 = c + dir[dIDx][1];

            if (isValid(nR1, nC1) && isValid(nR2, nC2) && !used[nR1][nC1] && !used[nR2][nC2]) {

                used[r][c] = true;
                used[nR1][nC1] = true;
                used[nR2][nC2] = true;

                int nextSum = sum + map[nR1][nC1] + map[nR2][nC2] + (map[r][c] * 2);

                // 다음 칸으로 이동
                find(idx + 1, nextSum);

                used[r][c] = false;
                used[nR1][nC1] = false;
                used[nR2][nC2] = false;
            }
        }
    }

    static boolean isValid(int r, int c) {
        return r >= 0 && r < N && c >= 0 && c < M;
    }
}