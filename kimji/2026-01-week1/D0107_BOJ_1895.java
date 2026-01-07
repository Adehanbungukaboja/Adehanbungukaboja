import java.io.*;
import java.util.*;

public class D0107_BOJ_1895
{

    static BufferedReader br;
    static StringTokenizer st;
    static StringBuilder sb;

    static int R, C;
    static int[][] originArray;
    static int[][] filteredArray;
    static int T;

    public static void main(String[] args) throws Exception
    {
        // 입력 받기
        br = new BufferedReader(new InputStreamReader(System.in));

        // 배열 크기 
        st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        // 배열 선언
        originArray = new int[R][C];
        // 3x3 필터이므로 결과 배열은 (R-2) x (C-2)
        filteredArray = new int[R-2][C-2];

        // 배열 입력
        for (int r = 0; r < R; r++) {
            st = new StringTokenizer(br.readLine());
            for (int c = 0; c < C; c++) {
                originArray[r][c] = Integer.parseInt(st.nextToken());
            }
        }

        // 필터링
        for (int r = 0; r < R-2; r++) {
            for (int c = 0; c < C-2; c++) {
                filteredArray[r][c] = filtering(r, c);
            }
        }

        // T 입력
        T = Integer.parseInt(br.readLine());

        // T보다 큰거 찾기
        int ans = 0;
        for (int r = 0; r < R-2; r++) {
            for (int c = 0; c < C-2; c++) {
                if (filteredArray[r][c] >= T) {
                    ans++;
                }
            }
        }

        System.out.println(ans);
    }

    // 3x3 필터의 중앙값 구하기
    static int filtering(int sr, int sc)
    {
        int[] arr = new int[9];
        int idx = 0;

        for (int r = sr; r < sr + 3; r++) {
            for (int c = sc; c < sc + 3; c++) {
                arr[idx++] = originArray[r][c];
            }
        }

        Arrays.sort(arr);

        // 중앙값
        return arr[4];
    }
}