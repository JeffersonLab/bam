package org.jlab.beamauth.persistence.entity;

import org.jlab.beamauth.persistence.util.YnStringToBoolean;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "BEAM_AUTH_DESTINATION", schema = "BEAM_AUTH_OWNER")
public class BeamAuthDestination {
    private BigInteger beamDestinationId;
    private String machine;
    private String currentLimitUnits;
    private String displayName;
    private boolean active;

    @Id
    @Column(name = "BEAM_DESTINATION_ID", nullable = false, precision = 0)
    public BigInteger getBeamDestinationId() {
        return beamDestinationId;
    }

    public void setBeamDestinationId(BigInteger beamDestinationId) {
        this.beamDestinationId = beamDestinationId;
    }

    @Basic
    @Column(name = "MACHINE", nullable = false, length = 32)
    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    @Basic
    @Column(name = "CURRENT_LIMIT_UNITS", nullable = false, length = 3)
    public String getCurrentLimitUnits() {
        return currentLimitUnits;
    }

    public void setCurrentLimitUnits(String currentLimitUnits) {
        this.currentLimitUnits = currentLimitUnits;
    }

    @Basic
    @Column(name = "DISPLAY_NAME", nullable = true, length = 32)
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Basic
    @Column(name = "ACTIVE_YN", nullable = false, length = 1)
    @Convert(converter=YnStringToBoolean.class)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active){this.active = active;};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeamAuthDestination that = (BeamAuthDestination) o;
        return beamDestinationId == that.beamDestinationId &&
                Objects.equals(machine, that.machine) &&
                Objects.equals(currentLimitUnits, that.currentLimitUnits) &&
                Objects.equals(displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beamDestinationId, machine, currentLimitUnits, displayName);
    }
}
