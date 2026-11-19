package co.com.usc.imperative.model.especie.gateways;

import co.com.usc.imperative.model.especie.Especie;

import java.util.List;

public interface EspecieGateway {
    List<Especie> listarEspecies() throws Exception;
}
