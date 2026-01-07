import java.io.*;
import java.util.*;

/*
1. 투입
좌하단 0,0 우상단 N,N
좌하단 r1,c1 우상단 r2,c2 새로 투입된 미생물에 의해
영역이 둘 이상으로 나눠지면 모두 사라짐. bfs

2. 이동
가장 차지한 영역이 넓은 무리부터. 둘 이상이라면 먼저 투입된.
기존 형태 유지, 다른 미생물과 겹치지 않게, 최대한 x, y 작은 위치 => y가 바깥 for문
어떤 곳에도 둘 수 없다면 사라짐 => cnt 처리

3. 기록
인접한 무리 쌍
성과는 A x B 모든 쌍 더한 값

*/

public class D0106_CDTR_microbial_research {

    static int N, Q;
    static int[] cnt = new int[52]; // groupId별로 사이즈 들고 있기
    static BufferedReader br;
    static int[][] board = new int[18][18];
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};

    static boolean OOB(int x, int y) {
        return x < 0 || x >= N || y < 0 || y >= N;
    }

    static void setStart(Queue<int[]> q, boolean[][] vis, int groupId) {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (board[i][j] == groupId) {
                    vis[i][j] = true;
                    q.add(new int[]{i, j});
                    return;
                }
            }
        }
    }

    static boolean checkBFS(Queue<int[]> q, boolean[][] vis, int groupId) {
        int this_cnt = 0;
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            ++this_cnt; // 뽑을 때마다 카운트하기

            for (int d = 0; d < 4; ++d) {
                int nx = cur[0] + dx[d], ny = cur[1] + dy[d];
                if (OOB(nx, ny) || vis[nx][ny]) continue;
                if (board[nx][ny] != groupId) continue;

                vis[nx][ny] = true;
                q.add(new int[]{nx, ny});
            }
        }
        return this_cnt != cnt[groupId];
    }

    static boolean isSeperated(int groupId) {
        if (cnt[groupId] <= 1) return false;

        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] vis = new boolean[N][N];

        // 시작점 찾고
        setStart(q, vis, groupId);

        // bfs로 분리됐는지 확인
        return checkBFS(q, vis, groupId);
    }

    static void removeGroup(int groupId) {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (board[i][j] == groupId) board[i][j] = 0;
            }
        }
        cnt[groupId] = 0;
    }

    static void put(int idx, int r1, int c1, int r2, int c2) {
        cnt[idx] = (r2 - r1 + 1) * (c2 - c1 + 1); // 미생물 개수 초기화
        Set<Integer> eaten = new HashSet<>();

        for (int i = r1; i <= r2; ++i) {
            for (int j = c1; j <= c2; ++j) {
                if (board[i][j] != 0) {
                    eaten.add(board[i][j]); // 분리 검사할 애들 모아놓기
                    --cnt[board[i][j]];
                }
                board[i][j] = idx; // 무조건 새로 투입된 미생물만 남아 있음
            }
        }

        for (int id : eaten) {
            if (isSeperated(id)) {
                removeGroup(id);
            }
        }
    }

    static List<int[]> getShape(int[][] prev, int groupId) {
        List<int[]> cells = new ArrayList<>();
        int minR = 99999, minC = 99999;

        // 먼저 좌표 수집
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (prev[i][j] == groupId) {
                    cells.add(new int[]{i, j});
                    minR = Math.min(minR, i);
                    minC = Math.min(minC, j);
                }
            }
        }

        // 베이스 좌표로 변환
        for (int[] cell : cells) {
            cell[0] -= minR;
            cell[1] -= minC;
        }

        return cells;
    }

    static boolean isMoveOk(List<int[]> shape, int sr, int sc) {
        for (int[] p: shape) {
            int r = sr + p[0];
            int c = sc + p[1];
            if (OOB(r, c)) return false;
            if (board[r][c] != 0) return false;
        }
        return true;
    }

    static void moveGroup(List<int[]> shape, int sr, int sc, int groupId) {
        for (int[] p: shape) {
            int r = sr + p[0];
            int c = sc + p[1];
            board[r][c] = groupId;
        }
    }

    static void move() {
        int[][] prev = board;
        board = new int[N][N];

        // 가장 영역이 넓은 무리부터
        List<Integer> gIds = new ArrayList<>();
        for (int g = 1; g <= Q; ++g) {
            if (cnt[g] > 0) gIds.add(g);
        }
        gIds.sort((a, b) -> Integer.compare(cnt[b], cnt[a]));
        
        // 이동. x가 가장 작으면서 y가 가장 작은 거 찾고, x가 가장 작으면서 y가 가장 작은 남는 곳으로 이동
        // 이동하다가 공간 부족해서 안 되면 다른 자리로 옮겨야 함
        // 이동 못 하면 사라짐. cnt 바꿔야 함
        for (int groupId : gIds) {
            List<int[]> shape = getShape(prev, groupId);
            boolean placed = false;

            outer:
            for (int c = 0; c < N; ++c) {       // x(열) 작은 것부터
                for (int r = 0; r < N; ++r) {   // y(행) 작은 것부터
                    if (isMoveOk(shape, r, c)) {
                        moveGroup(shape, r, c, groupId);
                        placed = true;
                        break outer;
                    }
                }
            }

            if (!placed) cnt[groupId] = 0;
        }
    }

    static void print() {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    static long scoring() {
        // 맞닿은 면이 하나라도 있는지 확인
        boolean[][] faced = new boolean[52][52];
        long score = 0;

        for (int r = 0; r < N; ++r) {
            for (int c = 0; c < N; ++c) {
                if (board[r][c] == 0) continue;
                
                for (int d = 0; d < 4; ++d) {
                    int nr = r + dx[d], nc = c + dy[d];
                    if (OOB(nr, nc) || board[r][c] == board[nr][nc]) continue;

                    if (board[r][c] != 0 && board[nr][nc] != 0) {
                        int a = Math.min(board[r][c], board[nr][nc]);
                        int b = Math.max(board[r][c], board[nr][nc]);
                        if (!faced[a][b]) {
                            faced[a][b] = true;
                            score += (long) cnt[a] * cnt[b];
                        }
                    }
                }
            }
        }
        return score;
    }

    static long run(int idx, int r1, int c1, int r2, int c2) {

        // 1) 미생물 투입
        put(idx, r1, c1, r2, c2);

        // 2) 배양 용기 이동
        move();

        // print();

        // 3) 실험 결과 기록
        return scoring();
    }

    public static void main(String args[]) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        for (int i = 0; i < Q; ++i) {
            int r1, c1, r2, c2;
            st = new StringTokenizer(br.readLine());
            c1 = Integer.parseInt(st.nextToken());
            r1 = Integer.parseInt(st.nextToken());
            c2 = Integer.parseInt(st.nextToken()) - 1;
            r2 = Integer.parseInt(st.nextToken()) - 1; // 각 좌표를 미생물 네모 좌하단이라 생각하는 게 나음

            System.out.println(run(i + 1, r1, c1, r2, c2));
        }
    }
}
