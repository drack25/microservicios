package com.proyecto.microservicioclientes.message;

import com.proyecto.microservicioclientes.entities.Cliente;
import com.proyecto.microservicioclientes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteRcp {

    private final ClienteService clienteService;

    @RabbitListener(queues = "cliente_rpc_queue")
    public Object handleRpcRequest(Map<String, Object> request) {
        log.info("Solicitud RPC recibida: {}", request);

        String action = (String) request.get("action");
        Long clienteId = ((Integer) request.get("clienteId")).longValue();

        switch (action) {
            case "VERIFICAR_CLIENTE":
                return clienteService.existeCliente(clienteId);
            case "OBTENER_NOMBRE_CLIENTE":
                return clienteService.obtenerNombreCliente(clienteId);
            default:
                log.error("Acción no reconocida: {}", action);
                return null;
        }
    }
}
