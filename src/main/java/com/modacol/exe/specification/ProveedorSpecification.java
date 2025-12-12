package com.modacol.exe.specification;

import com.modacol.exe.entity.Proveedor;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProveedorSpecification {

    public static Specification<Proveedor> filtrar(String razonSocial, String identificacion, String correo) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (razonSocial != null && !razonSocial.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("razonSocial")), 
                    "%" + razonSocial.toLowerCase() + "%"
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