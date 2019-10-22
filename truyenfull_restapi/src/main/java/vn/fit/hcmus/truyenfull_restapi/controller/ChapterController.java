package vn.fit.hcmus.truyenfull_restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.fit.hcmus.truyenfull_restapi.model.Chapter;
import vn.fit.hcmus.truyenfull_restapi.repository.ChapterRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class ChapterController {
    @Autowired
    ChapterRepository chapterRepository;

//    Xu li cho them 1 chuong vao 1 truyen da ton tai
    @PostMapping("/chapter")
    public Chapter addChapterToComic(
            @Valid @RequestBody Chapter chapter) {
        return chapterRepository.save(chapter);
    }
}
