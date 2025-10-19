package com.eam.surfspace.domain;

import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.dto.PaymentDTO;
import com.eam.surfspace.domain.service.impl.PaymentServiceImpl;
import com.eam.surfspace.persistence.dao.PaymentDAO;
import com.eam.surfspace.persistence.entity.EnumPaymentMethod;
import com.eam.surfspace.persistence.entity.EnumPaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Payment Service Test")

public class PaymentServiceTest {

    //mocks que simulan comportamientos de dependencias reales
    @Mock
    private PaymentDAO paymentDAO;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    //datos de prueba
    private PaymentDTO validPaymentDTO;
    private BookingResponseDTO validBookingDTO;
    private Integer validBookingId;

    @BeforeEach
    void setUp() {
        validBookingId = 10;

        //simulamos una reserva válida
        validBookingDTO = new BookingResponseDTO();
        validBookingDTO.setBookingId(validBookingId);

        //creamos un PaymentDTO válido
        validPaymentDTO = new PaymentDTO();
        validPaymentDTO.setPayId(1);
        validPaymentDTO.setAmount(150.0);
        validPaymentDTO.setMethod(EnumPaymentMethod.TARJETA_DE_CREDITO);
        validPaymentDTO.setDate(LocalDateTime.now());
        validPaymentDTO.setStatus(EnumPaymentStatus.APROBADO);
        validPaymentDTO.setBooking(validBookingDTO);
    }

    // TEST DE CREAR PAGOS ===================================================================================
    @Test
    @DisplayName("CREATE - Valid payment should be saved successfully")
    void savePayment_ValidData_ShouldReturnCreatedPayment() {
        //ARRANGE ----------------------------------------------------------
        when(paymentDAO.save(validPaymentDTO)).thenReturn(validPaymentDTO);

        //ACT --------------------------------------------------------------
        PaymentDTO result = paymentService.savePayment(validPaymentDTO);

        //ASSERT -----------------------------------------------------------
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(150.0);
        verify(paymentDAO, times(1)).save(validPaymentDTO);
    }

    //validaciones
    @Test
    @DisplayName("CREATE - Invalid amount should throw IllegalArgumentException")
    void savePayment_InvalidAmount_ShouldThrowException() {
        //ARRANGE ----------------------------------------------------------
        validPaymentDTO.setAmount(0.0); // monto inválido

        //ACT & ASSERT -----------------------------------------------------
        assertThatThrownBy(() -> paymentService.savePayment(validPaymentDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El monto debe ser mayor a cero");

        verify(paymentDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Null payment method should throw IllegalArgumentException")
    void savePayment_NullMethod_ShouldThrowException() {
        //ARRANGE ----------------------------------------------------------
        validPaymentDTO.setMethod(null);

        //ACT & ASSERT -----------------------------------------------------
        assertThatThrownBy(() -> paymentService.savePayment(validPaymentDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El método de pago no puede ser nulo");

        verify(paymentDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Null date should throw IllegalArgumentException")
    void savePayment_NullDate_ShouldThrowException() {
        //ARRANGE ----------------------------------------------------------
        validPaymentDTO.setDate(null);

        //ACT & ASSERT -----------------------------------------------------
        assertThatThrownBy(() -> paymentService.savePayment(validPaymentDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La fecha de pago no puede ser nula");

        verify(paymentDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Null status should throw IllegalArgumentException")
    void savePayment_NullStatus_ShouldThrowException() {
        //ARRANGE ----------------------------------------------------------
        validPaymentDTO.setStatus(null);

        //ACT & ASSERT -----------------------------------------------------
        assertThatThrownBy(() -> paymentService.savePayment(validPaymentDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El estado no puede ser nulo");

        verify(paymentDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Null booking should throw IllegalArgumentException")
    void savePayment_NullBooking_ShouldThrowException() {
        //ARRANGE ----------------------------------------------------------
        validPaymentDTO.setBooking(null);

        //ACT & ASSERT -----------------------------------------------------
        assertThatThrownBy(() -> paymentService.savePayment(validPaymentDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La reserva asociada a pago no puede ser nula");

        verify(paymentDAO, never()).save(any());
    }

    // TEST DE TRAER PAGOS ==================================================================================
    @Test
    @DisplayName("READ ALL - Should return list of payments")
    void getAllPayment_ShouldReturnListOfPayments() {
        //ARRANGE -------------------------------------------------------------------------
        List<PaymentDTO> payments = Arrays.asList(
                new PaymentDTO(1, validBookingDTO, EnumPaymentMethod.TARJETA_DE_CREDITO, 80000.0, LocalDateTime.now(), EnumPaymentStatus.APROBADO)
        );

        when(paymentDAO.findAll()).thenReturn(payments);

        //ACT -----------------------------------------------------------------------------
        List<PaymentDTO> result = paymentService.getAllPayment();

        //ASSERT --------------------------------------------------------------------------
        assertThat(result).hasSize(1);
        verify(paymentDAO, times(1)).findAll();
    }

    // TEST DE TRAER DE OBTENER PAGO POR EL ID DE UNA RESERVA ===============================================
    @Test
    @DisplayName("READ BY BOOKING - Existing booking should return payment")
    void getPaymentByIdBooking_ExistingBooking_ShouldReturnPayment() {
        //ARRANGE ----------------------------------------------------------------------------
        when(paymentDAO.findByBooking(validBookingId)).thenReturn(Optional.of(validPaymentDTO));

        //ACT --------------------------------------------------------------------------------
        PaymentDTO result = paymentService.getPaymentByIdBooking(validBookingId);

        //ASSERT -----------------------------------------------------------------------------
        assertThat(result).isNotNull();
        assertThat(result.getBooking().getBookingId()).isEqualTo(validBookingId);
        verify(paymentDAO, times(1)).findByBooking(validBookingId);
    }

    @Test
    @DisplayName("READ BY BOOKING - Nonexistent booking should throw RuntimeException")
    void getPaymentByIdBooking_NonExistent_ShouldThrowException() {
        //ARRANGE -------------------------------------------------------------------
        Integer invalidBookingId = 999;
        when(paymentDAO.findByBooking(invalidBookingId)).thenReturn(Optional.empty());

        //ACT & ASSERT --------------------------------------------------------------
        assertThatThrownBy(() -> paymentService.getPaymentByIdBooking(invalidBookingId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Producto no encontrado con ID: " + invalidBookingId);

        verify(paymentDAO, times(1)).findByBooking(invalidBookingId);
    }

}
