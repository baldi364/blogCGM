package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Reason;
import it.cgmconsulting.myblog.entity.ReasonId;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.ReasonRequest;
import it.cgmconsulting.myblog.repository.ReasonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReasonService
{
    private final ReasonRepository reasonRepository;
    private final UtilsService utilsService;

    public ResponseEntity<?> addReason(ReasonRequest request)
    {
        String reasonName = request.getReason().toLowerCase();
        if(reasonRepository.existsByReasonIdReason(reasonName))
        {
            return new ResponseEntity<>("Reason already present", HttpStatus.BAD_REQUEST);
        }

        Reason reason = new Reason(new ReasonId(reasonName, request.getStartDate()), request.getSeverity());
        reasonRepository.save(reason);
        return new ResponseEntity<>("Reason successfully saved", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updateReason(ReasonRequest request)
    {
        String reasonName = request.getReason().toLowerCase();
        //recupero la reason
        Reason oldReason= getValidReason(reasonName);

        // istanzio la nuova reason
        Reason newReason= new Reason(new ReasonId(reasonName, request.getStartDate()), request.getSeverity());
        if(oldReason.getSeverity() == newReason.getSeverity())
        {
            return new ResponseEntity<>("Same severity", HttpStatus.BAD_REQUEST);
        }
        oldReason.setEndDate(request.getStartDate().minusDays(1));
        // salvo il tutto
        reasonRepository.save(newReason);
        return new ResponseEntity<>("Reason updated", HttpStatus.OK);
    }

    public ResponseEntity<?> getValidReasons()
    {
        List<Reason> reasons = reasonRepository.findByEndDateIsNullOrderBySeverityDesc();
        List<String> validReasons = reasons.stream().map(r -> r.getReasonId().getReason()).collect(Collectors.toList());

        /*
        forEach metodo
        List<String> finalValidReasons = validReasons;
        reasons.forEach(r -> finalValidReasons.add(r.getReasonId().getReason()));

        //for semplificato
        for(Reason r : reasons)
        {
            validReasons.add(r.getReasonId().getReason());
        }
        */

        utilsService.isEmptyCollection(validReasons, "reasons");
        return new ResponseEntity<>(validReasons, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> removeReason(String reason)
    {
        Reason r = getValidReason(reason);
        r.setEndDate(LocalDate.now());
        return new ResponseEntity<>("Reason " + reason + " no more valid", HttpStatus.OK);
    }

    protected Reason getValidReason(String reason)
    {
        return reasonRepository.getValidReason(reason)
                .orElseThrow(() -> new ResourceNotFoundException("Reason", "id", reason));
    }
}
