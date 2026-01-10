/*
1. 입력
   - N M 
      - N: NxN 격자판 (2 <= N <= 50)
      - M: M개의 택배 (1 <= M <= 100)
   - k h w c 
      - k: 택배 번호 (1 <= k <= 100), unique한 값
      - h: 세로 크기 (1 <= h <= N)
      - w: 가로 크기 (1 <= w <= N)
      - c: 좌측 상단의 열 좌표 (1 <= c <= N), 1부터 시작 주의
2. 시뮬레이션
   - 2-1. 투입된 택배들은 중력에 의해 하단으로 떨어진다.   
      - '그대로 쭉 떨어짐' or '내려가던 중 다른 짐을 만나서 멈춤'
      - 모든 택배 투입 후, 절대 격자 공간을 벗어나지는 않는다는 것이 보장됨  
   - 2-2. 모든 택배 투입이 완료되면, '공간에 있는 택배를 모두 하차할 때까지' 다음 과정 무한 반복
      - 2-2-1. 좌측
         - 쌓인 택배 중에 잡고 왼쪽으로 이동했을 때, 다른 택배와 부딪히지 않고 뺄 수 있는 택배를 먼저 하차함
         - if) 그러한 택배가 여러개 --> 택배 번호(k)가 작은 택배를 먼저 하차함 
         - 다시 중력방향으로 떨어트림
      - 2-2-2. 우측
         - 쌓인 택배 중에 잡고 오른쪽으로 이동했을 때, 다른 택배와 부딪히지 않고 뺄 수 있는 택배를 먼저 하차함
         - if) 그러한 택배가 여러개 --> 택배 번호(k)가 작은 택배를 먼저 하차함 
         - 다시 중력방향으로 떨어트림
3. 좌표계 설정
   - N을 입력 받으면 --> [N+2][N+2]
4. 자료구조의 이원화
   - 4-1. 격자판 map: 택배 번호(k)만 저장
   - 4-2. 별도의 객체 배열(boxes): 택배의 좌표(r,c)와 크기(h,w)는 별도의 객체 배열로 관리
5. 초기 위치 잡기
   - 입력에 c(열)은 있지만 r(행)은 없다.
      - 따라서 처음에 택배를 투입한 직후 일단 행을 1로 생성해두고, 2-1 중력 로직을 돌려 제 자리를 찾아주어야 한다.
6. 종료 조건
   - '2-2' 무한 반복문에서 "좌측 하차도 실패하고(False) AND 우측 하차도 실패한(False)" 경우를 반드시 체크해서 break을 걸어야 한다. (무한루프로 인한 시간초과 방지)
*/

import java.util.*;
import java.io.*;

public class Main {
   // 전역 변수: 어디서든 접근 가능하도록 static 선언
   static StringBuilder sb; 
   static StringTokenizer st;
	static BufferedReader br;

   static int N, M;
   static int[][] map;  // 4-1
   static Box[] boxes;  // 4-2, 배열의 인덱스가 k를 의미함 
   static int remainingBoxCnt; // 남아있는 상자 개수

   static class Box {  // 4-2
      int r, c, h, w;
      boolean isRemove; // 하차 여부

      public Box(int r, int c, int h, int w) {
         this.r = r;
         this.c = c;
         this.h = h;
         this.w = w;
         this.isRemove = false;
      }
   }

   // map에 박스를 채우거나(val=k) 지우는(val=0) 함수
   public static void fillMap(int k, int val) {
      // 1. boxes[k] 정보를 가져와서,
      Box getBox = boxes[k]; // (참조값)

      // 2. 해당 박스 범위(r ~ r+h, c ~ c+w)만큼 map[][]에 val 값을 채운다.
      for (int y=getBox.r; y<getBox.r + getBox.h; y++) {
         for (int x=getBox.c; x<getBox.c + getBox.w; x++) {
            map[y][x] = val;
         }
      }
   }

   // 2-1. 중력 적용
   public static void gravity() {
      // while(true) 무한 루프 (더 이상 움직임이 없을때까지 반복!)
      while(true) {
         boolean isMoved = false;  // 이번 턴에 움직임이 있었는지 반복

         // 1. 1번~100번 박스 중 살아있는 것들을 순회
         for (int k=1; k<=100; k++) {
            Box box = boxes[k];

            if (box == null) {
               continue;  // 비어있으면 넘어간다.
            }

            if (box.isRemove == false) {  // 아직 없어지지 않은 상자에 대해서만 진행
               // 2. 각 박스를 "더 이상 못 내려갈 때까지" or "한 칸씩" 내리기 시도

               // 우선 내려갈 수 있는지부터 확인하기! 
               boolean canDrop = true; // 장애물이 하나라도 있으면 false로 바꾸고 break하기 
               int endY = box.r + box.h - 1;  // 상자영역에서 가장 큰 행 바닥의 행 번호 

               if (endY + 1 == N + 1) { // 범위를 벗어나면? 
                  canDrop = false;  
               }
               else { // 범위 안임 
                  for (int x = box.c; x < box.c + box.w; x++) { // 바닥행의 모든 칸 순회 
                     if (map[endY+1][x] != 0) { // 다음 칸이 비어있지 않으면
                        canDrop = false; // 다음이 막힘 
                        break; 
                     }
                  }
               }

               // 여기까지 통과했다면, 내려갈 수 있는거임 
               if (canDrop) { // 내려갈 수 있다면,
                  fillMap(k, 0); 
                  box.r += 1;  // 이동
                  fillMap(k, k);  // 다시 채우기
                  isMoved = true;
               }
            }
         }

         if (isMoved == false) { // 1~100번 다 훑었는데 아무도 안 움직 -> 종료 
            break;
         }
      }
   }

