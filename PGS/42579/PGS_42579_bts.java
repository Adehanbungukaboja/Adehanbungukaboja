import java.util.*;
class Solution {
    static class Music implements Comparable<Music>{
        int id;
        String genre;
        int plays;
        Music(int id, String genre, int plays){
            this.id=id;
            this.genre= genre;
            this.plays = plays;
        }
        @Override
        public int compareTo(Music o){
            if(this.plays==o.plays) return this.id-o.id;
            return o.plays-this.plays;
        }
    }
    public int[] solution(String[] genres, int[] plays) {
        int musicCount = genres.length;
        Map<String, PriorityQueue<Music>>map = new HashMap<>();
        TreeMap<String, Integer>playCount = new TreeMap<>();
        for(int i=0;i<musicCount;i++){
            if(!map.containsKey(genres[i])){
                map.put(genres[i],new PriorityQueue<>());
            }
            map.get(genres[i]).add(new Music(i,genres[i],plays[i]));
            playCount.put(genres[i],playCount.getOrDefault(genres[i],0)+plays[i]);
        }
        List<Integer>bestAlbum = new ArrayList<>();
        List<String>orders = new ArrayList<>(playCount.keySet());
        orders.sort((a,b) -> playCount.get(b)-playCount.get(a));
        for (String genre : orders) {
            PriorityQueue<Music> curPq = map.get(genre);
            for(int i=0;i<2;i++){
                if(!curPq.isEmpty()){
                    bestAlbum.add(curPq.poll().id);
                }
            }
        }
        int size = bestAlbum.size();
        int[] answer = new int[size];
        for(int i=0;i<size;i++){
            answer[i]=bestAlbum.get(i);
        }
        
        return answer;
    }
}