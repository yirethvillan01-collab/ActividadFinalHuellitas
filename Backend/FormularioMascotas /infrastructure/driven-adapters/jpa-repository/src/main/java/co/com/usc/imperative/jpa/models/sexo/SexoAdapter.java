package co.com.usc.imperative.jpa.models.sexo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sexo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SexoAdapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
}
