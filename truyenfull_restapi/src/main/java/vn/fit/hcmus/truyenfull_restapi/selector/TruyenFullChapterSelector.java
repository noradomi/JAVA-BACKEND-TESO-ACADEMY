package vn.fit.hcmus.truyenfull_restapi.selector;

public class TruyenFullChapterSelector implements  ChapterContentBaseSelector{
    private TruyenFullChapterSelector() {

    }

    private static class SingletonHelper {

        private static final TruyenFullChapterSelector INSTANCE = new TruyenFullChapterSelector();
    }

    public static TruyenFullChapterSelector getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public String name_index() {
        return ".chapter-title";
    }

    @Override
    public String content() {
        return "#chapter-c";
    }
}
