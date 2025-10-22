#include <iostream>
#include <string>
#include <vector>
#include <unordered_map>
#include <algorithm>

using namespace std;

// genrePlayCount 정렬 메서드
bool playCountSort(pair<string, int> a, pair<string, int> b) {
    return a.second > b.second;
}

// generPlayNum 정렬 메서드
bool playNumSort(pair<int, int> a, pair<int, int> b) {
    if (a.first == b.first) return a.second < b.second;  // 재생수가 같으면 고유번호 오름차순
    return a.first > b.first;
}

vector<int> solution(vector<string> genres, vector<int> plays) {
    vector<int> answer;
    
    // unordered map을 이용해서 장르를 기반으로 하여 장르마다 재생수 담고
    // 장르마다, 고유번호와 재생횟수를 담기
    unordered_map<string, int> genrePlayCount;
    unordered_map<string, vector<pair<int, int>>> genrePlayNum;
    
    // 담긴 개수
    int n = genres.size();
    
    // 비교를 위한 값 담기
    for (int i = 0; i < n; i++) {
        genrePlayCount[genres[i]] += plays[i];
        genrePlayNum[genres[i]].push_back({plays[i], i});
    }
    
    // 정렬하기
    // 정렬은 vector로 변환을 해서 정렬해야함
    vector<pair<string, int>> playCountVec(genrePlayCount.begin(), genrePlayCount.end());
    sort(playCountVec.begin(), playCountVec.end(), playCountSort);
    
    // 많이 재생된 장르 순으로 정렬이 됨.
    // 이 정렬된 장르로 접근
    for (pair<string, int> order : playCountVec) {
        sort(genrePlayNum[order.first].begin(), genrePlayNum[order.first].end(), playNumSort);
        answer.push_back(genrePlayNum[order.first][0].second);
        
        if (genrePlayNum[order.first].size() > 1) {
            answer.push_back(genrePlayNum[order.first][1].second);
        }
    }
    
    return answer;
}