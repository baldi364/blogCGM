package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Byte>
{
    Authority findByDefaultAuthorityTrue();

    boolean existsByAuthorityName(String authorityName);

    long countByDefaultAuthorityTrue();

    Set<Authority> findByVisibleTrueAndAuthorityNameIn(Set<String> authorities);
//ES.   SELECT *
//      FROM authority
//      WHERE visible = true
//      AND authority_name
//      IN('ROLE_MODERATOR', 'ROLE_WRITER')

    Authority findByAuthorityName(String authorityName);
}
