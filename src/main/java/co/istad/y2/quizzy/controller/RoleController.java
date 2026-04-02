package co.istad.y2.quizzy.controller;

import co.istad.y2.quizzy.model.Role;
import co.istad.y2.quizzy.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final AuthService authService;
    public RoleController(AuthService authService ){
        this.authService = authService;
    }
    @GetMapping
    public List<Role> getAllRoles(){
        return authService.getAllRole();
    }
}
