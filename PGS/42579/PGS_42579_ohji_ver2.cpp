#include <iostream>
#include <functional>
#include <algorithm>
#include <string>
#include <vector>
#include <queue>
#include <unordered_map>

using namespace std;

struct Song {
    int idx, play;
    bool operator<(const Song &oth) const {
        if (play != oth.play) return play < oth.play; // 최대힙 고려.
        return idx > oth.idx;
    }
};

vector<int> solution(vector<string> genres, vector<int> plays) {
    vector<int> answer;

    unordered_map<string, int> total;
    unordered_map<string, priority_queue<Song>> list;
    for (int i = 0; i < genres.size(); ++i) {
        total[genres[i]] += plays[i];
        list[genres[i]].push({i, plays[i]});
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
        int idx = 0; // 최대 2개까지 반환
        while (!songs.empty() && idx < 2) {
            answer.push_back(songs.top().idx);
            songs.pop();
            ++idx;
        }
    }
        
    return answer;
}
