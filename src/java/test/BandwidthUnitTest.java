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
import be.ac.ulg.montefiore.run.totem.domain.model.BandwidthUnit;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.DomainImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.NodeImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.LinkImpl;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.exception.*;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.facade.TrafficMatrixManager;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.TrafficMatrix;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.InvalidTrafficMatrixException;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;


/*
* Changes:
* --------
*
*/

/**
* <Replace this by a description of the class>
*
* <p>Creation date: 7 juil. 2006
*
* @author Ga�l Monfort (monfort@run.montefiore.ulg.ac.be)
*/

public class BandwidthUnitTest {
    @Test
    public void testBw() {
        BandwidthUnit kbps = BandwidthUnit.KBPS;
        BandwidthUnit gbps = BandwidthUnit.GBPS;
        BandwidthUnit bps = BandwidthUnit.BPS;

        //convert 2,5 GBPS to KBPS
        Assert.assertEquals(kbps.convert(gbps, 2.5f), 2500000f);

        //convert 2,5 KBPS to GBPS
        Assert.assertEquals(gbps.convert(kbps, 2.5f), 2.5E-6f);

        //convert 2,5 KBPS to KBPS
        Assert.assertEquals(kbps.convert(kbps, 2.5f), 2.5f);

        //convert 2,5 GBPS to BPS
        Assert.assertEquals(bps.convert(gbps, 2.5f), 2500000000f);


        boolean error = false;
        try {
            bps.convert(BandwidthUnit.DEFAULT_UNIT, 2.5f);
        } catch (IllegalArgumentException ex) {
            error = true;
        }
        Assert.assertTrue(error);

        error = false;
        try {
            BandwidthUnit.DEFAULT_UNIT.convert(kbps, 2.5f);
        } catch (IllegalArgumentException ex) {
            error = true;
        }
        Assert.assertTrue(error);

        error = false;
        try {
            BandwidthUnit.DEFAULT_UNIT.convert(BandwidthUnit.DEFAULT_UNIT, 2.5f);
        } catch (IllegalArgumentException ex) {
            error = true;
        }
        Assert.assertFalse(error);
    }

    @Test
    public void testBwConversion() throws InvalidDomainException, DomainAlreadyExistException, NodeNotFoundException, InvalidTrafficMatrixException {
        Domain domain;
        TrafficMatrix tm;//, tm2;

        InterDomainManager.getInstance().removeAllDomains();

        //load domaion with units (mbps)
        domain = InterDomainManager.getInstance().loadDomain("src/resources/junit-test/abilene.xml", true, false);

        //load matrix without units
        tm = TrafficMatrixManager.getInstance().loadTrafficMatrix("src/resources/junit-test/TM0-abilene.xml");

        Assert.assertEquals(tm.getUnit(), BandwidthUnit.MBPS);
        Assert.assertEquals(tm.get(0,1), 250.0f);
        Assert.assertEquals(domain.getBandwidthUnit(), BandwidthUnit.MBPS);

        //load matrix with units (bps)
        tm = TrafficMatrixManager.getInstance().loadTrafficMatrix("src/resources/junit-test/TM0-abilene-withunits.xml");

        // a conversion should have occurred
        Assert.assertEquals(tm.getUnit(), BandwidthUnit.MBPS);
        Assert.assertEquals(tm.get(0,1), 250.0f * 1E-6f);


        InterDomainManager.getInstance().removeAllDomains();
        //TrafficMatrixManager.getInstance().removeAllTrafficMatrices();


        /* The units of the domain are now mandatory, so that part is useless
        //load domaion without units
        domain = InterDomainManager.getInstance().loadDomain("src/resources/junit-test/abilene-nounits.xml", true, false);

        //load matrix without units
        tm = TrafficMatrixManager.getInstance().loadTrafficMatrix("src/resources/junit-test/TM0-abilene.xml");

        Assert.assertNull(tm.getUnit());
        Assert.assertEquals(tm.get(0,1), 250.0f);
        Assert.assertNull(domain.getBandwidthUnit());

        //load matrix with units (bps)
        tm2 = TrafficMatrixManager.getInstance().loadTrafficMatrix("src/resources/junit-test/TM0-abilene-withunits.xml");

        Assert.assertEquals(tm2.getUnit(), BandwidthUnit.BPS);
        Assert.assertEquals(tm2.get(0,1), 250.0f);
        Assert.assertEquals(domain.getBandwidthUnit(), BandwidthUnit.BPS);

        // the effect is to set the units of the previoulsy loaded matrix
        Assert.assertEquals(tm.getUnit(), BandwidthUnit.BPS);
        */

        InterDomainManager.getInstance().removeAllDomains();
        TrafficMatrixManager.getInstance().removeAllTrafficMatrices();
    }

