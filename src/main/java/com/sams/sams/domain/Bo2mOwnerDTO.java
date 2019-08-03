package com.sams.sams.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Bo2mOwnerDTO.
 */
@Entity
@Table(name = "bo2m_owner")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bo2mOwnerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "bo2mOwnerDTO")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Bo2mCarDTO> bo2mCarDTOS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Bo2mOwnerDTO name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Bo2mCarDTO> getBo2mCarDTOS() {
        return bo2mCarDTOS;
    }

    public Bo2mOwnerDTO bo2mCarDTOS(Set<Bo2mCarDTO> bo2mCarDTOS) {
        this.bo2mCarDTOS = bo2mCarDTOS;
        return this;
    }

    public Bo2mOwnerDTO addBo2mCarDTO(Bo2mCarDTO bo2mCarDTO) {
        this.bo2mCarDTOS.add(bo2mCarDTO);
        bo2mCarDTO.setBo2mOwnerDTO(this);
        return this;
    }

    public Bo2mOwnerDTO removeBo2mCarDTO(Bo2mCarDTO bo2mCarDTO) {
        this.bo2mCarDTOS.remove(bo2mCarDTO);
        bo2mCarDTO.setBo2mOwnerDTO(null);
        return this;
    }

    public void setBo2mCarDTOS(Set<Bo2mCarDTO> bo2mCarDTOS) {
        this.bo2mCarDTOS = bo2mCarDTOS;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bo2mOwnerDTO)) {
            return false;
        }
        return id != null && id.equals(((Bo2mOwnerDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Bo2mOwnerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
