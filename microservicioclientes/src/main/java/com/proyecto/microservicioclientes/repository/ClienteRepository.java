package com.proyecto.microservicioclientes.repository;

import com.proyecto.microservicioclientes.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByIdentificacion(String identificacion);
    boolean existsByIdentificacion(String identificacion);
}