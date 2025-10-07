#include <string>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

// 방문 처리
vector<vector<bool>> visited;

// 방향 배열
int deltaRow[4] = {-1, 1, 0, 0};
int deltaCol[4] = {0, 0, -1, 1};

// 기준을 -1, 1과 같은 음수 좌표 안생기게 좌표 조정하기
// 오직 모양만 비교!! 
// 처음에 좌표만 저장했는데, 좌표가 다를 수 있어서 시작 좌표 기준으로 차이나는 것을 저장
// 근데 그래도 기준으로 잡히는 것에 따라 모양은 같지만
// 기역자 모양이 0,0 0,1 1,1 이 될 수도 있지만 / 0,-1 0,0 1,0 이런식으로 나올 수도 있다.
// 따라서 제일 왼쪽상단의 좌표가 0,0이 되게 조절을 하고, 거기를 기준으로 움직이는 좌표가 될 수 있게 해야함
// 하지만 ㅓ 같은 모양이 있을 수도 있으므로, 음수 좌표가 나오지 않게 row와 col의 최소를 찾아야 함 
vector<vector<int>> transPos(vector<vector<int>> originBlock) {
    // 기존 블록에서 제일 작은 수들을 찾음
    int minRow = 51;
    int minCol = 51;
    
    for (int i = 0; i < originBlock.size(); i++) {
        minRow = min(minRow, originBlock[i][0]);
        minCol = min(minCol, originBlock[i][1]);
    }
    
    // 음수좌표 안나오게 대응하기
    for (int i = 0; i < originBlock.size(); i++) {
        // 음수 좌표가 나오면 빼야 더해서 0으로 되니깐 빼야함 
        originBlock[i][0] -= minRow;
        originBlock[i][1] -= minCol;
    }
    
    return originBlock;
}

vector<vector<int>> rotate(vector<vector<int>> puzzle) {
    // 90도 돌리는 것임
    // (r, c) 가 있으면 돌면 (c, -r) 이 되는 것을 볼 수 있음
    // 돌려보자
    vector<vector<int>> rotated;
    for (int i = 0; i < puzzle.size(); i++) {
        int row = puzzle[i][0];
        int col = puzzle[i][1];
        
        rotated.push_back({col, -row});
    }
    
    // 회전시킨것들도 좌표 변환
    return transPos(rotated);
}

bool isSame(vector<vector<int>> blank, vector<vector<int>> puzzle) {
    
    // 크기가 다르면 애초에 다른 블럭
    if (blank.size() != puzzle.size()) return false;
    
    // 같으면 일단 비교해볼만 함
    // 작은 좌표 순부터 비교해야하니 정렬
    sort(blank.begin(), blank.end());
    // 4번 돌려
    for (int rotateCount = 0; rotateCount < 4; rotateCount++) {
        // 작은 좌표 순부터 비교해야하니 정렬
        sort(puzzle.begin(), puzzle.end());
        
        bool flag = true;
        for (int i = 0; i < blank.size(); i++) {
            if (blank[i][0] != puzzle[i][0] || blank[i][1] != puzzle[i][1]) {
                flag = false;
                break;
            }
        }
        
        if (flag)   return true;
        
        // 이 모양이 아닌가? 돌려보자
        puzzle = rotate(puzzle);
    }
    
    // 회전까지 시켜봤는데 안나오면.. 틀려먹은거지
    return false;
}

