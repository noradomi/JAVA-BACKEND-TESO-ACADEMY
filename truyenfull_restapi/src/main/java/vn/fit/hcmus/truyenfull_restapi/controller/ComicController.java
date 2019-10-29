package vn.fit.hcmus.truyenfull_restapi.controller;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.fit.hcmus.truyenfull_restapi.exceptions.ResourceNotFoundException;
import vn.fit.hcmus.truyenfull_restapi.model.Category;
import vn.fit.hcmus.truyenfull_restapi.model.Chapter;
import vn.fit.hcmus.truyenfull_restapi.model.Comic;
import vn.fit.hcmus.truyenfull_restapi.repository.CategoryRepository;
import vn.fit.hcmus.truyenfull_restapi.repository.ChapterRepository;
import vn.fit.hcmus.truyenfull_restapi.repository.ComicRepositiory;
import vn.fit.hcmus.truyenfull_restapi.utils.ReponseUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static vn.fit.hcmus.truyenfull_restapi.utils.ReponseUtil.*;


@RestController
@RequestMapping("/")
public class ComicController {
    @Autowired
    ComicRepositiory comicRepositiory;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ChapterRepository chapterRepository;

    //    Xu li cho https://truyenfull.vn/kiem-ton/
    @GetMapping(value = "/{url_name}", produces = "application/json")
    public String getComicByName(@PathVariable(value = "url_name") String url_name) {
//        Name cua Comic la khac nhau
        Comic comic = comicRepositiory.findByUrlname(url_name);
        String response = returnComic(comic).toString();
        return response;
    }

    //     Xu li cho https://truyenfull.vn/kiem-ton/chuong-1/
    @GetMapping(value = "/{url_name}/chapter-{index}", produces = "application/json")
    public String getChapterById(@PathVariable(value = "url_name") String url_name,
                                 @PathVariable(value = "index") Long index) {
        Comic comic = comicRepositiory.findByUrlname(url_name);
//        Chapter chapter = chapterRepository.findByIdAndComic(idChapter,comic);
        List<Chapter> chapters = comic.getChapterList();
        for (Chapter chapter : chapters) {
            if (chapter.getIndex().equals(index))
                return returnChapter(chapter).toString();
        }
        return "Not Found";
    }

    //   Xử lí cho Post Request - thêm 1 truyện mới
    @PostMapping(value = "/comic")
    public String addComic(@Valid @RequestBody Comic comic) {
        try {
            if (StringUtils.isEmpty(comic.getName()) || StringUtils.isEmpty(comic.getUrlname()))
                return ReponseUtil.inValid();
            Comic comic1 = comicRepositiory.save(comic);
            return ReponseUtil.success(ReponseUtil.returnComic(comic1));
        } catch (Exception e) {
            return ReponseUtil.serverError();
        }
    }

    //    Xử lí cho Put Request - sửa thông tin 1 truyện
    @PutMapping("/comic/{id_comic}")
    public String updateComic(@PathVariable(value = "id_comic") Long comicId,
                              @Valid @RequestBody Comic comicDetails) {
        Comic comic = comicRepositiory.findById(comicId)
                .orElseThrow(() -> new ResourceNotFoundException("Comic", "id", comicId));
//        Giả sử ở đây chỉ đổi tên tác giả và nguồn - minh họa
        try {
            if (StringUtils.isEmpty(comicDetails.getAuthor()) || StringUtils.isEmpty(comicDetails.getSource()))
                return ReponseUtil.inValid();
            comic.setAuthor(comicDetails.getAuthor());
            comic.setSource(comicDetails.getSource());
            Comic updatedComic = comicRepositiory.save(comic);
            return ReponseUtil.success(ReponseUtil.returnComic(updatedComic));
        } catch (Exception e) {
            return ReponseUtil.serverError();
        }
    }

