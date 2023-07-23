package com.example.jwt.auth;


import com.example.jwt.jwt.JwtService;
import com.example.jwt.user.Role;
import com.example.jwt.user.User;
import com.example.jwt.user.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    //Encriptar contraseña antes de crear el Objeto User
    private final PasswordEncoder passwordEncoder;
    //Autentificación del login
    private final AuthenticationManager authenticationManager;
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        //Generar el token, para ello usamos el objeto UserDetail
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();

    }

    public AuthResponse register(RegisterRequest request) {

        //Encriptar la contraseña antes de guardarla.
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        //Creamos objeto de tipo User, con el patrón builder()
        User user = User.builder()
                .username(request.getUsername())
                .password(encryptedPassword)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .role(Role.USER)  //Cuando se cree el usuario por primera vez va a ser de tipo USER
                .build();

        //Invocamos al repository para pasarle el objeto para crear en base de datos

        userRepository.save(user);

        return AuthResponse.builder().token(jwtService.getToken(user)).build();

    }
}
