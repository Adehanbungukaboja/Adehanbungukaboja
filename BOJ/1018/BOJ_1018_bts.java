import java.io.*;
import java.util.*;

public class Main {
    static long[] map;
    //w는1로 b는 0으로
    static int wb = 0b10101010;
    static int bw = 0b01010101;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] st = br.readLine().split(" ");
        int maxRow = Integer.parseInt(st[0]);
        int maxCol = Integer.parseInt(st[1]);
        map = new long[maxRow];
        String s;
        for(int i=0;i<maxRow;i++){
            s = br.readLine();
            for(int j=maxCol-1;j>=0;j--){
                char c = s.charAt(j);
                if(c=='W'){
                    //long으로 하려면 1L을 밀어야 오버플로우가 나지않음
                    map[i]|=(1L<<j);
                }
            }
        }
        int count;
        int min=maxRow*maxCol;
        
        for(int i=0;i<=maxRow-8;i++){
            for(int j=0;j<=maxCol-8;j++){
                count = check(i,j);
                min = Math.min(min,count);
            }
        }
        System.out.println(min);
    }
    static int check(int row, int col){
        int sum1=0;
        int sum2=0;
        for(int i=row;i<row+8;i++){
            long target = (map[i] >> col) & 0xFFL;//8자까지나오게 255와 and연산
            if(i%2==0){
                //8자리의 비트에 ^연산을해서 1의갯수를 세면 다른칸의 갯수가 나옴
                sum1+=Long.bitCount(target^bw);
                sum2+=Long.bitCount(target^wb);
            }
            else{
                sum1+=Long.bitCount(target^wb);
                sum2+=Long.bitCount(target^bw);
            }
        }
        return Math.min(sum1,sum2);
    }
}
