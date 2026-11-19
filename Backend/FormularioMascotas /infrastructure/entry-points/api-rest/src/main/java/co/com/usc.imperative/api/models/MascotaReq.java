package co.com.usc.imperative.api.models;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MascotaReq {

    @NotBlank (message = "El nombre es obligatorio")
    private String nombre;


    @NotNull (message = "La especie es obligatorio")
    private Long especie;


    @NotBlank (message = "La raza es obligatorio")
    private String raza;

    @NotNull  (message = "La edad es obligatorio")
    @Min(value = 0, message = "La edad debe ser minímo de 0")
    @Max(value = 100, message = "La edad debe ser máximo 100")
    private int edad;

    @NotBlank (message = "EL responsable es obligatorio")
    private String responsable;

    private String observaciones;

    @NotNull (message = "Debes elegir el sexo")
    private Long sexo;

}