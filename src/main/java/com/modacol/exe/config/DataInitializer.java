package com.modacol.exe.config;

import com.modacol.exe.entity.Rol;
import com.modacol.exe.entity.Usuario;
import com.modacol.exe.repository.RolRepository;
import com.modacol.exe.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("Verificando usuarios existentes: " + usuarioRepository.count());
        
        // Crear roles si no existen
        Rol adminRole = rolRepository.findByTipo("ADMIN").orElseGet(() -> {
            Rol r = new Rol();
            r.setTipo("ADMIN");
            return rolRepository.save(r);
        });

        Rol flujoCajaRole = rolRepository.findByTipo("FLUJO_DE_CAJA").orElseGet(() -> {
            Rol r = new Rol();
            r.setTipo("FLUJO_DE_CAJA");
            return rolRepository.save(r);
        });

        Rol operativoRole = rolRepository.findByTipo("OPERATIVO").orElseGet(() -> {
            Rol r = new Rol();
            r.setTipo("OPERATIVO");
            return rolRepository.save(r);
        });

        if (!usuarioRepository.findByCorreo("admin@modacol.com").isPresent()) {
            Usuario admin = new Usuario();
            admin.setCorreo("admin@modacol.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setNombre("Administrador");
            admin.setRol(adminRole);
            usuarioRepository.save(admin);
            System.out.println("Usuario admin creado");
        }

        if (!usuarioRepository.findByCorreo("flujo@modacol.com").isPresent()) {
            Usuario flujoCaja = new Usuario();
            flujoCaja.setCorreo("flujo@modacol.com");
            flujoCaja.setPasswordHash(passwordEncoder.encode("flujo123"));
            flujoCaja.setNombre("Flujo de Caja");
            flujoCaja.setRol(flujoCajaRole);
            usuarioRepository.save(flujoCaja);
            System.out.println("Usuario flujo creado");
        }

        if (!usuarioRepository.findByCorreo("operativo@modacol.com").isPresent()) {
            Usuario operativo = new Usuario();
            operativo.setCorreo("operativo@modacol.com");
            operativo.setPasswordHash(passwordEncoder.encode("operativo123"));
            operativo.setNombre("Operativo");
            operativo.setRol(operativoRole);
            usuarioRepository.save(operativo);
            System.out.println("Usuario operativo creado");
        }
        
        System.out.println("Total usuarios: " + usuarioRepository.count());
    }
    }
