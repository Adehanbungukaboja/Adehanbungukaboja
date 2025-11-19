import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{
    static final int INF = 1_000_000_000;
    static int[][][] memo = new int[101][101][101];
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for(int[][] arr1 : memo){
            for(int[] arr2 : arr1){
                Arrays.fill(arr2,INF);
            }
        }
        int a;
        int b;
        int c;
        int ans;
        StringBuilder sb = new StringBuilder();
        String[] st;
        while(true){
            st = br.readLine().split(" ");
            a = Integer.parseInt(st[0]);
            b = Integer.parseInt(st[1]);
            c = Integer.parseInt(st[2]);
            if(a==-1&&b==-1&&c==-1) break;
            ans = w(a,b,c);
            sb.append("w(").append(a).append(", ").append(b).append(", ").append(c)
            .append(") = ").append(ans).append("\n");
        }
        System.out.print(sb);
    }
    static int w(int a,int b,int c){
        int result;
        if(memo[a+50][b+50][c+50]!=INF){
            result = memo[a+50][b+50][c+50];
        }
        else if(a<=0||b<=0||c<=0){
            result = 1;
        }
        else if(a>20||b>20||c>20){
            result = w(20,20,20);
        }
        else if(a<b&&b<c){
            result = w(a,b,c-1)+w(a,b-1,c-1)-w(a,b-1,c);
        }
        else{
            result = w(a-1,b,c)+w(a-1,b-1,c)+w(a-1,b,c-1)-w(a-1,b-1,c-1);
        }
        memo[a+50][b+50][c+50]= result;
        return result;
    }
}