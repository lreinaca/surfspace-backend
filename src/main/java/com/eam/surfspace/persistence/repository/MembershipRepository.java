package com.eam.surfspace.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.eam.surfspace.persistence.entity.MembershipEntity;

@Repository
public interface MembershipRepository extends JpaRepository<MembershipEntity, Integer> {

}