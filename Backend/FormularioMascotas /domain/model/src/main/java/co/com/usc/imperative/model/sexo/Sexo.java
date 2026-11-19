package co.com.usc.imperative.model.sexo;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor
public class Sexo {
    private Long id;
    private String nombre;
}
