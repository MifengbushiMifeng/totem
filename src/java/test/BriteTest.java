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
import Model.RouterWaxman;
import Model.Model;
import Model.ModelConstants;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.topgen.util.Converter;


import java.io.File;

import org.junit.Test;

/*
 * Changes:
 * --------
 *
 */

/**
 * Test Brite 
 *
 * <p>Creation date: 12-Jan-2005 17:24:56
 *
 * @author  Jean Lepropre (lepropre@run.montefiore.ulg.ac.be)
 */
public class BriteTest {

    @Test
    public void testConvertor() throws Exception {

        Model waxman = new RouterWaxman(50,10,10,ModelConstants.NP_RANDOM,1,0.5,0.25,ModelConstants.GT_INCREMENTAL,ModelConstants.BW_HEAVYTAILED, 100, 200);
        Topology.Topology topo = new Topology.Topology(waxman);
        Domain domain = Converter.briteTopologyToDomain(topo,true,Converter.METRIC_HOP_COUNT);
        Assert.assertEquals(domain.getNbNodes(), 50);
        DomainFactory.saveDomain("test-topo.xml", domain);

        File file = new File("test-topo.xml");
        Assert.assertEquals(file.isFile(),true);
        file.delete();
    }
}
