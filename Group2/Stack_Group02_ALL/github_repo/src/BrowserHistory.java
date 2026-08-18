/**
 * Interface กลางสำหรับ Algorithm A และ Algorithm B
 * เพื่อให้ Main Class เรียกใช้งานได้แบบเดียวกัน (Polymorphism)
 * และนับ Operation / เปรียบเทียบประสิทธิภาพได้อย่างยุติธรรม
 */
public interface BrowserHistory {

    /** VISIT page: เปิดหน้าเว็บใหม่ ต้องล้าง Forward History ทิ้ง */
    void visit(Page page);

    /** BACK: ย้อนกลับไปหน้าก่อนหน้า คืนค่า false ถ้าทำไม่ได้ */
    boolean back();

    /** FORWARD: ไปหน้าถัดไป คืนค่า false ถ้าทำไม่ได้ */
    boolean forward();

    /** CURRENT: คืนหน้าปัจจุบัน (null ถ้ายังไม่เคย visit) */
    Page current();

    /** จำนวนหน้าที่อยู่ใน Back History */
    int backSize();

    /** จำนวนหน้าที่อยู่ใน Forward History */
    int forwardSize();

    /** DISPLAY HISTORY: แสดงสถานะปัจจุบันทั้งหมด */
    void displayHistory();

    /** รีเซ็ต Operation Counter (ใช้ตอนเริ่มการทดลองแต่ละรอบ) */
    void resetCounters();

    /** จำนวน push ทั้งหมดตั้งแต่ resetCounters ครั้งล่าสุด */
    long getPushCount();

    /** จำนวน pop ทั้งหมดตั้งแต่ resetCounters ครั้งล่าสุด */
    long getPopCount();

    /** ชื่ออัลกอริทึม สำหรับพิมพ์รายงาน */
    String algorithmName();
}
