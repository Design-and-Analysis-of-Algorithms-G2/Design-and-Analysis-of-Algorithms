/**
 * Data Model Class: Page
 * เก็บข้อมูลของหน้าเว็บไซต์แต่ละหน้าตามที่โจทย์กำหนด
 * (Page ID, Page Title, URL, Visited Time)
 */
public class Page {
    private final String pageId;
    private final String title;
    private final String url;
    private final long visitedTime;

    public Page(String pageId, String title, String url) {
        if (pageId == null || pageId.isBlank()) {
            throw new IllegalArgumentException("pageId ห้ามว่าง");
        }
        this.pageId = pageId;
        this.title = title == null ? pageId : title;
        this.url = url == null ? ("https://example.com/" + pageId) : url;
        this.visitedTime = System.nanoTime();
    }

    public String getPageId() { return pageId; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public long getVisitedTime() { return visitedTime; }

    @Override
    public String toString() {
        return pageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;
        return pageId.equals(((Page) o).pageId);
    }

    @Override
    public int hashCode() {
        return pageId.hashCode();
    }
}
