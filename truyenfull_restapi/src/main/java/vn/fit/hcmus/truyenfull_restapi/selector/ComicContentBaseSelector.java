package vn.fit.hcmus.truyenfull_restapi.selector;

public interface ComicContentBaseSelector<T extends ChapterContentBaseSelector>{
    String title();

    String author();

    String category();

    String description();

    String image();

    String doneFlag();

    String rate();

    String dataFrom();

    String getChapterList();

    T getChapterSelector();

    String getNextChapterPageSelector();

    String getCurrChapterPageSelector();

}
