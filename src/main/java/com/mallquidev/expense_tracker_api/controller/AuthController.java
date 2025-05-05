package com.mallquidev.expense_tracker_api.controller;

import com.mallquidev.expense_tracker_api.dto.AuthResponse;
import com.mallquidev.expense_tracker_api.dto.LoginRequest;
import com.mallquidev.expense_tracker_api.dto.RegisterRequest;
import com.mallquidev.expense_tracker_api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController//indica que esta clase es un controllador REST(responde JSON por defecto)
@RequestMapping("/auth")//todas las rutas de este controlador comenzaran con /auth
public class AuthController {
    //11
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")//mapeamos el endpoint POST /auth/login
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        //llama al servicio para autenticar el usuario con los datos del request
        AuthResponse response = authService.authenticate(request);
        //Devuelve el token y datos del usuario en una respuesta HTTP 200
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")//mapeamos el endpoint POST /auth/register
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        //Llama al servicio para registrar al usuario con los datos del request
        AuthResponse response = authService.registerUser(request);
        //devuelve el token y datos del nuevo usuario en una respuesta HTTP 200
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-auth")
    public ResponseEntity<String> checkAuth(){
        return ResponseEntity.ok().body("Autenticado");
    }
}
