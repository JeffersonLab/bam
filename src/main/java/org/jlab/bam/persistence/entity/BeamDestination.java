package org.jlab.bam.persistence.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.jlab.bam.persistence.view.BeamDestinationVerification;

/**
 *
 * @author ryans
 */
@Entity
@Table(name = "BEAM_DESTINATION", schema = "HCO_OWNER", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NAME"})})
public class BeamDestination implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "BEAM_DESTINATION_ID", nullable = false, precision = 22, scale = 0)
    private BigInteger beamDestinationId;
    @Size(max = 128)
    @Column(length = 128)
    private String name;
    private BigInteger weight;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BEAM_DESTINATION_ID")
    private BeamDestinationVerification verification;
    @OneToMany(mappedBy = "beamDestination", fetch = FetchType.LAZY)
    private List<ControlVerification> controlVerificationList;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BEAM_DESTINATION_ID")
    private BeamAuthDestination authDestination;
    
    
    public BeamDestination() {
    }

    public BeamDestination(BigInteger beamDestinationId) {
        this.beamDestinationId = beamDestinationId;
    }

    public BigInteger getBeamDestinationId() {
        return beamDestinationId;
    }

    public void setBeamDestinationId(BigInteger beamDestinationId) {
        this.beamDestinationId = beamDestinationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getWeight() {
        return weight;
    }

    public void setWeight(BigInteger weight) {
        this.weight = weight;
    }

    public BeamDestinationVerification getVerification() {
        return verification;
    }

    public List<ControlVerification> getControlVerificationList() {
        return controlVerificationList;
    }

    public BeamAuthDestination getAuthDestination() {
        return authDestination;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (beamDestinationId != null ? beamDestinationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BeamDestination)) {
            return false;
        }
        BeamDestination other = (BeamDestination) object;
        if ((this.beamDestinationId == null && other.beamDestinationId != null) || (this.beamDestinationId != null && !this.beamDestinationId.equals(other.beamDestinationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.jlab.beamauth.persistence.entity.BeamDestination[ beamDestinationId=" + beamDestinationId + " ]";
    }
    
}
