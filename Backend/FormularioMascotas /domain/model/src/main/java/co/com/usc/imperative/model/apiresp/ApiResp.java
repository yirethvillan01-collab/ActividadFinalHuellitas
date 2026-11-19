package co.com.usc.imperative.model.apiresp;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApiResp {
    private LocalDateTime fecha;
    private String  mensaje;
    private int estado;
    private Object datos;
    private List<Errores> errores;

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Errores  {
        private String mensajeError;
        private String tipoError;
    }
}
