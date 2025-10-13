import java.util.*;
import java.io.*;
 
public class Solution {
    static BufferedReader br;
    static StringBuilder sb;
    static StringTokenizer st;
     
    static int N;
    static int M;
     
    public static void main(String[] args) throws Exception {
        sb = new StringBuilder();
        br = new BufferedReader(new InputStreamReader(System.in));
         
        int T = Integer.parseInt(br.readLine().trim());
        for (int t=1; t<=T; t++) {
            st = new StringTokenizer(br.readLine().trim());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
             
            // <M의 이진수 표현의 마지막 N비트가 모두 1인지 여부 판단하기>
            int lastNBitsAllOn = (1 << N) - 1; // 하위 N비트가 전부 1인 마스크
            int lastNBitsFromM = M & lastNBitsAllOn; // M의 하위 N비트만 추출
             
            sb.append("#").append(t).append(" ");
            if (lastNBitsAllOn == lastNBitsFromM) {
                sb.append("ON").append("\n");
            }
            else {
                sb.append("OFF").append("\n");
            }
        }
        System.out.println(sb);
    }
}