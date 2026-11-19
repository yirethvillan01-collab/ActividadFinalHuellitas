package co.com.usc.imperative.jpa.models.sexo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface SexoRepository extends CrudRepository<SexoAdapter, String>, QueryByExampleExecutor<SexoAdapter> {
}


