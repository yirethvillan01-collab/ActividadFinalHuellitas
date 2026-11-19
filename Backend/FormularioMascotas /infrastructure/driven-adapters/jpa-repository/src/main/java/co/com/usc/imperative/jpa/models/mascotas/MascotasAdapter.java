package co.com.usc.imperative.jpa.models.mascotas;

import co.com.usc.imperative.jpa.models.especies.EspeciesAdapter;
import co.com.usc.imperative.jpa.models.sexo.SexoAdapter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mascotas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)


public class MascotasAdapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie", referencedColumnName = "id", nullable = false)
    private EspeciesAdapter especie;

    private String raza;
    private int edad;
    private String responsable;
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sexo", referencedColumnName = "id", nullable = false)
    private SexoAdapter sexo;
}
