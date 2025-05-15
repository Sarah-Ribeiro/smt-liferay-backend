package br.com.smt.portal.servidor.authentication.auth.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordResponse {

    private String token;
    private String cpf;
    private String matricula;
    private String email;
    private String newPassword;

}
