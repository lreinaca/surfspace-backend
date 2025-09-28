package com.eam.surfspace.persistence.repository;

import com.eam.surfspace.domain.dto.SpaceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import com.eam.surfspace.persistence.entity.SpaceEntity;

import java.util.Optional;

public interface SpaceRepository  extends JpaRepository<SpaceEntity, Integer> {

}
