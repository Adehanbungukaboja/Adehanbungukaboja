#include <string>
#include <vector>
#include <queue>
#include <iostream>

/*
알파벳 하나만 바꿔야함
1. 한 글자만 다른 단어들 큐에 넣기
2. 큐에 있는 단어들 방문하고 다음 거 방문 -> BFS
*/

using namespace std;

#define STR first
#define DIST second

bool isOneDiff(string a, string oth) {
    int cnt = 0;
    for (int i = 0; i < a.size() && oth.size(); ++i) {
        if (a[i] != oth[i]) ++cnt;
        if (cnt > 1) return false;
    }
    return cnt == 1;
}

int solution(string begin, string target, vector<string> words) {
    queue<pair<string, int>> q;
    bool visit[52] = {};
    q.push({begin, 0});
    
    while (!q.empty()) {
        auto cur = q.front(); q.pop();
        cout << cur.STR << " " << cur.DIST << "\n";
        if (cur.STR == target) {
            return cur.DIST;
        }
        // words 돌면서 한 글자만 다른 단어 넣기
        for (int i = 0; i < words.size(); ++i) {
            if (visit[i]) continue;
            if (isOneDiff(cur.STR, words[i])) {
                q.push({words[i], cur.DIST + 1});
                visit[i] = true;
            }
        }
    }
    
    return 0;
}