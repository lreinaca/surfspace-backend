package com.eam.surfspace.persistence.mapper;

import com.eam.surfspace.domain.dto.PaymentDTO;
import com.eam.surfspace.persistence.entity.EnumPaymentStatus;
import com.eam.surfspace.persistence.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import org.mapstruct.*;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN, //avisa si olvido mapear algo
        uses = {BookingMapper.class}
)
public interface PaymentMapper {

    //Entity -> DTO
    @Mapping(source = "status", target = "status", qualifiedByName = "enumToString")
    PaymentDTO toPaymentDTO(PaymentEntity payment);

    //DTO -> Entity
    @InheritInverseConfiguration(name = "toPaymentDTO")
    PaymentEntity toPaymentEntity(PaymentDTO paymentDTO);

    List<PaymentDTO> toPaymentDTOList(List<PaymentEntity> paymentEntities);

    @InheritInverseConfiguration(name = "toPaymentDTOList")
    List<PaymentEntity> toPaymentEntityList(List<PaymentDTO> paymentDTOS);

    //Enum 'Status' a String
    @Named("enumToString")
    static String enumToStringStatus(EnumPaymentStatus enumStatus){
        return enumStatus != null ? enumStatus.name() : null;
    }

    @InheritInverseConfiguration(name = "enumToStringStatus")
    static EnumPaymentStatus stringToEnumStatus(String status){
        return status != null ? EnumPaymentStatus.valueOf(status.toUpperCase()) : null;
    }

}
