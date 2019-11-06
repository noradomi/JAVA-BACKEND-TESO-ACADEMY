package vn.fit.hcmus.truyenfull_restapi.selector;

public interface WebComicBaseSelector<T extends ComicContentBaseSelector,U extends CategoryContentBaseSelector, Z extends  CatalogContentBaseSelector> {
    String mainUrl();

    String getCategoryListSelector();

    String getComicListSelector();

    String getNextStoryPageSelector();

    String getCurrChapterOfComic();

    T getComicContentSelector();

    U getCategoryContentSelector();

    Z getCatalogContentSelector();

    String getUrlComic();
}
