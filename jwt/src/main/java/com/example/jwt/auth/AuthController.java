package com.example.jwt.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor


public class AuthController {
    //Atributo por el cuál vamos a acceder a los métodos del login y responese para acceder al token.
    private final AuthService authService;

    //Devolvemos el Objeto ResponseEntity representa todas las respuestas http, incluye(codigos de estado, encabezados
    // y el cuerpo de respuesta. Nos permite personalizar la respuesta(AuthResponse)).
    //La respuesta va a se AuthResponse y en el cuerpo del mensaje accedemos a las credenciales de usuario de loginrequest
    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")

    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
