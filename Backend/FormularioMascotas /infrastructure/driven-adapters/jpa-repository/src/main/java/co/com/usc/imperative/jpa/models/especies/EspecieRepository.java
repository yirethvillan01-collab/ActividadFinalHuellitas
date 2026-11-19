package co.com.usc.imperative.jpa.models.especies;

import co.com.usc.imperative.jpa.models.sexo.SexoAdapter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface EspecieRepository extends CrudRepository<EspeciesAdapter,
        String>, QueryByExampleExecutor<EspeciesAdapter> {
}


