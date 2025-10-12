#include <algorithm>
#include <iostream>
#include <vector>
#include <queue>
using namespace std;

// 판들의 입력 
int pan[5][5][5];
// 미로 저장 
int maze[5][5][5];
// 결과를 저장할 것임 - 최소 거리 
int ans = 987654321;

// 3차원 이동 
// 평면 상하좌우+ 층간이동 위아래
int dr[6] = {-1, 1, 0, 0, 0, 0};
int dc[6] = {0, 0, -1, 1, 0, 0};
int df[6] = {0, 0, 0, 0, -1, 1};

void printPan() {
    for (int f = 0; f < 5; f++) {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                cout << pan[f][r][c];
            }
            cout << '\n';
        }
        cout << '\n';
    }
}

void input() {
    // 5개의 판이 있고, 이를 위에서 본 상태로 25줄에 거쳐서 입력이 주어짐
    // 즉, 한 판에 5x5 크기를 의미함 
    for (int f = 0; f < 5; f++) {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                cin >> pan[f][r][c];
            }
        }
    }

}

// 유효한지 판단 -> 범위 안벗어나는지
bool inBoundary(int nf, int nr, int nc) {
    // 이 안에 있으면 true, 그렇지 않으면 false
    return (0 <= nr && nr < 5) && (0 <= nc && nc < 5) && (0 <= nf && nf < 5);
}

void BFS() {
    // 전부 회전시켜보고 갈 것이니깐
    // 출발지는 항상 완전 초기 0, 0, 0 
    // 도착지는 4, 4, 4

    // 출발지가 0이면 들어갈 수 없는 칸이니깐 출발 불가
    if (maze[0][0][0] == 0) return;

    // 거리 저장할 것
    vector<vector<vector<int>>> dist(5, vector<vector<int>>(5, vector<int>(5, -1)));
    // BFS할 큐
    queue<pair<int, pair<int, int>>> q;
    // 층(f), r, c
    q.push({0, {0, 0}});
    // 출발 거리는 0
    dist[0][0][0] = 0;

    // q가 빌때까지
    while (!q.empty()) {
        int cf = q.front().first;
        int cr = q.front().second.first;
        int cc = q.front().second.second;
        q.pop();

        // 방향 탐색
        for (int dir = 0; dir < 6; dir++) {

            int nf = cf + df[dir];
            int nr = cr + dr[dir];
            int nc = cc + dc[dir];

            // 범위 벗어나면 패스
            if (!inBoundary(nf, nr, nc)) continue;
            // 못지나가는 곳(판떼기가 0이면)이면 패스
            if (maze[nf][nr][nc] == 0)  continue;
            // 지나온길(-1이아니면)이면 패스
            if (dist[nf][nr][nc] != -1) continue;

            // 그렇지 않다면 갈 수 있는 곳임
            dist[nf][nr][nc] = dist[cf][cr][cc] + 1;
            q.push({nf, {nr, nc}});
        }
    }

    // 다 탐색했으니 최소값 갱신
    // 단, -1이 아닐때만 -1이 아니어야 도착지에 도착한 것임 
    if (dist[4][4][4] != -1)    ans = min(ans, dist[4][4][4]);
}

void rotate(int f) {
    // 입력된 층을 회전 시키기
    int tmp[5][5];
    
    // 입력된 층의 기본값을 저장하기
    for (int r = 0; r < 5; r++) {
        for (int c = 0; c < 5; c++) {
            tmp[r][c] = maze[f][r][c];
        }
    }

    // 90도씩 회전을 시키면 된다. 
    for (int r = 0; r < 5; r++) {
        for (int c = 0; c < 5; c++) {
            maze[f][r][c] = tmp[4-c][r];
        }
    }
}

void makeMaze() {
    // 쌓는 순서도 상관이 없네
    // 1 들어갈 수 있는 칸, 0 들어갈 수 없는 칸 
    
    // 1. 쌓는 순서 뭘로 할래? -> 넥퍼
    // 2. 쌓고 난 다음엔? 반복문으로 회전하기
    // 3. 회전이 끝나면? BFS 해보기 

    // 넥퍼를 위한 순서들 
    vector<int> perm = {0, 1, 2, 3, 4};
    do {
        // 넥퍼 순서대로 쌓여있음 
        // 그렇다면 이제 
        // 돌려봐야지
        
        // 그전에 입력된 것을 기준으로 순서대로 미로를 쌓음 -> 나온 순서대로 쌓기 
        for (int f = 0; f < 5; f++) {
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    maze[f][r][c] = pan[perm[f]][r][c];
                }
            }
        }

        // 다음이 이제 돌려 줍니다.
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                for (int c = 0; c < 4; c++) {
                    for (int d = 0; d < 4; d++) {
                        for (int e = 0; e < 4; e++) {
                            // 이미 최단 거리가 나왔으면 종료
                            if (ans == 12) return;

                            // 아래부터 회전 시키기
                            rotate(4);

                            // 현재 상태 계산하기 
                            BFS();
                        }
                        rotate(3);
                    }
                    rotate(2);
                }
                rotate(1);
            }
            rotate(0);
        }

    } while (next_permutation(perm.begin(), perm.end()));
}

int main() {
    ios::sync_with_stdio(0); cin.tie(0);

    // 입력을 합니닷
    input();

    // 미로 만드는 로직
    makeMaze();

    // 정답을 출력,
    // 초기값(큰값) 이면 -1, 그렇지 않으면 정답출력
    if (ans == 987654321)   ans = -1;
    cout << ans;

    return 0;
}