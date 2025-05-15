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
public class GetUsersRequest {

    private String token;
    private String utilizer;
    private String role;

}
