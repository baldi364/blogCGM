package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Reporting;
import it.cgmconsulting.myblog.entity.ReportingId;
import it.cgmconsulting.myblog.entity.common.ReportingStatus;
import it.cgmconsulting.myblog.payload.response.ReportingCheckBannedUserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportingRepository extends JpaRepository<Reporting, ReportingId>
{
    /*
    SELECT u.id, u.username, r.created_at, rs.severity, rs.reason, rs.start_date, rs.end_date
    FROM reporting r
    INNER JOIN reason rs ON (rs.reason = r.reason AND (CURRENT_TIMESTAMP BETWEEN rs.start_date AND rs.end_date OR(rs.start_date <= CURRENT_TIMESTAMP AND rs.end_date IS null )))
    INNER JOIN comment c ON c.id = r.comment_id
    INNER JOIN user_ u ON u.id = c.author
    WHEREu.id=9
     */

    @Query(value=
            "SELECT new it.cgmconsulting.myblog.payload.response.ReportingCheckBannedUserResponse(" +
            "r.createdAt, " +
            "r.reason.severity" +
            ") FROM Reporting r " +
            "WHERE " +
            "r.status = :status " +
            "AND r.reportingId.comment.author.id = :userId " +
            "ORDER BY r.createdAt") /* +
            "AND ((:now BETWEEN r.reason.reasonId.startDate AND r.reason.endDate) " +
            "OR (r.reason.reasonId.startDate <= :now AND  r.reason.endDate IS NULL))")
            */

    List<ReportingCheckBannedUserResponse> checkBannedUser(int userId, ReportingStatus status);
}
