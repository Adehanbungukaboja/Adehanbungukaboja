import java.io.*;
import java.util.*;

/*
    현재는 장르가 두가지니까 정답 배열 사이즈 = 4
    클래식은 총 1450번, 팝은 총 3100 이므로
    팝 2곡 먼저 수록
    -> 이 곡은 장르 내에서 많이 재생된 순으로 수록
    -> answer=[4, 1]
    나머지 두곡을 클래식에서 가져올 껀데
    클래식은 0: 5.0, 2: 150, 3: 800 이므로
    ->  answer =[4, 1, 3, 0]

*/
class Solution {
    public int[] solution(String[] genres, int[] plays) {
        int[] answer;

        // 일단 장르 종류 구별?
        Map<String, List<Song>> songMap = new HashMap<>();

        for(int idx = 0; idx < genres.length; idx++){
            //현재 정보로 Song 객체 생성
            Song song = new Song(genres[idx], plays[idx], idx);

            // 현재 장르가 없으면 key값에 저장
            if(!songMap.containsKey(genres[idx])){
                songMap.put(genres[idx], new ArrayList<Song>());
            }
            //있으면 해당 맵을 꺼내서 List에 저장
            songMap.get(genres[idx]).add(song);
        }

        /*
            Map
            classic -> {classic, 500, 0}, {classic, 150, 2}, {classc, 800, 3}
            pop -> {pop, 600, 1}, {pop, 2500, 4}
        */

        // 먼저 넣을 장르 순서 정하기
        // 맵에서 각 장르별 플레이 수를 더함
        // 더한 값을 Map에 <장르, 총 플레이 수> 형태로 저장
        Map<String, Integer> playMap = new HashMap<>();
        // map을 플레이 수로 정렬
        for(String genre : songMap.keySet()){
            //System.out.println("Genre: " + genre);
            int totalPlay = 0;
            for(Song s: songMap.get(genre)){
                totalPlay += s.play;
            }

            playMap.put(genre, totalPlay);
        }
        // 정렬된 map에서 key값을 받아온 다음에 해당 key의 List에서 인덱스 2개씩 answer에 넣기
        // map은 정렬이 안된다네..Map.Entry를 List로 변형해서 정렬을 해야된다는...
        // entrySet()은 map의 (key, value)를 쌍으로 반환
        List<Map.Entry<String, Integer>> genreList = new ArrayList<>(playMap.entrySet());
        // 정렬
        Collections.sort(genreList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b){
                // getValue -> map에 저장되어있던 totalPlay 수
                return b.getValue() - a.getValue();
            }
        });

        // 총 플레이수가 큰 장르부터 answer에 저장
        List<Integer> result = new ArrayList<>();

        for(Map.Entry<String, Integer> entry : genreList){
            String genre = entry.getKey();

            // 맵에 저장된 Song리스트 가져오기
            List<Song> songList = songMap.get(genre);
            // Song객체에 정의되어있는 정렬값으로 정렬
            Collections.sort(songList);

            // 상위 두곡의 인덱스만 answer에 저장 -> 한 장르에 2곡이 없을 수도 있음
            if(songList.size() < 2){
                result.add(songList.get(0).index);
            }else{
                for(int i = 0; i < 2; i++){
                    result.add(songList.get(i).index);
                }
            }
        }

        // answer 배열의 사이즈를 result의 사이즈로 지정 -> 한 장르에 2곡이 없을 수도 있음
        answer = new int[result.size()];
        // 정답 리스트를 배열에 저장
        for(int i = 0; i < result.size(); i++){
            answer[i] = result.get(i);
        }


        return answer;

    }

    static class Song implements Comparable<Song>{
        String genre;
        int play;
        int index;

        public Song(String genre, int play, int index){
            this.genre = genre;
            this.play = play;
            this.index = index;
        }

        @Override
        public int compareTo(Song o){
            // 만약 재생횟수가 같으면 index가 작은 순
            if(this.play == o.play){
                return Integer.compare(this.index, o.index);
            }
            return Integer.compare(this.play, o.play) * -1;
        }
    }
}