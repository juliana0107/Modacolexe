package com.modacol.exe.specification;

import com.modacol.exe.entity.Producto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductoSpecification {

    public static Specification<Producto> filtrar(String nombre, String descripcion, Long proveedorId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nombre != null && !nombre.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nombre")), 
                    "%" + nombre.toLowerCase() + "%"
                ));
            }

            if (descripcion != null && !descripcion.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("descripcion")), 
                    "%" + descripcion.toLowerCase() + "%"
                ));
            }

            if (proveedorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("proveedor").get("id"), proveedorId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}