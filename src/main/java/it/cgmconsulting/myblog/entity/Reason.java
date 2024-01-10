package it.cgmconsulting.myblog.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Reason
{
    /*
    CREATE TABLE IF NOT EXISTS reason(
reason VARCHAR(50) NOT NULL,
start_date DATE NOT NULL,
end_date DATE,
severity INT NOT NULL,
PRIMARY KEY(reason, start_date),
UNIQUE KEY (reason)
);
     */
    @EmbeddedId
    private ReasonId reasonId;

    private LocalDate endDate;

    private int severity; // gravità -> numero di giorni di ban associati alla reason

    public Reason(ReasonId reasonId, int severity)
    {
        this.reasonId = reasonId;
        this.severity = severity;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
}
