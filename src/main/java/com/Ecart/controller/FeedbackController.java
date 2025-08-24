package com.Ecart.controller;

import com.Ecart.dto.FeedbackDto;
import com.Ecart.model.Customer;
import com.Ecart.model.Feedback;
import com.Ecart.model.Order;
import com.Ecart.model.Product;
import com.Ecart.service.FeedbackService;
import com.Ecart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final OrderService orderService;

    public FeedbackController(FeedbackService feedbackService, OrderService orderService) {
        this.feedbackService = feedbackService;
        this.orderService = orderService;
    }
    

//    @PostMapping("/submit")
//    public ResponseEntity<Feedback> addFeedback(
//            @RequestParam Long orderId,
//            @RequestParam String description,
//            @RequestParam int rating) {
//        Order order = orderService.findById(orderId).orElse(null); // Changed to findById
//        if (order == null || !"CONFIRMED".equals(order.getStatus())) {
//            return ResponseEntity.badRequest().build();
//        }
//        Feedback feedback = feedbackService.addFeedback(order, description, rating);
//        return ResponseEntity.ok(feedback);
//    }
    
//    @PostMapping("/submit")
//    public ResponseEntity<Feedback> addFeedback(@RequestBody FeedbackDto feedbackDto) {
//        Long orderId = feedbackDto.getOrderId();
//        String description = feedbackDto.getDescription();
//        int rating = feedbackDto.getRating();
//
//        Order order = orderService.findById(orderId).orElse(null);
//        if (order == null || !"CONFIRMED".equals(order.getStatus())) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Feedback feedback = feedbackService.addFeedback(order, description, rating);
//        return ResponseEntity.ok(feedback);
//    }

    @PostMapping("/add")
    public ResponseEntity<FeedbackDto> giveFeedback(@RequestBody FeedbackDto feedbackDto) {
        FeedbackDto saved = feedbackService.saveFeedback(feedbackDto);
        return ResponseEntity.ok(saved);
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFeedbackByProductIdAndCustomerId(
            @RequestParam Long customerId,
            @RequestParam Long productId) {
        feedbackService.deleteFeedbackByProductIdAndCustomerId(customerId, productId);
        return ResponseEntity.noContent().build();
    }



//    @GetMapping("/order/{orderId}")
//    public List<Feedback> getOrderFeedback(@PathVariable Long orderId) {
//        Order order = orderService.findById(orderId).orElse(null); // Changed to findById
//        return feedbackService.getFeedbackForOrder(order);
//    }
    @GetMapping("/all")
    public List<Feedback> getAllFeedbacgetkForOrder() {
//        Order order = orderService.findAll(); // Changed to findById
        return feedbackService.getAllFeedbacgetkForOrder();
    }
}