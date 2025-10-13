/*
1. 문제 확인
- 호석이는 N의 배수 번호인 양을 세기로 함
   - 즉, 첫 번째에는 N번 양을 세고, 두 번째에는 2N번 양, … , k번째에는 kN번 양을 센다.
- 각 자리수에서 0에서 9까지의 모든 숫자를 보는 것은 최소 몇 번 양을 센 시점일까?

2. 목표
- 최소 몇 번 양을 세었을 때 이전에 봤던 숫자들의 자릿수에서 0에서 9까지의 모든 숫자를 보게 되는지 출력한다.
   - 호석이는 xN번 양을 세고 있다.
   
3. 문제에서 언급한 예시 이해하기
ex) N = 1295
- 첫 번째로 N = 1295번 양을 센다
   - 본 숫자: 1, 2, 5, 9
- 두 번째로 2N = 2590번 양을 센다
   - 본 숫자: 0, 2, 5, 9
- 세 번째로 3N = 3885번 양을 센다
   - 본 숫자: 3, 5, 8
- 네 번째로 4N = 5180번 양을 센다
   - 본 숫자: 0, 1, 5, 8
- 다섯 번째로 5N = 6475번 양을 센다
   - 본 숫자: 4, 5, 6, 7

=> 다섯 번째까지 살펴봤을 때, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9를 봤다. 
   즉, 5N번까지 양을 세면 0~9까지 모든 숫자를 보게 되므로 호석이는 양 세기를 멈춘다. 
=> 정답은 xN = 5*1295 = 6475

4. 알고리즘 접근
비트마스킹
*/

import java.util.*;
import java.io.*;

public class Solution {
	static StringBuilder sb;
	static StringTokenizer st;
	static BufferedReader br;
	static int N; 
	static int seen;
	
	static final int TARGET = (1 << 10) - 1; // 0~9까지 모두 본 상태
	
	public static void main(String[] args) throws Exception {
		sb = new StringBuilder();
		br = new BufferedReader(new InputStreamReader(System.in));
		
		int T = Integer.parseInt(br.readLine().trim());
		for (int t=1; t<=T; t++) {
			N = Integer.parseInt(br.readLine().trim());
			
			/*
			 - seen: 봤던 숫자 기록용 비트마스크
			    - 맨 처음에는 아직 아무 숫자로 못 봤으니 0으로 저장
			    - 새로운 숫자를 볼 때마다 해당 위치의 숫자 비트를 1로 바꿔주면 됨
			 - k: N의 몇 번째 배수인지 
			    - N, 2N, 3N, 4N, … 이렇게 곱하면서 계속 새로운 숫자가 나오는지 확인하잖아?
			    - 그때 지금 몇 번째 배수를 보고 있는지 저장 
			 */
			seen = 0;
			int k=0;
			for (k=1; ; k++) {
				/*
				 * - String.valueOf(숫자값): 숫자값을 문자열로 바꿔줌
				 *    - ex) String.valueOf(5535) --> "5535"
				 * - .toCharArray(): 문자열 --> 문자 배열(char[])로 변환
				 *    - ex) {'5', '5', '3', '5'}
				 */
				char[] arr = String.valueOf(N*k).toCharArray(); // N*k 값을 문자열로 표현한 것
				for (char c : arr) {
					int num = c - '0';  // 문자 --> 숫자
					seen = seen | (1<<num); // 각 숫자에 대해 등장했다는 의미로 bit를 1로 변경
				}
				
				if (seen == TARGET) {  // 모든 숫자가 등장했다면, 종료
					break;
				}
			}
			
			sb.append("#").append(t).append(" ").append(k*N).append("\n");
		}
		System.out.println(sb);
	}
}
