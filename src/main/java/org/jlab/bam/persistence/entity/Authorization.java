package org.jlab.bam.persistence.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ryans
 */
@Entity
@Table(schema = "BAM_OWNER")
@NamedQueries({
    @NamedQuery(name = "Authorization.findAll", query = "SELECT a FROM Authorization a")})
public class Authorization implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="AuthorizationId", sequenceName="AUTHORIZATION_ID", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AuthorizationId")        
    @Basic(optional = false)
    @NotNull
    @Column(name = "AUTHORIZATION_ID", nullable = false, precision = 22, scale = 0)
    private BigInteger authorizationId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AUTHORIZATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date authorizationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFIED_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @NotNull
    @JoinColumn(name = "AUTHORIZED_BY", referencedColumnName = "STAFF_ID")
    @ManyToOne(optional = false)
    private Staff authorizedBy;
    @NotNull
    @JoinColumn(name = "MODIFIED_BY", referencedColumnName = "STAFF_ID")
    @ManyToOne(optional = false)
    private Staff modifiedBy;    
    @Size(max = 2048)
    @Column(length = 2048)
    private String comments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "authorization", fetch = FetchType.EAGER)
    private List<DestinationAuthorization> destinationAuthorizationList;

    public Authorization() {
    }

    public BigInteger getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(BigInteger authorizationId) {
        this.authorizationId = authorizationId;
    }

    public Date getAuthorizationDate() {
        return authorizationDate;
    }

    public void setAuthorizationDate(Date authorizationDate) {
        this.authorizationDate = authorizationDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Staff getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(Staff authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public Staff getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Staff modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<DestinationAuthorization> getDestinationAuthorizationList() {
        return destinationAuthorizationList;
    }

    public void setDestinationAuthorizationList(List<DestinationAuthorization> destinationAuthorizationList) {
        this.destinationAuthorizationList = destinationAuthorizationList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (authorizationId != null ? authorizationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Authorization)) {
            return false;
        }
        Authorization other = (Authorization) object;
        return (this.authorizationId != null || other.authorizationId == null) && (this.authorizationId == null || this.authorizationId.equals(other.authorizationId));
    }

    @Override
    public String toString() {
        return "org.jlab.beamauth.persistence.entity.Authorization[ authorizationId=" + authorizationId + " ]";
    }

    public Authorization createAdminClone() {
        Authorization other = new Authorization();
        Staff admin = new Staff();
        admin.setStaffId(BigInteger.valueOf(26L)); // Hopefully this doesn't update staff record!
        other.authorizationDate = this.authorizationDate;
        other.authorizedBy = this.authorizedBy;
        other.comments = this.comments;
        other.setModifiedBy(admin);
        other.setModifiedDate(new Date());
        return other;
    }
    
}
