package co.com.usc.imperative.usecase.especies;

import co.com.usc.imperative.model.apiresp.ApiResp;
import co.com.usc.imperative.model.especie.Especie;
import co.com.usc.imperative.model.especie.gateways.EspecieGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class EspeciesUseCase {

    private final EspecieGateway especieGateway;

    public ApiResp listarEspecies () {

        List<Especie> listaEspecieList;

        try {
            listaEspecieList= especieGateway.listarEspecies();
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(listaEspecieList)
                    .estado(listaEspecieList.isEmpty()?204:200)
                    .mensaje(listaEspecieList.isEmpty()?"No existen datos agregados":"Especie listada correctamente")
                    .errores(null)
                    .build();
        } catch (Exception e){
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(null)
                    .estado(400)
                    .mensaje("Error al consultar las especies")
                    .errores(List.of(ApiResp.Errores.builder()
                            .mensajeError(e.getMessage())
                            .build()))
                    .build();
        }
    }
}


