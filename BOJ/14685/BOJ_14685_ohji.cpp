#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/*
L * L 트램펄린
별둥별 떨어지는 위치를 알고 있음. 최대한 많은 별똥별 튕겨내기
*/

struct Point {
  int x, y;
};

int N, M, L, K, out;
vector<Point> stars;

int inPlace(int x, int y) {
  int cnt = 0;
  for (int i = 0; i < K; ++i) {
    if (stars[i].x < x || stars[i].x > x + L) continue;
    if (stars[i].y < y || stars[i].y > y + L) continue;
    ++cnt;
  }
  return cnt;
}

int main(void) {
	ios::sync_with_stdio(0);
  cin.tie(0);

  cin >> N >> M >> L >> K;
  
  for (int i = 0; i < K; ++i) {
    Point p;
    cin >> p.x >> p.y;
    stars.push_back(p);
  }

  int out = 0;
  for (int i = 0; i < K; ++i) {
    for (int j = 0; j < K; ++j) {
      int x = min(stars[i].x, stars[j].x);
      int y = min(stars[i].y, stars[j].y);

      // 나머지 좌표들이 범위 안에 있는지 확인
      out = max(out, inPlace(x, y));
    }
  }
  cout << K - out;
}
