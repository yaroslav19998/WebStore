package WebStoreGroup.WebStore.DTOUtils;

import java.util.List;

public class PageDTO<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private Long elementsCount;
    public PageDTO(List<T> content, int currentPage, int totalPages) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public PageDTO(List<T> content, int currentPage, int totalPages, Long elementsCount) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.elementsCount = elementsCount;
    }

    public Long getElementsCount() {
        return elementsCount;
    }

    public void setElementsCount(Long elementsCount) {
        this.elementsCount = elementsCount;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
