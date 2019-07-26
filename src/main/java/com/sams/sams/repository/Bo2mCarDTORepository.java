package com.sams.sams.repository;

import com.sams.sams.domain.Bo2mCarDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Bo2mCarDTO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Bo2mCarDTORepository extends JpaRepository<Bo2mCarDTO, Long> {

}
