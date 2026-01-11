import java.io.*;
import java.util.*;

public class D0109_CDTR_delivery_service {

    static int N, M;
    static int[][] board = new int[52][52];
    static Box[] boxes = new Box[102];
    static BufferedReader br;

    static class Box {
        int lr, lc, w, h;
        public Box(int lr, int lc, int w, int h) {
            this.lr = lr; this.lc = lc; this.w = w; this.h = h;
        }
    }

    static boolean OOB(int r, int c) {
        return r < 0 || r >= N || c < 0 || c >= N;
    }

    static void removeBox(int idx) {
        Box box = boxes[idx];
        for (int r = box.lr; r > box.lr - box.h; --r) {
            for (int c = box.lc; c < box.lc + box.w; ++c) {
                board[r][c] = 0;
            }
        }
        boxes[idx] = null;
    }

    static boolean down(int idx, int h, int w, int lc) { // 이거 재사용 하자.
        int lineStart = h;

        if (boxes[idx] != null) {
            lineStart = boxes[idx].lr;
        }

        // 1) 투입
        // 떨어뜨리다가 멈추는 곳 위에
        for (int line = lineStart; line <= N; ++line) {
            boolean stop = false;
            
            outer:
            for (int r = line; r > line - h; --r) {
                for (int c = lc; c < lc + w; ++c) {
                    if (board[r][c] != 0 && board[r][c] != idx) {
                        stop = true;
                        break outer;
                    }
                }
            }

            if (stop) {
                // 멈췄는데 제자리였으면 내려간 게 아님
                if (boxes[idx] != null && boxes[idx].lr == line - 1 && boxes[idx].lc == lc) {
                    return false;
                }

                if (boxes[idx] != null) {
                    removeBox(idx);
                }
                boxes[idx] = new Box(line - 1, lc, w, h);

                for (int r = line - 1; r > line - 1 - h; --r) {
                    for (int c = lc; c < lc + w; ++c) {
                        board[r][c] = idx;
                    }
                }

                return true;
            }
        }
        return false;
    }

    static void print() {
        System.out.println("#-------#");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    static boolean pullLeftOk(int idx) {
        Box box = boxes[idx];
        // 내 왼쪽 면에 대해서 나갈 수 있어야 한다
        for (int r = box.lr; r > box.lr - box.h; --r) {
            for (int c = box.lc - 1; c >= 0; --c) {
                if (board[r][c] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    static boolean pullRightOk(int idx) {
        Box box = boxes[idx];
        // 내 오른쪽 면에 대해서 나갈 수 있어야 한다
        for (int r = box.lr; r > box.lr - box.h; --r) {
            for (int c = box.lc + box.w; c < N; ++c) {
                if (board[r][c] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    static void fall() {
        boolean moved = true;
        while (moved) {
            moved = false;
            for (int idx = 1; idx <= 100; ++idx) {
                if (boxes[idx] == null) continue;
    
                if (down(idx, boxes[idx].h, boxes[idx].w, boxes[idx].lc)) {
                    moved = true;
                }
            }
        }
    }

    static void pullBoxAndFall(int idx) {
        // 당기고
        removeBox(idx);
        --M;

        // 떨어뜨림.
        fall();
    }

    static void left(List<Integer> pulled) {
        // c를 왼쪽부터 보면서 뺄 수 있는 애들 다 0으로 만들어버림. 그리고 idx는 저장. --M.
        // 이때 택배번호 작은 것부터 그리고 나면 또 down 해야함
        // hmm 각 택배 위치를 들고 있어야 편함, 택배 번호순으로 들고 있는 게 나음

        // 그냥 매번 1~100까지 돌아버리는 게 낫지 않을까?
        for (int idx = 1; idx <= 100; ++idx) {
            if (boxes[idx] == null) continue;

            if (pullLeftOk(idx)) {
                pulled.add(idx);
                pullBoxAndFall(idx);

                // System.out.println("## LEFT ##");
                // print();
                return;
            }
        }
    }

    static void right(List<Integer> pulled) {
        // c를 왼쪽부터 보면서 뺄 수 있는 애들 다 0으로 만들어버림. 그리고 idx는 저장. --M.
        // 이때 택배번호 작은 것부터 그리고 나면 또 down 해야함
        // hmm 각 택배 위치를 들고 있어야 편함, 택배 번호순으로 들고 있는 게 나음

        // 그냥 매번 1~100까지 돌아버리는 게 낫지 않을까?
        for (int idx = 1; idx <= 100; ++idx) {
            if (boxes[idx] == null) continue;

            if (pullRightOk(idx)) {
                pulled.add(idx);
                pullBoxAndFall(idx);

                // System.out.println("## RIGHT ##");
                // print();
                return;
            }
        }
    }

    static void pull() {
        List<Integer> pulled = new ArrayList<>();

        while (M > 0) {
            // 2) 좌측 하차
            left(pulled);
            
            // 3) 우측 하차
            right(pulled);
        }

        // 하차 되는 택배 번호 순서대로 출력
        for (int idx : pulled) {
            System.out.println(idx);
        }
    }

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        for (int c = 0; c < N; ++c) {
            board[N][c] = -1; // 택배 떨어뜨릴 때 멈춤 용도
        }

        for (int i = 0; i < M; ++i) {
            // 번호, 세로, 가로, 좌측 좌표
            int idx, h, w, lc;

            st = new StringTokenizer(br.readLine());
        
            idx = Integer.parseInt(st.nextToken());
            h = Integer.parseInt(st.nextToken());
            w = Integer.parseInt(st.nextToken());
            lc = Integer.parseInt(st.nextToken()) - 1; // 1-based -> 0-based

            // 투입
            down(idx, h, w, lc);
            // print();
        }
        // 하차 -> 여기서부턴 M 건들여도 됨
        pull();
    }
}
