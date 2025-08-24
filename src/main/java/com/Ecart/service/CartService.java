package com.Ecart.service;

import com.Ecart.model.CartItem;
import com.Ecart.repository.CartItemRepository;
//import com.Ecart.model.Mycart;
//import com.Ecart.repository.MycartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
	@Autowired
    private CartItemRepository cartItemRepository;

    public CartItem addProductToCart(Long productId, Long customerId, String customerEmail, int quantity, String name, String description, double price, String category) {
        CartItem cartItem = new CartItem(productId, customerId, customerEmail, quantity, name, description, price, category);
        return cartItemRepository.save(cartItem);
    }
 

    public List<CartItem> getCartItemsByCustomerEmail(String customerEmail) {
        return cartItemRepository.findByCustomerEmail(customerEmail);
    }

    
    
    public boolean cartItemExists(Long productId, String customerEmail) {
        return cartItemRepository.existsByProductIdAndCustomerEmail(productId, customerEmail);
    }

    
    
    public CartItem updateQuantityByProductAndEmail(Long productId, String customerEmail, int quantity) {
        List<CartItem> items = cartItemRepository.findByCustomerEmail(customerEmail);
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                if (quantity <= 0) {
                    cartItemRepository.deleteById(item.getId());
                    return null; // Item deleted
                }
                item.setQuantity(quantity);
                return cartItemRepository.save(item);
            }
        }
        return null; // Item not found
    }
    
    public boolean deleteCartItem(Long productId, String customerEmail) {
        List<CartItem> items = cartItemRepository.findByCustomerEmail(customerEmail);

        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                cartItemRepository.deleteById(item.getId());
                return true; // Item found and deleted
            }
        }

        return false; // Item not found
    }
    
    public boolean deleteAllCartItem(String customerEmail) {
        List<CartItem> items = cartItemRepository.findByCustomerEmail(customerEmail);

        if(customerEmail!=null) {
                cartItemRepository.deleteAll();
                return true;
        }
                
        return false; // Item not found
    }



}
