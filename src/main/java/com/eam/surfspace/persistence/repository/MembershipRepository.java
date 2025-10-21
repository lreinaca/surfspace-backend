package com.eam.surfspace.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.eam.surfspace.persistence.entity.MembershipEntity;
import com.eam.surfspace.persistence.entity.MembershipStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface MembershipRepository extends JpaRepository<MembershipEntity, Integer> {
    long countByIdUsuario_IdUsuarioAndEstado(Integer idUsuario, MembershipStatus estado);

    @Query("SELECT COUNT(m) > 0 FROM MembershipEntity m " +
            "WHERE m.idUsuario.idUsuario = :idUsuario " +
            "AND m.estado = 'ACTIVA' " +
            "AND m.fechaInicio <= :fechaInicio " +
            "AND m.fechaFin >= :fechaFin")
    boolean hasActiveMembershipInRange(@Param("idUsuario") Integer idUsuario,
                                       @Param("fechaInicio") LocalDate fechaInicio ,
                                       @Param("fechaFin") LocalDate fechaFin);

    // Nuevos:
    @Query("SELECT m FROM MembershipEntity m WHERE m.estado = 'ACTIVA' AND m.fechaFin < :now")
    List<MembershipEntity> findExpiredActive(@Param("now") LocalDate now);
}






