
/* TOTEM-v3.2 June 18 2008*/

/*
 * ===========================================================
 * TOTEM : A TOolbox for Traffic Engineering Methods
 * ===========================================================
 *
 * (C) Copyright 2004-2006, by Research Unit in Networking RUN, University of Liege. All Rights Reserved.
 *
 * Project Info:  http://totem.run.montefiore.ulg.ac.be
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU General Public License version 2.0 as published by the Free Software Foundation;
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
*/

package test;

import junit.framework.Assert;

import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.model.Link;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.exception.DomainAlreadyExistException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.facade.TrafficMatrixManager;
import be.ac.ulg.montefiore.run.totem.repository.model.TotemActionList;
import be.ac.ulg.montefiore.run.totem.repository.model.TotemAction;
import be.ac.ucl.poms.repository.IGPWO.IGPWO;

import java.util.HashMap;

import org.junit.Test;

/*
* Changes:
* --------
*
*
*/

/**
* Test that IGPWO is able to change links TE metric in a domain
*
* <p>Creation date: 04-Nov-2005
*
* @author Ga�l Monfort (monfort@run.montefiore.ulg.ac.be)
*/

public class IGPWOtest {
    @Test
    public void testAddTEMetric() throws Exception {
        String fileName = "src/resources/junit-test/abilene.xml";
        String TM1 = "src/resources/junit-test/TM1-abilene.xml";
        String TM0 = "src/resources/junit-test/TM0-abilene.xml";
        Domain domain = DomainFactory.loadDomain(fileName);

        try {
            InterDomainManager.getInstance().addDomain(domain);
        } catch (DomainAlreadyExistException e) {
            e.printStackTrace();
        }

        int TM0Id = 0;
        int TM1Id = 1;
        TrafficMatrixManager.getInstance().loadTrafficMatrix(TM0, TM0Id, true);
        TrafficMatrixManager.getInstance().loadTrafficMatrix(TM1, TM1Id, true);

        IGPWO igpwo = new IGPWO();

        HashMap startAlgoParameters = new HashMap();
        startAlgoParameters.remove("ASID");
        startAlgoParameters.put("ASID", new Integer(domain.getASID()).toString());

        int[] TMIds = {TM1Id, TM0Id};
        igpwo.start(startAlgoParameters);

        TotemActionList alist;

        alist = igpwo.calculateWeights(domain.getASID(), TMIds);

        for (int i = 0; i < alist.size(); i++) {
            TotemAction action = (TotemAction)alist.get(i);
            action.execute();
        }

        igpwo.stop();

        for (int i = 0; i < domain.getNbLinks(); i++) {
            Link lnk = domain.getAllLinks().get(i);
            Assert.assertTrue(lnk.getTEMetric() != 0);
            //System.out.println(lnk.getTEMetric());
        }
    }
}
