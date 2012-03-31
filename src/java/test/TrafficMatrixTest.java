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
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.impl.TrafficMatrixImpl;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.TrafficMatrix;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.persistence.TrafficMatrixFactory;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.facade.TrafficMatrixManager;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;

import java.io.File;

import org.junit.Test;

/*
 * Changes:
 * --------
 *
 */

/**
 * Test class for traffic matrices (TrafficMatrixFactory,...)
 *
 * <p>Creation date: 08-03-2005
 *
 * @author  Olivier Delcourt (delcourt@run.montefiore.ulg.ac.be)
 */ 

public class TrafficMatrixTest {


    /**
     * Test if intra-domain traffic matrices are workign
     * @throws Exception
     */
    @Test
    public void testCreateTM() throws Exception{
        InterDomainManager.getInstance().loadDomain("src/resources/junit-test/test-domain3.xml",true,false);
        TrafficMatrix tm = new TrafficMatrixImpl(10003);

        tm.set(0,1,11);
        tm.set(0,2,12);
        tm.set(1,0,15);
        tm.set(2,0,18);


        TrafficMatrixFactory.saveTrafficMatrix("src/resources/junit-test/test-tm.xml",tm);

        TrafficMatrixManager.getInstance().loadTrafficMatrix("src/resources/junit-test/test-tm.xml",0,true);
        TrafficMatrix trafficMatrix = TrafficMatrixManager.getInstance().getDefaultTrafficMatrix();

        Assert.assertEquals(trafficMatrix.get(0,1),11,0.000001);
        Assert.assertEquals(trafficMatrix.get(0,2),12,0.000001);
        Assert.assertEquals(trafficMatrix.get(1,0),15,0.000001);
        Assert.assertEquals(trafficMatrix.get(2,0),18,0.000001);

        TrafficMatrixFactory.updateTrafficMatrix("src/resources/junit-test/test-tmupdate.xml",0);

        Assert.assertEquals(trafficMatrix.get(0,1),12,0.000001);
        Assert.assertEquals(trafficMatrix.get(0,2),13,0.000001);
        Assert.assertEquals(trafficMatrix.get(1,0),16,0.000001);
        Assert.assertEquals(trafficMatrix.get(2,0),19,0.000001);

        File file = new File("src/resources/junit-test/test-tm.xml");
        file.delete();
    }
}
