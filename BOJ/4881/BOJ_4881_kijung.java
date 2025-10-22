package BOJ_4881;

import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        while (true) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            if (a == 0 && b ==0) break;

            sb.append(a + " " + b + " ");

            // a의 싸이클 만들기
            List<Integer> aList = new ArrayList<>();
            Set<Integer> aSet = new HashSet<>();
            aList.add(a);
            aSet.add(a);

            while (true) {
                int nextNum = getNext(a);
                if (aSet.contains(nextNum)) break;
                aList.add(nextNum);
                aSet.add(nextNum);
                a = nextNum;
            }


            // b의 싸이클 만들기
            List<Integer> bList = new ArrayList<>();
            Set<Integer> bSet = new HashSet<>();
            bList.add(b);
            bSet.add(b);

            while (true) {
                int nextNum = getNext(b);
                if (bSet.contains(nextNum)) break;
                bList.add(nextNum);
                bSet.add(nextNum);
                b = nextNum;
            }

            sb.append(getAnswer(aList, bList) + "\n");
        }

        System.out.println(sb.toString());
    }

    static int getNext(int input) {
        int sum = 0;

        while (input != 0) {
            int mod = input % 10;
            sum += mod * mod;
            input /= 10;
        }

        return sum;
    }

    // 같은거 나타나는 위치 찾기
    static int getAnswer(List<Integer> aList, List<Integer> bList) {
        int answer = Integer.MAX_VALUE;

        for (int i = 0; i < aList.size(); i++) {
            int a = aList.get(i);
            for (int j = 0; j < bList.size(); j++) {
                int b = bList.get(j);
                if (a == b) {
                    answer = Math.min(answer, (i + 1) + (j + 1));
                }
            }
        }

        if (answer == Integer.MAX_VALUE) answer = 0;
        return answer;
    }
}

