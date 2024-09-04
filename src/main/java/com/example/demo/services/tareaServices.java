package com.example.demo.services;

import com.example.demo.entity.EstadoTarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.tareaRepository;
import com.example.demo.entity.Tarea;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.usuarioRepository;
import com.example.demo.entity.EstadoTarea;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class tareaServices {
    @Autowired
    private tareaRepository tareaRepository;

    @Autowired
    private usuarioRepository usuarioRepository;
    @Autowired
    private Usuario usuarioEntity;

    @Autowired
    private EstadoTarea estadoTarea;
    public List<Tarea> getAllTasks(Usuario usuario) {
        return tareaRepository.findAllByUsuario(usuario);
    }

    public Tarea addTask(Tarea tarea, Usuario usuario) {
        tarea.setUsuario(usuario);
        tarea.setFechaCreacion(LocalDateTime.now());
        tarea.setFechaActualizacion(LocalDateTime.now());
        tarea.setEstado(EstadoTarea.valueOf("NO_RESUELTO"));
        return tareaRepository.save(tarea);
    }

    public Tarea resolveTask(Long id, Usuario usuario) throws AccessDeniedException {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (!tarea.getUsuario().equals(usuario)) {
            throw new AccessDeniedException("Acceso denegado");
        }

        tarea.setEstado(EstadoTarea.valueOf("RESUELTO"));;
        tarea.setFechaActualizacion(LocalDateTime.now());
        return tareaRepository.save(tarea);
    }

    public void deleteTask(Long id, Usuario usuario) throws AccessDeniedException {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (!tarea.getUsuario().equals(usuario)) {
            throw new AccessDeniedException("Acceso denegado");
        }

        tareaRepository.delete(tarea);
    }
}
