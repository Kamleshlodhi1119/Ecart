//package com.Ecart.controller;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.Ecart.model.AdminLoginRequest;
//import com.Ecart.service.AdminService;
//
//import jakarta.servlet.http.HttpSession;
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
//@RestController
//@RequestMapping("/admin")
//
//public class AdminController {
//
//	@Autowired
//    private AdminService adminService;
//
//    @PostMapping("/admin-login")
//    public String login(@RequestBody AdminLoginRequest adminLoginRequest) {
//        return adminService.login(adminLoginRequest.getEmail(), adminLoginRequest.getPassword());
//    }
//
//    @GetMapping("/check-admin")
//    public ResponseEntity<Map<String, Boolean>> checkAdminSession(HttpSession session) {
//        boolean isLoggedIn = session.getAttribute("admin") != null;
//        return ResponseEntity.ok(Map.of("authenticated", isLoggedIn));
//    }
//
////    @PostMapping("/admin-login")
////    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody AdminLoginRequest request, HttpSession session) {
////        if (session.getAttribute("admin") != null) {
////            return ResponseEntity.ok()
////                   .body(Map.of("status", "error", "message", "An admin is already logged in!"));
////        }
////        // ... your login logic
////        return ResponseEntity.ok()
////               .body(Map.of("status", "success", "message", "Login successful!"));
////    }
//    @PostMapping("/admin-logout")
//    public String logout() {
//        return adminService.logout();
//    }
//
//    @GetMapping("/getall")
//    public List<AdminLoginRequest> getAll() {
//        return adminService.getAll();
//    }
//}
package com.Ecart.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Ecart.model.AdminLoginRequest;
import com.Ecart.service.AdminService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/admin-login")
    public ResponseEntity<String> login(@RequestBody AdminLoginRequest adminLoginRequest, HttpSession session) {
        String result = adminService.login(adminLoginRequest.getEmail(), adminLoginRequest.getPassword(), session);
        if (result.contains("successful")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(401).body(result);
        }
    }

    @PostMapping("/admin-logout")
    public ResponseEntity<String> logout(HttpSession session) {
        String result = adminService.logout(session);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/check-admin")
    public ResponseEntity<Map<String, Boolean>> checkAdminSession(HttpSession session) {
        boolean isAuthenticated = adminService.isAdminAuthenticated(session);
        return ResponseEntity.ok(Map.of("authenticated", isAuthenticated));
    }

    @GetMapping("/getall")
    public List<AdminLoginRequest> getAll() {
        return adminService.getAll();
    }
}
