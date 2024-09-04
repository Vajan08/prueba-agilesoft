package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.repository.usuarioRepository;
import com.example.demo.entity.Usuario;

import java.util.ArrayList;

@Service
public class usuarioServices {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private Usuario usuarioEntity;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registerUser(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);

    }

    public Usuario getUserByUsername(String username) {
        return usuarioRepository.findByusername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByusername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new org.springframework.security.core.userdetails.User(usuario.getUsername(), usuario.getPassword(), new ArrayList<>());
    }
}