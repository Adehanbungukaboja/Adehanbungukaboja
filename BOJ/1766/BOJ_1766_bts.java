import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] st = br.readLine().split(" ");
        int count = Integer.parseInt(st[0]);
        int firstCount = Integer.parseInt(st[1]);
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        int first;
        int second;
        int[] need = new int[count+1];
        Map<Integer,List<Integer>>map = new HashMap<>();
        List<Integer>list;
        for(int i=0;i<firstCount;i++){
            st = br.readLine().split(" ");
            first = Integer.parseInt(st[0]);
            second = Integer.parseInt(st[1]);
            if(map.get(first)==null){
                map.put(first,new ArrayList<Integer>());
            }
            list=map.get(first);
            list.add(second);
            need[second]++;
        }
        for(int i=1;i<=count;i++){
            if(need[i]==0){
                pq.add(i);
            }
        }
        int cur;
        StringBuilder sb = new StringBuilder();
        boolean[] visited = new boolean[count+1];
        while(!pq.isEmpty()){
            cur = pq.poll();
            if(!visited[cur]){
                visited[cur]=true;
                sb.append(cur).append(" ");
            }
            if(map.containsKey(cur)){
                list = map.get(cur);
                for(int next : list){
                    need[next]--;
                    if(need[next]==0){
                        pq.add(next);
                    }
                }
            }
        }
        System.out.println(sb);
    }
}
