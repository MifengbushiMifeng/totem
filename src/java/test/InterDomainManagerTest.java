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

import java.io.FileNotFoundException;

import junit.framework.Assert;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.exception.DomainAlreadyExistException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.InvalidTrafficMatrixException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.TrafficMatrixAlreadyExistException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.facade.TrafficMatrixManager;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.TrafficMatrix;
import org.junit.Test;

/*
 * Changes:
 * --------
 *  19-Oct-2005: add testAddRemoveDomain method (GMO)
 *  11-Jul-2006: add a test to see if matrices are unloaded when unloading the domain (GMO).
 */

/**
 * Test the InterDomainManager
 *
 * <p>Creation date: 19-Jan-2005 16:44:17
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 * @author  Ga�l Monfort (monfort@run.montefiore.ulg.ac.be)
 */
public class InterDomainManagerTest {
    @Test
    public void testLoadDomain() {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        InterDomainManager idm = InterDomainManager.getInstance();

        try {
	        idm.loadDomain(fileName,true,false);
            idm.getDomain(10000);
        } catch (InvalidDomainException e) {
            Assert.fail(e.toString());
            e.printStackTrace();
        } catch (DomainAlreadyExistException e){
            Assert.fail(e.toString());
            e.printStackTrace();
        }


    }

    @Test
    public void testAddRemoveDomain() throws DomainAlreadyExistException, InvalidDomainException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);

        InterDomainManager idm = InterDomainManager.getInstance();

        /* add domain with ASID 10000 */
        try {
            idm.addDomain(domain);
        }
        catch (DomainAlreadyExistException e) {}

        /* remove all */
        idm.removeAllDomains();

        idm.addDomain(domain);

        /* add domain a second time, check that exception is thrown */
        boolean error = false;
        try {
            idm.addDomain(domain);
        }
        catch (DomainAlreadyExistException e) {
            error = true;
        }
        Assert.assertTrue(error);

        /* get the added domain, if not present an exception is thrown and the test will fail */
        idm.getDomain(10000);

        /* add a second domain, remove it and check its absence */

        String fileName2 = "src/resources/junit-test/test-domain2.xml";
        idm.loadDomain(fileName2, true, true);

        domain = idm.getDomain(10002);
        idm.getDomain(10000);

        Assert.assertEquals(domain, idm.getDefaultDomain());

        idm.removeDefaultDomain();

        error = false;
        try {
            idm.getDomain(10002);
        }
        catch (InvalidDomainException e) {
            error = true;
        }
        Assert.assertTrue(error);

    }

    @Test
    public void testRemoveMatrices() throws InvalidDomainException, DomainAlreadyExistException, NodeNotFoundException, InvalidTrafficMatrixException, TrafficMatrixAlreadyExistException {
        InterDomainManager.getInstance().removeAllDomains();

        String fileName = "src/resources/junit-test/test-domain3.xml";
        InterDomainManager.getInstance().loadDomain(fileName, true, true);

        String tmFileName = "src/resources/junit-test/test-tmupdate.xml";
        TrafficMatrixManager.getInstance().loadTrafficMatrix(tmFileName, 2, true);

        //InterDomainManager.getInstance().removeAllDomains();
        InterDomainManager.getInstance().removeDomain(10003);

        boolean error = false;
        try {
            TrafficMatrix tm = TrafficMatrixManager.getInstance().getTrafficMatrix(10003, 2);
        } catch (InvalidTrafficMatrixException e) {
            error = true;
        }
        Assert.assertTrue(error);

    }

}
