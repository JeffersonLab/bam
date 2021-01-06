package org.jlab.beamauth.persistence.entity;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Note: we name this class ResponsibleGroup instead of Group because group
 * is a reserved word in SQL.
 *
 * @author ryans
 */
@Entity
@Table(name = "RESPONSIBLE_GROUP", schema = "HCO_OWNER")
public class ResponsibleGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "GroupId", sequenceName = "GROUP_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GroupId")        
    @Basic(optional = false)
    @NotNull
    @Column(name = "GROUP_ID", nullable = false, precision = 22, scale = 0)
    private BigInteger groupId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(nullable = false, length = 128)
    private String name;
    @Size(max = 1024)
    @Column(length = 1024)
    private String description;
    @NotNull
    @Basic(optional = false)
    @JoinColumn(name = "LEADER_WORKGROUP_ID", referencedColumnName = "WORKGROUP_ID", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Workgroup leaderWorkgroup;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private List<CreditedControl> ccList;        

    public ResponsibleGroup() {
    }

    public ResponsibleGroup(BigInteger groupId) {
        this.groupId = groupId;
    }

    public ResponsibleGroup(BigInteger groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }

    public BigInteger getGroupId() {
        return groupId;
    }

    public void setGroupId(BigInteger groupId) {
        this.groupId = groupId;
    }

    public List<CreditedControl> getCcList() {
        return ccList;
    }

    public void setCcList(List<CreditedControl> ccList) {
        this.ccList = ccList;
    }    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Workgroup getLeaderWorkgroup() {
        return leaderWorkgroup;
    }

    public void setLeaderWorkgroup(Workgroup leaderWorkgroup) {
        this.leaderWorkgroup = leaderWorkgroup;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupId != null ? groupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResponsibleGroup)) {
            return false;
        }
        ResponsibleGroup other = (ResponsibleGroup) object;
        if ((this.groupId == null && other.groupId != null) || (this.groupId != null && !this.groupId.equals(other.groupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.jlab.beamauth.persistence.entity.ResponsibleGroup[ groupId=" + groupId + " ]";
    }

}
