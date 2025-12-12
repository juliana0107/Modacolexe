package com.modacol.exe.security;

import com.modacol.exe.entity.Usuario;
import com.modacol.exe.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        System.out.println("Intentando login con: " + correo);
        
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> {
                    System.out.println("Usuario no encontrado: " + correo);
                    return new UsernameNotFoundException("Usuario no encontrado: " + correo);
                });

        System.out.println("Usuario encontrado: " + usuario.getNombre() + ", Rol: " + usuario.getRol().getTipo());
        
        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPasswordHash())
                .roles(usuario.getRol().getTipo())
                .build();
    }
}
