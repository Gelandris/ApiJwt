package com.example.jwt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


//Esta clase tiene que ver con el filtro. OncePerRequestFilter (crea filtros personalizados) y garantiza
//que el filtro se ejecuta solo una vez.
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //Agregamos los permisos
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    //Filtros relacionados con el token. FilterChain es la cadena de filtros ya configurada.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //Obtengo el token

        final String token = getTokenFromRequest(request);
        final String username;

        //Si el filtro es null lo devolvemos a la cadena de filtros.
        if(token == null) {
            filterChain.doFilter(request,response);
            return;
        }
        username= jwtService.getUsernameFromToken(token);
        //Si es distinto a null y no lo encuentra lo buscamos en base de datos.
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //Validamos si el token es válido
            if(jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //Llamamos al filtro para que siga su curso.
        filterChain.doFilter(request,response);
    }

    //Este método devuelve el token como String. Requiere el request ya que en el encabezado obtenemos el token.
    private String getTokenFromRequest(HttpServletRequest request) {

        //Tratamos de encontrar del encabezado el item o la propiedad de autentificación.
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        /*Verificamos si comienza por Bearer. 1. Si existe este texto en el encabezado y 2 para evaluar que
        authHeader comienze por Bearer, si es correcto es que viene el token y entonces lo tengo que extraer y
        retornar*/

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")) {

            return authHeader.substring(7); //Desde el caracter 7 es el token
        }
        return null;
    }
}
