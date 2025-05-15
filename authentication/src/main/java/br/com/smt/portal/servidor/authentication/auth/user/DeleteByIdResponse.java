package br.com.smt.portal.servidor.authentication.auth.user;

import br.com.smt.portal.servidor.authentication.entity.User;
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
public class DeleteByIdResponse {

    private List<User> user;
    private String token;
    private UUID userId;
    private String utilizer;

}
