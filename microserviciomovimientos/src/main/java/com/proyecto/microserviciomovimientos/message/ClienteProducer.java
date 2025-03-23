package com.proyecto.microserviciomovimientos.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.cliente}")
    private String clienteRoutingKey;

    /**
     * Verifica si un cliente existe en el sistema.
     *
     * @param clienteId El ID del cliente a verificar.
     * @return true si el cliente existe, false en caso contrario.
     */
    public boolean verificarCliente(Long clienteId) {
        log.info("Verificando existencia del cliente con ID: {}", clienteId);

        try {
            Map<String, Object> message = new HashMap<>();
            message.put("action", "VERIFICAR_CLIENTE");
            message.put("clienteId", clienteId);

            // Enviar el mensaje RPC con la routing key "cliente.rpc.key"
            Boolean respuesta = (Boolean) rabbitTemplate.convertSendAndReceive(
                    exchange, // Nombre del exchange
                    "cliente.rpc.key",             // Routing key para RPC
                    message
            );

            if (respuesta != null) {
                return respuesta;
            } else {
                log.error("No se recibió respuesta para la verificación del cliente ID: {}", clienteId);
                return false; // Asumimos que el cliente no existe si no hay respuesta
            }
        } catch (Exception e) {
            log.error("Error al verificar cliente: {}", e.getMessage());
            return false; // En caso de error, asumimos que el cliente no existe
        }
    }

    /**
     * Obtiene el nombre de un cliente.
     *
     * @param clienteId El ID del cliente.
     * @return El nombre del cliente o un valor por defecto si no se encuentra.
     */
    public String obtenerNombreCliente(Long clienteId) {
        log.info("Obteniendo nombre del cliente con ID: {}", clienteId);

        try {
            // Crear el mensaje
            Map<String, Object> message = new HashMap<>();
            message.put("action", "OBTENER_NOMBRE_CLIENTE");
            message.put("clienteId", clienteId);

            // Enviar el mensaje y esperar una respuesta
            String respuesta = (String) rabbitTemplate.convertSendAndReceive(exchange, clienteRoutingKey, message);

            if (respuesta != null) {
                return respuesta;
            } else {
                log.error("No se recibió respuesta para el nombre del cliente ID: {}", clienteId);
                return "Cliente " + clienteId; // Valor por defecto si no hay respuesta
            }
        } catch (Exception e) {
            log.error("Error al obtener nombre del cliente: {}", e.getMessage());
            return "Cliente " + clienteId; // Valor por defecto en caso de error
        }
    }
}
