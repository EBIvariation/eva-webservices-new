package uk.ac.ebi.eva.ws.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.eva.ws.dto.RsIdListResponse;
import uk.ac.ebi.eva.ws.dto.RsIdStudySamplesEntry;
import uk.ac.ebi.eva.ws.service.EVAWSService;

import java.util.List;

@RestController
@RequestMapping("/v3")
@Validated
public class EVAWSController {

    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int MAX_PAGE_SIZE = 1000;

    private final EVAWSService evawsService;

    public EVAWSController(EVAWSService evawsService) {
        this.evawsService = evawsService;
    }

    @GetMapping("/rsids")
    public ResponseEntity<RsIdListResponse> getRsIds(@RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE)
            @Min(1) @Max(MAX_PAGE_SIZE) int pageSize, @RequestParam(required = false) Long searchAfter) {

        return ResponseEntity.ok(evawsService.getRsIds(pageSize, searchAfter));
    }

    @GetMapping("/rsids/{rsId}/studies/{studyId}/samples")
    public ResponseEntity<List<RsIdStudySamplesEntry>> getSamplesForRSIDAndStudyID(@PathVariable long rsId,
                                                                  @PathVariable String studyId) {

        return ResponseEntity.ok(evawsService.getSamplesForRSIDAndStudyID(rsId, studyId));
    }


}
