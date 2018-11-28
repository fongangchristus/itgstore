package com.itgstore.wallet.repository;

import com.itgstore.wallet.domain.Device;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Device entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {

}
