package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "phone")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Phone extends AbstractEntity implements Serializable, DeepComparable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "jhi_number", nullable = false)
    @Size(max = 255, message = "Field value is too long.")
    private String number = "";

    @Column(name = "extension")
    private Integer extension;

    @Column(name = "jhi_type")
    @Size(max = 255, message = "Field value is too long.")
    private String type;

    @Column(name = "language")
    @Size(max = 255, message = "Field value is too long.")
    private String language;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", columnDefinition = "clob")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("")
    private Service srvc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("")
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("phones")
    private Shelter shelter;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Phone(Phone phone) {
        this.number = phone.number;
        this.extension = phone.extension;
        this.type = phone.type;
        this.language = phone.language;
        this.description = phone.description;
        this.location = phone.location;
        this.srvc = phone.srvc;
        this.organization = phone.organization;
        this.contact = phone.contact;
        this.shelter = phone.shelter;
    }

    public Phone number(String number) {
        this.number = number;
        return this;
    }

    public Phone extension(Integer extension) {
        this.extension = extension;
        return this;
    }

    public Phone type(String type) {
        this.type = type;
        return this;
    }

    public Phone language(String language) {
        this.language = language;
        return this;
    }

    public Phone description(String description) {
        this.description = description;
        return this;
    }

    public Phone location(Location location) {
        this.location = location;
        return this;
    }

    public Phone srvc(Service service) {
        this.srvc = service;
        return this;
    }

    public Phone organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public Phone contact(Contact contact) {
        this.contact = contact;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Phone phone = (Phone) o;
        if (phone.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), phone.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @SuppressWarnings("checkstyle:booleanExpressionComplexity")
    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Phone phone = (Phone) o;
        return Objects.equals(number, phone.number) &&
            Objects.equals(extension, phone.extension) &&
            Objects.equals(type, phone.type) &&
            Objects.equals(language, phone.language) &&
            Objects.equals(description, phone.description);
    }
}
