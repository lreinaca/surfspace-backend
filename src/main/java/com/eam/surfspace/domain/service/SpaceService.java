package com.eam.surfspace.domain.service;

import com.eam.surfspace.domain.dto.SpaceDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface SpaceService {

    SpaceDTO createSpace(SpaceDTO spaceDTO);

    List<SpaceDTO> getAllSpaces();

    SpaceDTO getSpaceById(Integer idSpace);

    SpaceDTO updateSpace(Integer idSpace, SpaceDTO spaceDTO);

    void deactivateSpace(Integer idSpace);

    boolean isSpaceAvailable(int idSpace, LocalDateTime requestedStart, LocalDateTime requestedEnd);

}
