#include <iostream>
#include <vector>

using namespace std;
/*
바람이 가는 방향. 현재에서 전진, 시계+전진, 반시계+전진
각 칸에 대해 [4]로 방향에 대한 벽 여부를 체크

1. 온풍기에서 바람 나옴
2. 온도 조절됨
3. 가장자리 온도 1씩 감소
4. 초콜릿 먹기
5. 조사하는 모든 칸 온도가 K 이상이면 멈춤
*/

#define UP 0
#define RIGHT 1
#define DOWN 2
#define LEFT 3

struct Point {
  int r, c, d;
};

int R, C, K, W, choco;
int board[22][22];
int simul[22][22];
bool vis[22][22];
bool wall[22][22][4];
int dr[4] = {-1, 0, 1, 0}; // UP, RIGHT, DOWN, LEFT
int dc[4] = {0, 1, 0, -1};
vector<Point> machines;
vector<Point> checkPoints;

bool OOB(int r, int c) {
  return r < 0 || r >= R || c < 0 || c >= C;
}

void checkTemperature() {
  for (auto &p : checkPoints) {
    if (simul[p.r][p.c] < K) return;
  }
  cout << choco;
  exit(0);
}

void eatChocolate() {
  ++choco;
  if (choco == 101) {
    cout << choco;
    exit(0);
  }
}

void border() {
  for (int c = 0; c < C; ++c) {
    if (simul[0][c] > 0) --simul[0][c];
    if (simul[R - 1][c] > 0) --simul[R - 1][c];
  }
  for (int r = 1; r < R - 1; ++r) { // 모서리 제외
    if (simul[r][0] > 0) --simul[r][0];
    if (simul[r][C - 1] > 0) --simul[r][C - 1];
  }
}

void temperature() {
  int temp[22][22] = {};

  // 4 방향에 대해서 온도 조절하기. 온도가 높은 칸에서 낮은 칸으로. 
  for (int r = 0; r < R; ++r) {
    for (int c = 0; c < C; ++c) {
      for (int d = 0; d < 4; ++d) {
        int nr = r + dr[d], nc = c + dc[d];
        if (OOB(nr, nc) || wall[r][c][d]) continue;
        if (simul[r][c] <= simul[nr][nc]) continue;

        int val = (simul[r][c] - simul[nr][nc]) / 4;
        temp[r][c] -= val;
        temp[nr][nc] += val;
      }
    }
  }

  // temp 값으로 다시 simul 업데이트 하기
  for (int r = 0; r < R; ++r) {
    for (int c = 0; c < C; ++c) {
      simul[r][c] += temp[r][c];
    }
  }
}

void wind(int r, int c, int d, int strength) {
  simul[r][c] += strength;
  if (strength == 1) return;
 
  int nr, nnr, nc, nnc;

  // 전진
  nr = r + dr[d], nc = c + dc[d];
  if (!(OOB(nr, nc) || vis[nr][nc] || wall[r][c][d])) {
    vis[nr][nc] = true;
    wind(nr, nc, d, strength - 1);
  }

  // 시계 + 전진
  nr = r + dr[(d + 1) % 4], nc = c + dc[(d + 1) % 4];
  nnr = nr + dr[d], nnc = nc + dc[d];
  if (!(OOB(nr, nc) || OOB(nnr, nnc) || vis[nnr][nnc] || wall[r][c][(d + 1) % 4] || wall[nr][nc][d])) {
    vis[nnr][nnc] = true;
    wind(nnr, nnc, d, strength - 1);
  }

  // 반시계 + 전진
  nr = r + dr[(d + 3) % 4], nc = c + dc[(d + 3) % 4];
  nnr = nr + dr[d], nnc = nc + dc[d];
  if (!(OOB(nr, nc) || OOB(nnr, nnc) || vis[nnr][nnc] || wall[r][c][(d + 3) % 4] || wall[nr][nc][d])) {
    vis[nnr][nnc] = true;
    wind(nnr, nnc, d, strength - 1);
  }
}

void init() {
  for (int i = 0; i < R; ++i) fill(vis[i], vis[i] + C, false);
}

void run() {
  while (1) {
    // 온풍기 가동
    for (auto m : machines) {
      int d = m.d, sr = m.r + dr[d], sc = m.c + dc[d];
      init();
      wind(sr, sc, d, 5);
    }

    // 온도 조절
    temperature();

    // 가장자리
    border();

    // 초콜렛 먹기
    eatChocolate();

    // 조사
    checkTemperature();
  }
  cout << choco;
}

int main(void) {
  ios::sync_with_stdio(0);
  cin.tie(0);

  cin >> R >> C >> K;

  for (int i = 0; i < R; ++i) {
    for (int j = 0; j < C; ++j) {
      cin >> board[i][j];
      if (board[i][j] == 1) machines.push_back({i, j, RIGHT});
      if (board[i][j] == 2) machines.push_back({i, j, LEFT});
      if (board[i][j] == 3) machines.push_back({i, j, UP});
      if (board[i][j] == 4) machines.push_back({i, j, DOWN});
      if (board[i][j] == 5) checkPoints.push_back({i, j});
    }
  }

  cin >> W;
  for (int i = 0; i < W; ++i) {
    int x, y, t;
    cin >> x >> y >> t;
    --x; --y; // 0-based로 변경
    if (t == 0) { // (x, y)와 (x-1, y)사이 벽
      wall[x][y][UP] = wall[x - 1][y][DOWN] = true;
    } else { // (x, y)와 (x, y+1) 사이 벽
      wall[x][y][RIGHT] = wall[x][y + 1][LEFT] = true;
    }
  }
  run();
}
