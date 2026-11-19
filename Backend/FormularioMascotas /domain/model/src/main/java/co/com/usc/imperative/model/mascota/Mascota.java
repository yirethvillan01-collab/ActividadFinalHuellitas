package co.com.usc.imperative.model.mascota;
import co.com.usc.imperative.model.especie.Especie;
import co.com.usc.imperative.model.sexo.Sexo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Mascota {
    private Long id;
    private String nombre;
    private Especie especie;
    private String raza;
    private int edad;
    private String responsable;
    private String observaciones;
    private Sexo sexo;
}
