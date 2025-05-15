package br.com.smt.portal.servidor.authentication.auth.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private String token;
    private String fullName;
    private String email;
    private String cpf;
    private String password;
    private String matricula;
    private String utilizer;

}
