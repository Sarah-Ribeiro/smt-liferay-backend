package br.com.smt.portal.servidor.authentication.auth.user;

import br.com.smt.portal.servidor.authentication.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class getAllUsersResponse {

    private String token;
    private String fullName;
    private String matricula;
    private String utilizer;
    private String email;
    private String cpf;
    private List<User> user;

}
