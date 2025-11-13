import java.util.*;
class Solution {
    static class Node{
        String word;
        int depth;
        Node(String word, int depth){
            this.word=word;
            this.depth=depth;
        }
    }
    static int length;
    public int solution(String begin, String target, String[] words) {
        length = begin.length();
        int answer = bfs(begin,target,words);
        return answer;
    }
    static int bfs(String begin, String target, String[] words){
        Queue<Node>q = new ArrayDeque<>();
        Set<String>visited = new HashSet<>();
        q.add(new Node(begin,0));
        
        while(!q.isEmpty()){
            Node cur = q.poll();
            if(target.equals(cur.word)){
                return cur.depth;
            }
            for(String next : change(cur.word,words)){
                if(visited.contains(next)) continue;
                visited.add(next);
                q.add(new Node(next,cur.depth+1));
            }
        }
        return 0;
    }
    static List<String> change(String word, String[] words){
        List<String> list = new ArrayList<>();
        for(String w : words){
            int diff=0;
            for(int i=0;i<length;i++){
                if(word.charAt(i)!=w.charAt(i)){
                    diff++;
                }
            }
            if(diff==1){
                list.add(w);
            }
        }
        return list;
    }
}