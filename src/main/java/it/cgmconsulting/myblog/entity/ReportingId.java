package it.cgmconsulting.myblog.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ReportingId implements Serializable
{
    @OneToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
}
