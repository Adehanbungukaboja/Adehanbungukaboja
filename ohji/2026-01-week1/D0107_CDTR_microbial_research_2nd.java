import java.io.*;
import java.util.*;

public class D0107_CDTR_microbial_research_2nd {

    static BufferedReader br;
    static int N, Q;
    static int[][] board = new int[18][18];
    static int[] memberCnt = new int[55];
    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};

    static void print() {
        System.out.println("#--------------#");
        for (int c = N - 1; c >= 0; --c) {
            for (int r = 0; r < N; ++r) {
                System.out.print(board[r][c]);
            }
            System.out.println();
        }
    }

    static boolean OOB(int r, int c) {
        return r < 0 || r >= N || c < 0 || c >= N;
    }

    static boolean checkBFS(int groupId, int sr, int sc) { // 분리됐는지 확인용 bfs
        int this_cnt = 0;
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] vis = new boolean[N][N];
        vis[sr][sc] = true;
        q.add(new int[]{sr, sc});

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            ++this_cnt;

            for (int d = 0; d < 4; ++d) {
                int nr = cur[0] + dr[d], nc = cur[1] + dc[d];
                if (OOB(nr, nc) || vis[nr][nc]) continue;
                if (board[nr][nc] == groupId) {
                    q.add(new int[]{nr, nc});
                    vis[nr][nc] = true;
                }
            }
        }
        return this_cnt != memberCnt[groupId];
    }

    static boolean isSeperated(int groupId) {
        if (memberCnt[groupId] <= 1) return false;
    
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (groupId == board[i][j]) {
                    return checkBFS(groupId, i, j);
                }
            }
        }

        return false;
    }

    static void removeGroup(int groupId) {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (board[i][j] == groupId) board[i][j] = 0;
            }
        }
        memberCnt[groupId] = 0;
    }

    static void put(int groupId, int r1, int c1, int r2, int c2) {
        // 미생물 개수 초기화
        memberCnt[groupId] = (r2 - r1) * (c2 - c1);
        // 영역 내 다른 미생물을 잡아먹고
        // 분리되면 해당 미생물은 사라진다.
        Set<Integer> eatenGroups = new HashSet<>();
        for (int i = r1; i < r2; ++i) {
            for (int j = c1; j < c2; ++j) {
                int g = board[i][j];
                if (g != 0 && g != groupId) {
                    eatenGroups.add(g);
                    --memberCnt[g];
                }
                board[i][j] = groupId;
            }
        }

        // 먹힌 그룹들에 대해서 분리됐는지 검사 필요
        // 분리됐다면 모두 사라짐
        for (int g : eatenGroups) {
            if (isSeperated(g)) {
                removeGroup(g);
            }
            // System.out.println("### seperated: " + g + " : " + sep);
        }
    }

    static List<int[]> getShape(int[][] prev, int groupId) {
        List<int[]> shape = new ArrayList<>();
        int minR = 9999, minC = 9999;
        // 셀 다 모으기
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (prev[i][j] == groupId) {
                    shape.add(new int[]{i, j});
                    minR = Math.min(minR, i);
                    minC = Math.min(minC, j);
                }
            }
        }
        // 상대 좌표로 반환하기
        for (int[] p : shape) {
            p[0] -= minR;
            p[1] -= minC;
        }
        return shape;
    }

    static boolean moveGroup(int groupId, List<int[]> shape, int sr, int sc) {
        // 옮길 수 있는지 먼저 확인
        for (int[] p : shape) {
            int r = sr + p[0], c = sc + p[1];
            if (OOB(r, c) || board[r][c] != 0) return false;
        }

        // 옮기기
        for (int[] p : shape) {
            int r = sr + p[0], c = sc + p[1];
            board[r][c] = groupId;
        }
        return true;
    }

    static void move(int curId) {
        int[][] prev = board;
        board = new int[N][N];
        // 미생물 이동. 영역이 큰 것 부터. 먼저 들어온 것 부터
        List<Integer> gIds = new ArrayList<>();
        for (int id = 1; id <= curId; ++id) gIds.add(id);
        gIds.sort((a, b) -> { return Integer.compare(memberCnt[b], memberCnt[a]); });
        
        for (int g : gIds) {
            boolean placed = false;
            List<int[]> shape = getShape(prev, g); // 모양 구하고
            outer:
            for (int r = 0; r < N; ++r) { // x좌표가 작으면서 y좌표가 작은 위치
                for (int c = 0; c < N; ++c) {
                    placed = moveGroup(g, shape, r, c);
                    if (placed) break outer;
                }
            }

            // 아예 못 옮기면 그냥 없앰 - cnt만 관리
            if (!placed) memberCnt[g] = 0;
        }
    }

    static long calc() {
        long score = 0;
        boolean[][] faced = new boolean[52][52];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (board[i][j] == 0) continue;
                for (int d = 0; d < 4; ++d) {
                    int nr = i + dr[d], nc = j + dc[d];
                    if (!OOB(nr, nc) && board[nr][nc] != 0 && board[i][j] != board[nr][nc]) {
                        int a = Math.min(board[i][j], board[nr][nc]);
                        int b = Math.max(board[i][j], board[nr][nc]);
                        
                        if (!faced[a][b]) {
                            faced[a][b] = true;
                            score += memberCnt[a] * memberCnt[b];
                        }
                    }
                }
            }
        }
        return score;
    }

    static long run(int groupId, int r1, int c1, int r2, int c2) {
        // 투입
        put(groupId, r1, c1, r2, c2);

        // 이동
        move(groupId);
        // print();

        // 계산
        return calc();
    }

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        for (int i = 0; i < Q; ++i) {
            int r1, r2, c1, c2;
            st = new StringTokenizer(br.readLine());
            r1 = Integer.parseInt(st.nextToken());
            c1 = Integer.parseInt(st.nextToken());
            r2 = Integer.parseInt(st.nextToken());
            c2 = Integer.parseInt(st.nextToken());
            System.out.println(run(i + 1, r1, c1, r2, c2));
        }
    }
}
