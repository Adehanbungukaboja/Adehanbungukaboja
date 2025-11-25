import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BOJ_9184_kimji {
	
	static BufferedReader br;
	static StringBuilder sb;
	static StringTokenizer st;
	
	// 그냥 하면 오래걸리기 때문에 dp를 이용해서 미리 값을 계산하기
	static int[][][] dp;
	
	static int w(int a, int b, int c) {
		if (a <= 0 || b <= 0 || c <= 0) {
			return 1;
		}
		if (a > 20 || b > 20 || c > 20) {
		    return w(20, 20, 20);
		}
		
		// 이미 계산된 값이라면?
		if (dp[a][b][c] != 0) {
			return dp[a][b][c];
		}
		
		// dp로 계산하기 
		if (a < b && b < c) {
			dp[a][b][c] = w(a, b, c-1) + w(a, b-1, c-1) - w(a, b-1, c);
		}
		else {
			dp[a][b][c] = w(a-1, b, c) + w(a-1, b-1, c) + w(a-1, b, c-1) - w(a-1, b-1, c-1);
		}
		
		return dp[a][b][c]; 
	}
	
	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		
		// 1 ~ 20의 값이기 때문에 21로 생성
		dp = new int[21][21][21];
		// 반복할때마다 계산하면 손해니깐 반복 시작전에 미리 게산하기
		for (int aa = 0; aa <= 20; aa++) {
			for (int bb = 0; bb <= 20; bb++) {
				for (int cc = 0; cc <= 20; cc++) {
					w(aa, bb, cc);
				}
			}
		}
		
		int a, b, c;
		while (true) {
			st = new StringTokenizer(br.readLine().trim());
			a = Integer.parseInt(st.nextToken());
			b = Integer.parseInt(st.nextToken());
			c = Integer.parseInt(st.nextToken());
			
			// -1 -1 -1 이면 입력 종료
			if (a == -1 && b == -1 && c == -1)	break;
			
			sb.append("w(").append(a).append(", ").append(b).append(", ").append(c).append(") = ").append(w(a,b,c)).append('\n');
		}
		System.out.println(sb);
	}
}
