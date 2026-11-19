package co.com.usc.imperative.jpa.models.mascotas;

import co.com.usc.imperative.jpa.helper.AdapterOperations;
import co.com.usc.imperative.jpa.models.especies.EspeciesAdapter;
import co.com.usc.imperative.jpa.models.especies.EspeciesRepositoryAdapter;
import co.com.usc.imperative.jpa.models.sexo.SexoAdapter;
import co.com.usc.imperative.jpa.models.sexo.SexoRepository;
import co.com.usc.imperative.jpa.models.sexo.SexoRepositoryAdapter;
import co.com.usc.imperative.model.mascota.Mascota;
import co.com.usc.imperative.model.mascota.MascotaDTO;
import co.com.usc.imperative.model.mascota.gateways.MascotasGateway;
import co.com.usc.imperative.model.sexo.Sexo;
import co.com.usc.imperative.model.sexo.gateways.SexoGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class MascotasRepositoryAdapter extends AdapterOperations<Mascota, MascotasAdapter, String, MascotasRepository>
implements MascotasGateway {

    private final SexoRepositoryAdapter sexoRepositoryAdapter;
    private final EspeciesRepositoryAdapter especiesRepositoryAdapter;

    public MascotasRepositoryAdapter(MascotasRepository repository, ObjectMapper mapper,
                                     SexoRepositoryAdapter sexoRepositoryAdapter,
                                     EspeciesRepositoryAdapter especiesRepositoryAdapter) {


        super(repository, mapper, d -> mapper.map(d, Mascota.class));
        this.sexoRepositoryAdapter=sexoRepositoryAdapter;
        this.especiesRepositoryAdapter=especiesRepositoryAdapter;
    }

    @Override
    public List<Mascota> listarMascotas() throws Exception{

        Iterable <MascotasAdapter> mascotaIterable=repository.findAll();
        return StreamSupport
                .stream(mascotaIterable.spliterator(), false)
                .map( mascotasAdapter ->  mapper.map(mascotasAdapter, Mascota.class))
                .collect(Collectors.toList());
    }

    @Override
    public Mascota crearMascota(MascotaDTO mascotaDTO) throws Exception {

        Optional <SexoAdapter> sexoAdapter = sexoRepositoryAdapter.buscarPorId(mascotaDTO.getSexo());
        Optional <EspeciesAdapter> especiesAdapter = especiesRepositoryAdapter.buscarPorId(mascotaDTO.getEspecie());
        if (sexoAdapter.isEmpty()) {
            throw new Exception("No existe el sexo ingresado");
        }
        if (especiesAdapter.isEmpty()) {
            throw new Exception("No existe la especie ingresada");
        }
        MascotasAdapter mascotasAdapter = mapper.map(mascotaDTO, MascotasAdapter.class);
        mascotasAdapter.setSexo(sexoAdapter.get());
        mascotasAdapter.setEspecie(especiesAdapter.get());

        return mapper.map(repository.save(mascotasAdapter), Mascota.class);
    }

    @Override
    public Mascota actualizarMascota(MascotaDTO mascotaDTO) throws Exception {

        Optional <MascotasAdapter> mascotasOptionalAdapter = repository.findById(mascotaDTO.getId().toString());
        Optional <SexoAdapter> sexoAdapter = sexoRepositoryAdapter.buscarPorId(mascotaDTO.getSexo());
        Optional <EspeciesAdapter> especiesAdapter = especiesRepositoryAdapter.buscarPorId(mascotaDTO.getEspecie());


        if (mascotasOptionalAdapter.isEmpty()){
            throw new Exception("No existe la mascota");
        }
        if (sexoAdapter.isEmpty()) {
            throw new Exception("No existe el sexo ingresado");
        }
        if (especiesAdapter.isEmpty()) {
            throw new Exception("No existe la especie ingresada");
        }
        MascotasAdapter mascotasAdapter = mapper.map(mascotaDTO, MascotasAdapter.class);
        mascotasAdapter.setSexo(sexoAdapter.get());
        mascotasAdapter.setEspecie(especiesAdapter.get());
        return mapper.map(repository.save(mascotasAdapter), Mascota.class);

    }

    @Override
    public Mascota eliminarMascota(Long id) throws Exception {
        Optional <MascotasAdapter> mascotasOptionalAdapter = repository.findById(id.toString());

        if (mascotasOptionalAdapter.isEmpty()){
            throw new Exception("No existe la mascota");
        }
        repository.delete(mascotasOptionalAdapter.get());
        return mapper.map(mascotasOptionalAdapter.get(), Mascota.class);

    }


}
