#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int n, m, l, k;
vector<pair<int, int>> stars; // 별 좌표 저장
int ans = -1;

void input() {
	cin >> n >> m >> l >> k;
	int x, y;
	for (int i = 0; i < k; i++) {
		cin >> x >> y;
		stars.push_back({ x, y }); // 1-based 좌표 그대로 저장
	}
}

void solution() {
	// 모든 별 좌표를 트램펄린 왼쪽 아래 꼭짓점 후보로 사용
	for (int i = 0; i < k; i++) {
		for (int j = 0; j < k; j++) {
			// 첫번쨰 왼쪽 아래 꼭짓점
			int x0 = stars[i].first;
			int y0 = stars[j].second;

			// 이를 기준으로 테두리 포함해서 별이 속하는 지 확인하기 
			int cnt = 0;
			for (int s = 0; s < k; s++) {
				int x = stars[s].first;
				int y = stars[s].second;

				// 별똥별이 떨어지는 위치는 범위 안에 있기 때문에
				// 트램펄린 테두리 포함해서 안에만 있으면 됨 
				if (x0 <= x && x <= x0 + l && y0 <= y && y <= y0 + l)	cnt++;
			}
			ans = max(ans, cnt);
		}
	}
}

int main() {
	ios::sync_with_stdio(0); cin.tie(0);

	input();
	solution();

	cout << (k - ans); // 트램펄린 밖의 별 개수
	return 0;
}
