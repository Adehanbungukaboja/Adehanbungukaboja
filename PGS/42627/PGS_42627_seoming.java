import java.util.*;
import java.io.*;

class Solution {
    static Taskinfo[] taskArr;
    static int taskInfoCount;

    static class Taskinfo implements Comparable<Taskinfo> { // 작업 정보
        int taskNum; // 작업의 번호
        int requestTime; // 작업의 요청 시간
        int workingTime; // 작업의 소요 시간
        int returnTime; // 반환 시간 (용어: 반환 시간 = 작업완료시간 - 요청시간)

        public Taskinfo(int taskNum, int requestTime, int workingTime) {
            this.taskNum = taskNum;
            this.requestTime = requestTime;
            this.workingTime = workingTime;
        }

        @Override
        public int compareTo(Taskinfo o) { // default 정렬 기준 (별 다른 정렬 기준을 설정하지 않으면, 이 기준으로 정렬됨)
            return this.requestTime - o.requestTime; // 요청 시간 기준 오름차순
        }
    }

    static void Scheduling() { // 비선점형 스케줄링: 평균 대기시간 반환
        /*
         * <필요한 알고리즘: 우선순위 큐>
         * - if) "꺼내는 기준이 항상 정해져 있다면" --> 그냥 한 번 처음에 정렬해서 순서대로 처리하면 된다.
         * - if) "하지만 새로운 데이터가 들어올 때마다 우선순위(정렬 기준)이 계속 바뀐다면" --> 우선순위 큐가 필요하다.
         * 
         * 즉, 정렬이 동적으로 변하는 상황에서는 우선순위 큐가 필요하다.
         */

        /*
         * <우선순위>
         * 1. 작업의 소요시간이 짧은 것
         * 2. 작업의 요청 시각이 빠른 것
         * 3. 작업의 번호가 작은 것
         */

        /*
         * <PriorityQueue 안에 정렬 기준(Comparator)을 람다식으로 바로 정의하는 방식>
         * PriorityQueue 생성자의 인자 타입이 이미 Comparator로 정해져 있어서, 거기에 람다식을 넣으면
         * 자바가 "아~ 이건 정렬 기준(compare 함수)이구나"하고 자동으로 알아들음. (그렇다고 함?! 사실 나는 잘 모름 ㅠㅠ pq 어려워)
         */
        PriorityQueue<Taskinfo> waitingPQ = new PriorityQueue<>((a, b) -> {
            if (a.workingTime != b.workingTime) {
                return a.workingTime - b.workingTime; // 오름차순
            } else if (a.requestTime != b.requestTime) {
                return a.requestTime - b.requestTime; // 오름차순
            } else {
                return a.taskNum - b.taskNum; // 오름차순
            }
        });

        int time = 0; // 현재 시간
        int checkArriveTaskIdx = 0; // 현재 checkArriveTaskIdx 의 작업을 아직 큐에 안넣음 (아직 도착 안함)
        int doneTaskCount = 0; // 작업이 끝난 task 개수

        while (true) {
            // 현재 시간에 도착한 작업은 PQ에 삽입
            /*
             * Q. 왜 taskArr[checkArriveTaskIdx].reqeustTime <= time 에서 <= 이어야 하냐? == 이면 안되냐?
             * A. 만약에 == 일때만 추가한다고 하면, 현재 시각(time)과 요청 시각(requestTime)이 딱 같을 때만 추가된다.
             * 문제는, 작업을 하나 끝내면 time값이 점프해서 훅 커진다. 그러면 누락되는 경우들이 생길수도...
             */
            while ((checkArriveTaskIdx < taskInfoCount) && (taskArr[checkArriveTaskIdx].requestTime <= time)) {
                waitingPQ.offer(taskArr[checkArriveTaskIdx]); // 작업 정보를 큐에 넣기
                checkArriveTaskIdx++;
            }

            // 본격적인 작업 시작! (그러기 전에 당연히 작업들이 큐에 다 정리가 되어있어야겠지?ㅎㅎ)
            if (!waitingPQ.isEmpty()) { // 큐에 뭐라도 있다면 (=지금 일할 수 있는 애가 있다)
                Taskinfo getTask = waitingPQ.poll(); // 가장 우선순위 높은 작업을 꺼내옴

                // 비선점이므로, task가 끝날 때까지 쭉 수행
                time += getTask.workingTime;

                // 반환 시간 계산
                getTask.returnTime = time - getTask.requestTime;

                // 완료된 작업 수 카운트
                doneTaskCount++;

                // 만약에 모든 작업을 다 완료했다면,
                if (doneTaskCount == taskInfoCount) {
                    break; // while문 탈출
                }
            } else { // 대기 큐가 비었다면,
                // [상황1] 아직 모든 작업이 도착하지 않았음 ex. A, B, C 셋 다 아직 시작 전...
                // [상황2] 다음 작업 도착을 기다리는 중 ex. A,B는 이미 수행되었는데, C는 아직 도착도 안함

                // CHOICE 1) 1초씩 증가 (느릴 수 있음)
                // time++;

                // CHOICE 2) 더 효율적으로: 다음 작업 도착 시각으로 점프
                if (checkArriveTaskIdx < taskInfoCount) {
                    time = taskArr[checkArriveTaskIdx].requestTime; // OPTIONAL
                }
            }
        }
    }

    public int solution(int[][] jobs) { // ex) jobs: [[0, 3], [1, 9], [3, 5]]
        /*
         * <taskArr 배열에 Taskinfo 넣어놓기>
         * 물론 ArrayList로 관리해줘도 되지만, jobs의 길이를 미리 알 수 있으므로 그냥 배열로 처리하겠음.
         */

        taskInfoCount = jobs.length;
        taskArr = new Taskinfo[taskInfoCount];
        for (int i = 0; i < taskInfoCount; i++) {
            taskArr[i] = new Taskinfo(i, jobs[i][0], jobs[i][1]);
        }

        // 요청 시간 기준 오름차순 정렬
        Arrays.sort(taskArr); // 이걸 호출해야 compareTo 기준으로 정렬됨

        // 비선점형 스케줄링(한번 작업한 작업은 비선점형이여서 다시 대기큐에 안넣어도 됨)
        Scheduling(); // 각 작업의 returnTime(턴어라운드)이 채워짐

        // 반환 시간 더하기
        int totalReturnTime = 0; // 반환 시간 합
        for (Taskinfo t : taskArr) {
            totalReturnTime += t.returnTime;
        }

        return totalReturnTime / taskInfoCount; // 평균 반환 시간 반환 (정수/정수 하면 알아서 몫만 계산함, 소수점 아래 버림)
        // 뭐 정석대로 하고 싶으면 return (int)(totalReturnTime / taskInfoCount)
    }
}
