package com.grupoplazaverde.controlapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.grupoplazaverde.controlapp.model.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

}

