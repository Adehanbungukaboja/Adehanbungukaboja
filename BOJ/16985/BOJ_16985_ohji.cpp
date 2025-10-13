#include <iostream>
#include <queue>
#include <algorithm>
#include <climits>
using namespace std;

#define NO 0
#define OK 1

int simul[5][5][5];
int cube[5][5][5][4];
int order[5][2];  // 판 쌓는 순서, 판 회전
bool used[5];
int dz[6] = {0, 0, 0, 0, 1, -1};
int dx[6] = {-1, 0, 1, 0, 0, 0};
int dy[6] = {0, 1, 0, -1, 0, 0};
int ans = INT_MAX;

// 입구는 (0,0,0) 출구는 (4,4,4)로 고정
// 판 쌓는 순서 + 판 회전

struct Point {
	int z, x, y;
};

void setRotate(int idx) { // 판 회전 시킨 거 미리 넣어놓기
	for (int r = 1; r < 4; ++r) {
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				cube[idx][j][4 - i][r] = cube[idx][i][j][r - 1];
			}
		}
	}
}

int bfs() { // bfs로 최단 거리 구하기. simul 사용.
	if (simul[0][0][0] == NO || simul[4][4][4] == NO) return INT_MAX;

	int dist[5][5][5] = {};
	queue<Point> q;
	q.push({0, 0, 0});
	dist[0][0][0] = 1;

	while (!q.empty()) {
		auto cur = q.front(); q.pop();

		for (int d = 0; d < 6; ++d) {
			if (dist[cur.z][cur.x][cur.y] >= ans) return INT_MAX; // 백트래킹

			int nz = cur.z + dz[d], nx = cur.x + dx[d], ny = cur.y + dy[d];
			if (nz == 4 && nx == 4 && ny == 4) return dist[cur.z][cur.x][cur.y];

			if (nz < 0 || nz >= 5 || nx < 0 || nx >= 5 || ny < 0 || ny >= 5) continue;
			if (simul[nz][nx][ny] == NO || dist[nz][nx][ny]) continue;

			dist[nz][nx][ny] = dist[cur.z][cur.x][cur.y] + 1;
			q.push({nz, nx, ny});
		}
	}
	return INT_MAX;
}

void dfs2(int depth) { // 회전 정하기
	if (depth == 5) {
		for (int k = 0; k < 5; ++k) { // simul에 옮기고 bfs로 최단거리 구하기
			int idx = order[k][0];
			int r = order[k][1];
			for (int i = 0; i < 5; ++i) {
				for (int j = 0; j < 5; ++j) {
					simul[k][i][j] = cube[idx][i][j][r];
				}
			}
		}
		ans = min(bfs(), ans);
		return;
	}
	for (int r = 0; r < 4; ++r) {
		order[depth][1] = r;
		dfs2(depth + 1);
	}
}

void dfs(int depth) { // 쌓는 순서 5개
	if (depth == 5) {
		dfs2(0);
		return;
	}
	for (int i = 0; i < 5; ++i) {
		if (used[i]) continue;
		used[i] = true;
		order[depth][0] = i;
		dfs(depth + 1);
		used[i] = false;
	}
}

int main(void) {
	ios::sync_with_stdio(0);
	cin.tie(0);

	// 입력 & 회전판 저장
	for (int k = 0; k < 5; ++k) {
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				cin >> cube[k][i][j][0];
			}
		}
		setRotate(k);
	}

	// 쌓는 순서 정하기 + 회전 정하기 (dfs) & bfs로 최소 이동 횟수 구하기
	dfs(0);

	// 출력
	if (ans == INT_MAX) cout << -1;
	else cout << ans;
}
