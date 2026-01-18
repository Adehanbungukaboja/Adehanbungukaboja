import java.io.*;
import java.util.*;

public class D0118_BOJ_16953 {

    static BufferedReader br;
    static StringTokenizer st;

    static int A, B;

    static void input() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        A = Integer.parseInt(st.nextToken());
        B = Integer.parseInt(st.nextToken());
    }

    static int calculate() {
        int count = 0;

        // B에서 2를 나누거나 뒤에서 1을 제거하는 형식으로 진행할 것
        while (A <= B) {
            if (A == B) {
                // 같아 졌으니 바로 결과 반환하고 탈출
                return count + 1;
            }

            // 조건 1. 2로 나누어떨어지는가?
            if (B % 2 == 0) {
                B /= 2;
            }
            else {
                // 조건 2. 뒤에자리가 1인가
                if (B % 10 == 1) {
                    B /= 10;
                }
                // 조건 3. 아무것도 만족안함.. 불가 판정. -1 return
                else {
                    return -1;
                }
            }

            count++;
        }

        // 반복문을 나왔다는건 B가 A보다 작아졌다는 것.. 만족 못함 -1 return
        return -1;
    }

    public static void main(String[] args) throws Exception {
        /*
        A와 B가 주어지면 A에서 B를 만드는 문제
        가능한 연산은
        2를 곱하거나
        그냥 뒤에 1을 추가하는 것

        이를 역으로 연산을 생각해보면
        1. B가 2로 나누어지는가 -> 나누어지면 2로 나눔
        2. 뒤의 숫자가 1인가 -> 1을 떼버림 10으로 나눴을 때의 몫
        3. 둘 다 성립 안하면 만들 수 있는 숫자가 아니니 중간에 중단하기

        이러한 조건을 잡고 진행..
        결과는 연산의 최솟값에 1을 더한 값임. 불가능하면 -1을 출력
         */

        // 입력 받기
        input();

        // 계산 진행
        int answer = calculate();

        // 결과 출력
        System.out.print(answer);
    }
}
