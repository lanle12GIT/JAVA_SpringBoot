package com.project.Shopapp.Controllers;

import com.project.Shopapp.dtos.ProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.*;

import org.springframework.web.server.ResponseStatusException;


import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductDTO productDTO,
            //@RequestPart("file") MultipartFile file,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<MultipartFile>() : files;
            for (MultipartFile file : files) {

                //save the product
                //Kiểm tra kích thước file vaf định dạng
                if (file.getSize()==0){
                    continue;
                }
                if (file.getSize() > 10 * 1024 * 1024) { // kích thước lớn hơn 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).
                            body("File is not an image");
                }
                //Lưu file và cập nhật thumbnail trong DTO
                String fileName = StoreFile(file);  //Thay thé hàn này với code của bạn để lưu file
                //Lưu vào đối tuợng product trong DB=> làm sau lưu vào bảng product_images
            }

            return ResponseEntity.ok("Product created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private String StoreFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Thêm UUID vào trước tên file đểDỉ đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + fileName;
        //Đường dẫn đến thư mực mà bạn muốn lưu file
        java.nio.file.Path uploaDir = Paths.get("uploads");
        if (!Files.exists(uploaDir)) {
            Files.createDirectories(uploaDir);
        }
        // Đường dẫn đến file
        java.nio.file.Path destination = Paths.get(uploaDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @GetMapping("")
    public ResponseEntity<String> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok("getProducts here");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductByID(@PathVariable("id") String productId) {
        return ResponseEntity.ok(" Product with ID:" + productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProducts(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body("This is deleteProduct with id =" + id);
    }
}
/*   {
"name":"ipad pro 2023",
"price": 812.34,
"thumbnail": "",
"description" : " This is a test product",
"category_id" : 1
}
    
 */