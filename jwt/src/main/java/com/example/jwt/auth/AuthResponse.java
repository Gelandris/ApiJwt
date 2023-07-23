package com.example.jwt.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder //Poder construir los objetos.
@AllArgsConstructor
@NoArgsConstructor

public class AuthResponse {

    String token;
}
