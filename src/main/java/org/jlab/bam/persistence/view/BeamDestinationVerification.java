package org.jlab.bam.persistence.view;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ryans
 */
@Entity
@Table(name = "BEAM_DESTINATION_VERIFICATION", schema = "BAM_OWNER")
public class BeamDestinationVerification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "BEAM_DESTINATION_ID", nullable = false)
    private BigInteger beamDestinationId;
    @Column(name = "VERIFICATION_ID")
    private Integer verificationId;
    @Column(name = "EXPIRATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    public BeamDestinationVerification() {
    }

    public BigInteger getBeamDestinationId() {
        return beamDestinationId;
    }

    public Integer getVerificationId() {
        return verificationId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
