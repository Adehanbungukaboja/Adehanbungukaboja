import java.util.*;
class Solution {
    public class Job implements Comparable<Job>{
        int time;
        int cost;
        int id;
        Job(int time, int cost, int id){
            this.time=time;
            this.cost=cost;
            this.id=id;
        }
        @Override
        public int compareTo(Job o){
            if(this.cost==o.cost){
                if(this.time==o.time){
                    return this.id-o.id;
                }
                return this.time-o.time;
            }
            return this.cost-o.cost;
        }
    }
    public int solution(int[][] jobs) {
        //디스크 컨트롤러를 pq로 설정
        PriorityQueue<Job>pq = new PriorityQueue<>();
        //디스크 컨트롤러에 넣을 pq를 따로 설정
        PriorityQueue<Job>pqByTime = new PriorityQueue<>(
            (p1, p2) -> p1.time-p2.time
        );
        int length = jobs.length;
        for(int i=0;i<jobs.length;i++){
            pqByTime.add(new Job(jobs[i][0],jobs[i][1],i));
        }
        
        //첫번째 작업 시간부터 시작
        int time=pqByTime.peek().time;
        //만약에 같은 시작시간이 여러개라면 다 넣어놓기
        while(!pqByTime.isEmpty()&&pqByTime.peek().time<=time){
            pq.add(pqByTime.poll());
        }
        int endTime;
        int ans=0;
        while(!pq.isEmpty()){
            Job cur = pq.poll();
            System.out.println(cur.time+" "+cur.cost);
            endTime = time+cur.cost;
            ans+=endTime-cur.time;
            time=endTime;
            //pq가 비었으면 다음 요청을 받기위해 시간을 조절함
            if(pq.isEmpty()&&!pqByTime.isEmpty()){
                time = Math.max(time,pqByTime.peek().time);
            }
            //해당시간 이하의 시간에 요청한것들 다 다시 pq에 넣기
            while(!pqByTime.isEmpty()&&pqByTime.peek().time<=time){
                pq.add(pqByTime.poll());
            }
        }
        
        int answer = ans/length;
        return answer;
    }
}