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
import vn.fit.hcmus.truyenfull_restapi.model.Comic;
import vn.fit.hcmus.truyenfull_restapi.repository.CategoryRepository;
import vn.fit.hcmus.truyenfull_restapi.utils.ReponseUtil;

import javax.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static vn.fit.hcmus.truyenfull_restapi.utils.ReponseUtil.returnCategory;

@RestController
@RequestMapping("/")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

//    Xu li cho https://truyenfull.vn/the-loai/kiem-hiep/
    @GetMapping(value = "/the-loai/{url_name}",produces = "application/json")
    public String getAllComicByNameCategory(@PathVariable(value = "url_name") String urlname){
        Category category = categoryRepository.findByUrlname(urlname);
        String response = returnCategory(category).toString();
        return response;
    }

//   Thêm 1 thể loại
    @PostMapping("/category")
    public Category addCategory(@Valid @RequestBody Category category){
        return categoryRepository.save(category);
    }

//    Sửa thông tin 1 thể loại
    @PutMapping("/category/{id}")
    public String updateComic(@PathVariable(value = "id") Long categoryId,
                              @Valid @RequestBody Category categoryDetails){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "id", categoryId));
//        Giả sử ở đây chỉ đổi tên tác giả và nguồn - minh họa
        try {
            if(StringUtils.isEmpty(categoryDetails.getName()) || StringUtils.isEmpty(categoryDetails.getUrlname()))
                return ReponseUtil.inValid();
            category.setName(categoryDetails.getName());
            category.setUrlname(categoryDetails.getUrlname());
            Category updatedCategory = categoryRepository.save(categoryDetails);
            return ReponseUtil.success(ReponseUtil.returnCategory(updatedCategory));
        } catch (Exception e) {
            return ReponseUtil.serverError();
        }
    }

    //    Xu li cho Delete Request - xoa 1 the loai
    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteComic(@PathVariable(value = "id") Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "id", categoryId));
        categoryRepository.delete(category);

        return ResponseEntity.ok().build();
    }

//    Lấy dữ liệu vơi JSOUP

    public  boolean crawlerTest() throws IOException {
        String url = "https://truyenfull.vn";
        String[] temp;
        String urlname;
        Document document = Jsoup.connect(url).get();
        System.out.println("Title"+document.title());
        Elements categories = document.select("div.row > div.col-md-4 > ul > li > a");
        for (Element category : categories) {
//            System.out.println("Category name: "+category.text());
//            System.out.println("Link :"+category.attr("href"));
//            System.out.println("");
            Category newCategory = new Category();
            newCategory.setName(category.text());
            temp = category.attr("href").split("/");
            newCategory.setUrlname(temp[temp.length - 1]);
//            System.out.println("Category name: "+newCategory.getName());
//            System.out.println("Category urlname: "+newCategory.getUrlname());
            categoryRepository.save(newCategory);
        }
        return  true;
    }

//    Lây tất cả truyện của Thể loại Tiên hiệp
    @GetMapping("/getComic")
    public  boolean getComics() throws IOException {
        String url = "https://truyenfull.vn/the-loai/tien-hiep/";
        Document document = Jsoup.connect(url).get();
        Elements comics = document.select("div.row > div.col-xs-7 > div > h3 > a");
        for (Element comic : comics) {
            System.out.println("Comic name: "+comic.text());
            System.out.println("Link :"+comic.attr("href"));
        }
        return  true;
    }



}
