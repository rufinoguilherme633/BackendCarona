package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.backendFateCarona.dto.RouteCoordinatesDTO;
import com.backendFateCarona.entity.RideManagement;
import com.backendFateCarona.repository.RideManagementRepository;
import com.backendFateCarona.service.HaversineService;

public class HaversineServiceTest {

    @InjectMocks
    private HaversineService haversineService;

    @Mock
    private RideManagementRepository rideRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindNearbyDrivers_ReturnsDrivers() throws Exception {
        // Arrange
        RouteCoordinatesDTO dto = new RouteCoordinatesDTO(
                -23.6021, -46.9186, // Origem (FATEC Cotia)
                -23.5600, -46.6500  // Destino (centro SP)
        );

        RideManagement driverProximo = new RideManagement();
        driverProximo.setLatitudeOrigem(-23.6030);
        driverProximo.setLongitudeOrigem(-46.9190);
        driverProximo.setLatitudeDestino(-23.5590);
        driverProximo.setLongitudeDestino(-46.6510);

        RideManagement driverLonge = new RideManagement();
        driverLonge.setLatitudeOrigem(-22.0000);
        driverLonge.setLongitudeOrigem(-44.0000);
        driverLonge.setLatitudeDestino(-22.0000);
        driverLonge.setLongitudeDestino(-44.0000);

        when(rideRepository.findAll()).thenReturn(List.of(driverProximo, driverLonge));

        // Act
        List<RideManagement> result = haversineService.findNearbyDrivers(dto);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(driverProximo));
        assertFalse(result.contains(driverLonge));
    }

    @Test
    public void testFindNearbyDrivers_ThrowsExceptionWhenNoneNearby() {
        // Arrange
        RouteCoordinatesDTO dto = new RouteCoordinatesDTO(
                -23.6021, -46.9186,
                -23.5600, -46.6500
        );

        RideManagement driverLonge = new RideManagement();
        driverLonge.setLatitudeOrigem(-22.0000);
        driverLonge.setLongitudeOrigem(-44.0000);
        driverLonge.setLatitudeDestino(-22.0000);
        driverLonge.setLongitudeDestino(-44.0000);

        when(rideRepository.findAll()).thenReturn(List.of(driverLonge));

        // Act + Assert
        Exception exception = assertThrows(Exception.class, () -> {
            haversineService.findNearbyDrivers(dto);
        });

        assertEquals("Nenhum motorista proximo", exception.getMessage());
    }
}