   // 2-2-1. 좌측 하차 로직
   public static boolean canRemoveLeft() {
      // TODO: 1번~100번 박스 순회 (번호 작은 순)
         // 그러한 택배가 여러개 --> 택배 번호(k)가 작은 택배를 먼저 하차함 
         // --> 문제의 이 조건때문에 번호 작은 순으로 봐야함 
      
      for (int k=1; k<=100; k++) {
         // 1. 해당 박스의 "왼쪽 열"이 전부 비어있는지(0인지) 확인
         Box box = boxes[k];

         // 박스가 없거나, 이미 제거된 박스는 패스
         if (box == null || box.isRemove == true) {
            continue;
         }

         boolean canRemove = true; // 왼쪽이 비어있는지 검사

         for (int y=box.r; y<box.r+box.h; y++) {
            for (int x=1; x<box.c; x++) {
               if (map[y][x] != 0) {
                  canRemove = false;
                  break;
               }
            }

            // x에서 실패해서 나왔다면, y 반복문도 탈출해야 함!
            if (!canRemove) {
               break;
            }
         }

         // 검사를 무사히 통과했다면?
         if (canRemove == true) {
            fillMap(k, 0);  // 맵에서 지우기
            box.isRemove = true; 
            sb.append(k).append("\n"); // 번호 기록
            remainingBoxCnt--;  // 남은 개수 감소
            return true;
         }
      }
      // 다 봤는데도 뺄 게 없음
      return false;
   }

   // [2-2-2] 우측 하차 로직
   public static boolean canRemoveRight() {
      // TODO: 1번~100번 박스 순회 (번호 작은 순)
         // 그러한 택배가 여러개 --> 택배 번호(k)가 작은 택배를 먼저 하차함 
         // --> 문제의 이 조건때문에 번호 작은 순으로 봐야함 
      
      for (int k=1; k<=100; k++) {
         // 1. 해당 박스의 "오른쪽 열"이 전부 비어있는지(0인지) 확인
         Box box = boxes[k];

         // 박스가 없거나, 이미 제거된 박스는 패스
         if (box == null || box.isRemove == true) {
            continue;
         }

         boolean canRemove = true; // 오른쪽이 비어있는지 검사

         for (int y=box.r; y<box.r+box.h; y++) {
            for (int x=box.c + box.w; x<N+1; x++) {
               if (map[y][x] != 0) {
                  canRemove = false;
                  break;
               }
            }

            // x에서 실패해서 나왔다면, y 반복문도 탈출해야 함!
            if (!canRemove) {
               break;
            }
         }

         // 검사를 무사히 통과했다면?
         if (canRemove == true) {
            fillMap(k, 0);  // 맵에서 지우기
            box.isRemove = true; 
            sb.append(k).append("\n"); // 번호 기록
            remainingBoxCnt--;  // 남은 개수 감소
            return true;
         }
      }
      // 다 봤는데도 뺄 게 없음
      return false;
   }


   public static void main(String[] args) throws IOException {
      sb = new StringBuilder();
		br = new BufferedReader(new InputStreamReader(System.in));
      
      st = new StringTokenizer(br.readLine().trim());
      N = Integer.parseInt(st.nextToken());
      M = Integer.parseInt(st.nextToken());
      remainingBoxCnt = M; // 처음에는 M개 남음 

      map = new int[N+2][N+2];
      boxes = new Box[105]; // k는 최대 100인데, 널널하게 105로 설정 

      // 모든 택배 투입
      for (int m=0; m<M; m++) {
         st = new StringTokenizer(br.readLine().trim());
         int k = Integer.parseInt(st.nextToken());
         int h = Integer.parseInt(st.nextToken()); // 세로
         int w = Integer.parseInt(st.nextToken()); // 가로
         int c = Integer.parseInt(st.nextToken()); // 열 위치

         // 5. 초기 위치 잡기
         // 일단 맨 위(행 1)에 생성해두고, 나중에 제자리 찾기
         boxes[k] = new Box(1, c, h, w);

         // map에 정보 채우기
         // k번째 박스 위치에 val값을 채우는 함수
         fillMap(k, k); // fillMap(k, val)

         // 2-1. 투입된 택배들은 중력에 의해 하단으로 떨어진다. 
         gravity();  // 바닥까지 내리기 
      }

      // 2-2. 모든 택배 투입이 완료되면, '공간에 있는 택배를 모두 하차할 때까지' 다음 과정 무한 반복
      while (remainingBoxCnt > 0) {  // "남은 상자가 있다면 계속 해!"
         // 2-2-1. 좌측 하차 시도
         if (canRemoveLeft()) { // 왼쪽 삭제가 가능하다면 true 반환, canRemoveLeft에서 삭제 가능하다면 삭제까지 진행됨 
            gravity(); // 하나 뺏으니 중력 적용
         }
         
         // 2-2-2. 우측 하차 시도
         if (canRemoveRight()) {
            gravity(); 
         }
      }

      // [출력] 최종 정답 출력 (여기까지 있어야 완성!)
      System.out.print(sb);
      
    }
}