package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import it.cgmconsulting.myblog.entity.common.ReportingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Reporting extends CreationUpdate
{
    /*
    CREATE TABLE IF NOT EXISTS reporting(
comment_id INT PRIMARY KEY NOT NULL,
reason VARCHAR(50) NOT NULL,
start_date DATE NOT NULL,
user_id INT NOT NULL,
created_at DATETIME NOT NULL,
updated_at DATETIME NOT NULL,
note VARCHAR(50),
`status` ENUM('OPEN', 'IN_PROGRESS', 'CLOSED_WITH_BAN', 'CLOSED_WITHOUT_BAN') NOT NULL,
FOREIGN KEY (comment_id) REFERENCES `comment`(id),
FOREIGN KEY (reason, start_date) REFERENCES reason(reason, start_date),
FOREIGN KEY (user_id) REFERENCES `user`(id)
);
     */

    @EmbeddedId
    private ReportingId reportingId;

    @ManyToOne
    @JoinColumns({
            //          nome nella tabella                nome nella classe
            @JoinColumn(name = "reason", referencedColumnName = "reason", nullable = false),
            @JoinColumn(name = "start_date", referencedColumnName = "startDate", nullable = false)
    })
    private Reason reason;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // colui che segnala un commento

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 18)
    private ReportingStatus status = ReportingStatus.OPEN;

    private String note;  // ad uso e consumo del moderatore

    public Reporting(ReportingId reportingId, Reason reason, User user)
    {
        this.reportingId = reportingId;
        this.reason = reason;
        this.user = user;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reporting reporting = (Reporting) o;
        return Objects.equals(reportingId, reporting.reportingId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(reportingId);
    }
}
