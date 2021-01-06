package org.jlab.beamauth.persistence.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ryans
 */
@Entity
@Table(name = "BEAM_AUTH_SETTINGS", schema = "BEAM_AUTH_OWNER")
public class BeamAuthSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "BEAM_AUTH_SETTINGS_ID", nullable = false, precision = 22, scale = 0)
    private BigInteger beamAuthSettingsId;
    @Basic(optional = true)
    @Column(name = "DOWNGRADE_EMAIL_ADDRESSES", nullable = true)
    private String downgradeEmailAddressesStr;    

    public String getDowngradedOrExpiredAddressCsv() {
        return downgradeEmailAddressesStr;
    }

    public List<Address> getDowngradeOrExpiredAdminEmailAddresses() throws AddressException {
        List<Address> addressList = new ArrayList<>();
        
        if(downgradeEmailAddressesStr != null && !downgradeEmailAddressesStr.isEmpty()) {
            String[] tokens = downgradeEmailAddressesStr.split(",");
            
            for(String token: tokens) {
                addressList.add(new InternetAddress(token.trim()));
            }
        }
        
        return addressList;
    }
    
    public BigInteger getBeamAuthSettingsId() {
        return beamAuthSettingsId;
    }

    public void setBeamAuthSettingsId(BigInteger beamAuthSettingsId) {
        this.beamAuthSettingsId = beamAuthSettingsId;
    }
}
