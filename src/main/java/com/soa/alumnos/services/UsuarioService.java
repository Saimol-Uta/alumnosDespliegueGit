package com.soa.alumnos.services;

import com.soa.alumnos.dto.RegistroUsuarioDto;
import com.soa.alumnos.entity.Usuario;
import com.soa.alumnos.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getActivo(),
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())));
    }

    public Usuario registrar(RegistroUsuarioDto dto) {
        if (usuarioRepo.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (usuarioRepo.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Si no hay usuarios, el primero será ADMIN
        boolean esPrimerUsuario = usuarioRepo.count() == 0;

        Usuario usuario = Usuario.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .email(dto.email())
                .nombre(dto.nombre())
                .rol(esPrimerUsuario ? Usuario.Rol.ADMIN : Usuario.Rol.SECRETARIA)
                .activo(true)
                .build();

        return usuarioRepo.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepo.findAll();
    }

    public Usuario porId(Long id) {
        return usuarioRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    public void eliminar(Long id) {
        usuarioRepo.deleteById(id);
    }

    public long contarUsuarios() {
        return usuarioRepo.count();
    }
}
