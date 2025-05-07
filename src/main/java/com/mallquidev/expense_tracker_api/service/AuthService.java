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
    //13
    private final UserService userService;//para buscar, registrar y verificar usuarios
    private final RoleRepository roleRepository;//para buscar roles desde la base de datos
    private final PasswordEncoder passwordEncoder;//para encripar la nueva contrasena del nuevo usuario
    private final JwtUtil jwtUtil;//para generar el token
    private final AuthenticationManager authenticationManager;//para autenticar usuarios (login y despues del registro)

    public AuthService(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    //login
    public AuthResponse authenticate(LoginRequest request) {
        //Autenticamos al usuario con spring security usando su username y password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );

        //guardamos esa autenticacion en el contexto de seguridad de spring (para futuras validaciones)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //buscamos al usuario desde la bd (para extraer datos como nombre y rol)
        User user = userService.findByUserName(request.getUserName());
        //generamos un token JWT valido para el usuario autenticado
        String token = jwtUtil.generateToken(authentication);
        //retornamos una respuesta con el token y algunos datos del usuario
        return new AuthResponse(token, user.getUserName(), user.getRole().getName());
    }

    //register
    public AuthResponse registerUser(RegisterRequest request) {
        //verifica si el nombre de usuario ya existe
        if (userService.existsByUserName(request.getUserName())){
            throw new RuntimeException("Usuario ya existe");//Se usa if + throw porque existsByUserName() devuelve un boolean
        }
        //busca el rol USER desde la bd
        Role roleUser = roleRepository.findByName("USER")
                .orElseThrow(()-> new RuntimeException("Rol USER no encontrado"));//Se usa orElseThrow porque findByName() devuelve un Optional

        //crea el nuevo usuario, encriptando su contrasena
        User user = new User(

                request.getUserName(),
                passwordEncoder.encode(request.getPassword()),
                roleUser
        );
        //guardar el nuevo usuario en la bd
        userService.save(user);
        //Autenticamos al nuevo usuario (como el login)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );
        //Guardamos la autenticacion en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //generamos token JWT para el nuevo usuario
        String token = jwtUtil.generateToken(authentication);
        //retornamos una respuesta con el token y datos del usuario
        return new AuthResponse(token, user.getUserName(), user.getRole().getName());

    }

}
