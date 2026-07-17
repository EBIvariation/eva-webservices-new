package uk.ac.ebi.eva.ws.dto;

import java.util.List;

public record RsIdListResponse(
        List<Long> rsIds,
        Long nextPageAfter,
        int pageSize
) {
}

