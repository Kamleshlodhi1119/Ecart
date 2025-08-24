//package com.Ecart.dto;
//
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
//@Data
//public class FeedbackDto {
//	 private Long productId;          // ✅ Instead of orderId
//	    private Long customerId;         // ✅ To track who gave feedback
//	    private String description;
//	    private int rating;
//	    private LocalDateTime feedbackDate;
//		public Long getProductId() {
//			return productId;
//		}
//		public void setProductId(Long productId) {
//			this.productId = productId;
//		}
//		public Long getCustomerId() {
//			return customerId;
//		}
//		public void setCustomerId(Long customerId) {
//			this.customerId = customerId;
//		}
//		public String getDescription() {
//			return description;
//		}
//		public void setDescription(String description) {
//			this.description = description;
//		}
//		public int getRating() {
//			return rating;
//		}
//		public void setRating(int rating) {
//			this.rating = rating;
//		}
//		public LocalDateTime getFeedbackDate() {
//			return feedbackDate;
//		}
//		public void setFeedbackDate(LocalDateTime feedbackDate) {
//			this.feedbackDate = feedbackDate;
//		}
//		public FeedbackDto(Long productId, Long customerId, String description, int rating,
//				LocalDateTime feedbackDate) {
//			super();
//			this.productId = productId;
//			this.customerId = customerId;
//			this.description = description;
//			this.rating = rating;
//			this.feedbackDate = feedbackDate;
//		}
//	    
//	    
//	    
//	    
//}



package com.Ecart.dto;

import java.time.LocalDateTime;

public class FeedbackDto {
    private Long productId;
    private Long customerId;
    private String description;
    private int rating;
    private LocalDateTime feedbackDate;

    public FeedbackDto() {
        // Required for ModelMapper
    }

    // Getters and setters (or use @Data if you're not customizing anything)
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDateTime feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
}