    //    Xu li cho Delete Request - xoa 1 truyen
    @DeleteMapping("/comic/{id_comic}")
    public ResponseEntity<?> deleteComic(@PathVariable(value = "id_comic") Long comicId) {
        Comic comic = comicRepositiory.findById(comicId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", comicId));
        comicRepositiory.delete(comic);

        return ResponseEntity.ok().build();
    }

    //    Lấy tat ca truyen theo tung the loai
    @GetMapping("/getComicsBasedCategory")
    public boolean getComicBasedCategory() throws IOException {
        List<Category> list = categoryRepository.findAll();
//        for (Category category : list) {
//            String url = "https://truyenfull.vn/the-loai/"+category.getUrlname().toString()+"/";
//           System.out.println("The loai :"+category.getName());
//
//            Document document = Jsoup.connect(url).get();
//
//            Elements comicList  = document.select("div.row > div.col-xs-7 > div > h3 > a");
////            Thu lay tat ca cac truyen cua the loai Tien hiep.
//            getAComicAndAllChapter(comicList.get(0).attr("href"));
////            Voi moi truyen trong tung the loai
////            for (Element comic : comicList) {
////                getAComicAndAllChapter(comic.attr("href"));
////            }
//            System.out.println("Finished a comic!!!");
//        }
        String url = "https://truyenfull.vn/the-loai/" + list.get(0).getUrlname().toString() + "/";
        System.out.println("The loai :" + list.get(0).getName());

        Document document = Jsoup.connect(url).get();

        Elements comicList = document.select("div.row > div.col-xs-7 > div > h3 > a");
//            Thu lay tat ca cac truyen cua the loai Tien hiep.
        for (Element comic : comicList) {
            getAComicAndAllChapter(comic.attr("href"));
//            System.out.println(comic.attr("href"));
            System.out.println("Finished a comic!");
        }
        return true;
    }

    @GetMapping("/getAComic")
    public boolean getAComicAndAllChapter(String url) throws IOException {
        String[] temp;
        String url1;
        Document document = Jsoup.connect(url).get();

//        Lay tong so trang phan trang cho cac chuong cua truyen
        int totalChapterPages = Integer.parseInt(document.selectFirst("#total-page").attr("value"));


        Comic crawedComic = new Comic();

        Element urlStr = document.selectFirst("ul.list-chapter > li > a"); // de lay urlname
        temp = urlStr.attr("href").split("/");

//        Gan thong tin cua truyen : ten,tac gia, nguon, trang thai
        crawedComic.setName(document.selectFirst("h3.title").text());
        crawedComic.setUrlname(temp[temp.length - 2]);
        crawedComic.setAuthor(document.selectFirst("div.info > div:nth-child(1) > a").text());
//        The loai giai quyet sau
        crawedComic.setSource(document.select("div.info > div > span.source").text());
        crawedComic.setStatus(document.select("div.info > div > span.text-primary").text());

        comicRepositiory.save(crawedComic);
        Comic comic = comicRepositiory.findByUrlname(temp[temp.length - 2]);

//        Lay list chapter cua truyen
        int j = 1;
        for (int i = 0; i < totalChapterPages; i++) {

            Elements chapterList = Jsoup.connect(url+"/trang-" + (i + 1) + "/").get()
                    .select("ul.list-chapter > li > a");


            for (Element element : chapterList) {
//            Connect den chapter cua comic
                url1 = element.attr("href");
                Document document1 = Jsoup.connect(url1).get();
                Chapter chapter = new Chapter();
                chapter.setIndex(new Long(j));
                Element chapter_title = document1.selectFirst("#chapter-big-container > div > div > h2 > a");
                String temp2 = chapter_title.attr("title");
                int index = temp2.indexOf(":");
                chapter.setName(temp2.substring(index + 1, temp2.length()));
                Element chapter_content = document1.selectFirst("#chapter-c");
                chapter.setContent(chapter_content.text());
                comic.addChapter(chapter);
                chapterRepository.save(chapter);
                j++;
            }
        }


        return true;
    }


}
