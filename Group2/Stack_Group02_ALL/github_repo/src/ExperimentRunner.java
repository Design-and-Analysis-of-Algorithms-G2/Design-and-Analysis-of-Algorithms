import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * การทดลองประสิทธิภาพบังคับตามโจทย์:
 *   1. สร้างประวัติขนาด n = 1,000 / 10,000 / 50,000 / 100,000 หน้า
 *   2. ย้อนกลับ (Back) ไปยังตำแหน่งกึ่งกลาง
 *   3. เปิดหน้าใหม่ (VISIT) -> วัดเวลาที่ใช้ล้าง/ตัด Forward History
 *   4. เปรียบเทียบ Algorithm A (Two-Stack) กับ Algorithm B (ArrayList+Index)
 *   5. ทำซ้ำ 5 รอบต่อขนาดข้อมูล แล้วรายงานค่าเฉลี่ย
 *
 * ผลลัพธ์ถูกบันทึกเป็น experiment_results.csv เพื่อนำไปทำกราฟและใส่ในรายงาน
 */
public class ExperimentRunner {

    private static final int[] SIZES = {1000, 10000, 50000, 100000};
    private static final int ROUNDS = 5;

    public static void run() {
        System.out.println("\n=== การทดลองประสิทธิภาพ: สร้างประวัติ n หน้า -> Back ไปกึ่งกลาง -> Visit หน้าใหม่ ===");
        System.out.printf("%-8s %-12s %14s %10s %10s %10s%n",
                "n", "Algorithm", "AvgTime(ns)", "Push", "Pop", "Cleared");

        try (PrintWriter csv = new PrintWriter(new FileWriter("experiment_results.csv"))) {
            csv.println("n,algorithm,avg_time_ns,push_count,pop_count,cleared_forward_entries");

            for (int n : SIZES) {
                Result resultA = benchmark(n, true);
                Result resultB = benchmark(n, false);

                System.out.printf("%-8d %-12s %14.1f %10d %10d %10d%n",
                        n, "A (TwoStack)", resultA.avgTimeNs, resultA.push, resultA.pop, resultA.cleared);
                System.out.printf("%-8d %-12s %14.1f %10d %10d %10d%n",
                        n, "B (ArrayList)", resultB.avgTimeNs, resultB.push, resultB.pop, resultB.cleared);

                csv.printf("%d,A_TwoStack,%.1f,%d,%d,%d%n", n, resultA.avgTimeNs, resultA.push, resultA.pop, resultA.cleared);
                csv.printf("%d,B_ArrayList,%.1f,%d,%d,%d%n", n, resultB.avgTimeNs, resultB.push, resultB.pop, resultB.cleared);
            }
            System.out.println("\nบันทึกผลการทดลองลง experiment_results.csv แล้ว");
        } catch (IOException e) {
            System.out.println("  บันทึกไฟล์ CSV ไม่สำเร็จ: " + e.getMessage());
        }
    }

    private static Result benchmark(int n, boolean useTwoStack) {
        long totalTime = 0;
        long lastPush = 0, lastPop = 0;
        int cleared = 0;

        for (int round = 0; round < ROUNDS; round++) {
            BrowserHistory h = useTwoStack ? new TwoStackHistory() : new ArrayListHistory();

            // 1. สร้างประวัติขนาด n
            for (int i = 0; i < n; i++) {
                h.visit(new Page("P" + i, null, null));
            }

            // 2. Back ไปยังตำแหน่งกึ่งกลาง
            int mid = n / 2;
            for (int i = 0; i < mid; i++) {
                h.back();
            }

            int forwardBefore = h.forwardSize();
            h.resetCounters();

            // 3. เปิดหน้าใหม่ และวัดเวลาเฉพาะขั้นตอนนี้ (ซึ่งรวมการล้าง Forward History)
            long start = System.nanoTime();
            h.visit(new Page("NEW", null, null));
            long end = System.nanoTime();

            totalTime += (end - start);
            lastPush = h.getPushCount();
            lastPop = h.getPopCount();
            cleared = forwardBefore;
        }

        double avg = totalTime / (double) ROUNDS;
        return new Result(avg, lastPush, lastPop, cleared);
    }

    private static class Result {
        final double avgTimeNs;
        final long push;
        final long pop;
        final int cleared;

        Result(double avgTimeNs, long push, long pop, int cleared) {
            this.avgTimeNs = avgTimeNs;
            this.push = push;
            this.pop = pop;
            this.cleared = cleared;
        }
    }
}
