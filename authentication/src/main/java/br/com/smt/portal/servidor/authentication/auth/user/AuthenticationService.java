package br.com.smt.portal.servidor.authentication.auth.user;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.smt.portal.servidor.authentication.config.JwtService;
import br.com.smt.portal.servidor.authentication.entity.Role;
import br.com.smt.portal.servidor.authentication.entity.User;
import br.com.smt.portal.servidor.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private String gerarSenhaAleatoria() {
        // Gera uma senha aleatória com 8 caracteres
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        Random random = new Random();
        StringBuilder senha = new StringBuilder(12);

        for (int i = 0; i < 12; i++) {
            senha.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return senha.toString();
    }

    public RegisterResponse register(RegisterRequest request) {
        String senhaGerada = gerarSenhaAleatoria();

        // Cria a matricula
        String matriculaGerada;
        do {
            int numero = 10000 + new Random().nextInt(90000);
            matriculaGerada = String.valueOf(numero);
        } while (repository.existsByMatricula(matriculaGerada));

        // Gera username com primeiro e último nome do fullName
        String fullName = request.getFullName().trim();
        String[] partes = fullName.split("\s+");
        String usernameFinal = partes.length == 1
                ? partes[0]
                : partes[0] + " " + partes[partes.length - 1];

        var user = User.builder().fullName(request.getFullName()).cpf(request.getCpf()).utilizer(usernameFinal)
                .matricula(matriculaGerada).email(request.getEmail())
                .password(passwordEncoder.encode(senhaGerada))
                .role(Role.USER).build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return RegisterResponse.builder().token(jwtToken).email(user.getEmail()).fullName(user.getFullName()).cpf(user.getCpf()).utilizer(user.getUtilizer()).password(senhaGerada).matricula(user.getMatricula()).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().id(user.getId()).cpf(user.getCpf())
                .fullName(user.getFullName()).matricula(user.getMatricula())
                .email(user.getEmail()).role(user.getRole().name())
                .utilizer(user.getUtilizer()).token(jwtToken).build();
    }

    public GetUserByMatriculaResponse getUserByMatricula(GetUserByMatriculaRequest request) {
        var user = repository.findUserByMatricula(request.getMatricula()).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        var jwtToken = jwtService.generateToken(user);
        return GetUserByMatriculaResponse.builder().token(jwtToken).matricula(request.getMatricula()).utilizer(user.getUtilizer()).role(user.getRole().name()).cpf(user.getCpf()).build();
    }

    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        if ((request.getCpf() == null || request.getCpf().isBlank()) &&
                (request.getMatricula() == null || request.getMatricula().isBlank())) {
            throw new IllegalArgumentException("Informe o CPF ou a matrícula.");
        }

        Optional<User> optionalUser = Optional.empty();

        if (request.getCpf() != null && !request.getCpf().isBlank()) {
            optionalUser = repository.findByCpf(request.getCpf());
        } else if (request.getMatricula() != null && !request.getMatricula().isBlank()) {
            optionalUser = repository.findByMatricula(request.getMatricula());
        }

        var user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return ForgotPasswordResponse.builder()
                .token(jwtToken)
                .cpf(user.getCpf())
                .email(user.getEmail())
                .matricula(user.getMatricula())
                .newPassword(user.getPassword())
                .build();
    }

    public RegisterAdminResponse registerNewAdmin(RegisterAdminRequest request) {
        String senhaGerada = gerarSenhaAleatoria();

        // Cria a matricula
        String matriculaGerada;
        do {
            int numero = 10000 + new Random().nextInt(90000);
            matriculaGerada = String.valueOf(numero);
        } while (repository.existsByMatricula(matriculaGerada));

        // Gera username com primeiro e último nome do fullName
        String fullName = request.getFullName().trim();
        String[] partes = fullName.split("\s+");
        String usernameFinal = partes.length == 1
                ? partes[0]
                : partes[0] + " " + partes[partes.length - 1];

        var user = User.builder().fullName(request.getFullName()).cpf(request.getCpf()).utilizer(usernameFinal)
                .matricula(matriculaGerada).email(request.getEmail())
                .password(passwordEncoder.encode(senhaGerada))
                .role(Role.ADMIN).build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return RegisterAdminResponse.builder().fullName(user.getFullName()).cpf(user.getCpf()).token(jwtToken).email(user.getEmail()).role(user.getRole().name()).utilizer(user.getUtilizer()).password(senhaGerada).matricula(user.getMatricula()).build();
    }

    public GetUsersResponse getUsers(GetUsersRequest request) {
        var users = repository.findAll();

        List<GetUsersResponse.UserResponse> userResponses = users.stream()
                .map(user -> {
                    String userToken = jwtService.generateTokenForUser(user);
                    return GetUsersResponse.UserResponse.builder()
                            .id(user.getId())
                            .token(userToken) // ou remova se não usar
                            .fullName(user.getFullName())
                            .cpf(user.getCpf())
                            .utilizer(user.getUtilizer())
                            .email(user.getEmail())
                            .matricula(user.getMatricula())
                            .role(user.getRole().name())
                            .build();
                }).toList();

        return GetUsersResponse.builder()
                .users(userResponses)
                .build();
    }

    public GetOneUserResponse getOneUser(GetOneUserRequest request, UUID id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String userToken = jwtService.generateTokenForUser(user);

        return GetOneUserResponse.builder()
                .id(user.getId())
                .token(userToken)
                .fullName(user.getFullName())
                .email(user.getEmail())
                .cpf(user.getCpf())
                .role(user.getRole().name())
                .build();
    }

    public DeleteByIdResponse deleteById(UUID id) throws AccessDeniedException {
        // Verifica se o usuário tem permissão de admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("Somente administradores podem excluir usuários.");
        }

        // Busca o usuário pelo ID
        User userToDelete = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Exclui o usuário
        repository.deleteById(id);

        // Retorna a resposta com o usuário excluído
        return DeleteByIdResponse.builder().user(List.of(userToDelete)).build();
    }
}
