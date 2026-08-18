/**
 * Test Cases บังคับ 8 กรณีตามโจทย์ + AssertUtil เล็ก ๆ
 * รันกับทั้ง Algorithm A และ Algorithm B แล้วเทียบว่าให้ผลลัพธ์ตรงกัน (Current / Back size / Forward size)
 */
public class TestCases {

    private static int passCount = 0;
    private static int failCount = 0;

    public static void runAll() {
        passCount = 0;
        failCount = 0;
        System.out.println("\n=== รัน Test Cases บังคับ 8 กรณี (เทียบ Algorithm A กับ B) ===");

        test1_backWhenNoPrevious();
        test2_forwardWhenNoNext();
        test3_openFirstPage();
        test4_openSamePageAgain();
        test5_backMultipleTimes();
        test6_forwardMultipleTimes();
        test7_backThenVisitNew();
        test8_largeHistory();

        System.out.println("\nสรุปผล: PASS=" + passCount + " FAIL=" + failCount);
    }

    private static BrowserHistory[] both() {
        return new BrowserHistory[]{ new TwoStackHistory(), new ArrayListHistory() };
    }

    private static void check(String testName, boolean condition, String detail) {
        if (condition) {
            passCount++;
            System.out.println("  [PASS] " + testName + " - " + detail);
        } else {
            failCount++;
            System.out.println("  [FAIL] " + testName + " - " + detail);
        }
    }

    /** 1. กด Back เมื่อไม่มีหน้าก่อนหน้า */
    private static void test1_backWhenNoPrevious() {
        for (BrowserHistory h : both()) {
            h.visit(new Page("A", null, null));
            boolean result = h.back();
            check("1. Back เมื่อไม่มีหน้าก่อนหน้า [" + h.algorithmName() + "]",
                    !result && h.current().getPageId().equals("A"),
                    "back()=" + result + ", current=" + h.current());
        }
    }

    /** 2. กด Forward เมื่อไม่มีหน้าถัดไป */
    private static void test2_forwardWhenNoNext() {
        for (BrowserHistory h : both()) {
            h.visit(new Page("A", null, null));
            boolean result = h.forward();
            check("2. Forward เมื่อไม่มีหน้าถัดไป [" + h.algorithmName() + "]",
                    !result && h.current().getPageId().equals("A"),
                    "forward()=" + result + ", current=" + h.current());
        }
    }

    /** 3. เปิดหน้าแรก (จาก state ว่างเปล่า) */
    private static void test3_openFirstPage() {
        for (BrowserHistory h : both()) {
            check("3a. ก่อน Visit ใด ๆ [" + h.algorithmName() + "]",
                    h.current() == null, "current=" + h.current());
            h.visit(new Page("Home", null, null));
            check("3b. เปิดหน้าแรก [" + h.algorithmName() + "]",
                    h.current() != null && h.current().getPageId().equals("Home") && h.backSize() == 0,
                    "current=" + h.current() + ", backSize=" + h.backSize());
        }
    }

    /** 4. เปิดหน้าเดิมซ้ำ */
    private static void test4_openSamePageAgain() {
        for (BrowserHistory h : both()) {
            h.visit(new Page("A", null, null));
            h.visit(new Page("A", null, null)); // เปิดซ้ำ ถือเป็นการ visit ใหม่ตามปกติของ browser
            check("4. เปิดหน้าเดิมซ้ำ [" + h.algorithmName() + "]",
                    h.backSize() == 1 && h.current().getPageId().equals("A"),
                    "backSize=" + h.backSize() + ", current=" + h.current());
        }
    }

    /** 5. Back หลายครั้ง */
    private static void test5_backMultipleTimes() {
        for (BrowserHistory h : both()) {
            h.visit(new Page("A", null, null));
            h.visit(new Page("B", null, null));
            h.visit(new Page("C", null, null));
            h.back();
            h.back();
            check("5. Back หลายครั้ง [" + h.algorithmName() + "]",
                    h.current().getPageId().equals("A") && h.forwardSize() == 2,
                    "current=" + h.current() + ", forwardSize=" + h.forwardSize());
        }
    }

    /** 6. Forward หลายครั้ง */
    private static void test6_forwardMultipleTimes() {
        for (BrowserHistory h : both()) {
            h.visit(new Page("A", null, null));
            h.visit(new Page("B", null, null));
            h.visit(new Page("C", null, null));
            h.back();
            h.back();
            h.forward();
            h.forward();
            check("6. Forward หลายครั้ง [" + h.algorithmName() + "]",
                    h.current().getPageId().equals("C") && h.forwardSize() == 0,
                    "current=" + h.current() + ", forwardSize=" + h.forwardSize());
        }
    }

    /** 7. Back แล้วเปิดหน้าใหม่ -> Forward History ต้องถูกล้าง */
    private static void test7_backThenVisitNew() {
        for (BrowserHistory h : both()) {
            h.visit(new Page("A", null, null));
            h.visit(new Page("B", null, null));
            h.visit(new Page("C", null, null));
            h.back(); // current = B, forward = [C]
            h.visit(new Page("D", null, null)); // forward ต้องถูกล้าง
            check("7. Back แล้วเปิดหน้าใหม่ ล้าง Forward [" + h.algorithmName() + "]",
                    h.forwardSize() == 0 && h.current().getPageId().equals("D") && h.backSize() == 2,
                    "forwardSize=" + h.forwardSize() + ", current=" + h.current() + ", backSize=" + h.backSize());
        }
    }

    /** 8. ประวัติจำนวนมาก */
    private static void test8_largeHistory() {
        for (BrowserHistory h : both()) {
            int n = 20000;
            for (int i = 0; i < n; i++) {
                h.visit(new Page("P" + i, null, null));
            }
            for (int i = 0; i < 5000; i++) {
                h.back();
            }
            check("8. ประวัติจำนวนมาก (n=" + n + ") [" + h.algorithmName() + "]",
                    h.current().getPageId().equals("P" + (n - 1 - 5000)) && h.backSize() == n - 1 - 5000,
                    "current=" + h.current() + ", backSize=" + h.backSize());
        }
    }
}
