package com.modacol.exe.repository;

import com.modacol.exe.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    
    List<Mensaje> findByDestinatarioIdOrderByFechaEnvioDesc(Long destinatarioId);
    
    List<Mensaje> findByRemitenteIdOrderByFechaEnvioDesc(Long remitenteId);
    
    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.destinatario.id = :userId AND m.leido = false")
    Long countMensajesNoLeidos(@Param("userId") Long userId);
}