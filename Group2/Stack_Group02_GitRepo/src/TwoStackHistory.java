import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Algorithm A: Two-Stack Method
 *
 * ใช้ backStack, forwardStack และ currentPage
 * แนวคิดหลัก:
 *   - backStack เก็บหน้าที่ผ่านมาแล้ว (บนสุด = หน้าล่าสุดก่อนหน้าปัจจุบัน)
 *   - forwardStack เก็บหน้าที่เคย Back ออกมา เผื่อผู้ใช้กด Forward กลับไป
 *   - เมื่อ Visit หน้าใหม่ ต้อง push หน้าปัจจุบันลง backStack แล้วล้าง forwardStack ทิ้งทั้งหมด
 *
 * Invariant ที่รักษาไว้ตลอดเวลา:
 *   ลำดับใน backStack (จากล่างขึ้นบน) คือลำดับการเข้าชมหน้าก่อน currentPage เรียงตามเวลา
 *   ลำดับใน forwardStack (จากล่างขึ้นบน) คือลำดับหน้าที่ถูก Back ออกมา เรียงจากไกลสุดไปใกล้สุด
 */
public class TwoStackHistory implements BrowserHistory {

    private final Deque<Page> backStack = new ArrayDeque<>();
    private final Deque<Page> forwardStack = new ArrayDeque<>();
    private Page currentPage = null;

    private long pushCount = 0;
    private long popCount = 0;

    @Override
    public void visit(Page page) {
        if (page == null) throw new IllegalArgumentException("page ห้ามเป็น null");
        if (currentPage != null) {
            backStack.push(currentPage);
            pushCount++;
        }
        // ล้าง Forward History ทิ้งทั้งหมด (Amortized O(1) ต่อ operation โดยรวม
        // เพราะแต่ละ element ถูก push ลง forwardStack ได้อย่างมากหนึ่งครั้ง
        // ก่อนจะถูกล้างทิ้งไปตลอดกาล)
        int cleared = forwardStack.size();
        forwardStack.clear();
        popCount += cleared; // นับการนำออกเป็น "pop" เชิงตรรกะ
        currentPage = page;
    }

    @Override
    public boolean back() {
        if (backStack.isEmpty()) {
            return false; // ไม่มีหน้าก่อนหน้า
        }
        if (currentPage != null) {
            forwardStack.push(currentPage);
            pushCount++;
        }
        currentPage = backStack.pop();
        popCount++;
        return true;
    }

    @Override
    public boolean forward() {
        if (forwardStack.isEmpty()) {
            return false; // ไม่มีหน้าถัดไป
        }
        if (currentPage != null) {
            backStack.push(currentPage);
            pushCount++;
        }
        currentPage = forwardStack.pop();
        popCount++;
        return true;
    }

    @Override
    public Page current() {
        return currentPage;
    }

    @Override
    public int backSize() { return backStack.size(); }

    @Override
    public int forwardSize() { return forwardStack.size(); }

    @Override
    public void displayHistory() {
        System.out.println("  Current : " + (currentPage == null ? "(none)" : currentPage));
        System.out.println("  Back    : " + stackToString(backStack));
        System.out.println("  Forward : " + stackToString(forwardStack));
    }

    private String stackToString(Deque<Page> stack) {
        if (stack.isEmpty()) return "(empty)";
        StringBuilder sb = new StringBuilder("[");
        Iterator<Page> it = stack.iterator(); // top -> bottom
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) sb.append(", ");
        }
        sb.append("]  (ซ้าย = บนสุด/ใกล้ current)");
        return sb.toString();
    }

    @Override
    public void resetCounters() {
        pushCount = 0;
        popCount = 0;
    }

    @Override
    public long getPushCount() { return pushCount; }

    @Override
    public long getPopCount() { return popCount; }

    @Override
    public String algorithmName() { return "Algorithm A: Two-Stack"; }
}
