package com.alumipro.service;

import com.alumipro.domain.entity.Cliente;
import com.alumipro.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void crearDebeGuardarClienteValido() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente QA");
        cliente.setTelefono("3001234567");
        cliente.setEmail("qa@alumipro.com");
        cliente.setDireccion("Calle 1 # 2-3");

        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> {
            Cliente saved = inv.getArgument(0);
            saved.setId(99);
            return saved;
        });

        Cliente saved = clienteService.crear(cliente);

        assertEquals(99, saved.getId());
        assertEquals("Cliente QA", saved.getNombre());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void crearDebeRechazarEmailInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente QA");
        cliente.setEmail("correo-invalido");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> clienteService.crear(cliente));

        assertEquals("El email no tiene un formato válido.", ex.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void actualizarDebeRechazarTelefonoInvalido() {
        Cliente cambios = new Cliente();
        cambios.setNombre("Cliente Base");
        cambios.setTelefono("12AB");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> clienteService.actualizar(1, cambios));

        assertEquals("El teléfono debe ser numérico y tener mínimo 7 dígitos.", ex.getMessage());
        verify(clienteRepository, never()).findById(any());
        verify(clienteRepository, never()).save(any());
    }
}
