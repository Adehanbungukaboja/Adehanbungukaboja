/*
// 문제 이해
- 현우는 N일 동안 자신이 사용할 금액을 계산함
- 돈을 펑펑 쓰지 않기 위해 정확히 M번만 통장에서 돈을 빼서 쓰기로 함
- 현우는 통장에서 K원을 인출하며,
   - if) 통장에서 뺀 돈으로 하루를 보낼 수 있다
      - 그대로 사용
   - if) 하루를 보내기에 돈이 모자르다
      - 남은 금액은 통장에 집어넣는다.
      - 다시 K원을 인출한다. 
- 현우는 돈을 아끼기 위해 인출 금액 K를 최소화하기로 하였다. 

// 목표
최소 금액 K 구하기 

// 예제로 문제 파악하기
N: 7   // 7일 동안 사용할 금액 계산
M: 5   // 딱 5번만 통장에서 돈을 빼서 쓸거임

if) k=500 --> 지갑:500
1번째 날: 100 --> 지갑:400
2번째 날: 400 --> 지갑:0
3번째 날: 300 --> 통장:0, 지갑:500(m=1) --> 지갑:200
4번째 날: 100 --> 지갑:100
5번째 날: 500 --> 통장:100, 지갑:500(m=2) --> 지갑:0
6번째 날: 101 --> 통장:100, 지갑:500(m=3) --> 지갑:399
7번째 날: 400 --> 통장:499, 지갑:500(m=4) --> 지갑:100

근데 현우는 M이라는 숫자를 좋아해서, 
정확히 M번을 맞추기 위해서 남은 금액이 그날 사용한 금액보다 많더라도
남은 금액은 통장에 집어넣고, 다시 K원 인출 가능!
*/

import java.util.*;
import java.io.*;

public class Main {
	static BufferedReader br;
	static StringTokenizer st;
	
	static int dayNum;  // N일 동안 사용할 금액
	static int withdrawGoal;  // 원하는 출금 횟수 (M)
	static int K;  // 현우는 통장에서 K원 인출
	static int[] useInfoArr;
	static int answer;  // 최종 답안 저장
	
	static boolean simulation(int K) { // 총 출금 횟수가 withdrawGoal을 넘지 않는지 확인한다. 
		// int account = 0;  // 통장 (문제상에만 언급되어있지, 사실상 필요 없어서 주석 처리)
		int wallet = 0; // 지갑
		int withdrawCount = 0; // 지금까지 출금 횟수
		boolean isAvailable = true; 
		
		for (int day=0; day<useInfoArr.length; day++) {
			if (wallet < useInfoArr[day]) { // 돈이 모자람
				// 남은 돈은 통장에
				// account += wallet;
				
				// wallet 갱신
				wallet = K;
				withdrawCount++;
				
				if (withdrawCount > withdrawGoal) {
					isAvailable = false;
					break; // [조기종료] 어짜피 지금 M을 넘었으므로 그 이후에 더 볼 필요가 없음
					       // 더 봐봤자, 불필요한 루프만 수행
				}
				
				// 돈 쓰기
				wallet = wallet - useInfoArr[day];
			}
			else { // 돈이 안모자람
				// 돈 쓰기
				wallet = wallet - useInfoArr[day];
			}
		}
		
		return isAvailable;
	}
	
	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		
		st = new StringTokenizer(br.readLine().trim());
		dayNum = Integer.parseInt(st.nextToken());
		withdrawGoal = Integer.parseInt(st.nextToken());
		
		// dayNum 동안의 사용 정보에 대한 배열 구성
		int maxUse = 0;
		int useInfoSum = 0;
		useInfoArr = new int[dayNum];
		for (int i=0; i<dayNum; i++) {
			int getUseInfo = Integer.parseInt(br.readLine().trim());
			useInfoArr[i] = getUseInfo;
			
			if (getUseInfo > maxUse) {
				maxUse = getUseInfo;
			}
			useInfoSum += getUseInfo;
		} 
		
		// [확인용 출력]
		// System.out.println(Arrays.toString(useInfoArr));
		// 출력 결과: [100, 400, 300, 100, 500, 101, 400]
		
		// 이분 탐색 진행
		int left = maxUse; // 하한
		int right = useInfoSum; // 상한
		
		while (left <= right) {
			int mid = (left + right) / 2;
			boolean isAvailable = simulation(mid); // K가 mid일 때, 가능한지 test하기 
			
			if (isAvailable == true) {
				// 더 작은 mid(=K)가 없는지 확인해본다. 
				// 일단 그 다음에 더 작게 했을 때, 불가능이 될수도 있으니 현재 답안 저장 
				answer = mid;
				right = mid - 1;
			}
			else { 
				// 현재 불가능하니, mid(=K)를 늘려 가능하게 해야함.
				left = mid + 1;
			}
		}
		System.out.println(answer);
	}
	
}
