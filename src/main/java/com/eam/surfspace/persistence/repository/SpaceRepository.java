package com.eam.surfspace.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eam.surfspace.persistence.entity.SpaceEntity;

public interface SpaceRepository  extends JpaRepository<SpaceEntity, Integer> {

}
