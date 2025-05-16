package br.com.smt.portal.servidor.authentication.auth.user;

import br.com.smt.portal.servidor.authentication.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUsersResponse {

    private List<UserResponse> users; // Lista de usuários

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponse {
        private UUID id;
        private String token;
        private String fullName;
        private String cpf;
        private String utilizer;
        private String email;
        private String matricula;
        private String role; // Aqui, a role pode ser um enum ou outra entidade
    }

}
