package com.itgstore.wallet.repository;

import com.itgstore.wallet.domain.Ecriture;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ecriture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EcritureRepository extends JpaRepository<Ecriture, Long>, JpaSpecificationExecutor<Ecriture> {

}
