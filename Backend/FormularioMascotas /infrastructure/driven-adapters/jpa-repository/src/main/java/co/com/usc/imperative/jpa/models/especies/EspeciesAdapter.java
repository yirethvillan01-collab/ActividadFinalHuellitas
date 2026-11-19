package co.com.usc.imperative.jpa.models.especies;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EspeciesAdapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
}


