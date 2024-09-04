package com.example.demo.repository;

import com.example.demo.entity.Tarea;
import com.example.demo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface tareaRepository extends JpaRepository<Tarea,Long> {
    List<Tarea> findAllByUsuario(Usuario usuario);
}
