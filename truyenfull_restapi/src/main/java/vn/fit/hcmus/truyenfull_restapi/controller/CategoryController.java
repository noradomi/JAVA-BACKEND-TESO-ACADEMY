package vn.fit.hcmus.truyenfull_restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.fit.hcmus.truyenfull_restapi.exceptions.ResourceNotFoundException;
import vn.fit.hcmus.truyenfull_restapi.model.Category;
import vn.fit.hcmus.truyenfull_restapi.repository.CategoryRepository;
import vn.fit.hcmus.truyenfull_restapi.utils.ReponseUtil;

import javax.validation.Valid;

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
}
