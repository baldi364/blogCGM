package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Reason;
import it.cgmconsulting.myblog.entity.ReasonId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, ReasonId>
{
    boolean existsByReasonIdReason(String reason);

    @Query(value= "SELECT r FROM Reason r WHERE r.endDate IS NULL AND r.reasonId.reason = :reason ")
    Optional<Reason> getValidReason(String reason);

    List<Reason> findByEndDateIsNullOrderBySeverityDesc();
}
