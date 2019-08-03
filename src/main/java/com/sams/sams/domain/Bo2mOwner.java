package com.sams.sams.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Bo2mOwner.
 */
@Entity
@Table(name = "bo2m_owner")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bo2mOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "bo2mOwner")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Bo2mCar> bo2mCars = new HashSet<>();

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

    public Bo2mOwner name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Bo2mCar> getBo2mCars() {
        return bo2mCars;
    }

    public Bo2mOwner bo2mCars(Set<Bo2mCar> bo2mCars) {
        this.bo2mCars = bo2mCars;
        return this;
    }

    public Bo2mOwner addBo2mCar(Bo2mCar bo2mCar) {
        this.bo2mCars.add(bo2mCar);
        bo2mCar.setBo2mOwner(this);
        return this;
    }

    public Bo2mOwner removeBo2mCar(Bo2mCar bo2mCar) {
        this.bo2mCars.remove(bo2mCar);
        bo2mCar.setBo2mOwner(null);
        return this;
    }

    public void setBo2mCars(Set<Bo2mCar> bo2mCars) {
        this.bo2mCars = bo2mCars;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bo2mOwner)) {
            return false;
        }
        return id != null && id.equals(((Bo2mOwner) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Bo2mOwner{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
