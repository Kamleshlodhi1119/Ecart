package com.Ecart.controller;

import com.Ecart.dto.ProductDto;
import com.Ecart.model.Product;
import com.Ecart.service.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    

    public ProductController(ProductService productService) {
		super();
		this.productService = productService;
	}

	@GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllActiveProducts());
    }

    @PostMapping("/additems")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @GetMapping("/allitems")
    public ResponseEntity<List<Product>> getallProduct() {
        return ResponseEntity.ok(productService.getallProduct());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }

    // ✅ Upload product image (folder-based)
//    @PostMapping("/{id}/upload-image")
//    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
//        try {
//            Product product = productService.getRawProductById(id)
//                    .orElseThrow(() -> new RuntimeException("Product not found"));
//
//            // Get original file extension (.jpg, .png, etc.)
//            String originalName = file.getOriginalFilename();
//            if (originalName == null) throw new RuntimeException("Invalid file name");
//            String extension = originalName.substring(originalName.lastIndexOf('.'));
//
//            // Build new filename using product ID
//            String filename = id + extension;
//
//            // Save to local folder
//            String folder = "D:/Users/279004/MyDB/products/";
//            Files.createDirectories(Paths.get(folder));
//            Path filepath = Paths.get(folder + filename);
//            Files.write(filepath, file.getBytes());
//
//            // Update image URL in DB
//            product.setImageUrl("/products/" + filename);
//            productService.save(product);
//
//            return ResponseEntity.ok("Image uploaded as " + filename);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Image upload failed: " + e.getMessage());
//        }
//    }
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            Product product = productService.getRawProductById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Get extension
            String originalName = file.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                return ResponseEntity.badRequest().body("Invalid image file.");
            }

            String extension = originalName.substring(originalName.lastIndexOf('.'));
            String filename = id + extension;

            // Save image
            String folder = "D:/Users/279004/MyDB/products/";
            Files.createDirectories(Paths.get(folder));
            Path filepath = Paths.get(folder + filename);
            Files.write(filepath, file.getBytes());

            // Save image path in product
            product.setImageUrl("/products/" + filename);
            productService.save(product);

            return ResponseEntity.ok("Image uploaded as " + filename);
        } catch (Exception e) {
            e.printStackTrace();  // Log error in console
            return ResponseEntity.status(500).body("Image upload failed: " + e.getMessage());
        }
    }

    
//    /delete the product
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.ok("Product deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Product not found or could not be deleted.");
        }
    }

    // ✅ Toggle product active status (admin use)
    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<?> toggleProductStatus(@PathVariable Long id) {
        boolean updated = productService.toggleProductStatus(id);
        return updated
                ? ResponseEntity.ok("Product status updated successfully.")
                : ResponseEntity.badRequest().body("Product not found.");
    }
}
