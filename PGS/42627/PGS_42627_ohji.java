import java.io.*;
import java.util.*;

public class PGS_42627_ohji {
  public int solution(int[][] jobs) {
        // 시간순으로 먼저 정렬하기. 문제에서 jobs 입력이 시간순 보장이 아님. 그래서 sort 따로 해줘야 함.
        Arrays.sort(jobs, (a, b) -> a[0] - b[0]);
        
        PriorityQueue<int[]> pq = new PriorityQueue<>(
            (a, b) -> a[1] != b[1] ? a[1] - b[1] : a[0] - b[0]
        );
        
        int curr = 0;
        int idx = 0;
        int total = 0;
        int count = 0;
        while (count < jobs.length) {
            // 현재까지 도착한 요청들 pq에 담기
            while (idx < jobs.length && jobs[idx][0] <= curr) {
                pq.offer(jobs[idx++]);
            }
            
            // 도착한 애들 없다면 다음 도착 애로 continue
            if (pq.isEmpty()) {
                curr = jobs[idx][0];
                continue;
            }

            // pq에서 수행
            int[] job = pq.poll();
            ++count;
            curr += job[1]; // 현재시각 = 이전시각 + 소요시간
            total += curr - job[0]; // 종료 - 요청
        }
        return total / jobs.length;
    }
}
