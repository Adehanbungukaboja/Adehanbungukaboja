#include <string>
#include <vector>
#include <queue>
#include <algorithm>
#include <climits>
#include <functional>
#include <iostream>

// 회전을 해서 꼭 들어맞는다면 무조건 넣기
// 퍼즐 그룹핑을 하기
// 4회전 하면서 넣을 수 있는 퍼즐이면 넣고 체크하기

// 퍼즐 시작점, 그룹핑을 하고
// 빈 공간 그룹핑을 해서
// 회전시키면서 꼭 들어맞는 거 몇 개인지 체크하기

using namespace std;

struct Point {
    int x, y;
};

int board[52][52];
int table[52][52];
Point puzzles[2502]; // 퍼즐 리스트
int whichPuzzle[2502]; // 빈 공간에 몇 번 퍼즐 넣었는지 체크용
Point place[2502]; // 빈 공간 리스트
int N, P, E;
int dx[4] = {-1, 0, 1, 0};
int dy[4] = {0, 1, 0, -1};

void puzzleBFS(int sx, int sy) {
    ++P; // 퍼즐 번호 업데이트
    puzzles[P].x = sx; // 퍼즐 찾기 쉽게 넣어놓기
    puzzles[P].y = sy;

    queue<Point> q;
    q.push({sx, sy});
    table[sx][sy] = P;
    
    while (!q.empty()) {
        auto cur = q.front(); q.pop();
        
        for (int d = 0; d < 4; ++d) {
            int nx = cur.x + dx[d], ny = cur.y + dy[d];
            if (nx < 0 || nx >= N || ny < 0 || ny >= N) continue;
            if (table[nx][ny] != -1) continue;
            
            table[nx][ny] = P;
            q.push({nx, ny});
        }
    }
}

void grouping() { // table에 놓인 퍼즐 그룹핑
    int tmp[52][52] = {};
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            if (table[i][j] == -1) {
                puzzleBFS(i, j);
            }
            
        }
    }

    // for (int i = 0; i < N; ++i) {
    //     for (int j = 0; j < N; ++j) {
    //         cout << table[0][i][j] << ' ';
    //     }
    //     cout << '\n';
    // }
}

void rotate() {
    int newTable[52][52];

    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            newTable[j][N - 1 - i] = table[i][j];
        }
    }
    
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            table[i][j] = newTable[i][j];
        }
    }
}

void simulation() {
    // 퍼즐 넣기
    for (int r = 0; r < 4; ++r) {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                
            }
        }
        rotate();
        
        // for (int i = 0; i < N; ++i) {
        //     for (int j = 0; j < N; ++j) cout << table[i][j] << ' ';
        //     cout << '\n';
        // }
        // cout << '\n';
    }

    
}

void isOK(int bx, int by, int p) {
    // 둘이 같이 bfs하면서 서로 같은 모양이면 ok
    bool vis[]
    queue<Point> q;
    q.push({bx, by});
    
    while (!q.empty()) {
        if (board[])
    }
}

void init(vector<vector<int>> a, vector<vector<int>> b) {
    N = a.size();
    
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            board[i][j] = -a[i][j];
            table[i][j] = -b[i][j]; // -1로 받아버리기
        }
    }
}

int solution(vector<vector<int>> a, vector<vector<int>> b) {
    int answer = -1;
    init(a, b);

    grouping();
    simulation();
    
    return answer;
}
