package com.modacol.exe.specification;

import com.modacol.exe.entity.Compra;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompraSpecification {

    public static Specification<Compra> filtrar(LocalDate fechaInicio, LocalDate fechaFin, Long proveedorId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (fechaInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fecha"), fechaInicio));
            }

            if (fechaFin != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fecha"), fechaFin));
            }

            if (proveedorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("proveedor").get("id"), proveedorId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}