package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

/**
 * A DTO for the LocationMatch entity.
 */
@Data
public class LocationMatchDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID location;

    private UUID matchingLocation;

    private UUID orgId;

    private String organizationName;

    private String locationName;

    private Double similarity;
}
