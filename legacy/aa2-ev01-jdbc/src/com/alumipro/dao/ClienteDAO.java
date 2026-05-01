/* archivo java clientedao */
package com.alumipro.dao;

import com.alumipro.model.Cliente;
import java.util.List;

public interface ClienteDAO {

    void insertar(Cliente cliente);
    void actualizar(Cliente cliente);
    void eliminar(int id);
    Cliente obtenerPorId(int id);
    List<Cliente> listar();
}
