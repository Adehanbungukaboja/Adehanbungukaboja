#include <iostream>
#include <string>
#include <algorithm>

using namespace std;

int N, M, ans = 0;
int board[8][8];
bool vis[8][8];
int dr[4] = {1, -1, -1, 1};
int dc[4] = {-1, -1, 1, 1};

void dfs(int idx, int summ) {
  if (idx == N * M) {
    ans = max(ans, summ);
    return;
  }

  int r = idx / M, c = idx % M;

  dfs(idx + 1, summ); // 나를 선택 안 하고 들어가기

  if (vis[r][c]) return;
  for (int d = 0; d < 4; ++d) {
    int nr = r + dr[d], nc = c + dc[d];

    if (nr < 0 || nr >= N || nc < 0 || nc >= M) continue;
    if (vis[nr][c] || vis[r][nc]) continue;

    vis[nr][c] = vis[r][nc] = vis[r][c] = true;
    int val = board[nr][c] + board[r][nc] + 2 * board[r][c];
    dfs(idx + 1, summ + val);
    vis[nr][c] = vis[r][nc] = vis[r][c] = false;
  }
}

int main(void) {
  ios::sync_with_stdio(0);
  cin.tie(0);

  cin >> N >> M;
  for (int i = 0; i < N; ++i) {
    for (int j = 0; j < M; ++j) {
      cin >> board[i][j];
    }
  }

  dfs(0, 0);
  cout << ans;
}
