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

import junit.framework.Test;
import junit.framework.JUnit4TestAdapter;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/*
 * Changes:
 * --------
 *
 */

/**
 * Executes all the tests.
 *
 * <p>Creation date: 05-Jan.-2004
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 * @author  Simon Balon (balon@run.montefiore.ulg.ac.be)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        
    DomainFactoryTest.class,
    DomainConvertorTest.class,
    InterDomainManagerTest.class,

    DomaintTest.class,

    SimplifiedDomainTest.class,
    NodeTest.class,
    DiffServTest.class,
    PreemptTest.class,
    CSPFPriorityQueueTest.class,
    BWSharingTest.class,
    DAMOTETest.class,
    CSPFTest.class,
    MIRATest.class,
    XAMCRATest.class,
    IGPWOtest.class,

    BhandariTest.class,
    AllDistinctRoutesTest.class,
    MCFTest.class,
    DistributedScenarioTest.class,
    TrafficMatrixTest.class,
    //ReoptTest.class,
    RoutingMatrixTest.class,

    ECMPTest.class,

    ScenarioTest.class,

    ListenersTest.class,

    HybridStrategiesTest.class,

    BandwidthUnitTest.class,
    RoutingPathTest.class,

        DiffservModelTest.class,
        PreemptTest2.class
})
public class AllTest {

    public static void main(java.lang.String[] args) {
        be.ac.ulg.montefiore.run.totem.core.Totem.init();
        JUnitCore.runClasses(new Class [] {AllTest.class});
    }

    public static Test suite() {
        /*
        be.ac.ulg.montefiore.run.totem.core.Totem.init();

        TestSuite suite = new TestSuite();

        suite.addTestSuite(DomainFactoryTest.class);
        suite.addTestSuite(DomainConvertorTest.class);
        suite.addTestSuite(InterDomainManagerTest.class);

        suite.addTestSuite(DomaintTest.class);

        suite.addTestSuite(SimplifiedDomainTest.class);
        suite.addTestSuite(NodeTest.class);
        suite.addTestSuite(DiffServTest.class);
        suite.addTestSuite(PreemptTest.class);
        suite.addTestSuite(CSPFPriorityQueueTest.class);
        suite.addTestSuite(BWSharingTest.class);
        suite.addTestSuite(DAMOTETest.class);
        suite.addTestSuite(CSPFTest.class);
        suite.addTestSuite(MIRATest.class);
        suite.addTestSuite(XAMCRATest.class);
        suite.addTestSuite(IGPWOtest.class);

        suite.addTestSuite(BhandariTest.class);
        suite.addTestSuite(AllDistinctRoutesTest.class);
        suite.addTestSuite(MCFTest.class);
        suite.addTestSuite(DistributedScenarioTest.class);
        suite.addTestSuite(TrafficMatrixTest.class);
        //suite.addTestSuite(ReoptTest.class);
        suite.addTestSuite(RoutingMatrixTest.class);

        suite.addTestSuite(ECMPTest.class);

        suite.addTestSuite(ScenarioTest.class);

        suite.addTestSuite(ListenersTest.class);

        suite.addTestSuite(HybridStrategiesTest.class);

        suite.addTestSuite(BandwidthUnitTest.class);
        suite.addTestSuite(RoutingPathTest.class);

        suite.addTest(AllTests.suite());
        */

        return new JUnit4TestAdapter(AllTest.class);
    }

}

