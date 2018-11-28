package com.itgstore.wallet.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.itgstore.wallet.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Compte.class.getName(), jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Compte.class.getName() + ".ecritureComptes", jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Client.class.getName(), jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Client.class.getName() + ".factureClientRecepteurs", jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Client.class.getName() + ".devicePlientProprietaires", jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Client.class.getName() + ".operationClientEmetteurs", jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Facture.class.getName(), jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Partenaire.class.getName(), jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Partenaire.class.getName() + ".facturePartenaireEmetteurs", jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Device.class.getName(), jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Device.class.getName() + ".factureDevicePayeurs", jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Device.class.getName() + ".operationDeviceEmetteurs", jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Agence.class.getName(), jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Agence.class.getName() + ".operationAgenceEmeteurs", jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Agence.class.getName() + ".operationAgencePayeurs", jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Ecriture.class.getName(), jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Transaction.class.getName(), jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Transaction.class.getName() + ".ecritureTransactions", jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Operation.class.getName(), jcacheConfiguration);
            cm.createCache(com.itgstore.wallet.domain.Operation.class.getName() + ".transactionOperations", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
