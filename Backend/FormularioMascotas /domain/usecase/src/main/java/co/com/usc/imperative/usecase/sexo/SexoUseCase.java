package co.com.usc.imperative.usecase.sexo;

import co.com.usc.imperative.model.apiresp.ApiResp;
import co.com.usc.imperative.model.sexo.Sexo;
import co.com.usc.imperative.model.sexo.gateways.SexoGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor

public class SexoUseCase {

    private final SexoGateway sexoGateway;



    public ApiResp listarSexos () {

        List<Sexo> listaSexoList;
        try {
            listaSexoList= sexoGateway.listarSexos();
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(listaSexoList)
                    .estado(listaSexoList.isEmpty()?204:200)
                    .mensaje(listaSexoList.isEmpty()?"No existen datos agregados":"Sexo listado exitosamente")
                    .errores(null)
                    .build();
        } catch (Exception e){
            return ApiResp.builder()
                    .fecha(LocalDateTime.now())
                    .datos(null)
                    .estado(400)
                    .mensaje("Error al consultar los sexos")
                    .errores(List.of(ApiResp.Errores.builder()
                            .mensajeError(e.getMessage())
                            .build()))
                    .build();
        }
    }
}
