package org.jlab.beamauth.business.session;

import java.math.BigInteger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jlab.beamauth.persistence.entity.BeamAuthSettings;

/**
 *
 * @author ryans
 */
@Stateless
public class BeamAuthSettingsFacade extends AbstractFacade<BeamAuthSettings> {
    @PersistenceContext(unitName = "beam-authorizationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BeamAuthSettingsFacade() {
        super(BeamAuthSettings.class);
    }

    @PermitAll
    public BeamAuthSettings findSettings() {
        return find(BigInteger.ONE);
    }
    
}
