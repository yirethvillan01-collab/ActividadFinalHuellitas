package co.com.usc.imperative.model.mascota;
import co.com.usc.imperative.model.especie.Especie;
import co.com.usc.imperative.model.sexo.Sexo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MascotaDTO {
    private Long id;
    private String nombre;
    private Long especie;
    private String raza;
    private int edad;
    private String responsable;
    private String observaciones;
    private Long sexo;
}
