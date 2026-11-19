package co.com.usc.imperative.jpa.models.mascotas;

import co.com.usc.imperative.jpa.models.sexo.SexoAdapter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface MascotasRepository extends CrudRepository<MascotasAdapter, String>, QueryByExampleExecutor<MascotasAdapter> {
}


