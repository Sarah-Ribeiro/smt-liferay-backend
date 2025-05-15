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
public class GetOneUserResponse {

    private String token;
    private String role;
    private String utilizer;

}
