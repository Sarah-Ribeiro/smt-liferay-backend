package br.com.smt.portal.servidor.authentication.auth.user;

import br.com.smt.portal.servidor.authentication.entity.Role;
import br.com.smt.portal.servidor.authentication.entity.User;
import br.com.smt.portal.servidor.authentication.repository.UserRepository;
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
    private final UserRepository repository;

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
    public ResponseEntity<GetUsersResponse> getUsers(GetUsersRequest request) {
        return ResponseEntity.ok(service.getUsers(request));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<RegisterAdminResponse> registerNewAdmin(@RequestBody RegisterAdminRequest request) {
        return ResponseEntity.ok(service.registerNewAdmin(request));
    }

    @GetMapping("/me/{id}")
    public ResponseEntity<GetOneUserResponse> getOneUser(@PathVariable UUID id, GetOneUserRequest request) {
        return ResponseEntity.ok(service.getOneUser(request, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeleteByIdResponse> deleteById(@PathVariable UUID userId) throws AccessDeniedException {
        DeleteByIdResponse response = service.deleteById(userId);
        return ResponseEntity.ok(response);
    }

}