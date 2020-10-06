package org.benetech.servicenet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private Date nextFireDate;

    private Date prevFireDate;

    private String state;

    private UUID lastReportId;
}
