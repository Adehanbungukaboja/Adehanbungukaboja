#include <iostream>
#include <functional>
#include <algorithm>
#include <string>
#include <vector>
#include <unordered_map>

using namespace std;

vector<int> solution(vector<string> genres, vector<int> plays) {
    vector<int> answer;

    unordered_map<string, int> total;
    unordered_map<string, vector<pair<int, int>>> list; // idx, plays
    for (int i = 0; i < genres.size(); ++i) {
        total[genres[i]] += plays[i];
        list[genres[i]].push_back({i, plays[i]});
    }
    
    // total을 정렬하고
    vector<pair<string, int>> total_v(total.begin(), total.end());
    sort(total_v.begin(), total_v.end(), [](auto &a, auto &b) {
        return a.second > b.second;
    });
    
    // list에서 total순서대로 키값을 뽑아내기
    for (auto &t : total_v) {
        string genre = t.first;
        
        auto &songs = list[genre];
        // play는 내림차, idx는 오름차
        sort(songs.begin(), songs.end(), [](auto &a, auto &b) {
            if (a.second != b.second) return a.second > b.second;
            return a.first < b.first;
        });
        
        // 2개만 반환
        answer.push_back(songs[0].first);
        if (songs.size() > 1) answer.push_back(songs[1].first); // 곡이 하나일 수도 있음
    }
        
    return answer;
}
