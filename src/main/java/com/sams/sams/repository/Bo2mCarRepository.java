package com.sams.sams.repository;

import com.sams.sams.domain.Bo2mCar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Bo2mCar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Bo2mCarRepository extends JpaRepository<Bo2mCar, Long> {

}
