package com.eam.surfspace.persistence.entity;

public enum EnumSpaceStatus {
    
    AVAILABLE, //disponible
    RESERVED, //no está disponible
    MAINTENANCE, //mantenimiento
    IDLE // desactivado -> "eliminado"
}
