package com.example.demo.controller;

import com.example.demo.Security.JWTUtil;
import com.example.demo.entity.Usuario;
import com.example.demo.services.usuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private usuarioServices usuarioService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Registro de nuevos usuarios
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.registerUser(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // Login de usuarios
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JWTUtil.AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Credenciales incorrectas", e);
        }

        // Cargar detalles del usuario
        final UserDetails userDetails = usuarioService.loadUserByUsername(authRequest.getUsername());

        // Generar JWT
        final String jwt = jwtUtil.generateToken(userDetails);

        // Retornar el JWT como respuesta
        return ResponseEntity.ok(new JWTUtil.AuthResponse(jwt));
    }

    // Obtener los datos del usuario autenticado
    @GetMapping("/me")
    public ResponseEntity<Usuario> getUserDetails(Principal principal) {
        Usuario usuario = usuarioService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(usuario);
    }
}