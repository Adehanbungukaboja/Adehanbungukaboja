import java.util.*;
import java.io.*;

public class Solution {
    static class Node {
        int data;
        Node next;
        Node(int data) { this.data = data; }
    }

    public static void main(String args[]) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = 10;
        StringBuilder sb = new StringBuilder();

        for (int test_case = 1; test_case <= T; test_case++) {
            Node head = new Node(0);
            Node cur = head;
            Node tail = head;

            int passwordCount = Integer.parseInt(br.readLine());
            String[] st = br.readLine().split(" ");
            for (int i = 0; i < passwordCount; i++) {
                cur.next = new Node(Integer.parseInt(st[i]));
                cur = cur.next;
                tail = cur;
            }

            int orderCount = Integer.parseInt(br.readLine());
            st = br.readLine().split(" ");
            int i = 0;
            int orderSize = st.length;

            while (i < orderSize) {
                String order = st[i];

                if (order.equals("I")) {
                    cur = head;
                    int pos = Integer.parseInt(st[++i]);
                    for (int idx = 0; idx < pos; idx++) cur = cur.next;
                    //현재노드 다음노드를 마지막에 넣어야하니까 선언해놓음
                    Node after = cur.next;
                    int count = Integer.parseInt(st[++i]);
                    for (int c = 0; c < count; c++) {
                        cur.next = new Node(Integer.parseInt(st[++i]));
                        cur = cur.next;
                    }
                    //추가한노드의 끝에 after노드 연결. 만약 after가 없다면 tail은 cur이됨
                    cur.next = after;
                    if (after == null) tail = cur;
                    i++;

                } else if (order.equals("D")) {
                    cur = head;
                    int pos = Integer.parseInt(st[++i]);
                    int deleteCount = Integer.parseInt(st[++i]);
                    for (int idx = 0; idx < pos; idx++) cur = cur.next;

                    Node next = cur.next;
                    for (int idx = 0; idx < deleteCount && next != null; idx++)
                        next = next.next;
                    //현재노드와 넘긴노드를 연결. 만약에 노드가 없다면 tail은 현재노드가 됨
                    cur.next = next;
                    if (next == null) tail = cur;
                    i++;

                } else if (order.equals("A")) {
                    //tail뒤에 잇기
                    cur = tail;
                    int addCount = Integer.parseInt(st[++i]);
                    for (int idx = 0; idx < addCount; idx++) {
                        cur.next = new Node(Integer.parseInt(st[++i]));
                        cur = cur.next;
                    }
                    //맨끝이므로 tail은 cur이됨
                    tail = cur;
                    i++;
                }
            }

            cur = head.next;
            sb.append("#").append(test_case).append(" ");
            for (int k = 0; k < 10; k++) {
                sb.append(cur.data).append(" ");
                cur = cur.next;
            }
            sb.append("\n");
        }

        System.out.print(sb);
    }
}
