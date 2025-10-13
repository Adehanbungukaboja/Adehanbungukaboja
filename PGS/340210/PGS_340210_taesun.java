import java.util.*;
class Solution {
    public String[] solution(String[] expressions) {
        int maxNum =0;
        for(String s : expressions){
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    maxNum = Math.max(maxNum, (c - '0') + 1);
                }
            }
        }
        List<Integer>canUse = new ArrayList<>();
        for(int i=maxNum;i<=9;i++){
            boolean canMake= true;
            for(String s : expressions){
                if(s.contains("X")) continue;
                String[] st = s.split(" ");
                boolean plus=st[1].equals("+");
                int left = Integer.parseInt(st[0],i);
                int right = Integer.parseInt(st[2],i);
                int result = Integer.parseInt(st[4],i);
                if(plus){
                    if(left+right!=result){
                        canMake=false;
                        break;
                    }
                }
                else{
                    if(left-right!=result){
                        canMake=false;
                        break;
                    }
                }
            }
            if(canMake){
                canUse.add(i);
            }
        }
        List<String> list = new ArrayList<>();
        for(String s : expressions){
            if(s.contains("X")){
                Set<String>dupCheck = new HashSet<>();
                String[] st = s.split(" ");
                String leftStr = st[0];
                String sign = st[1];
                String rightStr = st[2];
                for(int num : canUse){
                    boolean plus=sign.equals("+");
                    int left = Integer.parseInt(leftStr,num);
                    int right = Integer.parseInt(rightStr,num);
                    int result;
                    if(plus){
                        result = left+right;
                    }
                    else{
                        result = left-right;
                    }
                    dupCheck.add(Integer.toString(result,num));
                }
                String ans;
                if(dupCheck.size()==1){
                    ans = dupCheck.iterator().next();
                }
                else{
                    ans="?";
                }
                list.add(new String(leftStr+" "+sign+" "+rightStr+" = "+ans));
            }
        }
        String[] answer = list.toArray(new String[0]);
        return answer;
    }
}