import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Map<Integer,Integer>map = new HashMap<>();
        Map<Integer,Integer>secondMap = new HashMap<>();
        String[] st;
        int first;
        int second;
        int count;
        int ans;
        StringBuilder sb = new StringBuilder();
        while(true){
            map.clear();
            secondMap.clear();
            st = br.readLine().split(" ");
            first = Integer.parseInt(st[0]);
            second = Integer.parseInt(st[1]);
            if(first==0&&second==0){
                break;
            }
            sb.append(first).append(" ").append(second).append(" ");
            count = 1;
            map.put(first,count);
            count++;
            while(true){
                first = getNum(first);
                if(map.containsKey(first)){
                    break;
                }
                else{
                    map.put(first,count);
                    count++;
                }
            }
            count = 1;
            secondMap.put(second,count);
            count++;
            while(true){
                second = getNum(second);
                if(secondMap.containsKey(second)){
                    break;
                }
                else{
                    secondMap.put(second,count);
                    count++;
                }
            }
            ans = Integer.MAX_VALUE;
            for(int firstnum : map.keySet()){
                for(int secondNum : secondMap.keySet()){
                    if(firstnum==secondNum){
                        ans = Math.min(ans,map.get(firstnum)+secondMap.get(secondNum));
                    }
                }
            }
            if(ans==Integer.MAX_VALUE){
                ans=0;
            }
            sb.append(ans).append("\n");
        }
        System.out.print(sb);
    }
    static int getNum(int num){
        int sum=0;
        int tmp;
        while(num>0){
            tmp=num%10;
            sum+=tmp*tmp;
            num/=10;
        }
        return sum;
    }
}
