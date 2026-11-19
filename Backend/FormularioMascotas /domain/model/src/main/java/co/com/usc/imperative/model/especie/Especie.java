package co.com.usc.imperative.model.especie;
import lombok.*;
//import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Especie {
    private Long id;
    private String nombre;
}
