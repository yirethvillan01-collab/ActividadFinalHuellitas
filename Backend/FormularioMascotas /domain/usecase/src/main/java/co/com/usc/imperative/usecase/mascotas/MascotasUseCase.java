package co.com.usc.imperative.usecase.mascotas;

import co.com.usc.imperative.model.apiresp.ApiResp;
import co.com.usc.imperative.model.mascota.Mascota;
import co.com.usc.imperative.model.mascota.MascotaDTO;
import co.com.usc.imperative.model.mascota.gateways.MascotasGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor


public class MascotasUseCase {

    private final MascotasGateway mascotasGateway;


    public ApiResp crearMascota(MascotaDTO mascotaDTO){

        Mascota mascota;

        try {
            mascota = mascotasGateway.crearMascota(mascotaDTO);
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(mascota)
                    .estado(200)
                    .mensaje("Mascota creada exitosamente")
                    .errores(null)
                    .build();
        } catch (Exception e){
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(null)
                    .estado(400)
                    .mensaje("Error al crear la mascota")
                    .errores(List.of(ApiResp.Errores.builder()
                            .mensajeError(e.toString())
                            .build()))
                    .build();
        }
    }

    public ApiResp actualizarMascota(MascotaDTO mascotaDTO){

        Mascota mascota;

        try {
            mascota = mascotasGateway.actualizarMascota(mascotaDTO);
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(mascota)
                    .estado(200)
                    .mensaje("Mascota actualizada exitosamente")
                    .errores(null)
                    .build();
        } catch (Exception e){
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(null)
                    .estado(400)
                    .mensaje("Error al actualizar la mascota")
                    .errores(List.of(ApiResp.Errores.builder()
                            .mensajeError(e.toString())
                            .build()))
                    .build();
        }
    }

    public ApiResp listarMascotas () {

        List<Mascota> listaMascotasList;

        try {
            listaMascotasList= mascotasGateway.listarMascotas();
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(listaMascotasList)
                    .estado(listaMascotasList.isEmpty()?204:200)
                    .mensaje(listaMascotasList.isEmpty()?"No existen datos agregados":"Mascota listada exitosamente")
                    .errores(null)
                    .build();
        } catch (Exception e){
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(null)
                    .estado(400)
                    .mensaje("Error al consultar la mascota")
                    .errores(List.of(ApiResp.Errores.builder()
                            .mensajeError(e.toString())
                            .build()))
                    .build();
        }
    }

    public ApiResp eliminarMascotas (Long id) {

      Mascota mascotaEliminada;

        try {
            mascotaEliminada= mascotasGateway.eliminarMascota(id);
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(mascotaEliminada)
                    .estado(200)
                    .mensaje("Se ha eliminado a la mascota correctamente")
                    .errores(null)
                    .build();
        } catch (Exception e){
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(null)
                    .estado(400)
                    .mensaje("Error al eliminar la mascota")
                    .errores(List.of(ApiResp.Errores.builder()
                            .mensajeError(e.toString())
                            .build()))
                    .build();
        }
    }

}
