import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class AutoGitHelper {
    public static void main(String[] args) throws Exception {
        // Windows 콘솔 UTF-8 모드 강제 설정
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                new ProcessBuilder("cmd", "/c", "chcp 65001").inheritIO().start().waitFor();
            } catch (Exception e) {
                // 무시
            }
        }

        Scanner sc = new Scanner(System.in, "UTF-8");

        // ✅ 문제 타입 입력 검증
        String type = "";
        while (type.isEmpty()) {
            System.out.print("문제타입 입력 (boj/pgs/swea): ");
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("BOJ") || input.equals("PGS") || input.equals("SWEA")) {
                type = input;
            } else {
                System.out.println("⚠️ 올바른 문제 타입을 입력하세요 (boj/pgs/swea)");
            }
        }

        System.out.print("문제번호 입력: ");
        String problemNum = sc.nextLine().trim();

        String problemName = "";

        // ✅ BOJ 문제 제목 크롤링
        if (type.equals("BOJ")) {
            try {
                String url = "https://www.acmicpc.net/problem/" + problemNum;
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder html = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) html.append(line);
                br.close();

                Pattern pattern = Pattern.compile("<span id=\"problem_title\">(.*?)</span>");
                Matcher matcher = pattern.matcher(html.toString());
                if (matcher.find()) {
                    problemName = matcher.group(1).trim();
                    System.out.println("문제 제목: " + problemName);
                } else {
                    System.out.print("문제 제목을 찾지 못했습니다. 직접 입력: ");
                    problemName = sc.nextLine().trim();
                }
            } catch (Exception e) {
                System.out.print("크롤링 실패. 수동 입력: ");
                problemName = sc.nextLine().trim();
            }
        } else {
            System.out.print("문제 이름 입력: ");
            problemName = sc.nextLine().trim();
        }

        System.out.print("영문 이름 입력: ");
        String engName = sc.nextLine().trim();

        System.out.print("한글 이름 입력: ");
        String korName = sc.nextLine().trim();

        System.out.print("소스 파일명 입력 (공백=Main.java): ");
        String inputFileName = sc.nextLine().trim();

        // ✅ 문제 링크 입력
        String problemLink = "";
        if (type.equals("BOJ")) {
            problemLink = "https://www.acmicpc.net/problem/" + problemNum;
        } else {
            System.out.print("문제 링크 입력: ");
            problemLink = sc.nextLine().trim();
        }

        // ✅ 언어 선택 (기본값: JAVA)
        System.out.print("Java를 사용하셨나요? (Enter=yes, n=no): ");
        String langInput = sc.nextLine().trim().toLowerCase();
        boolean isJava = langInput.isEmpty() || !langInput.equals("n");
        boolean isCpp = !isJava;

        // ✅ 성능 정보 입력
        System.out.print("메모리 입력 (" + (type.equals("PGS") ? "MB" : "KB") + "): ");
        String memory = sc.nextLine().trim();
        String memoryUnit = type.equals("PGS") ? "MB" : "KB";

        System.out.print("실행 시간 입력 (ms): ");
        String execTime = sc.nextLine().trim();

        System.out.print("푸는 데 걸린 시간 입력 (분): ");
        String solveTime = sc.nextLine().trim();

        String srcFileName;
        String fileExtension;

        if (inputFileName.isEmpty()) {
            srcFileName = "Main.java";
            fileExtension = ".java";
        } else {
            srcFileName = inputFileName;
            // 확장자 추출
            int dotIndex = inputFileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = inputFileName.substring(dotIndex);
            } else {
                fileExtension = ".java"; // 기본값
            }
        }

        String branchName = String.format("%s/%s/%s", type, problemNum, engName);
        String newFileName = String.format("%s_%s_%s%s", type, problemNum, engName, fileExtension);

        Path targetDir = Paths.get("BOJ".equals(type) ? "BOJ" : "src", problemNum);
        Files.createDirectories(targetDir);

        Path srcFile = Paths.get(srcFileName);
        if (!Files.exists(srcFile)) {
            System.out.println("⚠️ " + srcFileName + " 파일이 없습니다. 프로젝트 루트에 두세요.");
            return;
        }

        // ✅ git 명령 실행 (파일 이동 전에 브랜치 생성)
        runCommand("git checkout main");  // main 브랜치로 먼저 이동
        runCommand("git checkout -b " + branchName);

        Path destFile = targetDir.resolve(newFileName);
        // 안전하게 복사 후 삭제
        Files.copy(srcFile, destFile, StandardCopyOption.REPLACE_EXISTING);
        Files.delete(srcFile);

        runCommand("git add .");

        // ✅ 커밋 메시지 UTF-8 파일로 만들어서 전달 (제목 + body)
        String commitTitle = String.format("[%s %s] %s - %s", type, problemNum, problemName, korName);

        // 커밋 body 템플릿 생성
        StringBuilder commitBody = new StringBuilder();
        commitBody.append("\n\n"); // 제목과 body 사이 공백
        commitBody.append("## 🔗 문제 링크\n");
        commitBody.append(String.format("[%s %s - %s](%s)\n\n", type, problemNum, problemName, problemLink));
        commitBody.append("## 📘 언어\n");
        commitBody.append(String.format("- [%s] C++\n", isCpp ? "x" : " "));
        commitBody.append(String.format("- [%s] JAVA\n\n", isJava ? "x" : " "));
        commitBody.append("## ⏱️ 성능\n");
        commitBody.append(String.format("- 메모리: %s %s\n", memory, memoryUnit));
        commitBody.append(String.format("- 실행 시간: %s ms\n", execTime));
        commitBody.append(String.format("- 푸는 데 걸린 시간(개인): %s 분\n\n", solveTime));
        commitBody.append("## ✏️ 풀이 아이디어\n");
        commitBody.append("- 핵심 접근 방법 요약\n");
        commitBody.append("- 사용한 알고리즘 / 자료구조\n");

        String fullCommitMsg = commitTitle + commitBody.toString();

        Path tempMsg = Files.createTempFile("git_commit_msg_", ".txt");
        Files.writeString(tempMsg, fullCommitMsg, StandardCharsets.UTF_8);
        runCommand("git commit -F \"" + tempMsg.toAbsolutePath() + "\"");
        Files.deleteIfExists(tempMsg);

        // ✅ push 실행
        runCommand("git push -u origin " + branchName);

        System.out.println("\n✅ 모든 작업 완료!");
        System.out.println("📂 이동된 파일: " + destFile.toAbsolutePath());
        System.out.println("🌿 브랜치명: " + branchName);
        System.out.println("💬 커밋 제목: " + commitTitle);
        System.out.println("🚀 Push 완료! GitHub에서 PR을 생성하세요.");
    }

    // ✅ Windows / Mac / Linux 자동 감지
    private static void runCommand(String cmd) throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder pb;
        if (os.contains("win")) {
            pb = new ProcessBuilder("cmd.exe", "/c", cmd);
        } else {
            pb = new ProcessBuilder("/bin/bash", "-c", cmd);
        }
        pb.directory(new File(System.getProperty("user.dir")));

        // UTF-8 환경 변수 설정
        Map<String, String> env = pb.environment();
        env.put("JAVA_TOOL_OPTIONS", "-Dfile.encoding=UTF-8");

        pb.inheritIO();
        Process process = pb.start();
        process.waitFor();
    }
}
