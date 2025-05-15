package br.com.smt.portal.servidor.folha_de_ponto.controller;

import br.com.smt.portal.servidor.folha_de_ponto.model.RegistroPonto;
import br.com.smt.portal.servidor.folha_de_ponto.model.TipoRegistro;
import br.com.smt.portal.servidor.folha_de_ponto.model.Usuario;
import br.com.smt.portal.servidor.folha_de_ponto.repository.RegistroPontoRepository;
import br.com.smt.portal.servidor.folha_de_ponto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/ponto")
public class RegistroPontoController {

    @Autowired
    private RegistroPontoRepository registroRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping("/listar")
    public ResponseEntity<?> listarRegistroDoMes(String email) {
        var registros = registroRepo.findAll(); // retorna tudo
        return ResponseEntity.ok(registros);
    }

    @PostMapping("/marcar")
    public ResponseEntity<?> marcarPonto(@RequestBody RegistroPontoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal(); // pego do token

        Usuario usuario = criarUsuarioSeNaoExistir(email);
        LocalDate hoje = LocalDate.now();

        RegistroPonto registro = registroRepo.findByUsuarioAndData(usuario, hoje)
                        .orElse(new RegistroPonto());

        LocalDate agora = LocalDate.now();

        registro.setUsuario(usuario);
        registro.setData(hoje);

        if (registro.getHoraEntrada() == null) {
            registro.setHoraEntrada(LocalTime.from(agora));
        } else if (registro.getHoraAlmocoInicio() == null) {
            registro.setHoraAlmocoInicio(LocalTime.from(agora));
        } else if (registro.getHoraAlmocoRetorno() == null) {
            registro.setHoraAlmocoRetorno(LocalTime.from(agora));
        } else if (registro.getHoraSaida() == null) {
            registro.setHoraSaida(LocalTime.from(agora));
        } else {
            return ResponseEntity.badRequest().body("Todos os horários do dia já foram registrados.");
        }

        registroRepo.save(registro);

        return ResponseEntity.ok("Ponto registrado com sucesso!");
    }

    private Usuario criarUsuarioSeNaoExistir(String email) {
        return usuarioRepo.findByEmail(email)
                .orElseGet(() -> {
                    Usuario novo = new Usuario();
                    novo.setNome("Usuário importado"); // ou extraído do token futuramente
                    novo.setEmail(email);
                    return usuarioRepo.save(novo);
                });
    }

    private boolean verificarAtraso(Usuario usuario, TipoRegistro tipo) {
        if (tipo != TipoRegistro.ENTRADA) {
            return LocalTime.now().isAfter(LocalTime.of(8,0));
        }
        return false;
    }

}

