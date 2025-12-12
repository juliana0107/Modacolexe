package com.modacol.exe.specification;

import com.modacol.exe.entity.Usuario;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UsuarioSpecification {

    public static Specification<Usuario> filtrar(String nombre, String correo, Long rolId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nombre != null && !nombre.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nombre")), 
                    "%" + nombre.toLowerCase() + "%"
                ));
            }

            if (correo != null && !correo.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("correo")), 
                    "%" + correo.toLowerCase() + "%"
                ));
            }

            if (rolId != null) {
                predicates.add(criteriaBuilder.equal(root.get("rol").get("id"), rolId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