// 타켓에 따라 모양을 찾는 함수
vector<vector<int>> findShape(vector<vector<int>> board, int startRow, int startCol, int target) {
    // BFS를 하는 것이니, BFS를 위한 queue
    queue<pair<int, int>> q;
    // 초기값 넣음
    q.push({startRow, startCol});
    // 방문 처리 하기
    visited[startRow][startCol] = true;
    
    // 블록을 구성하는 좌표 저장하기?
    // 좌표를 저장하는 것이 모양을 저장하는 것이니
    // 모양의 기준 점은 0,0으로 할 수 있도록 하기
    vector<vector<int>> block;
    block.push_back({0, 0});
    
    // q가 다 빌 때까지
    while (!q.empty()) {
        int curRow = q.front().first;
        int curCol = q.front().second;
        q.pop();
        
        // 4방향 탐색
        for (int dir = 0; dir < 4; dir++) {
            int newRow = curRow + deltaRow[dir];
            int newCol = curCol + deltaCol[dir];
            
            // 범위 벗어나면 안됨
            if (newRow < 0 || newRow >= board.size() || newCol < 0 || newCol >= board.size())   continue;
            // 방문했거나 target이 아닌 블록이면 안됨
            if (visited[newRow][newCol] || board[newRow][newCol] != target) continue;
            
            // 그게 아니라면 모양이면 넣고 탐색하기
            q.push({newRow, newCol});
            // 방문처리도
            visited[newRow][newCol] = true;
            // 기준이 되는 0, 0 좌표에서 얼마만큼 움직인 위치에 있느냐를 저장
            // 좌표 저장이 아닌 목표를 구성하는 것이기에 
            block.push_back({newRow - startRow, newCol - startCol});
        }
    }
    
    // 좌표 변환 
    return transPos(block);
}

int solution(vector<vector<int>> game_board, vector<vector<int>> table) {
    int answer = 0;
    
    // 테이블에서 사용가능한 퍼즐의 모양 찾기 -> 1을 기준으로(퍼즐이 있는 공간)
    // 게임보드에서 채워넣어질 공간 찾기 -> 0을 기준으로(빈공간 즉, 퍼즐이 들어갈 공간)
    // BFS 이용
    
    // 1. 게임 보드에서 빈 공간 추출하기
    vector<vector<vector<int>>> blanks;
    // 방문 배열 초기화
    // visited 초기화
    visited.assign(game_board.size(), vector<bool>(game_board.size(), false));
    for (int row = 0; row < game_board.size(); row++) {
        for (int col = 0; col < game_board.size(); col++) {
            // 방문한 적이 없고, 빈공간(0)이면 bfs로 빈공간 추출
            if (!visited[row][col] && game_board[row][col] == 0) {
                blanks.push_back(findShape(game_board, row, col, 0));
            }
        }
    }
    
    // 2. 테이블에서 퍼즐 조각 추출하기
    vector<vector<vector<int>>> puzzles;
    // 방문 배열 초기화
    // visited 초기화
    visited.assign(game_board.size(), vector<bool>(game_board.size(), false));
    for (int row = 0; row < table.size(); row++) {
        for (int col = 0; col < table.size(); col++) {
            // 방문한 적이 없고, 퍼즐인 공간(1)이면 bfs로 빈공간 추출
            if (!visited[row][col] && table[row][col] == 1) {
                puzzles.push_back(findShape(table, row, col, 1));
            }
        }
    }
    
    // 3. 빈 공간과 퍼즐 조각 비교하기 
    // 완탐으로 하고, 퍼즐 종각은 회전시키며 모양이 일치해지는 지 판단하기
    vector<bool> used(puzzles.size(), false);
    for (int blankIndex = 0; blankIndex < blanks.size(); blankIndex++) {
        for (int puzzleIndex = 0; puzzleIndex < puzzles.size(); puzzleIndex++) {
            // 사용한 퍼즐이면 패스
            if (used[puzzleIndex])  continue;
            // 사이즈가 달라도 패스 
            if (blanks[blankIndex].size() != puzzles[puzzleIndex].size()) continue;
            
            // 그렇지 않다면 이 퍼즐을 해당 모양에 집어넣을 수 있는 지 보기
            if (isSame(blanks[blankIndex], puzzles[puzzleIndex])) {
                // 집어 넣을 수 있으면 정답에 해당 블록의 크기만큼 더하기
                answer += puzzles[puzzleIndex].size();
                used[puzzleIndex] = true;
                break; // 해당 퍼즐 비교 중단 
            }
        }
    }
    
    return answer;
}