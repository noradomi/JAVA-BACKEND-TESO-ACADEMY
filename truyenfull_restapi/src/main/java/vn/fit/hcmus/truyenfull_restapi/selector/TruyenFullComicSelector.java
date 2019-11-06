package vn.fit.hcmus.truyenfull_restapi.selector;

public class TruyenFullComicSelector implements ComicContentBaseSelector<TruyenFullChapterSelector> {

    private TruyenFullComicSelector(){};

//  Singleton Pattern
    private static class SingletonHelper{
        private static final TruyenFullComicSelector INSTANCE = new TruyenFullComicSelector();
    }

    public static TruyenFullComicSelector getInstance(){
        return SingletonHelper.INSTANCE;
    }

    @Override
    public String title() {
        return "h3[class=title][itemprop=name]";
    }

    @Override
    public String author() {
        return "a[itemprop=author]";
    }

    @Override
    public String category() {
        return ".info a[itemprop=genre]";
    }

    @Override
    public String description() {
        return ".desc-text";
    }

    @Override
    public String image() {
        return "img[itemprop=image]";
    }

    @Override
    public String doneFlag() {
        return ".info .text-success";
    }

    @Override
    public String rate() {
        return ".rate-holder";
    }

    @Override
    public String dataFrom() {
        return ".info .source";
    }

    @Override
    public String getChapterList() {
        return ".list-chapter li a";
    }

    @Override
    public TruyenFullChapterSelector getChapterSelector() {
        return TruyenFullChapterSelector.getInstance();
    }

    @Override
    public String getNextChapterPageSelector() {
        return ".pagination li[class=active] + li > a";
    }

    @Override
    public String getCurrChapterPageSelector() {
        return ".pagination li[class=active]";
    }
}
