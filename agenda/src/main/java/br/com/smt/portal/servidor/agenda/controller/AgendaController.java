package br.com.smt.portal.servidor.agenda.controller;

import br.com.smt.portal.servidor.agenda.config.JwtService;
import br.com.smt.portal.servidor.agenda.model.Agenda;
import br.com.smt.portal.servidor.agenda.repository.EventRepository;
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
@RequestMapping("/api/events")
public class AgendaController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Agenda> createEvent(@RequestBody Agenda agenda) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Captura o nome de usuário do token JWT
        agenda.setUserId(username); // Associa o evento ao usuário
        return ResponseEntity.ok(eventRepository.save(agenda));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Agenda> getEventsUser(Authentication authentication) {
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
            return eventRepository.findByUserId(username);
        } else {
            System.out.println("ADMIN");
            return eventRepository.findAll();
        }
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteByEventId(Authentication authentication, @PathVariable UUID eventId) {
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
            Optional<Agenda> eventToDelete = eventRepository.findById(eventId);

            if (eventToDelete.isPresent()) {
                eventRepository.deleteById(eventId);
                return ResponseEntity.noContent().build();  // Retorna HTTP 204 No Content para exclusão bem-sucedida
            } else {
                // Caso o evento não exista
                throw new RuntimeException("Evento não encontrado com o ID: " + eventId);
            }
        }
    }


}
