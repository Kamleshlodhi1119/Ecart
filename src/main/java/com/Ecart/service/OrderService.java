package com.Ecart.service;

import com.Ecart.dto.OrderDto;
import com.Ecart.dto.OrderItemDto;
import com.Ecart.model.*;
import com.Ecart.repository.CartItemRepository;
import com.Ecart.repository.CustomerRepository;
import com.Ecart.repository.OrderRepository;
import com.Ecart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CartItemRepository cartItemRepository; // ✅ Add this
    private final ModelMapper modelMapper;
    
    
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
            CustomerRepository customerRepository, CartItemRepository cartItemRepository,
            ModelMapper modelMapper) {
this.orderRepository = orderRepository;
this.productRepository = productRepository;
this.customerRepository = customerRepository;
this.cartItemRepository = cartItemRepository; // ✅ include here
this.modelMapper = modelMapper;
}

//	public OrderDto createOrder(OrderDto orderDto) {
//        Customer customer = customerRepository.findById(orderDto.getCustomerId())
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//        
//        List<OrderItem> items = orderDto.getItems().stream()
//                .map(itemDto -> {
//                    Product product = productRepository.findById(itemDto.getProductId())
//                            .orElseThrow(() -> new RuntimeException("Product not found"));
//                    OrderItem item = new OrderItem();
//                    item.setProduct(product);
//                    item.setQuantity(itemDto.getQuantity());
//                    item.setPrice(product.getPrice());
//                    return item;
//                }).collect(Collectors.toList());
//        
//        double totalAmount = items.stream()
//                .mapToDouble(item -> item.getPrice() * item.getQuantity())
//                .sum();
//        
//        Order order = new Order();
//        order.setCustomer(customer);
//        order.setItems(items);
//        order.setTotalAmount(totalAmount);
//        order.setPaymentMethod(orderDto.getPaymentMethod());
//        order.setStatus("CONFIRMED");
//        order.setOrderDate(LocalDateTime.now());
//        
//        // Update product quantities
//        items.forEach(item -> {
//            Product product = item.getProduct();
//            product.setQuantity(product.getQuantity() - item.getQuantity());
//            productRepository.save(product);
//        });
//        
//        
//        Order savedOrder = orderRepository.save(order);
//        return modelMapper.map(savedOrder, OrderDto.class);
//    }
	
	
    public OrderDto createOrder(OrderDto orderDto) {
        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<OrderItem> items = orderDto.getItems().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    OrderItem item = new OrderItem();
                    item.setProduct(product);
                    item.setQuantity(itemDto.getQuantity());
                    item.setPrice(product.getPrice());
                    return item;
                }).collect(Collectors.toList());

        double totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        Order order = new Order();
        order.setCustomer(customer);
        order.setItems(items);
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setStatus("CONFIRMED");
        order.setOrderDate(LocalDateTime.now());

        // Update product stock
        items.forEach(item -> {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        });

        Order savedOrder = orderRepository.save(order);

        // ✅ Automatically clear cart after order placed
        cartItemRepository.deleteAll(
            cartItemRepository.findByCustomerEmail(customer.getEmail())
        );

        return modelMapper.map(savedOrder, OrderDto.class);
    }

	
	
    public List<Order> getalluser(){
    	return orderRepository.findAll();
    }
    public List<OrderDto> getCustomerOrders(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        return orderRepository.findByCustomer(customer)
                .stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }
    
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .filter(o -> "CONFIRMED".equals(o.getStatus()))
                .orElseThrow(() -> new RuntimeException("Order not found or cannot be cancelled"));
        
        order.setStatus("CANCELLED");
        orderRepository.save(order);
        
        // Restore product quantities
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        });
    }

    
    
    
//    public  List
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }
    
    
    
    //this is new code
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    
      }