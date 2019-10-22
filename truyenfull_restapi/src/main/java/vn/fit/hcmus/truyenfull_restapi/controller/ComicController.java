package vn.fit.hcmus.truyenfull_restapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.fit.hcmus.truyenfull_restapi.exceptions.ResourceNotFoundException;
import vn.fit.hcmus.truyenfull_restapi.model.Chapter;
import vn.fit.hcmus.truyenfull_restapi.model.Comic;
import vn.fit.hcmus.truyenfull_restapi.repository.ComicRepositiory;
import vn.fit.hcmus.truyenfull_restapi.utils.ReponseUtil;

import javax.validation.Valid;
import java.util.List;

import static vn.fit.hcmus.truyenfull_restapi.utils.ReponseUtil.*;


@RestController
@RequestMapping("/")
public class ComicController {
    @Autowired
    ComicRepositiory comicRepositiory;

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

}
