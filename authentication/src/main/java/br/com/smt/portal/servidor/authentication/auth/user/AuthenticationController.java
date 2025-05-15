package br.com.smt.portal.servidor.authentication.auth.user;

import br.com.smt.portal.servidor.authentication.entity.Role;
import br.com.smt.portal.servidor.authentication.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/find-by/{matricula}")
    public ResponseEntity<GetUserByMatriculaResponse> getUserByMatricula(GetUserByMatriculaRequest request, @PathVariable String matricula) {
        return ResponseEntity.ok(service.getUserByMatricula(request));
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(service.forgotPassword(request));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetUsersResponse> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role, // Recebendo como String
            @RequestHeader("Authorization") String token
    ) {
        String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Aqui fazemos a conversão para o enum Role
        Role roleEnum = null;
        if (role != null && !role.isEmpty()) {
            try {
                roleEnum = Role.valueOf(role); // Converte o String para o enum Role
            } catch (IllegalArgumentException e) {
                // Se não for um valor válido, log ou lançar um erro
                System.out.println("Role inválido: " + role);
            }
        }

        GetUsersRequest request = new GetUsersRequest(cleanToken, username, roleEnum != null ? roleEnum.name() : null);
        System.out.println(cleanToken);
        return ResponseEntity.ok(service.getUsers(request));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<RegisterAdminResponse> registerNewAdmin(@RequestBody RegisterAdminRequest request) {
        return ResponseEntity.ok(service.registerNewAdmin(request));
    }

    @GetMapping("/me/{username}")
    public ResponseEntity<GetOneUserResponse> getOneUser(
            @RequestHeader("Authorization") String token,
            @PathVariable String username) {

        // Remove o "Bearer " (com espaço)
        String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        GetOneUserRequest request = GetOneUserRequest.builder()
                .token(cleanToken)
                .utilizer(username)
                .build();

        return ResponseEntity.ok(service.getOneUser(request, username));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeleteByIdResponse> deleteById(@PathVariable UUID userId) throws AccessDeniedException {
        DeleteByIdResponse response = service.deleteById(userId);  // Passa o UUID diretamente
        return ResponseEntity.ok(response);
    }

}
