//package com.Ecart.service;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.Ecart.model.AdminLoginRequest;
//import com.Ecart.repository.AdminRepository;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Service
//public class AdminService {
//
//	 @Autowired
//	    private AdminRepository adminRepository;
//
//	    private AdminLoginRequest loggedInAdmin; // To track the logged-in admin
//	  
//	    @PostConstruct
//	    public void init() {
//	        // Add hardcoded admin data to the database
//	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//	    String encodedPassword = encoder.encode("password123");
//	    adminRepository.save(new AdminLoginRequest("admin123@gmail.com", encodedPassword));
//	   //adminRepository.save(new AdminLoginRequest("admin123@gmail.com", "password123")); // Consider hashing the password
//	    }
//
//	    public String login(String email, String password) {
//	        // Check if an admin is already logged in
//	        if (loggedInAdmin != null) {
//	            return "An admin is already logged in!";
//	        }
//
////	        Optional<AdminLoginRequest> admin = Optional.ofNullable(adminRepository.findByEmail(email));
////	        if (admin.isPresent() && admin.get().getPassword().equals(password)) {
////	            loggedInAdmin = admin.get(); // Set the logged-in admin
////	            return "Login successful!";
////	        }
//	        Optional<AdminLoginRequest> admin = Optional.ofNullable(adminRepository.findByEmail(email));
//	        if (admin.isPresent()) {
//	            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//	            if (encoder.matches(password, admin.get().getPassword())) {
//	                loggedInAdmin = admin.get(); // Set the logged-in admin
//	                return "Login successful!";
//	            }
//	        }
//	        return "Invalid credentials!";
//	    }
//
//	    public String logout() {
//	        if (loggedInAdmin != null) {
//	            loggedInAdmin = null; // Clear the logged-in admin
//	            return "Logout successful!";
//	        }
//	        return "No admin is currently logged in!";
//	    }
//
//	    public List<AdminLoginRequest> getAll() {
//	        return adminRepository.findAll();
//	    }
//}


package com.Ecart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.Ecart.model.AdminLoginRequest;
import com.Ecart.repository.AdminRepository;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void init() {
        if (adminRepository.findByEmail("admin123@gmail.com") == null) {
            String encodedPassword = encoder.encode("password123");
            adminRepository.save(new AdminLoginRequest("admin123@gmail.com", encodedPassword));
        }
    }

    public String login(String email, String password, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            return "An admin is already logged in!";
        }

        Optional<AdminLoginRequest> adminOpt = Optional.ofNullable(adminRepository.findByEmail(email));
        if (adminOpt.isPresent() && encoder.matches(password, adminOpt.get().getPassword())) {
            session.setAttribute("admin", email);
            return "Login successful!";
        }

        return "Invalid credentials!";
    }

    public String logout(HttpSession session) {
        if (session.getAttribute("admin") != null) {
            session.invalidate();
            return "Logout successful!";
        }
        return "No admin is currently logged in!";
    }

    public List<AdminLoginRequest> getAll() {
        return adminRepository.findAll();
    }

    public boolean isAdminAuthenticated(HttpSession session) {
        return session.getAttribute("admin") != null;
    }
}
