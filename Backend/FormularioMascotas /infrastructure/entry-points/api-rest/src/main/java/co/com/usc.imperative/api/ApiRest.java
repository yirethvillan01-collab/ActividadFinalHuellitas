package co.com.usc.imperative.api;
import co.com.usc.imperative.model.apiresp.ApiResp;
import co.com.usc.imperative.model.mascota.MascotaDTO;
import co.com.usc.imperative.usecase.especies.EspeciesUseCase;
import co.com.usc.imperative.usecase.mascotas.MascotasUseCase;
import co.com.usc.imperative.usecase.sexo.SexoUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiRest {

    private final SexoUseCase sexoUseCase;
    private final EspeciesUseCase especiesUsecase;
    private final MascotasUseCase mascotasUseCase;
    private final ObjectMapper objectMapper;



    @GetMapping(path = "/listarSexos")
    public ResponseEntity<ApiResp> listarSexos() {
        ApiResp resp = sexoUseCase.listarSexos();
        return ResponseEntity.status(HttpStatus.valueOf(resp.getEstado())).body(resp);
    }

    @GetMapping(path = "/listarEspecies")
    public ResponseEntity<ApiResp> listarEspecies() {
        ApiResp resp = especiesUsecase.listarEspecies();
        return ResponseEntity.status(HttpStatus.valueOf(resp.getEstado())).body(resp);
    }

    @GetMapping(path = "/listarMascotas")
    public ResponseEntity<ApiResp> listarMascotas() {
        ApiResp resp = mascotasUseCase.listarMascotas();
        return ResponseEntity.status(HttpStatus.valueOf(resp.getEstado())).body(resp);
    }
    @PostMapping(path = "/crearMascotas", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResp> crearMascotas(@Valid @RequestBody co.com.usc.imperative.api.models.MascotaReq mascota) {
        MascotaDTO mascotaDTO = objectMapper.map(mascota, MascotaDTO.class);
        ApiResp resp = mascotasUseCase.crearMascota(mascotaDTO);
        return ResponseEntity.status(HttpStatus.valueOf(resp.getEstado())).body(resp);
    }

    @PutMapping(path = "/actualizarMascotas", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResp> actualizarMascotas(@Valid @RequestBody co.com.usc.imperative.api.models.MascotaReqUpdate mascota) {
        MascotaDTO mascotaDTO = objectMapper.map(mascota, MascotaDTO.class);
        ApiResp resp = mascotasUseCase.actualizarMascota(mascotaDTO);
        return ResponseEntity.status(HttpStatus.valueOf(resp.getEstado())).body(resp);
    }

    @DeleteMapping(path = "/eliminarMascotas/{id}")
    public ResponseEntity<ApiResp> eliminarMascotas(@PathVariable("id") Long id) {
        ApiResp resp = mascotasUseCase.eliminarMascotas(id);
        return ResponseEntity.status(HttpStatus.valueOf(resp.getEstado())).body(resp);
    }


}
