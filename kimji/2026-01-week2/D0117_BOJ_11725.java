import java.io.*;
import java.util.*;

public class D0117_BOJ_11725 {

    static BufferedReader br;
    static StringTokenizer st;
    static StringBuilder sb = new StringBuilder();

    // 노드의 개수 N
    static int N;
    // 연결된 노드를 담을 인접리스트
    static ArrayList<Integer>[] adjList;

    // 방문 처리 배열
    static boolean[] visited;
    // 각 노드의 부모를 저장할 배열
    static int[] parent;

    static void input() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());

        visited = new boolean[N + 1];
        parent = new int[N + 1];

        // 인접리스트 선언
        adjList = new ArrayList[N + 1];
        for (int i = 0; i <= N; i++) {
            adjList[i] = new ArrayList<>();
        }

        // 트리의 간선 개수는 N - 1개
        for (int i = 1; i <= N - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            // 양방향 연결
            adjList[a].add(b);
            adjList[b].add(a);
        }
    }

    static void dfs(int currentNode) {
        // 해당 노드 방문 처리
        visited[currentNode] = true;

        // 해당 노드와 연결된 자식 노드 탐색
        for (int nextNode : adjList[currentNode]) {
            // 다음 노드가 방문한 적이 없으면?
            if (!visited[nextNode]) {
                // 다음노드의 부모노드는 지금의 노드
                parent[nextNode] = currentNode;
                // 다음으로 이동
                dfs(nextNode);
            }
        }
    }

    static void bfs(int startNode) {
        // bfs는 queue를 기반으로 진행
        Queue<Integer> queue = new LinkedList<>();

        // 시작 노드 추가
        queue.add(startNode);
        // 방문 처리
        visited[startNode] = true;

        // queue가 빌 때까지 반복
        while (!queue.isEmpty()) {
            // 현재 위치한 노드 (부모 노드)
            int currentNode = queue.poll();

            // 현재의 이어져 있는 다음 노드 확인 (자식 노드)
            for (int nextNode : adjList[currentNode]) {
                // 다음 노드 (자식 노드)를 방문 한적이 없으면
                if (!visited[nextNode]) {
                    // 자식 노드의 부모는 현재의 노드
                    parent[nextNode] = currentNode;
                    // 방문 처리
                    visited[nextNode] = true;
                    // 큐에 넣고 계속 탐색
                    queue.offer(nextNode);
                }
            }
        }

    }

    public static void main(String[] args) throws Exception {
        /*
        트리 구조라고 했으니 인접리스트로 구현이 가능함
        근데 이제 트리 상에서 연결된 두 정점이 주어진다고 했고,
        루트는 1이니깐,
        양방향 연결로 하고, 방문한 것은 true처리
        그리고 1부터해서 탐색하며,
        방문하게 되는 노드의 이전 노드가 즉 부모 노드가 될 것

        그렇기 때문에 이를 생각하고, 모든 노드를 방문하게 하면 됨.
        따라서 bfs든 dfs든 뭐든 사용하면 될 것 같다.
         */

        // 입력 받기
        input();

        // 1. dfs 방안
        // dfs(1);

        // 2. bfs 방안
        bfs(1);

        // 2번째 노드부터 한줄에 하나씩 부모 노드 출력
        for (int node = 2; node <= N; node++) {
            sb.append(parent[node]).append('\n');
        }

        System.out.print(sb);
    }
}
