package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.dto.AddressDTO;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing PostalAddress.
 */
@RestController
@RequestMapping("/api")
public class PostalAddressResource {

    private static final String ENTITY_NAME = "postalAddress";
    private final Logger log = LoggerFactory.getLogger(PostalAddressResource.class);
    private final PostalAddressService postalAddressService;

    public PostalAddressResource(PostalAddressService postalAddressService) {
        this.postalAddressService = postalAddressService;
    }

    /**
     * POST  /postal-addresses : Create a new postalAddress.
     *
     * @param postalAddressDTO the postalAddressDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new postalAddressDTO,
     * or with status 400 (Bad Request) if the postalAddress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/postal-addresses")
    @Timed
    public ResponseEntity<AddressDTO> createPostalAddress(
        @Valid @RequestBody AddressDTO postalAddressDTO) throws URISyntaxException {
        log.debug("REST request to save PostalAddress : {}", postalAddressDTO);
        if (postalAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new postalAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AddressDTO result = postalAddressService.save(postalAddressDTO);
        return ResponseEntity.created(new URI("/api/postal-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /postal-addresses : Updates an existing postalAddress.
     *
     * @param postalAddressDTO the postalAddressDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated postalAddressDTO,
     * or with status 400 (Bad Request) if the postalAddressDTO is not valid,
     * or with status 500 (Internal Server Error) if the postalAddressDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/postal-addresses")
    @Timed
    public ResponseEntity<AddressDTO> updatePostalAddress(
        @Valid @RequestBody AddressDTO postalAddressDTO) throws URISyntaxException {
        log.debug("REST request to update PostalAddress : {}", postalAddressDTO);
        if (postalAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AddressDTO result = postalAddressService.save(postalAddressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postalAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /postal-addresses : get all the postalAddresses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of postalAddresses in body
     */
    @GetMapping("/postal-addresses")
    @Timed
    public ResponseEntity<List<AddressDTO>> getAllPostalAddresses(Pageable pageable) {
        log.debug("REST request to get all PostalAddresses");
        Page<AddressDTO> page = postalAddressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/postal-addresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /postal-addresses/:id : get the "id" postalAddress.
     *
     * @param id the id of the postalAddressDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the postalAddressDTO, or with status 404 (Not Found)
     */
    @GetMapping("/postal-addresses/{id}")
    @Timed
    public ResponseEntity<AddressDTO> getPostalAddress(@PathVariable UUID id) {
        log.debug("REST request to get PostalAddress : {}", id);
        Optional<AddressDTO> postalAddressDTO = postalAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postalAddressDTO);
    }

    /**
     * DELETE  /postal-addresses/:id : delete the "id" postalAddress.
     *
     * @param id the id of the postalAddressDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/postal-addresses/{id}")
    @Timed
    public ResponseEntity<Void> deletePostalAddress(@PathVariable UUID id) {
        log.debug("REST request to delete PostalAddress : {}", id);
        postalAddressService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
