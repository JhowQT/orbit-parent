package com.orbitbook.aiservice.ai;

import com.orbitbook.aiservice.dto.DestinationDTO;
import com.orbitbook.aiservice.feign.BookingClient;
import com.orbitbook.aiservice.feign.BookingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DestinationTools {

    private final BookingClient bookingClient;

    @Tool(description = "Lista todos os destinos espaciais disponíveis na plataforma OrbitBook com nome, descrição, preço base e capacidade.")
    public List<DestinationDTO> searchAllDestinations() {
        return bookingClient.getAllDestinations();
    }

    @Tool(description = "Filtra destinos espaciais cujo preço base seja menor ou igual ao orçamento máximo informado em reais.")
    public List<DestinationDTO> filterDestinationsByBudget(
            BigDecimal maxBudget) {

        return bookingClient.getAllDestinations()
                .stream()
                .filter(d ->
                        d.getBasePrice() != null
                        && d.getBasePrice()
                                .compareTo(maxBudget) <= 0
                )
                .toList();
    }

    @Tool(description = "Retorna o histórico de reservas anteriores de um usuário, incluindo destinos visitados, datas e status das reservas.")
    public List<BookingResponseDTO> getUserBookingHistory(
            Long userId) {

        return bookingClient.getBookingsByUser(userId);
    }
}