    //add test when loading domain with or without unit, then changing it.
    // This test is now useless as we cannot determine if the unit was present in the TM file or is herited from the domain.
    /*
    @Test
    public void testSaveUnits() throws InvalidDomainException, DomainAlreadyExistException, NodeNotFoundException, TrafficMatrixIdException, InvalidTrafficMatrixException, FileNotFoundException {
        //load domaion without units
        Domain domain = InterDomainManager.getInstance().loadDomain("../run-totem/src/resources/junit-test/abilene-nounits.xml", true, false);
        //load matrix without units
        TrafficMatrix tm = TrafficMatrixManager.getInstance().loadTrafficMatrix("../run-totem/src/resources/junit-test/TM0-abilene.xml");

        //domain.setBandwidthUnit(BandwidthUnit.MBPS);
        //tm.setUnit(BandwidthUnit.MBPS);

        String domainFileName = "src/resources/junit-test/domain.tmp";
        String tmFileName = "src/resources/junit-test/matrice.tmp";
        InterDomainManager.getInstance().saveDomain(domain.getASID(), domainFileName);
        TrafficMatrixManager.getInstance().saveTrafficMatrix(domain.getASID(), tm.getTmId(), tmFileName);

        InterDomainManager.getInstance().removeAllDomains();
        TrafficMatrixManager.getInstance().removeAllTrafficMatrices();

        domain = DomainFactory.loadDomain(domainFileName);
        Assert.assertEquals(domain.getBandwidthUnit(), BandwidthUnit.MBPS);

        //reload the domain without units in the manager
        InterDomainManager.getInstance().loadDomain("../run-totem/src/resources/junit-test/abilene-nounits.xml", true, false);

        tm = TrafficMatrixFactory.loadTrafficMatrix(tmFileName);
        Assert.assertEquals(tm.getUnit(), BandwidthUnit.MBPS);

        InterDomainManager.getInstance().removeAllDomains();

        File d = new File(domainFileName);
        d.delete();

        d = new File(tmFileName);
        d.delete();

    }
    */

    @Test
    public void testDomainCreation() throws NodeAlreadyExistException, NodeNotFoundException, LinkAlreadyExistException, InvalidDomainException, FileNotFoundException {
        Domain domain = new DomainImpl(3456);

        domain.setBandwidthUnit(BandwidthUnit.BPS);

        domain.addNode(new NodeImpl(domain, "1"));
        domain.addNode(new NodeImpl(domain, "2"));
        domain.addNode(new NodeImpl(domain, "3"));

        domain.addLink(new LinkImpl(domain, "link1", "1", "2", 250));
        domain.addLink(new LinkImpl(domain, "link2", "3", "2", 250));

        String fileName = "saving-domain1.xml";
        DomainFactory.saveDomain(fileName, domain);

        domain = DomainFactory.loadDomain(fileName);
        Assert.assertNotNull(domain.getBandwidthUnit());
        Assert.assertEquals(domain.getBandwidthUnit(), BandwidthUnit.BPS);

        File file = new File(fileName);

        file.delete();


    }
}
