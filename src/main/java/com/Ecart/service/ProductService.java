package com.Ecart.service;

import com.Ecart.dto.ProductDto;
import com.Ecart.model.Product;
import com.Ecart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    
    

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
		super();
		this.productRepository = productRepository;
		this.modelMapper = modelMapper;
	}

	public List<ProductDto> getAllActiveProducts() {
        return productRepository.findByActive(true)
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    public List<Product> getallProduct() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        return modelMapper.map(product, ProductDto.class);
    }

    public List<ProductDto> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndActive(category, true)
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    public List<ProductDto> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCaseAndActive(query, true)
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    public Optional<Product> getRawProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public boolean deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            // ✅ Delete image file if it exists
            String folder = "D:/Users/279004/MyDB/products/";
            String imageUrl = product.get().getImageUrl(); // e.g., /products/5.jpg
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String filename = imageUrl.replace("/products/", "");
                Path imagePath = Paths.get(folder + filename);
                try {
                    Files.deleteIfExists(imagePath);
                } catch (Exception e) {
                    System.err.println("Failed to delete image file: " + e.getMessage());
                }
            }

            // ✅ Delete product from DB
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public boolean toggleProductStatus(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setActive(!product.isActive());
            productRepository.save(product);
            return true;
        }
        return false;
    }
}
