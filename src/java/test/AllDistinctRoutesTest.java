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

import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.domain.simplifiedDomain.SimplifiedDomain;
import be.ac.ulg.montefiore.run.totem.domain.simplifiedDomain.SimplifiedDomainBuilder;
import be.ac.ulg.montefiore.run.totem.domain.simplifiedDomain.SimplifiedPath;
import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.exception.LinkNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;
import be.ac.ulg.montefiore.run.totem.repository.allDistinctRoutes.AllDistinctRoutes;
import be.ac.ulg.montefiore.run.totem.repository.allDistinctRoutes.AllDistinctRoutesException;

import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

import junit.framework.Assert;
import org.junit.Test;

/*
 * Changes:
 * --------
 *
 */

/**
 * Test all distincts routes algorithms
 *
 * <p>Creation date: 31-Jan-2005 10:54:59
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 */
public class AllDistinctRoutesTest {
    @Test
    public void testSimpleDomain() throws LinkNotFoundException, NodeNotFoundException, AllDistinctRoutesException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        SimplifiedDomain sDomain = SimplifiedDomainBuilder.build(domain);
        AllDistinctRoutes adr = new AllDistinctRoutes();
        adr.computeAllDistinctRoute(sDomain,10);

        int[] p0 = {0, 3};
        Assert.assertTrue(adr.getAllDistinctRoutes(0,1).contains(new SimplifiedPath(sDomain,p0)));
        int[] p1 = { 0, 7 };
        Assert.assertTrue(adr.getAllDistinctRoutes(0,3).contains(new SimplifiedPath(sDomain,p1)));
        int[] p2 = { 0, 5, 9 };
        Assert.assertTrue(adr.getAllDistinctRoutes(0,3).contains(new SimplifiedPath(sDomain,p2)));
        int[] p3 = { 6, 1 };
        Assert.assertTrue(adr.getAllDistinctRoutes(3,0).contains(new SimplifiedPath(sDomain,p3)));
        int[] p4 = { 8, 4, 1 };
        Assert.assertTrue(adr.getAllDistinctRoutes(3,0).contains(new SimplifiedPath(sDomain,p4)));
        int[] p5 = { 6, 3 };
        Assert.assertTrue(adr.getAllDistinctRoutes(3,1).contains(new SimplifiedPath(sDomain,p5)));
        int[] p6 = { 8, 4, 3 };
        Assert.assertTrue(adr.getAllDistinctRoutes(3,1).contains(new SimplifiedPath(sDomain,p6)));
        int[] p7 = { 6 };
        Assert.assertTrue(adr.getAllDistinctRoutes(3,2).contains(new SimplifiedPath(sDomain,p7)));

    }

    @Test
    public void testLoadSaveDistinctRoutes() throws LinkNotFoundException, NodeNotFoundException, AllDistinctRoutesException, IOException, FileNotFoundException, InvalidDomainException {
        String domainFileName = "src/resources/junit-test/test-domain1.xml";
        String outFileName = "src/resources/junit-test/distinctRoutes.out";

        Domain domain = DomainFactory.loadDomain(domainFileName);
        SimplifiedDomain sDomain = SimplifiedDomainBuilder.build(domain);

        AllDistinctRoutes adr = new AllDistinctRoutes();
        adr.computeAllDistinctRoute(sDomain,10);

        adr.saveAllDistinctRoute(outFileName);

        AllDistinctRoutes adr2 = new AllDistinctRoutes();
        adr2.loadAllDistinctRoute(sDomain, outFileName);

        for (int i=0; i < sDomain.getNbNodes(); i++)
        for (int j=0; j < sDomain.getNbNodes(); j++) {
            List<SimplifiedPath> lst1 = adr.getAllDistinctRoutes(i, j);
            List<SimplifiedPath> lst2 = adr2.getAllDistinctRoutes(i, j);

            if (lst1 == null || lst2 == null) {
                Assert.assertTrue(lst1 == lst2);
            }
            else {
                Assert.assertTrue(lst1.containsAll(lst2));
                Assert.assertTrue(lst2.containsAll(lst1));
            }
        }

        File file = new File(outFileName);
        file.delete();

    }

    @Test
    public void testMaxDepth() throws LinkNotFoundException, NodeNotFoundException, IOException, AllDistinctRoutesException, InvalidDomainException, FileNotFoundException {
        String domainFileName = "src/resources/junit-test/test-domain1.xml";

        Domain domain = DomainFactory.loadDomain(domainFileName);
        SimplifiedDomain sDomain = SimplifiedDomainBuilder.build(domain);
        AllDistinctRoutes adr = new AllDistinctRoutes();
        adr.computeAllDistinctRoute(sDomain,2);

        for (int i=0; i < sDomain.getNbNodes(); i++)
        for (int j=0; j < sDomain.getNbNodes(); j++) {
            List<SimplifiedPath> lst = adr.getAllDistinctRoutes(i, j);
            if (lst != null) {
                for (int k = 0; k < lst.size(); k++)
                    Assert.assertTrue(lst.get(k).getLinkIdPath().length <= 2);
            }
        }

        adr.computeAllDistinctRoute(sDomain, 3);

        int[] p2 = { 0, 5, 9 };
        Assert.assertTrue(adr.getAllDistinctRoutes(0,3).contains(new SimplifiedPath(sDomain,p2)));
        int[] p4 = { 8, 4, 1 };
        Assert.assertTrue(adr.getAllDistinctRoutes(3,0).contains(new SimplifiedPath(sDomain,p4)));
        int[] p6 = { 8, 4, 3 };
        Assert.assertTrue(adr.getAllDistinctRoutes(3,1).contains(new SimplifiedPath(sDomain,p6)));

    }

}
