package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.entity.common.ReportingStatus;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.ReportingCloseRequest;
import it.cgmconsulting.myblog.payload.request.ReportingRequest;
import it.cgmconsulting.myblog.payload.request.ReportingUpdateRequest;
import it.cgmconsulting.myblog.payload.response.ReportingCheckBannedUserResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import it.cgmconsulting.myblog.repository.ReportingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportingService
{
    private final ReportingRepository reportingRepository;
    private final CommentRepository commentRepository;
    private final ReasonService reasonService;

    public ResponseEntity<?> createReporting(ReportingRequest request, UserDetails userDetails)
    {
        // prima di salvare una segnalazione, verificare che non esista un'altra segnalazione per quel commento
        User user = (User) userDetails;
        Comment c = getCommentById(request.getComment());
        if(user.getId() == c.getAuthor().getId())
        {
            return new ResponseEntity<>("You cannot report yourself", HttpStatus.BAD_REQUEST);
        }

        //devo istanziare la pk di reportingId
        ReportingId reportingId = new ReportingId(c);

        //verifico se esiste già un reporting per quel commento
        if(reportingRepository.existsById(reportingId))
        {
            return new ResponseEntity<>("Reporting for comment " + c.getId() + " already present", HttpStatus.BAD_REQUEST);
        }

        //creazione della segnalazione
        Reason reason = reasonService.getValidReason(request.getReason());

        //Ho tutto quello che mi serve per istanziare un oggetto di tipo Reporting
        Reporting rep = new Reporting(reportingId, reason, (User) userDetails);

        //Ora possiamo salvare la nostra segnalazione
        reportingRepository.save(rep);
        return new ResponseEntity<>("Comment " + rep.getReportingId().getComment().getId() + " has been reported", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updateReporting(ReportingUpdateRequest request)
    {
        //Bisogna recuperare la nostra segnalazione tramite l'id del commento
        Comment c = getCommentById(request.getComment());

        //cerco la segnalazione per PK, ovvero ReasonId
        Reporting rep = getReportingById(new ReportingId(c));

        //controllo in caso di cambio segnalazione rispetto a quella già presente
        if(request.getReason() != null && !request.getReason().isBlank() && !rep.getReason().getReasonId().getReason().equals(request.getReason().toLowerCase()))
        {
            Reason reason = reasonService.getValidReason(request.getReason());
            rep.setReason(reason);
        }
        rep.setNote(request.getNote());
        rep.setStatus(ReportingStatus.IN_PROGRESS);

        return new ResponseEntity<>("Reporting is now in progress status", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> closeReporting(ReportingCloseRequest request)
    {
        /*
            Su reporting far settare lo status:
            IN_PROGESS -> CLOSED_WITH_BAN oppure CLOSED_WITHOUT_BAN

            Se chiudo con CLOSED_WITH_BAN
            - devo censurare il commento
            - disabilitare l'utente

            Che la segnalazione sia chiusa con o senza ban:
            - aggiornarla col nuovo status
            - salvare il tutto
         */

        //Verifichiamo che venga passato lo status corretto
        if(!ReportingStatus.valueOf(request.getStatus()).equals(ReportingStatus.CLOSED_WITH_BAN) &&
                !ReportingStatus.valueOf(request.getStatus()).equals(ReportingStatus.CLOSED_WITHOUT_BAN))
        {
            return new ResponseEntity("Incorrect status", HttpStatus.BAD_REQUEST);
        }

            //Recupero la segnalazione
        Comment c = getCommentById(request.getCommentId());
        Reporting rep = getReportingById(new ReportingId(c));

        //Dobbiamo verificare qual è lo status
        if(ReportingStatus.valueOf(request.getStatus()).equals(ReportingStatus.CLOSED_WITH_BAN))
        {
            //settare il commento a true per la censura e disabilito l'utente
            c.setCensored(true);
            c.getAuthor().setEnabled(false);
        }

        //settare lo status che mi arriva dalla request
        rep.setStatus(ReportingStatus.valueOf(request.getStatus()));
        rep.setNote(request.getNote());

        return new ResponseEntity<>("Reporting closed", HttpStatus.OK);
    }

    public LocalDateTime checkUser(User user)
    {
        // - verificare tra tutti i reporting chiusi con ban
        //   se lo user, autore del commento, esiste
        List<ReportingCheckBannedUserResponse> list = reportingRepository.checkBannedUser(user.getId(), ReportingStatus.CLOSED_WITH_BAN);
        LocalDateTime createdAtOlder = null;
        LocalDateTime bannedUntil;
        int severities = 0;

        for (ReportingCheckBannedUserResponse r : list)
        {
            bannedUntil = r.getCreatedAt().plusDays(r.getSeverity());
            if(bannedUntil.isAfter(LocalDateTime.now()))
            {
                severities += r.getSeverity();
                if(createdAtOlder == null ||createdAtOlder.isAfter(r.getCreatedAt()))
                {
                    createdAtOlder = r.getCreatedAt();
                }
            }
        }

        if (createdAtOlder != null)
        {
            createdAtOlder = createdAtOlder.plusDays(severities);
        }
        return createdAtOlder;

        // - se sì, recuperare la severity della reason per la quale è stato bannato

        // - sommare la severity alla data di creazione della segnalazione

        // - se il ban è scaduto, riabilitare l'utente
        // - altrimenti avvisarlo con un messaggio. es -> "sei bannato fino al.."
    }

    protected Reporting getReportingById(ReportingId id)
    {
        return reportingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReportingId", "comment", id));
    }

    protected Comment getCommentById(int id)
    {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "comment", id));
    }
}
