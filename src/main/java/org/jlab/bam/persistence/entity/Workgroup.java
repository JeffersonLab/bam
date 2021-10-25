package org.jlab.bam.persistence.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ryans
 */
@Entity
@Table(name="WORKGROUP")
public class Workgroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WORKGROUP_ID", nullable = false, precision = 22, scale = 0)
    private BigInteger workgroupId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(nullable = false, length = 64)
    private String name;
    @JoinTable(name = "WORKGROUP_MEMBERSHIP", joinColumns = {
        @JoinColumn(name = "WORKGROUP_ID", referencedColumnName = "WORKGROUP_ID", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID", nullable = false)})   
    @ManyToMany
    @OrderBy("lastname asc")
    private List<Staff> groupLeaderList;    
    
    public BigInteger getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(BigInteger groupId) {
        this.workgroupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Staff> getGroupLeaderList() {
        return groupLeaderList;
    }

    public void setGroupLeaderList(List<Staff> groupLeaderList) {
        this.groupLeaderList = groupLeaderList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.workgroupId != null ? this.workgroupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Workgroup other = (Workgroup) obj;
        if (this.workgroupId != other.workgroupId && (this.workgroupId == null || !this.workgroupId.equals(other.workgroupId))) {
            return false;
        }
        return true;
    }
}
