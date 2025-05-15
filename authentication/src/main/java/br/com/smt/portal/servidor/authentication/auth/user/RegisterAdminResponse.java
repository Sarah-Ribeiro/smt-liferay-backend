package br.com.smt.portal.servidor.authentication.auth.user;

import br.com.smt.portal.servidor.authentication.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAdminResponse {

    private String token;
    private String password;
    private String matricula;
    private String utilizer;
    private String role;
    private String email;
    private String cpf;
    private String fullName;

}
