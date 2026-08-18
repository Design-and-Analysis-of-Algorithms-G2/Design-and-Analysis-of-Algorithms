# Browser Navigation — Two-Stack vs ArrayList+Index

งานปฏิบัติการกลุ่มที่ 2: การออกแบบและวิเคราะห์อัลกอริทึมด้วย Stack โดยใช้ภาษา Java
รายวิชา: [ชื่อรายวิชา] — สมาชิกกลุ่ม:
- นายธนโชติ กสิกิจกรกุล — 68122250097
- นายณัฐดนัย ลิ้มธรรมรงค์ — 66122250037
- นายพงษ์นรินทร์ โคตรวงษ์ทอง — 68122250015
- นายภูวสิษฏ์ ปั้นดี — 67122250042
- นายมณเฑียร พุทธเสน — 68122250063
- นายณัฐวุฒิ ชมภูวิเศษ — 67122250014
- นางสาวพัชราภรณ์ โป้สูงเนิน — 67122250065

## โจทย์

ระบบ Browser ต้องรองรับการเปิดหน้าเว็บใหม่ (`VISIT`), ย้อนกลับ (`BACK`) และไปข้างหน้า (`FORWARD`)
เมื่อผู้ใช้กด Back แล้วเปิดหน้าใหม่ ประวัติของ Forward ต้องถูกล้างทิ้งทันที
เปรียบเทียบอัลกอริทึม 2 วิธี:

- **Algorithm A — Two-Stack Method**: `backStack` + `forwardStack`
- **Algorithm B — ArrayList + Current Index**: `historyList` + `currentIndex`

## โครงสร้างโปรเจกต์

```
src/
  Page.java              Data Model: pageId, title, url, visitedTime
  BrowserHistory.java    Interface กลางของ Algorithm A และ B
  TwoStackHistory.java   Algorithm A (Two-Stack)
  ArrayListHistory.java  Algorithm B (ArrayList + Current Index)
  Main.java              เมนูรับคำสั่ง + Input Validation
  TestCases.java         Test Cases บังคับ 8 กรณี (เทียบ A กับ B)
  ExperimentRunner.java  การทดลองประสิทธิภาพ n = 1k/10k/50k/100k
diagrams/
  flowchart_visit.png          Flowchart ของ VISIT()
  flowchart_back_forward.png   Flowchart ของ BACK() และ FORWARD()
  class_diagram.png            UML Class Diagram
docs/
  Stack_Group02_Report.docx    รายงานฉบับเต็ม
experiment_results.csv         ผลการทดลองจริง (เฉลี่ย 5 รอบ/ขนาดข้อมูล)
```

## วิธีคอมไพล์และรัน

```bash
javac src/*.java -d out
cd out
java Main
```

คำสั่งในเมนู:

| คำสั่ง | ทำอะไร |
|---|---|
| `VISIT <id> [title] [url]` | เปิดหน้าเว็บใหม่ |
| `BACK` / `FORWARD` | ย้อนกลับ / ไปข้างหน้า |
| `CURRENT` | แสดงหน้าปัจจุบัน |
| `DISPLAY HISTORY` | แสดงสถานะทั้งหมด |
| `SWITCH A` / `SWITCH B` | สลับอัลกอริทึมที่ใช้งาน |
| `DEMO` | รันชุดคำสั่งบังคับตามโจทย์ |
| `TEST` | รัน Test Cases 8 กรณี เทียบ A กับ B |
| `EXPERIMENT` | รันการทดลองประสิทธิภาพ บันทึกผลเป็น CSV |

## ผลสรุป

- Test Cases: **18/18 PASS** (8 กรณี × 2 อัลกอริทึม + สถานะเริ่มต้น)
- Time Complexity: ทั้งคู่เป็น `O(1) amortized` สำหรับ `VISIT`, `O(1)` สำหรับ `BACK`/`FORWARD`
- ผลทดลองจริง: **Two-Stack เร็วกว่า ArrayList ประมาณ 4–35 เท่า** แม้ Big-O จะเท่ากัน (ดูรายละเอียดใน `docs/Stack_Group02_Report.docx` หัวข้อ 9)

## การใช้ Generative AI

ดูบันทึกการใช้ AI ฉบับเต็มในรายงาน หัวข้อ 11 (AI Usage Log)
