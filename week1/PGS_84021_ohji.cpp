#include <string>
#include <vector>
#include <iostream>
#include <climits>
#include <algorithm>
#include <queue>

using namespace std;

struct Point {
    int x, y;
    bool operator<(const Point &oth) const {
        if (x != oth.x) { return x < oth.x; }
        return y < oth.y;
    }
    bool operator==(const Point &oth) const {
        return x == oth.x && y == oth.y;
    }
};

vector<vector<int>> gboard, tboard;
vector<vector<Point>> placeList;
vector<vector<Point>> puzzleList[4];
bool used[2502];
int N;
int dx[4] = {-1, 0, 1, 0};
int dy[4] = {0, 1, 0, -1};

void normalize(vector<Point> &v) {
    int minx = INT_MAX, miny = INT_MAX;
    for (int i = 0; i < v.size(); ++i) { // minx, miny 찾고
        minx = min(minx, v[i].x);
        miny = min(miny, v[i].y);
    }
    
    for (int i = 0; i < v.size(); ++i) { // 각 좌표를 좌상단이 0,0이 되도록 바꿔줌
        v[i].x -= minx;
        v[i].y -= miny;
    }
    
    sort(v.begin(), v.end());
}

vector<Point> collectBFS(int sx, int sy, vector<vector<int>> &board, int WALL) { // bfs로 공간들 모아서 넘기기
    queue<Point> q; 
    vector<Point> v;
    q.push({sx, sy});
    v.push_back({sx, sy});
    board[sx][sy] = WALL;
    
    while (!q.empty()) {
        auto cur = q.front(); q.pop();
        
        for (int d = 0; d < 4; ++d) {
            int nx = cur.x + dx[d], ny = cur.y + dy[d];
            
            if (nx < 0 || nx >= N || ny < 0 || ny >= N) continue;
            if (board[nx][ny] == WALL) continue;
                
            board[nx][ny] = WALL;
            q.push({nx, ny});
            v.push_back({nx, ny});
        }
    }
    
    // v를 정규화 + 정렬 시켜서 넘기기 => 이래야 모양 일치하는지 비교하기 편함
    normalize(v);

    return v;
}

vector<Point> rotate90(const vector<Point> &prev) { // (x, y) -> (y, -x)
    vector<Point> v;
    for (int i = 0; i < prev.size(); ++i) {
        v.push_back({prev[i].y, -prev[i].x});
    }
    normalize(v);
    return v;
}

void init(const vector<vector<int>> &gb, const vector<vector<int>> &tb) {
    gboard = gb;
    tboard = tb;
    N = gboard.size();
}

bool putPuzzle(int placeIdx, int puzzleIdx) {
    for (int r = 0; r < 4; ++r) {
        if (placeList[placeIdx] == puzzleList[r][puzzleIdx]) {
            used[puzzleIdx] = true;
            return true;
        }
    }
    return false;
}

int solution(vector<vector<int>> gb, vector<vector<int>> tb) {
    init(gb, tb);
    
    // 빈 공간, 퍼즐 공간 리스트 생성
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            if (gboard[i][j] == 0) {
                vector<Point> v = collectBFS(i, j, gboard, 1);
                placeList.push_back(v);
            }
            
            if (tboard[i][j] == 1) {
                vector<Point> v = collectBFS(i, j, tboard, 0);
                puzzleList[0].push_back(v);
                int idx = puzzleList[0].size() - 1;
                // 퍼즐 회전
                for (int r = 1; r < 4; ++r) {
                    puzzleList[r].push_back(rotate90(puzzleList[r - 1][idx]));
                }
            }
        }
    }

    // 퍼즐 넣기
    int ans = 0;
    for (int placeIdx = 0; placeIdx < placeList.size(); ++placeIdx) {
        for (int puzzleIdx = 0; puzzleIdx < puzzleList[0].size(); ++puzzleIdx) {
            if (used[puzzleIdx]) continue;
            if (putPuzzle(placeIdx, puzzleIdx)) {
                ans += placeList[placeIdx].size();
                break;
            }
        }
    }

    return ans;
}
