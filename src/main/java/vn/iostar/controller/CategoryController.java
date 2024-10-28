package vn.iostar.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import vn.iostar.Service.ICategoryService;
import vn.iostar.Service.IStorageService;
import vn.iostar.entity.Category;
import vn.iostar.model.Response;
@Controller
@RequestMapping(path = "/admin/categories")
public class CategoryController {
	@Autowired
	ICategoryService categoryService;
	@Autowired
	IStorageService storageService;
	@GetMapping(value = {"/home","/"})
	public String trangchu(ModelMap model) {
		model.addAttribute("categories", categoryService.findAll());
		return "index";
	}
	@GetMapping("/searchpaginated")
    public String search(ModelMap model, @RequestParam(name="name", required = false) String name,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {

        int count = (int) categoryService.count();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("categoryName"));
        Page<Category> resultPage;

        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByCategoryNameContaining(name, pageable);
            model.addAttribute("name", name);
        } else {
            resultPage = categoryService.findAll(pageable);
        }

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);

            if (totalPages > count) {
                if (end == totalPages) start = end - count;
                else if (start == 1) end = start + count;
            }

            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("categoryPage", resultPage);
        return "admin/category/list";
    }


    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return new ResponseEntity<Response>(new Response(true, "Thành công", categoryService.findAll()), HttpStatus.OK);
    }

    @PostMapping(path = "/getCategory")
    public ResponseEntity<?> getCategory(@Validated @RequestParam("id") Long id) {
        Optional<Category> category = categoryService.findById(id);

        if (category.isPresent()) {
            return new ResponseEntity<Response>(new Response(true, "Thành công", category.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(new Response(false, "Thất bại", null), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/addCategory")
    public ResponseEntity<?> addCategory(@Validated @RequestParam("categoryName") String categoryName,
                                         @Validated @RequestParam("icon") MultipartFile icon) {
        Optional<Category> optCategory = categoryService.findByCategoryName(categoryName);

        if (optCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category đã tồn tại trong hệ thống");
        } else {
            Category category = new Category();
            if (!icon.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String uuString = uuid.toString();
                category.setIcon(storageService.getSorageFilename(icon, uuString));
                storageService.store(icon, category.getIcon());
            }

            category.setCategoryName(categoryName);
            categoryService.save(category);
            return new ResponseEntity<Response>(new Response(true, "Thêm Thành công", category), HttpStatus.OK);
        }
    }

    @PutMapping(path = "/updateCategory")
    public String updateCategory(@Validated @RequestParam("categoryId") Long categoryId,
                                 @Validated @RequestParam("categoryName") String categoryName,
                                 @Validated @RequestParam("icon") MultipartFile icon,
                                 ModelMap model) {
        Optional<Category> optCategory = categoryService.findById(categoryId);

        if (optCategory.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy Category");
            return "error"; // Return an error page if category not found
        }

        Category category = optCategory.get();
        if (!icon.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            category.setIcon(storageService.getSorageFilename(icon, uuString));
            storageService.store(icon, category.getIcon());
        }

        category.setCategoryName(categoryName);
        categoryService.save(category);

        model.addAttribute("categories", category);
        return "category_edit"; // Return the category_edit.html template
    }

    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<?> deleteCategory(@Validated @RequestParam("categoryId") Long categoryId) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Category", null), HttpStatus.BAD_REQUEST);
        }

        categoryService.delete(optCategory.get());
        return new ResponseEntity<>(new Response(true, "Xóa Thành công", optCategory.get()), HttpStatus.OK);
    }
}
