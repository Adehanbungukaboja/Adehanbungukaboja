#include <iostream>
#include <vector> 
#include <algorithm>

using namespace std;

int N, M;
// 나무의 강도를 담을 벡터
vector<vector<int>> tree;
// 해당 날개 선택했는 지 확인 벡터 
vector<vector<bool>> visited;

// 부메랑 모양 : 중심 기준
// ㄱ, ㅢ, ㄴ, r
int dr[4][2] = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
int dc[4][2] = {{-1, 0}, {-1, 0}, {0, 1}, {0, 1}};

// 정답
int answer = 0;

void input() {
    // 나무 재요릐 세로 크기 의미 N, M 주어짐
    cin >> N >> M;

    // 나무 입력받을 거임 
    tree.assign(N, vector<int>(M)); 
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            cin >> tree[i][j];
        }
    }

    // 방문 배열 초기화
    visited.assign(N, vector<bool>(M));
}

bool inBoundary(int row1, int co1, int row2, int col2) {
    // 행이 벗어나는지
    if (row1 < 0 || row1 >= N || row2 < 0 || row2 >= N) return false; // 벗어남
    // 열이 벗어나는지
    if (co1 < 0 || co1 >= M || col2 < 0 || col2 >= M) return false; // 벗어남
    // 그렇지 않다면 true
    return true; // inBoundary의 true는 안벗어 났다는 것 
}

void dfs(int row, int col, int sum) {
    // 중심점을 열을 이동하면서 탐색하기 때문에 열을 끝까지 탐색했으면
    // 중심점을 다음 행으로 이동
    if (col == M) {
        col = 0;
        row++;
    }
  
    // 끝까지 탐색했으면
    if (row == N) {
        // 최대값 갱신
        answer = max(answer, sum);
        return;
    }

    // 해당 중심점이 방문한적이 없으면
    if (!visited[row][col]) {
        // 4가지의 부메랑 만들어서 탐색하기
        for (int dir = 0; dir < 4; dir++) {
            // 중심을 기준으로 하나
            int row1 = row + dr[dir][0];
            int col1 = col + dc[dir][0];
            // 중심을 기준으로 둘
            int row2 = row + dr[dir][1];
            int col2 = col + dc[dir][1];

            // 범위 안 벗어나는지?
            if (!inBoundary(row1, col1, row2, col2)) continue; // 벗어남 
            // 이미 방문한 것인지?
            if (visited[row1][col1] || visited[row2][col2]) continue; // 방문함 

            // 범위도 안벗어났고, 방문도 안헀으면 부메랑 제작 가능
            // 방문처리
            visited[row][col] = true; visited[row1][col1] = true; visited[row2][col2] = true;
            // 부메랑의 강도 = 중심*2 + 하나 + 둘
            int power = 2*tree[row][col] + tree[row1][col1] + tree[row2][col2];
            // 중심 다음으로 옮기기 
            dfs(row, col+1, sum+power);
            
            // 원복 (백트래킹)
            visited[row][col] = false; visited[row1][col1] = false; visited[row2][col2] = false;
        }
    }
    
    // 부메랑을 만들지 않을 경우
    dfs(row, col+1, sum);
}

int main() {

    ios::sync_with_stdio(0); cin.tie(0);

    // 입력을 받습니다.
    input();
    // 입력완료 

    // 부메랑은 항상 3칸을 차지하는 ㄱ모양으로 만들어야 함 
    // 중심이 되는 칸은 강도의 영향을 2배로 받음 
    // 이것은 백트래잌(DFS)로 하면 될 듯
    dfs(0, 0, 0);

    // 정답 출력 -> 안만들어졌으면 0이 그대로 출력될 것 
    cout << answer;

    return 0;
}