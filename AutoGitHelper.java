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

        System.out.print("문제타입 입력 (boj/pgs/swea): ");
        String type = sc.next().toUpperCase();

        System.out.print("문제번호 입력: ");
        String problemNum = sc.next();

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
                    problemName = sc.next();
                }
            } catch (Exception e) {
                System.out.print("크롤링 실패. 수동 입력: ");
                problemName = sc.next();
            }
        } else {
            System.out.print("문제 이름 입력: ");
            problemName = sc.next();
        }

        System.out.print("영문 이름 입력: ");
        String engName = sc.next();

        System.out.print("한글 이름 입력: ");
        String korName = sc.next();

        // 파일명 입력 (공백이면 Main.java 기본값)
        sc.nextLine(); // 버퍼 비우기
        System.out.print("소스 파일명 입력 (공백=Main.java): ");
        String inputFileName = sc.nextLine().trim();

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

        Path targetDir = Paths.get("BOJ".equals(type) ? "BOJ" : "src", type, problemNum);
        Files.createDirectories(targetDir);

        Path srcFile = Paths.get(srcFileName);
        if (!Files.exists(srcFile)) {
            System.out.println("⚠️ " + srcFileName + " 파일이 없습니다. 프로젝트 루트에 두세요.");
            return;
        }

        Path destFile = targetDir.resolve(newFileName);
        // 안전하게 복사 후 삭제
        Files.copy(srcFile, destFile, StandardCopyOption.REPLACE_EXISTING);
        Files.delete(srcFile);

        // ✅ git 명령 실행
        runCommand("git checkout -b " + branchName);
        runCommand("git add .");

        // ✅ 커밋 메시지 UTF-8 파일로 만들어서 전달
        String commitMsg = String.format("[%s %s] %s - %s", type, problemNum, problemName, korName);
        Path tempMsg = Files.createTempFile("git_commit_msg_", ".txt");
        Files.writeString(tempMsg, commitMsg, StandardCharsets.UTF_8);
        runCommand("git commit -F \"" + tempMsg.toAbsolutePath() + "\"");
        Files.deleteIfExists(tempMsg);

        System.out.println("\n✅ 모든 작업 완료!");
        System.out.println("📂 이동된 파일: " + destFile.toAbsolutePath());
        System.out.println("🌿 브랜치명: " + branchName);
        System.out.println("💬 커밋 메시지: " + commitMsg);
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
