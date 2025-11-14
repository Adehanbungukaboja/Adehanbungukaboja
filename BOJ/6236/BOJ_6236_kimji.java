import java.io.*;
import java.util.*;

public class BOJ_6236_kimji {
	
	static BufferedReader br;
	static StringTokenizer st;
	static StringBuilder sb;
	
	// n일 m번
	static int n, m;
	// i번째 날에 이용할 금액 
	static int[] useMoney;
	
	// 가능한 최소값 
	static int canMinK;
	static int canMaxK;
	
	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		// 가능한 k의 범위는 하루에 2번씩 빼면 안되기에
		// 최소 k는 하루에 한번은 뺄 수 있는 useMoney의 최대값, 그래야 넣었다 뺐다 하면서 하루에 한번씩은 인출이 가능함 (n == m일 경우 이론상 최소)  
		// 최대 k는 첫날 한번빼고, 다 쓰고 m번 충족시켜버리기를 하면 되니깐 useMoney의 합 (m == 1일때 이론상 최소) 
		canMinK = -1;
		canMaxK = 0;
		useMoney = new int[n];
		for (int i = 0; i < n; i++) {
			useMoney[i] = Integer.parseInt(br.readLine().trim());
			canMinK = Math.max(canMinK, useMoney[i]);
			canMaxK += useMoney[i];
		}
		
		// 이분탐색을 하며 이 중간값으로도 되네? 최대값을 그 중간값-1
		// 안되네? 최소값을 그 중간값+1 
		// 그 중간값으로 n일치 시뮬레이션 돌리기 
	
		// 이분탐색 시작
		int ans = canMaxK;
		while (canMinK <= canMaxK) {
			// 가지 치기 m = 1이면 canMaxK가 답임. 한번 인출로 모든 날짜 사용금액 만족해야하니
			if (m == 1) {
				break;
			}
			
			// 인출 횟수
			int count = 1;
			// 현재의 중간값
			int middleK = (canMinK + canMaxK) / 2;
			// 지금 가지고 있는 돈
			int currentMoney = middleK;
			
			// n일 시뮬레이션
			for (int i = 0; i < n; i++) {
				// 현재 가지고 있는 돈이 오늘 써야할 돈보다 작다면?
				if (currentMoney < useMoney[i]) {
					// 인출하기
					count++;
					// 넣었다가 빼면 초기화 
					currentMoney = middleK;
				}
				
				// 가지치기
				if (count > m)	{
					break;
				}
				
				// 돈쓰기
				currentMoney -= useMoney[i];
			}
			
			// 인출횟수가 m이하이면 됨. 횟수맞추기 위해 그냥 돈 뽑아버리면 되니깐.
			if (count <= m) {
				// 현재의 중간값이 지금 가능한 최소가 됨
				ans = middleK;
				// 이론상 가능한 최대값 줄이기 (중간값 - 1)
				canMaxK = middleK - 1;
			}
			// 인출횟수가 m을 넘어버렸다..
			else {
				// 이론상 가능한 최소값 늘리기 (중간값 + 1)
				canMinK = middleK + 1;
			}
		}
		
		sb.append(ans);
		System.out.println(sb);
	}
}
