import java.io.*;
import java.util.*;

public class D0101_CDTR_frog_journey {
    
    static PriorityQueue<Node> pq;
    static long[][][] dist;
    static int N, Q;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};
    static char[][] board = new char[52][52];
    static BufferedReader br;

    static class Node implements Comparable<Node> {
        int x, y, p;
        long cost;

        public Node(int x, int y, int p, long cost) { this.x = x; this.y = y; this.p = p; this.cost = cost; }
        public int compareTo(Node o) { return Long.compare(this.cost, o.cost); }
    }

    static boolean OOB(int x, int y) {
        return x < 0 || x >= N || y < 0 || y >= N;
    }

    static void power_down(int x, int y, int p, long cost) {
        ++cost;
        while (--p >= 1) {
            if (dist[x][y][p] == -1 || cost < dist[x][y][p]) {
                dist[x][y][p] = cost;
                pq.add(new Node(x, y, p, cost));
            }
        }
    }

    static void power_up(int x, int y, int p, long cost) {
        if (p == 5) return;
        ++p; // 점프력 증가는 1만큼만 가능
        cost += 1L * p * p;
        if (dist[x][y][p] == -1 || cost < dist[x][y][p]) {
            dist[x][y][p] = cost;
            pq.add(new Node(x, y, p, cost));
        }
    }

    static void jump(int d, int x, int y, int p, long cost) {
        ++cost;
        int nx, ny;
        for (int step = 1; step <= p; ++step) { // 중간에 천적 있으면 안 됨
            nx = x + step * dx[d];
            ny = y + step * dy[d];
            if (OOB(nx, ny)) return;
            if (board[nx][ny] == '#') return;
        }

        // 가려는 위치에 돌이 없거나 미끄러운 돌이라면 안 됨
        nx = x + p * dx[d];
        ny = y + p * dy[d];
        if (board[nx][ny] != '.') return;

        if (dist[nx][ny][p] == -1 || cost < dist[nx][ny][p]) {
            dist[nx][ny][p] = cost;
            pq.add(new Node(nx, ny, p, cost));
        }
    }

    static long run(int r1, int c1, int r2, int c2) {
        init();

        int startP = 1; // 시작 점프력
        dist[r1][c1][startP] = 0;
        pq.add(new Node(r1, c1, startP, 0));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            int x = cur.x, y = cur.y, p = cur.p;
            long cost = cur.cost;

            if (dist[x][y][p] != cost) continue;
            if (x == r2 && y == c2) return cost;

            for (int d = 0; d < 4; ++d) jump(d, x, y, p, cost);
            power_up(x, y, p, cost);
            power_down(x, y, p, cost);
        }

        return -1;
    }

    static void init() {
        pq = new PriorityQueue<>();
        dist = new long[52][52][6]; // 점프력 1~5
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                for (int p = 1; p <= 5; ++p) dist[i][j][p] = -1;
            }
        }
    }

    public static void main(String args[]) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; ++i) {
            board[i] = br.readLine().toCharArray();
        }

        Q = Integer.parseInt(br.readLine());
        for (int i = 0; i < Q; ++i) {
            // 출발, 도착
            StringTokenizer st = new StringTokenizer(br.readLine());
            int r1, c1, r2, c2;
            r1 = Integer.parseInt(st.nextToken()) - 1; // 1-based -> 0-based
            c1 = Integer.parseInt(st.nextToken()) - 1;
            r2 = Integer.parseInt(st.nextToken()) - 1;
            c2 = Integer.parseInt(st.nextToken()) - 1;

            System.out.println(run(r1, c1, r2, c2));
        }
    }
}
