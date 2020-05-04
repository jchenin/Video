package jin.chen.utils;

import java.util.List;

/**
 * @Description: 封装分页后的数据格式
 */
public class PagedResult {

    private int currentPage;			// 当前页数
    private int totalPages;			// 总页数
    private long records;		// 总记录数
    private List<?> rows;		// 每行显示的内容

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getRecords() {
        return records;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
