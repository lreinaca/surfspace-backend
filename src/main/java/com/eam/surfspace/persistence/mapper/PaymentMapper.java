package com.eam.surfspace.persistence.mapper;

import com.eam.surfspace.domain.dto.PaymentDTO;
import com.eam.surfspace.persistence.entity.EnumPaymentMethod;
import com.eam.surfspace.persistence.entity.EnumPaymentStatus;
import com.eam.surfspace.persistence.entity.PaymentEntity;
import org.mapstruct.*;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN, //avisa si olvido mapear algo
        uses = {BookingMapper.class}
)
public interface PaymentMapper {

    //Entity -> DTO
    @Mapping(source = "status", target = "status", qualifiedByName = "enumStatusToString")
    @Mapping(source= "method", target= "method", qualifiedByName = "enumMethodToString")
    PaymentDTO toPaymentDTO(PaymentEntity payment);

    //DTO -> Entity
    @Mapping(source = "status", target = "status", qualifiedByName = "stringToEnumStatus")
    @Mapping(source= "method", target= "method", qualifiedByName = "stringToEnumMethod")
    PaymentEntity toPaymentEntity(PaymentDTO paymentDTO);

    List<PaymentDTO> toPaymentDTOList(List<PaymentEntity> paymentEntities);

    List<PaymentEntity> toPaymentEntityList(List<PaymentDTO> paymentDTOS);

    // Enum 'Status' <-> String
    @Named("enumStatusToString")
    static String enumStatusToString(EnumPaymentStatus enumStatus) {
        return enumStatus != null ? enumStatus.name() : null;
    }

    @Named("stringToEnumStatus")
    static EnumPaymentStatus stringToEnumStatus(String status) {
        return status != null ? EnumPaymentStatus.valueOf(status.toUpperCase()) : null;
    }

    // Enum 'Method' <-> String
    @Named("enumMethodToString")
    static String enumMethodToString(EnumPaymentMethod enumMethod) {
        return enumMethod != null ? enumMethod.name() : null;
    }

    @Named("stringToEnumMethod")
    static EnumPaymentMethod stringToEnumMethod(String method) {
        return method != null ? EnumPaymentMethod.valueOf(method.toUpperCase()) : null;
    }
}
