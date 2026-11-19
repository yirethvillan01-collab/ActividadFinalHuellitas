package co.com.usc.imperative.jpa.models.especies;

import co.com.usc.imperative.jpa.helper.AdapterOperations;
import co.com.usc.imperative.jpa.models.sexo.SexoAdapter;
import co.com.usc.imperative.jpa.models.sexo.SexoRepository;
import co.com.usc.imperative.model.especie.Especie;
import co.com.usc.imperative.model.especie.gateways.EspecieGateway;
import co.com.usc.imperative.model.sexo.Sexo;
import co.com.usc.imperative.model.sexo.gateways.SexoGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class EspeciesRepositoryAdapter extends AdapterOperations<Especie, EspeciesAdapter, String, EspecieRepository>
implements EspecieGateway {
    public EspeciesRepositoryAdapter(EspecieRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Especie.class));
    }

    @Override
    public List<Especie> listarEspecies() throws Exception{

        Iterable <EspeciesAdapter> especieIterable=repository.findAll();
        return StreamSupport
                .stream(especieIterable.spliterator(), false)
                .map( especieAdapter ->  mapper.map(especieAdapter, Especie.class))
                .collect(Collectors.toList());
    }

    public Optional<EspeciesAdapter> buscarPorId (Long id){
        return repository.findById(id.toString());
    }
}
