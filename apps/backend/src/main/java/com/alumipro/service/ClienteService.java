/* archivo que contiene la lógica de cliente */
package com.alumipro.service;

import com.alumipro.domain.entity.Cliente;
import com.alumipro.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + id));
    }

    @Transactional
    public Cliente crear(Cliente cliente) {
        validar(cliente);
        // FIX: sanear campos antes de guardar — recortar espacios, normalizar nulos
        sanitizar(cliente);
        cliente.setId(null);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente actualizar(Integer id, Cliente cliente) {
        validar(cliente);
        sanitizar(cliente);

        Cliente existente = obtenerPorId(id);
        existente.setNombre(cliente.getNombre());
        existente.setEmail(cliente.getEmail());
        existente.setTelefono(cliente.getTelefono());
        existente.setDireccion(cliente.getDireccion());

        return clienteRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        Cliente existente = obtenerPorId(id);
        clienteRepository.delete(existente);
    }

    /**
     * Sanea los campos antes de persistir: recorta espacios y normaliza cadenas vacías a null
     * para columnas que son NULL en la BD (telefono, direccion, email).
     */
    private void sanitizar(Cliente c) {
        c.setNombre(c.getNombre().trim());

        if (c.getEmail() != null) {
            c.setEmail(c.getEmail().trim().isEmpty() ? null : c.getEmail().trim().toLowerCase());
        }
        if (c.getTelefono() != null) {
            c.setTelefono(c.getTelefono().trim().isEmpty() ? null : c.getTelefono().trim());
        }
        if (c.getDireccion() != null) {
            c.setDireccion(c.getDireccion().trim().isEmpty() ? null : c.getDireccion().trim());
        }
    }

    private void validar(Cliente c) {
        if (c.getNombre() == null || c.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (c.getNombre().trim().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar 100 caracteres.");
        }

        if (c.getEmail() != null && !c.getEmail().trim().isEmpty()) {
            String email = c.getEmail().trim();
            if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
                throw new IllegalArgumentException("El email no tiene un formato válido.");
            }
            if (email.length() > 100) {
                throw new IllegalArgumentException("El email no puede superar 100 caracteres.");
            }
        }

        if (c.getTelefono() != null && !c.getTelefono().trim().isEmpty()) {
            String tel = c.getTelefono().trim();
            if (!tel.matches("\\d+") || tel.length() < 7) {
                throw new IllegalArgumentException("El teléfono debe ser numérico y tener mínimo 7 dígitos.");
            }
            if (tel.length() > 20) {
                throw new IllegalArgumentException("El teléfono no puede superar 20 dígitos.");
            }
        }

        if (c.getDireccion() != null && c.getDireccion().length() > 160) {
            throw new IllegalArgumentException("La dirección supera el límite de 160 caracteres.");
        }
    }
}
