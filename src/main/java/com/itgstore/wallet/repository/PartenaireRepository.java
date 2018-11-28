package com.itgstore.wallet.repository;

import com.itgstore.wallet.domain.Partenaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Partenaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartenaireRepository extends JpaRepository<Partenaire, Long>, JpaSpecificationExecutor<Partenaire> {

}
