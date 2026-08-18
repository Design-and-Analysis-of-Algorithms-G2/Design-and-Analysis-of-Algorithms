import java.util.Locale;
import java.util.Scanner;

/**
 * Main Class
 * เมนูสำหรับรับคำสั่งแบบ interactive และเลือกทดสอบ Algorithm A หรือ B
 *
 * คำสั่งที่รองรับ:
 *   VISIT <pageId> [title] [url]
 *   BACK
 *   FORWARD
 *   CURRENT
 *   DISPLAY HISTORY
 *   SWITCH A|B          -> สลับอัลกอริทึมที่ใช้งาน
 *   DEMO                -> รันชุดคำสั่งบังคับตามโจทย์ (VISIT A,B,C,BACK,BACK,FORWARD,VISIT D,BACK,FORWARD)
 *   TEST                -> รัน Test Cases ทั้งหมดและเทียบผลสองอัลกอริทึม
 *   EXPERIMENT          -> รันการทดลองประสิทธิภาพ (n=1000/10000/50000/100000) และบันทึกไฟล์ CSV
 *   EXIT
 */
public class Main {

    private static BrowserHistory history = new TwoStackHistory();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Browser Navigation: Two-Stack vs ArrayList+Index ===");
        System.out.println("กำลังใช้งาน: " + history.algorithmName());
        printHelp();

        while (true) {
            System.out.print("\n> ");
            if (!sc.hasNextLine()) break;
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            try {
                if (!handleCommand(line)) {
                    break; // EXIT
                }
            } catch (Exception e) {
                System.out.println("  [Input Validation] คำสั่งไม่ถูกต้อง: " + e.getMessage());
            }
        }
        System.out.println("จบการทำงาน");
        sc.close();
    }

    private static void printHelp() {
        System.out.println("คำสั่ง: VISIT <id> [title] [url] | BACK | FORWARD | CURRENT | DISPLAY HISTORY");
        System.out.println("        SWITCH A|B | DEMO | TEST | EXPERIMENT | HELP | EXIT");
    }

    /** คืนค่า false เมื่อผู้ใช้สั่ง EXIT */
    private static boolean handleCommand(String line) {
        String upper = line.toUpperCase(Locale.ROOT);

        if (upper.equals("EXIT") || upper.equals("QUIT")) {
            return false;
        }
        if (upper.equals("HELP")) {
            printHelp();
            return true;
        }
        if (upper.startsWith("VISIT")) {
            String[] parts = line.split("\\s+", 4);
            if (parts.length < 2 || parts[1].isBlank()) {
                throw new IllegalArgumentException("ต้องระบุ pageId เช่น VISIT A หรือ VISIT A \"Google\" https://google.com");
            }
            String id = parts[1];
            String title = parts.length >= 3 ? parts[2] : null;
            String url = parts.length >= 4 ? parts[3] : null;
            history.visit(new Page(id, title, url));
            System.out.println("  Visited " + id);
            printStatus();
            return true;
        }
        if (upper.equals("BACK")) {
            boolean ok = history.back();
            System.out.println(ok ? "  ย้อนกลับสำเร็จ" : "  [ไม่มีหน้าก่อนหน้า] BACK ทำไม่ได้");
            printStatus();
            return true;
        }
        if (upper.equals("FORWARD")) {
            boolean ok = history.forward();
            System.out.println(ok ? "  ไปข้างหน้าสำเร็จ" : "  [ไม่มีหน้าถัดไป] FORWARD ทำไม่ได้");
            printStatus();
            return true;
        }
        if (upper.equals("CURRENT")) {
            Page p = history.current();
            System.out.println("  Current: " + (p == null ? "(none, ยังไม่เคย VISIT)" : p));
            return true;
        }
        if (upper.equals("DISPLAY HISTORY") || upper.equals("DISPLAY")) {
            printStatus();
            return true;
        }
        if (upper.startsWith("SWITCH")) {
            String[] parts = line.split("\\s+");
            if (parts.length < 2 || !(parts[1].equalsIgnoreCase("A") || parts[1].equalsIgnoreCase("B"))) {
                throw new IllegalArgumentException("ใช้ SWITCH A หรือ SWITCH B");
            }
            history = parts[1].equalsIgnoreCase("A") ? new TwoStackHistory() : new ArrayListHistory();
            System.out.println("  สลับไปใช้ " + history.algorithmName() + " (ประวัติถูกล้างใหม่)");
            return true;
        }
        if (upper.equals("DEMO")) {
            runDemo();
            return true;
        }
        if (upper.equals("TEST")) {
            TestCases.runAll();
            return true;
        }
        if (upper.equals("EXPERIMENT")) {
            ExperimentRunner.run();
            return true;
        }

        throw new IllegalArgumentException("ไม่รู้จักคำสั่ง '" + line + "' (พิมพ์ HELP เพื่อดูคำสั่งทั้งหมด)");
    }

    private static void printStatus() {
        history.displayHistory();
    }

    /** ชุดคำสั่งบังคับตามโจทย์: VISIT A,B,C -> BACK,BACK -> FORWARD -> VISIT D -> BACK -> FORWARD */
    private static void runDemo() {
        System.out.println("\n--- DEMO: ชุดคำสั่งบังคับ (" + history.algorithmName() + ") ---");
        String[] commands = {
            "VISIT A", "VISIT B", "VISIT C", "BACK", "BACK", "FORWARD", "VISIT D", "BACK", "FORWARD"
        };
        for (String cmd : commands) {
            System.out.println("\n$ " + cmd);
            handleCommand(cmd);
        }
    }
}
