package co.com.usc.imperative.jpa.models.sexo;

import co.com.usc.imperative.jpa.helper.AdapterOperations;
import co.com.usc.imperative.model.sexo.Sexo;
import co.com.usc.imperative.model.sexo.gateways.SexoGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class SexoRepositoryAdapter extends AdapterOperations<Sexo,SexoAdapter, String, SexoRepository>
implements SexoGateway {
    public SexoRepositoryAdapter(SexoRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Sexo.class));
    }

    @Override
    public List<Sexo> listarSexos() throws Exception{

        Iterable <SexoAdapter> sexoIterable=repository.findAll();
        return StreamSupport
                .stream(sexoIterable.spliterator(), false)
                .map( sexoAdapter ->  mapper.map(sexoAdapter, Sexo.class))
                .collect(Collectors.toList());
    }

    public Optional<SexoAdapter> buscarPorId (Long id){
        return repository.findById(id.toString());
    }

}
