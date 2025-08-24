package com.Ecart.service;


import com.Ecart.dto.FeedbackDto;
import com.Ecart.model.Customer;
import com.Ecart.model.Feedback;
import com.Ecart.model.Order;
import com.Ecart.model.Product;
import com.Ecart.repository.CustomerRepository;
import com.Ecart.repository.FeedbackRepository;
import com.Ecart.repository.ProductRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final  ModelMapper modelMapper;

   

    public FeedbackService(FeedbackRepository feedbackRepository, ProductRepository productRepository,
			CustomerRepository customerRepository, ModelMapper modelMapper) {
		super();
		this.feedbackRepository = feedbackRepository;
		this.productRepository = productRepository;
		this.customerRepository = customerRepository;
		this.modelMapper=modelMapper;
	}

	public FeedbackDto saveFeedback(FeedbackDto dto) {
        Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

        Customer customer = customerRepository.findById(dto.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        Optional<Feedback> existingFeedback = feedbackRepository.findByCustomerAndProduct(customer, product);
        if (existingFeedback.isPresent()) {
            throw new RuntimeException("Feedback already exists for this product and customer.");
        }
        Feedback feedback = new Feedback();
        feedback.setProduct(product);
        feedback.setCustomer(customer);
        feedback.setDescription(dto.getDescription());
        feedback.setRating(dto.getRating());
        feedback.setFeedbackDate(LocalDateTime.now());

        Feedback saved = feedbackRepository.save(feedback);
        return modelMapper.map(saved, FeedbackDto.class);
    }

//    public Feedback addFeedback(Order order, String description, int rating) {
//        Feedback feedback = new Feedback();
//        feedback.setOrder(order);
//        feedback.setDescription(description);
//        feedback.setRating(rating);
//        feedback.setFeedbackDate(LocalDateTime.now());
//        return feedbackRepository.save(feedback);
//    }

//    public List<Feedback> getFeedbackForOrder(Order order) {
//        return feedbackRepository.findByOrder(order);
//    }
    
    public List<Feedback> getAllFeedbacgetkForOrder() {
        return feedbackRepository.findAll();
    }
   
    public void deleteFeedbackByProductIdAndCustomerId(Long customerId, Long productId) {
    	
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Feedback feedback = feedbackRepository.findByCustomerAndProduct(customer, product)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        feedbackRepository.delete(feedback);
    }

  
    
    public List<Map<String, Object>> getAverageRatingsPerProduct() {
        return feedbackRepository.findAverageRatingPerProduct();
    }

}