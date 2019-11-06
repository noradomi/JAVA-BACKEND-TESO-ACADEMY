package vn.fit.hcmus.truyenfull_restapi.selector;

public class TruyenFullCatalogSelector implements CatalogContentBaseSelector{
    private TruyenFullCatalogSelector(){};

    private static class SingletonHelper{
        private static final TruyenFullCatalogSelector INSTANCE = new TruyenFullCatalogSelector();
    }

    public static TruyenFullCatalogSelector getInstance(){
        return SingletonHelper.INSTANCE;
    }

    @Override
    public String homePage() {
        return "https://truyenfull.vn/";
    }

    @Override
    public String listCatalog() {
        return "#nav > div.container > div.navbar-collapse.collapse > ul > li:nth-child(1) > ul > li > a";
    }

    @Override
    public String getNextComicPageSelector() {
        return ".pagination li[class=active] + li > a";
    }

    @Override
    public String getCurrComicPageSelector() {
        return ".pagination li[class=active]";
    }
}
