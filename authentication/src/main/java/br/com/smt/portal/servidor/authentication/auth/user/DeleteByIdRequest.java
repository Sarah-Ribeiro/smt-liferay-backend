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
public class DeleteByIdRequest {

    private String token;
    private UUID userId;
    private String utilizer;

}
