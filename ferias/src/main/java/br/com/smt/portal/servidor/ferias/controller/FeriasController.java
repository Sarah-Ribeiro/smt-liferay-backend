package br.com.smt.portal.servidor.ferias.controller;

import br.com.smt.portal.servidor.ferias.config.JwtService;
import br.com.smt.portal.servidor.ferias.model.Ferias;
import br.com.smt.portal.servidor.ferias.repository.FeriasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/ferias")
public class FeriasController {

    @Autowired
    private FeriasRepository feriasRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Ferias> createEvent(@RequestBody Ferias ferias) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Captura o nome de usuário do token JWT
        ferias.setUserId(username); // Associa o evento ao usuário
        return ResponseEntity.ok(feriasRepository.save(ferias));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getEventsUser(Authentication authentication, Ferias ferias) {
        String username = authentication.getName();
        System.out.println("Username: " + username);
        authentication.getAuthorities().forEach(authority -> {
            System.out.println("Authority: " + authority.getAuthority());
        });

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            System.out.println("USER");
            var feriasPorId = feriasRepository.findByUserId(username);
            return ResponseEntity.ok().body(feriasPorId);
        } else {
            System.out.println("ADMIN");
            var feriasTodas = feriasRepository.findAll();
            return ResponseEntity.ok(feriasTodas);
        }
    }

    @DeleteMapping("/{feriasId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteByEventId(Authentication authentication, @PathVariable UUID feriasId) {
        String username = authentication.getName();
        System.out.println("Username: " + username);
        authentication.getAuthorities().forEach(authority -> {
            System.out.println("Authority: " + authority.getAuthority());
        });

        // Verifica se o usuário tem a role de ADMIN
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_ADMIN"));

        // Se o usuário não for admin, lança exceção
        if (!isAdmin) {
            throw new RuntimeException("Um usuário comum não pode deletar um evento");
        } else {
            // Verifica se o evento existe antes de deletar
            Optional<Ferias> eventToDelete = feriasRepository.findById(feriasId);

            if (eventToDelete.isPresent()) {
                feriasRepository.deleteById(feriasId);
                return ResponseEntity.noContent().build();  // Retorna HTTP 204 No Content para exclusão bem-sucedida
            } else {
                // Caso o evento não exista
                throw new RuntimeException("Evento não encontrado com o ID: " + feriasId);
            }
        }
    }


}
