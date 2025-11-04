import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] st = br.readLine().split(" ");
        int days = Integer.parseInt(st[0]);
        int limit = Integer.parseInt(st[1]);
        int[] arr = new int[days];
        int sum=0;
        int max=0;
        for(int i=0;i<days;i++){
            arr[i]=Integer.parseInt(br.readLine());
            max = Math.max(max,arr[i]);
            sum+=arr[i];
        }
        int left=max;
        int right=sum;
        int mid;
        int curMoney;
        int ans=0;
        while(left<=right){
            mid =(left+right)/2;
            int count=1;
            curMoney=mid;
            for(int i=0;i<days;i++){
                if(curMoney<arr[i]){
                    curMoney=mid;
                    count++;
                }
                curMoney-=arr[i];
            }
            if(count<=limit){
                ans=mid;
                right=mid-1;
            }
            else{
                left=mid+1;
            }
        }
        System.out.println(ans);
    }
}