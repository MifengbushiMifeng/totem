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

import be.ac.ulg.montefiore.run.totem.domain.exception.DomainAlreadyExistException;
import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.exception.LinkNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.NoRouteToHostException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.RoutingException;
import be.ac.ulg.montefiore.run.totem.topgen.util.RealRoutingMatrix;
import junit.framework.Assert;
import org.junit.Test;

/*
 * Changes:
 * --------
 *
 */

/**
 * This class implements some tests for the routing matrix classes.
 *
 * <p>Creation date: 17-mai-2005
 *
 * @author Jean Lepropre (lepropre@run.montefiore.ulg.ac.be)
 */
public class RoutingMatrixTest {
    @Test
    public void testRealRoutingMatrix() throws NoRouteToHostException, RoutingException, DomainAlreadyExistException, NodeNotFoundException, LinkNotFoundException, InvalidDomainException, FileNotFoundException {
        InterDomainManager.getInstance().loadDomain("src/resources/junit-test/test-domain-realRoutingMatrix.xml", true, true);
        Domain domain = InterDomainManager.getInstance().getDefaultDomain();
        RealRoutingMatrix matrix = new RealRoutingMatrix(domain);
        
        Assert.assertEquals(1f, matrix.getElement(domain.getConvertor().getLinkId("1"), domain.getConvertor().getNodeId("S"), domain.getConvertor().getNodeId("G")), 0);
        Assert.assertEquals(0.5f, matrix.getElement(domain.getConvertor().getLinkId("2"), domain.getConvertor().getNodeId("S"), domain.getConvertor().getNodeId("G")), 0);
        Assert.assertEquals(0.25f, matrix.getElement(domain.getConvertor().getLinkId("3"), domain.getConvertor().getNodeId("S"), domain.getConvertor().getNodeId("G")), 0);
        Assert.assertEquals(0.25f, matrix.getElement(domain.getConvertor().getLinkId("4"), domain.getConvertor().getNodeId("S"), domain.getConvertor().getNodeId("G")), 0);
        Assert.assertEquals(0.25f, matrix.getElement(domain.getConvertor().getLinkId("5"), domain.getConvertor().getNodeId("S"), domain.getConvertor().getNodeId("G")), 0);
        Assert.assertEquals(0.5f, matrix.getElement(domain.getConvertor().getLinkId("6"), domain.getConvertor().getNodeId("S"), domain.getConvertor().getNodeId("G")), 0);
        Assert.assertEquals(0.5f, matrix.getElement(domain.getConvertor().getLinkId("7"), domain.getConvertor().getNodeId("S"), domain.getConvertor().getNodeId("G")), 0);
        Assert.assertEquals(0.5f, matrix.getElement(domain.getConvertor().getLinkId("8"), domain.getConvertor().getNodeId("S"), domain.getConvertor().getNodeId("G")), 0);
        Assert.assertEquals(1f, matrix.getElement(domain.getConvertor().getLinkId("9"), domain.getConvertor().getNodeId("S"), domain.getConvertor().getNodeId("G")), 0);
    }

}
