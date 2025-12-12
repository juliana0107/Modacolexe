package com.modacol.exe.specification;

import com.modacol.exe.entity.Cliente;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClienteSpecification {

    public static Specification<Cliente> filtrar(String empresa, String nombre, String identificacion, String correo) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (empresa != null && !empresa.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("empresa")), 
                    "%" + empresa.toLowerCase() + "%"
                ));
            }

            if (nombre != null && !nombre.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nombre")), 
                    "%" + nombre.toLowerCase() + "%"
                ));
            }

            if (identificacion != null && !identificacion.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    root.get("identificacion"), 
                    "%" + identificacion + "%"
                ));
            }

            if (correo != null && !correo.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("correo")), 
                    "%" + correo.toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}