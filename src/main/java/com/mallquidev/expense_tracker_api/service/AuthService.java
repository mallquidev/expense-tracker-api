package com.mallquidev.expense_tracker_api.service;

import com.mallquidev.expense_tracker_api.dto.AuthResponse;
import com.mallquidev.expense_tracker_api.dto.LoginRequest;
import com.mallquidev.expense_tracker_api.dto.RegisterRequest;
import com.mallquidev.expense_tracker_api.entities.Role;
import com.mallquidev.expense_tracker_api.entities.User;
import com.mallquidev.expense_tracker_api.jwt.JwtUtil;
import com.mallquidev.expense_tracker_api.repositories.RoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    //10
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse authenticate(LoginRequest request) {
        //autenticar el usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );

        //Guardar la autentication en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //obtenemos usuarios desde la bd
        User user = userService.findByName(request.getUserName());

        //generamos el token
        String token = jwtUtil.generateToken(authentication);

        return new AuthResponse(token, user.getUserName(), user.getRole().getName());
    }

    public AuthResponse registerUser(RegisterRequest request) {
        //verificamos si el usuario ya exister
        if(userService.existsByUsername(request.getUserName())) {
            throw new RuntimeException("El usuario ya existe");
        }

        //Buscar el rol en la base de datos
        Role roleUser = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("El ROL USER no existe"));

        //creare usuario con contrasena encriptada
        User newUser = new User(
                request.getUserName(),
                null,
                passwordEncoder.encode(request.getPassword()),
                roleUser
        );

        //Registramos en la bd y con esto nada mas ya estaria solo si quieres autenticar o solo creas el
        // token y retornamos datos
        userService.save(newUser);

        //generar el token JWT para el nuevo usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //generar el token
        String token = jwtUtil.generateToken(authentication);

        //6 retornar respuesta con token y datos basico
        return  new AuthResponse(token, newUser.getUserName(), roleUser.getName());
    }
}
