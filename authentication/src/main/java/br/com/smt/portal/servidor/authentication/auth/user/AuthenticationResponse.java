package br.com.smt.portal.servidor.authentication.auth.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private UUID id;
    private String token;
    private String utilizer;
    private String matricula;
    private String role;
    private String email;
    private String cpf;
    private String fullName;

}
