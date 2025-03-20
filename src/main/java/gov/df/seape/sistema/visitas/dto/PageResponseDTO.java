package gov.df.seape.sistema.visitas.dto;

import java.util.List;

/**
 * DTO genérico para respostas paginadas.
 * Fornece informações de paginação junto com os resultados.
 *
 * @param <T> Tipo dos elementos da página
 */
public class PageResponseDTO<T> {
    /**
     * Conteúdo da página atual.
     */
    private List<T> content;

    /**
     * Número total de páginas.
     */
    private int totalPages;

    /**
     * Número total de elementos.
     */
    private long totalElements;

    /**
     * Tamanho da página atual.
     */
    private int pageSize;

    /**
     * Número da página atual.
     */
    private int pageNumber;

    /**
     * Indica se é a primeira página.
     */
    private boolean first;

    /**
     * Indica se é a última página.
     */
    private boolean last;

    // Construtores
    public PageResponseDTO() {}

    public PageResponseDTO(List<T> content, int totalPages, long totalElements, 
                           int pageSize, int pageNumber, boolean first, boolean last) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.first = first;
        this.last = last;
    }

    // Getters e Setters
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}