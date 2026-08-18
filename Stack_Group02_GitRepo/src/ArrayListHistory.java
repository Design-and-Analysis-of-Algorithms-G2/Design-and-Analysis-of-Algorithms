import java.util.ArrayList;
import java.util.List;

/**
 * Algorithm B: ArrayList and Current Index Method
 *
 * ใช้ historyList (ลำดับหน้าทั้งหมดที่เคยเปิด) และ currentIndex (ตำแหน่งปัจจุบัน)
 * แนวคิดหลัก:
 *   - historyList[0 .. currentIndex] คือ Back History + Current
 *   - historyList[currentIndex+1 .. end] คือ Forward History
 *   - เมื่อ Visit หน้าใหม่ ต้องตัดทุกอย่างหลัง currentIndex ทิ้ง (เทียบเท่าล้าง Forward)
 *     แล้วเพิ่มหน้าใหม่ต่อท้าย และเลื่อน currentIndex ไปหน้าสุดท้าย
 *
 * Invariant ที่รักษาไว้ตลอดเวลา:
 *   0 <= currentIndex < historyList.size() (เมื่อมีอย่างน้อย 1 หน้า)
 *   historyList เรียงตามลำดับเวลาที่เข้าชมจริงในสาย (branch) ปัจจุบันเสมอ
 */
public class ArrayListHistory implements BrowserHistory {

    private final List<Page> historyList = new ArrayList<>();
    private int currentIndex = -1; // ยังไม่มีหน้าใด ๆ

    private long pushCount = 0; // นับการ "เพิ่ม/ตัด" element เป็นหน่วยเทียบเท่า push/pop
    private long popCount = 0;

    @Override
    public void visit(Page page) {
        if (page == null) throw new IllegalArgumentException("page ห้ามเป็น null");

        // ตัด Forward History ทิ้งทั้งหมด: ลบ element ตั้งแต่ currentIndex+1 ถึงท้าย list
        int removedCount = historyList.size() - (currentIndex + 1);
        if (removedCount > 0) {
            historyList.subList(currentIndex + 1, historyList.size()).clear();
            popCount += removedCount;
        }

        historyList.add(page);
        currentIndex++;
        pushCount++;
    }

    @Override
    public boolean back() {
        if (currentIndex <= 0) {
            return false; // ไม่มีหน้าก่อนหน้า
        }
        currentIndex--;
        popCount++; // เทียบเท่าการ pop เชิงตรรกะ (ย้ายตำแหน่งถอยหลัง)
        return true;
    }

    @Override
    public boolean forward() {
        if (currentIndex < 0 || currentIndex >= historyList.size() - 1) {
            return false; // ไม่มีหน้าถัดไป
        }
        currentIndex++;
        pushCount++; // เทียบเท่าการ push เชิงตรรกะ (ย้ายตำแหน่งไปข้างหน้า)
        return true;
    }

    @Override
    public Page current() {
        if (currentIndex < 0) return null;
        return historyList.get(currentIndex);
    }

    @Override
    public int backSize() {
        return Math.max(0, currentIndex);
    }

    @Override
    public int forwardSize() {
        if (currentIndex < 0) return 0;
        return historyList.size() - 1 - currentIndex;
    }

    @Override
    public void displayHistory() {
        System.out.println("  Current : " + (current() == null ? "(none)" : current()));
        System.out.println("  Back    : " + sliceToString(0, currentIndex));
        System.out.println("  Forward : " + sliceToString(currentIndex + 1, historyList.size()));
    }

    private String sliceToString(int from, int toExclusive) {
        if (from >= toExclusive || from < 0) return "(empty)";
        StringBuilder sb = new StringBuilder("[");
        for (int i = toExclusive - 1; i >= from; i--) { // ใกล้ current ก่อน เพื่อเทียบกับ Two-Stack
            sb.append(historyList.get(i));
            if (i > from) sb.append(", ");
        }
        sb.append("]  (ซ้าย = ใกล้ current)");
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
    public String algorithmName() { return "Algorithm B: ArrayList + Current Index"; }
}
