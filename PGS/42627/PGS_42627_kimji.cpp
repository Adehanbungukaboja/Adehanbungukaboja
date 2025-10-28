#include <string>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

// 소요 시간을 기준으로 정렬하는 것
struct cmp {
    bool operator()(vector<int> a, vector<int> b) {
        return a[1] > b[1];
    }
};

int solution(vector<vector<int>> jobs) {
    int answer = 0;
    
    // 우선순위 디스크 컨트롤러라는 말이 있음
    // 우선순위 큐를 이용할 것임 
    
    // 작업의 소요시간이 짧은 것, 
    // 작업의 요청 시각이 빠른 것, 
    // 작업의 번호가 작은 것 순으로 우선순위가 높습니다.
    // 소요시간을 기준이 짧은 순으로 정렬되는 pq
    
    // c++에서 우선순위 큐의 오름차순은 비교함수를 써줘야 함
    // greater<vector<int>>를 쓰게 되면 0번째 요소로 비교를 해주기 때문에
    // 구조체 형식으로 비교 함수를 선언해서 1번째 요소인 소요시간으로 정렬하게 해줌
    priority_queue<vector<int>, vector<vector<int>>, cmp> pq;
    
    // 총 소요 시간
    int totalTime = 0;
    // 현재 작업 소요 시간
    int currentTime = 0;
    // 작업을 완료한 개수 
    int cnt = 0;
    // 작업 빨리 시작하는 거 순서대로 정렬
    sort(jobs.begin(), jobs.end());
    
    while (cnt < jobs.size() || !pq.empty()) {
        
        // 아직 작업이 남아있고, 작업 시작 시간이 현재 시간보다 작거나 같으면
        if (cnt < jobs.size() && jobs[cnt][0] <= currentTime) {
            // pq에 넣어서 작업하기
            pq.push([jobs[cnt]]);
            cnt++;
            continue;
        }
        
        // 처리할 작업이 있으면
        if (!pq.empty()) {
            // 현재 작업의 소요시간 더해서 작업 완료 시키고, 현재 시간 증가
            currentTime += pq.top()[1];
            
            // 작업 대기 시간 = 현재 시간 - 요청 시간
            totalTime += (currentTime - pq.top()[0]);
            
            // 작업 완료
            pq.pop();
        }
        // 처리할 작업이 없다면
        else {
            // 제일 빨리 요청되는 작업의 시간으로 점프
            currentTime = jobs[cnt][0];
        }
    }
    
    // 평균 대기 시간 = 총 대기 시간 / 작업의 개수
    answer = totalTime / jobs.size();
    return answer;
}