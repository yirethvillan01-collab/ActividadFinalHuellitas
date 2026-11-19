package co.com.usc.imperative.model.mascota.gateways;

import co.com.usc.imperative.model.mascota.Mascota;
import co.com.usc.imperative.model.mascota.MascotaDTO;
import co.com.usc.imperative.model.sexo.Sexo;

import java.util.List;

public interface MascotasGateway {

    List<Mascota> listarMascotas () throws Exception;

    Mascota crearMascota (MascotaDTO mascotaDTO) throws Exception;

    Mascota actualizarMascota (MascotaDTO mascotaDTO) throws Exception;

    Mascota eliminarMascota (Long id) throws Exception;

}
