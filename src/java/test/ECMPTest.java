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

import org.apache.log4j.Logger;
import org.junit.Test;
import junit.framework.Assert;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.TrafficMatrix;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.persistence.TrafficMatrixFactory;
import be.ac.ulg.montefiore.run.totem.repository.CSPF.CSPFHopCount;

/*
 * Changes:
 * --------
 */

/**
 * This class test the ECMP functionnality on the abilene topology.
 * <p/>
 * <p>Creation date : 23 mai 2005 11:28:44
 *
 * @author Simon Balon (balon@run.montefiore.ulg.ac.be)
 */
public class ECMPTest {
    private static Logger logger = Logger.getLogger(ECMPTest.class);

    @Test
    public void testECMPOnAbilene() {
        try {
            InterDomainManager.getInstance().removeAllDomains();
            String fileName = "src/resources/junit-test/abilene.xml";
            InterDomainManager.getInstance().loadDomain(fileName, true, false);

            String trafficMatrixName = "src/resources/junit-test/TM2-abilene.xml";
            TrafficMatrix tm = TrafficMatrixFactory.loadTrafficMatrix(trafficMatrixName);

            tm.getLinkLoadStrategy().setSPFAlgo(new CSPFHopCount());
            tm.getLinkLoadStrategy().setECMP(true);
            double[] loads = tm.getLinkUtilisation();

            Assert.assertEquals(0.5f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("ATLA-IPLS")], 0.001f);
            Assert.assertEquals(0.5f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("ATLA-HSTN")], 0.001f);
            Assert.assertEquals(0.5f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("IPLS-KSCY")], 0.001f);
            Assert.assertEquals(0.75f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("KSCY-DNVR")], 0.001f);
            Assert.assertEquals(0.25f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("HSTN-KSCY")], 0.001f);
            Assert.assertEquals(0.25f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("HSTN-LOSA")], 0.001f);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testECMPMultipleLinks() {
        try {
            InterDomainManager.getInstance().removeAllDomains();
            String fileName = "src/resources/junit-test/multidomain.xml";
            InterDomainManager.getInstance().loadDomain(fileName, true, false);

            Domain domain = InterDomainManager.getInstance().getDefaultDomain();

            String trafficMatrixName = "src/resources/junit-test/TM0-multidomain.xml";
            TrafficMatrix tm = TrafficMatrixFactory.loadTrafficMatrix(trafficMatrixName);

            tm.getLinkLoadStrategy().setSPFAlgo(new CSPFHopCount());
            tm.getLinkLoadStrategy().setECMP(true);
            double[] loads = tm.getLinkUtilisation();

            /*
            for (Link lnk : domain.getAllLinks()){
                System.out.println(lnk.getId());
                System.out.println(loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId(lnk.getId())]);   
            }*/

            Assert.assertEquals(0.25f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("0-1")], 0.001f);
            Assert.assertEquals(0.25f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("0-1-bis")], 0.001f);
            Assert.assertEquals(0.25f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("0-2")], 0.001f);
            Assert.assertEquals(0.25f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("0-3")], 0.001f);
            Assert.assertEquals(0.25f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("1-4")], 0.001f);
            Assert.assertEquals(0.25f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("2-4")], 0.001f);
            Assert.assertEquals(0.25f, loads[InterDomainManager.getInstance().getDefaultDomain().getConvertor().getLinkId("3-4")], 0.001f);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }


}
