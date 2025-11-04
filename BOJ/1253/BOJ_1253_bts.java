import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int count = Integer.parseInt(br.readLine());
        int[] arr = new int[count];
        String[] st = br.readLine().split(" ");
        for(int i=0;i<count;i++){
            arr[i]=Integer.parseInt(st[i]);
        }
        Arrays.sort(arr);
        int num;
        int left;
        int right;
        int sum;
        int ans=0;
        for(int i=0;i<count;i++){
            num = arr[i];
            left = 0;
            right = count-1;
            while(left<right){
                sum=arr[left]+arr[right];
                if(sum==num){
                    if(i!=left&&i!=right){
                    ans++;
                    break;
                    }
                    else if(i==left){
                        left++;
                    }
                    else if(i==right){
                        right--;
                    }
                }
                else if(sum>num){
                    right--;
                }
                else if(sum<num){
                    left++;
                }
            }
        }
        System.out.println(ans);
    }
}