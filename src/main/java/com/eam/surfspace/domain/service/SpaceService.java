package com.eam.surfspace.domain.service;

import com.eam.surfspace.domain.dto.SpaceDTO;

import java.util.List;
import java.util.Optional;

public interface SpaceService {

    SpaceDTO createSpace(SpaceDTO spaceDTO);

    List<SpaceDTO> getAllSpaces();

    SpaceDTO getSpaceById(Integer idSpace);

    SpaceDTO updateSpace(Integer idSpace, SpaceDTO spaceDTO);

    void deactivateSpace(Integer idSpace);

}
