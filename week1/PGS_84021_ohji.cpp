#include <string>
#include <vector>
#include <iostream>
#include <queue>
#include <algorithm>

using namespace std;

struct Point {
    int x, y;
    bool operator<(const Point &oth) const { // 정렬 위해
        if (x != oth.x) return x < oth.x;
        return y < oth.y;
    }
    bool operator==(const Point &oth) const { // 같은지 비교 위해
        return x == oth.x && y == oth.y;
    }
};

int N;
int dx[4] = {-1, 0, 1, 0};
int dy[4] = {0, 1, 0, -1};
vector<vector<Point>> placeList, puzzleList[4];

void normalize(vector<Point> &v) { // 좌상단 0,0으로 & 좌표 순서 정렬
    int minx = 1e9, miny = 1e9;
    for (auto &p : v) { minx = min(p.x, minx); miny = min(p.y, miny); }
    for (auto &p : v) { p.x -= minx; p.y -= miny; }
    sort(v.begin(), v.end());
}

vector<Point> shape(int sx, int sy, vector<vector<int>> &board, int WALL) {
    vector<Point> v;
    queue<Point> q;
    q.push({sx, sy});
    v.push_back({sx, sy});
    board[sx][sy] = WALL;
    
    while (!q.empty()) {
        auto cur = q.front(); q.pop();
        
        for (int d = 0; d < 4; ++d) {
            int nx = cur.x + dx[d], ny = cur.y + dy[d];
            if (nx < 0 || nx >= N || ny < 0 || ny >= N) continue;
            if (board[nx][ny] == WALL) continue;
            
            q.push({nx, ny});
            v.push_back({nx, ny});
            board[nx][ny] = WALL;
        }
    }
    normalize(v); // 좌상단 0,0으로 하고 좌표 순서를 정렬
    return v;
}

vector<Point> rotate90(const vector<Point> &prev) {
    vector<Point> v;
    for (int i = 0; i < prev.size(); ++i) {
        v.push_back({prev[i].y, -prev[i].x});
    }
    normalize(v);
    return v;
}

void pushPuzzle(vector<Point> v) {
    for (int r = 0; r < 4; ++r) {
        puzzleList[r].push_back(v);
        v = rotate90(v);
    }
}

int solution(vector<vector<int>> gboard, vector<vector<int>> tboard) {
    N = gboard.size(); // game_board, table 행, 열 다 같음
    
    // 모양 모으기
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            if (gboard[i][j] == 0) {
                placeList.push_back(shape(i, j, gboard, 1));
            }
            if (tboard[i][j] == 1) {
                pushPuzzle(shape(i, j, tboard, 0));
            }
        }
    }

    // 퍼즐 넣기
    bool used[2502] = {}; // 퍼즐 썼는지 확인용
    int ans = 0;
    for (auto &place : placeList) {
        bool putted = false;
        for (int r = 0; r < 4 && !putted; ++r) {
            for (int i = 0; i < puzzleList[r].size(); ++i) {
                if (used[i]) continue;
                if (place == puzzleList[r][i]) {
                    used[i] = true;
                    putted = true;
                    ans += place.size();
                    break;
                }
            }
        }
    }

    return ans;
}
