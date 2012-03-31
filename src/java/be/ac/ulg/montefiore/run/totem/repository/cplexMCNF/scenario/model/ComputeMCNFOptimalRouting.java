/* CplexMCNF-v3.2 June 18 2008*/

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
package be.ac.ulg.montefiore.run.totem.repository.cplexMCNF.scenario.model;

import ilog.concert.IloConstraint;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import be.ac.ulg.montefiore.run.totem.domain.exception.LinkNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.model.DomainConvertor;
import be.ac.ulg.montefiore.run.totem.domain.model.Link;
import be.ac.ulg.montefiore.run.totem.domain.model.Node;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.DomainConvertorImpl;
import be.ac.ulg.montefiore.run.totem.domain.simplifiedDomain.SimplifiedDomainBuilder;
import be.ac.ulg.montefiore.run.totem.domain.simplifiedDomain.SimplifiedPath;
import be.ac.ulg.montefiore.run.totem.repository.allDistinctRoutes.AllDistinctRoutes;
import be.ac.ulg.montefiore.run.totem.repository.allDistinctRoutes.AllDistinctRoutesException;
import be.ac.ulg.montefiore.run.totem.repository.cplexMCNF.scenario.model.jaxb.impl.ComputeMCNFOptimalRoutingImpl;
import be.ac.ulg.montefiore.run.totem.scenario.exception.EventExecutionException;
import be.ac.ulg.montefiore.run.totem.scenario.model.Event;
import be.ac.ulg.montefiore.run.totem.scenario.model.EventResult;
import be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ParamType;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.InvalidTrafficMatrixException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.facade.TrafficMatrixManager;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.TrafficMatrix;

/*
 * Changes:
 * --------
 *
 *
 */

/**
 * This event calls CPLEX and computes the optimal routing based on the MCNF algorithm.
 * Possible optimization functions can be :
 *   - the "Fortz" objective function
 *   - the "MIRA" objective function
 *   - the "Blanchy" objective function (needs a parameter "alpha" (double))
 *   - the "MeanDelay" objective function (needs a parameter nbInt (int))
 *   - the "WMeanDelay" (Weighted Mean Delay) objective function (needs a parameter nbInt (int))
 *   - the "InvCap" objective function
 *   - the "umax" (maximum utilization) objective function
 *   - the "Degrande" objective function (needs two parameters C_B and C_U (both double))
 *   - the "MinHop" objective function
 * 
 * The computation can be made using the Node-Link formulation or the Link-Path formulation,
 * in which case you have to choose the maximum number of paths used.
 *
 * <p>Creation date: 26-mars-07
 *
 * @author Gregory Fryns (gfryns@student.ulg.ac.be)
 */
public class ComputeMCNFOptimalRouting extends ComputeMCNFOptimalRoutingImpl implements Event {

	private static Logger logger = Logger.getLogger(ComputeMCNFOptimalRouting.class);
	
	private Domain domain;
	private TrafficMatrix tm;
	private List<Node> nodes;
	private List<Link> links;
	private double Capa[];
	                    
	private IloCplex cplex;
	private IloNumVar x[][][], load[], utilization[], specific[][];
	
	private int InLinks[][], OutLinks[][];

	private double OnesArray[], ZerosArray[];
	private double load_sol[], util_sol[], x_sol[][][], specific_sol[][];
	
	private boolean verbose;

	private long time1, time2, time3, time4, time5, time6, time7;

	public EventResult action() throws EventExecutionException {
		// TODO Auto-generated method stub
		/*        logger.info("This method will contain the code for the ComputeMCNFOptimalRouting event!");
        EventResult er = new EventResult();
        er.setMessage("This method will contain the code for the ComputeMCNFOptimalRouting event!");
        return er;
		 */ 
		
		time1 = System.currentTimeMillis();
		// verbose mode?
		if (this.isSetVerbose() && this.isVerbose()) {
			verbose = true;
			System.out.println("Verbose mode");
		}
		else 
			verbose = false;

		logger.info("verbose = " + verbose);
		
		if (this.isSetObjectiveFunction()) {
			String objFun = this.getObjectiveFunction().getName();

			logger.info("Objective function = " + objFun);
			if (verbose) System.out.println("Objective function = " + objFun);

			if (this.isSetFormulation()) {
				time2 = System.currentTimeMillis();
				try {
					this.collectData();
					
				} catch (NodeNotFoundException e) {
					e.printStackTrace();
					logger.info("NodeNotFoundException caught : " + e);
					throw new EventExecutionException();
				} catch (InvalidTrafficMatrixException e) {
					e.printStackTrace();
					logger.info("InvalidTrafficMatrixException caught : " + e);
					throw new EventExecutionException();
				}
				time3 = System.currentTimeMillis();
				// node-link formulation
				if (this.getFormulation().isSetNodeLink()) {

					logger.info("Formulation = Node-Link");
					if (verbose) System.out.println("Formulation = Node-Link");

					try {
						this.nodeLinkOps();

					} catch (IloException e) {
						System.err.println("Concert exception caught: " + e);
						logger.info("Concert exception caught: " + e);
						throw new EventExecutionException();
					} catch (NodeNotFoundException e) {
						e.printStackTrace();
						logger.info("NodeNotFoundException caught : " + e);
						throw new EventExecutionException();
					}
				}

				// link-path formulation
				else if (this.getFormulation().isSetLinkPath()) {

					logger.info("Formulation = Link-Path");
					if (verbose) System.out.println("Formulation = Link-Path");

					if (this.getFormulation().getLinkPath().isSetNbPaths()) {
						int nbPaths = this.getFormulation().getLinkPath().getNbPaths();

						logger.info("nb Paths = " + nbPaths);
						if (verbose) System.out.println("nb Paths = " + nbPaths);

						try {
							this.linkPathOps(nbPaths);

						} catch (AllDistinctRoutesException e) {
							e.printStackTrace();
							logger.info("AllDistinctRoutesException caught : " + e);
							throw new EventExecutionException();
						} catch (NodeNotFoundException e) {
							e.printStackTrace();
							logger.info("NodeNotFoundException caught : " + e);
							throw new EventExecutionException();
						} catch (LinkNotFoundException e) {
							e.printStackTrace();
							logger.info("LinkNotFoundException caught : " + e);
							throw new EventExecutionException();
						} catch (InvalidTrafficMatrixException e) {
							e.printStackTrace();
							logger.info("InvalidTrafficMatrixException caught : " + e);
							throw new EventExecutionException();
						} catch (IloException e) {
							System.err.println("Concert exception caught: " + e);
							logger.info("Concert exception caught: " + e);
							throw new EventExecutionException();
						}
					} 
					else {
						System.err.println("Error : nbPaths not defined");
						logger.info("Error : nbPaths not defined");
						// TODO : maybe use other method when nbPaths is not defined
						throw new EventExecutionException();
					}
				} 
				else {
					System.err.println("Error : no formulation defined");
					logger.info("Error : no formulation defined");
					throw new EventExecutionException();
				}
				time4 = System.currentTimeMillis();
				try {
					// which objective function is called?
					if (objFun.equals("Fortz")) {
						this.minimizeFortzObjective();
					}
					else if (objFun.equals("MIRA")) {
						this.maximizeMIRAObjective();
					}
					else if (objFun.equals("Blanchy")) {
						double alpha = -1;
						if (!this.getObjectiveFunction().isSetParam()) {
							System.out.println("Warning: No parameters defined for BLANCHY Objective function");
							logger.info("Warning: No parameters defined for BLANCHY Objective function");
						}
						else {
							List param = this.getObjectiveFunction().getParam();

							ListIterator iter = param.listIterator();
							while (iter.hasNext()) {
								ParamType paramType = (ParamType) iter.next();
								if (paramType.getName().equals("alpha"))
									alpha = Double.parseDouble(paramType.getValue());
							}
						}

						if (alpha == -1) {
							alpha = 1.0;
							System.out.println("Warning: alpha not defined, using default value = " + alpha);
							logger.info("Warning: alpha not defined, using default value = " + alpha);
						}

						logger.info("alpha = " + alpha);
						if (verbose) System.out.println("alpha = " + alpha);

						this.minimizeBlanchyObjective(alpha);
					}
					else if (objFun.equals("MeanDelay")) {
						int nbInt = -1;
						if (!this.getObjectiveFunction().isSetParam()) {
							System.out.println("Warning: No parameters defined for MeanDelay Objective function");
							logger.info("Warning: No parameters defined for MeanDelay Objective function");
						}
						else {
							List param = this.getObjectiveFunction().getParam();

							ListIterator iter = param.listIterator();
							while (iter.hasNext()) {
								ParamType paramType = (ParamType) iter.next();
								if (paramType.getName().equals("nbInt"))
									nbInt = Integer.parseInt(paramType.getValue());
							}
						}

						if (nbInt == -1) {
							nbInt = 9;
							System.out.println("Warning: nbInt not defined, using default value = " + nbInt);
							logger.info("Warning: nbInt not defined, using default value = " + nbInt);
						}

						logger.info("nbInt = " + nbInt);
						if (verbose) System.out.println("nbInt = " + nbInt);

						this.minimizeMeanDelayObjective(nbInt);
					}
					else if (objFun.equals("WMeanDelay")) {
						int nbInt = -1;
						if (!this.getObjectiveFunction().isSetParam()) {
							System.out.println("Warning: No parameters defined for WMeanDelay Objective function");
							logger.info("Warning: No parameters defined for WMeanDelay Objective function");
						}
						else {
							List param = this.getObjectiveFunction().getParam();

							ListIterator iter = param.listIterator();
							while (iter.hasNext()) {
								ParamType paramType = (ParamType) iter.next();
								if (paramType.getName().equals("nbInt"))
									nbInt = Integer.parseInt(paramType.getValue());
							}
						}

						if (nbInt == -1) {
							nbInt = 9;
							System.out.println("Warning: nbInt not defined, using default value = " + nbInt);
							logger.info("Warning: nbInt not defined, using default value = " + nbInt);
						}

						logger.info("nbInt = " + nbInt);
						if (verbose) System.out.println("nbInt = " + nbInt);

						this.minimizeWMeanDelayObjective(nbInt);
					}
					else if (objFun.equals("InvCap")) {
						this.minimizeInvCapObjective();
					}
					else if (objFun.equals("umax")) {
						this.minimizeUmaxObjective();
					}
					else if (objFun.equals("Degrande")) {
						double C_B = -1;
						double C_U = -1;

						if (!this.getObjectiveFunction().isSetParam()) {
							System.out.println("Warning: No parameters defined for InvCap Objective function");
							logger.info("Warning: No parameters defined for InvCap Objective function");
						}
						else {
							List param = this.getObjectiveFunction().getParam();

							ListIterator iter = param.listIterator();
							while (iter.hasNext()) {
								ParamType paramType = (ParamType) iter.next();
								if (paramType.getName().equals("C_B"))
									C_B = Double.parseDouble(paramType.getValue());
								else if (paramType.getName().equals("C_U"))
									C_U = Double.parseDouble(paramType.getValue());
							}
						}

						if (C_B == -1) {
							C_B = 1.0;
							System.out.println("Warning: C_B not defined, using default value = " + C_B);
							logger.info("Warning: C_B not defined, using default value = " + C_B);
						}
						if (C_U == -1) {
							C_U = 1.0;
							System.out.println("Warning: C_U not defined, using default value = " + C_U);
							logger.info("Warning: C_U not defined, using default value = " + C_U);
						}

						logger.info("C_B = " + C_B);
						logger.info("C_U = " + C_U);
						if (verbose) {
							System.out.println("C_B = " + C_B);
							System.out.println("C_U = " + C_U);
						}

						this.minimizeDegrandeObjective(C_B, C_U);
					}
					else if (objFun.equals("MinHop")) {
						this.minimizeMinHopObjective();
					}
					else {
						System.err.println("Error : unknown objective function");
						logger.info("Error : unknown objective function");
						throw new EventExecutionException();
					}

					// export model to file    /!\ must be a *.lp file
					if (this.isSetExportModelToFile()) {
						cplex.exportModel(this.getExportModelToFile());
						
						logger.info("Model exported to file : " + this.getExportModelToFile());
						if (verbose) System.out.println("Model exported to file : " + this.getExportModelToFile());
					}
					time5 = System.currentTimeMillis();

					this.optimize();

					time6 = System.currentTimeMillis();
					
					if (this.isSetDisplayTEMetrics() && this.isDisplayTEMetrics()) {
						
						logger.info("DisplayTEMetrics = true");
						if (verbose) System.out.println("DisplayTEMetrics = true");
						this.displayTEMetrics();
					}
					else {
						logger.info("DisplayTEMetrics = false");
						if (verbose) System.out.println("DisplayTEMetrics = false");
					}


				} catch (IloException e) {
					System.err.println("Concert exception caught: " + e);
					logger.info("Concert exception caught: " + e);
					throw new EventExecutionException();
				}
			}
		}
		else {
			System.err.println("No objective function defined!");
			logger.info("No objective function defined!");
			throw new EventExecutionException();
		}
		time7 = System.currentTimeMillis();
/*
		System.out.println();
		System.out.println("total time = " + (time7 - time1));
		System.out.println("collectData time = " + (time3 - time2));
		System.out.println("common constraints time = " + (time4 - time3));
		System.out.println("specific constraints time = " + (time5 - time4));
		System.out.println("optimization time = " + (time6 - time5));
*/
		EventResult er = new EventResult();
		return er;
	}

	/**
	 * Collects useful data from default domain
	 * 
	 * @throws NodeNotFoundException
	 * @throws InvalidTrafficMatrixException
	 */
	private void collectData() throws NodeNotFoundException,
	InvalidTrafficMatrixException {

		// Get required data
		domain = InterDomainManager.getInstance().getDefaultDomain();
		tm = TrafficMatrixManager.getInstance().getDefaultTrafficMatrix();

		nodes = domain.getAllNodes();
		links = domain.getAllLinks();

		InLinks = new int[nodes.size()][links.size()];
		OutLinks = new int[nodes.size()][links.size()];
		Capa = new double[links.size()];
		// OnesArray = array of [links.size()] "1.0"'s
		// ZerosArray = array of [links.size()] "0.0"'s
		OnesArray = new double[links.size()];
		ZerosArray = new double[links.size()];

		////////////////////////////////////////////////
		// Parameter initialization
		////////////////////////////////////////////////

		for (int i = 0; i < links.size(); ++i) {
			OnesArray[i] = 1.0;
			ZerosArray[i] = 0.0;
			// Capa[i] = capacity of the link i
			Capa[i] = links.get(i).getBandwidth();
		}
		for (int i = 0; i < nodes.size(); ++i) {
			for (int j = 0; j < links.size(); ++j) {
				// InLinks[i][j] = 1 if link j enters the node i
				if (nodes.get(i) == links.get(j).getDstNode()) {
					InLinks[i][j] = 1;
					OutLinks[i][j] = 0;
				}
				// OutLinks[i][j] = 1 if link j leaves the node i
				else if (nodes.get(i) == links.get(j).getSrcNode()) {
					InLinks[i][j] = 0;
					OutLinks[i][j] = 1;
				}

				else {
					InLinks[i][j] = 0;
					OutLinks[i][j] = 0;
				}
			}

			if (verbose) {
				for (int j = 0; j < nodes.size(); ++j) {
					System.out.println("Demand from " + nodes.get(i).getId() + " to " + nodes.get(j).getId() + " = " + tm.get(i,j));
				}
			}
		}
	}

	/**
	 * Sets common variables and constraints for the Node-Link formulation
	 * 
	 * @throws IloException
	 */
	private void nodeLinkOps() throws IloException, NodeNotFoundException {
		cplex = new IloCplex();
		
		// use network optimizer
//		 cplex.setParam(IloCplex.IntParam.RootAlg, 3);

		///////////////////////////////////////////////////////
		// Common variables for the Node-Link implementation
		///////////////////////////////////////////////////////

		// namei are used to give names to the variables
		String[][][] name1 = new String[nodes.size()][nodes.size()][links.size()];

		for (int u = 0; u < nodes.size(); ++u) {
			for (int v = 0; v < nodes.size(); ++v) {
				for (int l = 0; l < links.size(); ++l) {
					name1[u][v][l] = "x(" + nodes.get(u).getId() + "," + nodes.get(v).getId() + "," + links.get(l).getId() + ")";
				}
			}
		}
		String[] name2 = new String[links.size()];
		String[] name3 = new String[links.size()];

		for (int i = 0; i < links.size(); ++i) {
			name2[i] = "load(" + links.get(i).getId() + ")";
			name3[i] = "utilization(" + links.get(i).getId() + ")";

		}

		// x(u,v,l) >= 0
		x = new IloNumVar[nodes.size()][nodes.size()][];
		for (int src = 0; src < nodes.size(); ++src) {
			for (int dst = 0; dst < nodes.size(); ++dst) {
				x[src][dst] = cplex.numVarArray(links.size(), 0.0, Double.MAX_VALUE, name1[src][dst]);
			}
		}

		// 0 <= load(l) <= Capa(l)
		load = cplex.numVarArray(links.size(), ZerosArray, Capa, name2);
		// 0 <= utilization(l) <= 1
		utilization = cplex.numVarArray(links.size(), 0.0, 1.0, name3);

		///////////////////////////////////////////////////////
		// Common constraints for the Node-Link implementation
		///////////////////////////////////////////////////////

		// Flow conservation
		IloLinearNumExpr flow, dem;
		IloConstraint[][][] flowConservationC = new IloConstraint[nodes.size()][nodes.size()][nodes.size()];

		for (int n = 0; n < nodes.size(); ++n) {
			for (int src = 0; src < nodes.size(); ++src) {
				for (int dst = 0; dst < nodes.size(); ++dst) {
					if (src == dst)
						tm.set(src, dst, 0);
					
					flow = cplex.linearNumExpr();
					if (n == dst) {
						dem = cplex.linearNumExpr(tm.get(src,dst));
					} else if (n == src) {
						dem = cplex.linearNumExpr((-1.0 * tm.get(src,dst)));
					} else
						dem = cplex.linearNumExpr(0.0);
					for (int l = 0; l < links.size(); ++l) {
						if (InLinks[n][l] == 1)
							flow.addTerm(1.0, x[src][dst][l]);
						else if (OutLinks[n][l] == 1)
							flow.addTerm(-1.0, x[src][dst][l]);
					}
					flowConservationC[src][dst][n] = cplex.addEq(dem, flow,
							("Flow conservation for node " + nodes.get(n).getId()
							                                          + ", with src = " + nodes.get(src).getId()
							                                                                       + " and dst = " + nodes.get(dst).getId()));
				}
			}
		}

		// Load and utilization constraints
		IloLinearNumExpr calcLoad, calcUtil;
		IloConstraint[] loadC = new IloConstraint[links.size()];
		IloConstraint[] utilC = new IloConstraint[links.size()];

		for (int l = 0; l < links.size(); ++l) {
			calcLoad = cplex.linearNumExpr();
			for (int u = 0; u < nodes.size(); ++u) {
				for (int v = 0; v < nodes.size(); ++v) {
					calcLoad.addTerm(1.0, x[u][v][l]);
				}
			}

			loadC[l] = cplex.addEq(load[l], calcLoad,
					("Load of link " + links.get(l).getId()));

			calcUtil = cplex.linearNumExpr();
			calcUtil.addTerm(1 / Capa[l], load[l]);

			utilC[l] = cplex.addEq(utilization[l], calcUtil,("Utilization of link " + links.get(l).getId()));
		}
	}

	/**
	 * Collects additional data for the Link-Path formulation, then
	 * sets common variables and constraints for the Link-Path formulation
	 * 
	 * @throws AllDistinctRoutesException
	 * @throws LinkNotFoundException
	 * @throws NodeNotFoundException
	 * @throws InvalidTrafficMatrixException
	 * @throws IloException
	 */
	private void linkPathOps(int nbPaths) throws IloException, AllDistinctRoutesException, LinkNotFoundException, NodeNotFoundException, InvalidTrafficMatrixException {

		// get link-path specific data

		ArrayList[][] paths = (ArrayList<SimplifiedPath>[][])new ArrayList[nodes.size()][nodes.size()];

		AllDistinctRoutes allRoutes = new AllDistinctRoutes();
		DomainConvertor convertor = new DomainConvertorImpl(domain);
		int[] linkIds;
		// computeAllDistinctRoute can only be used with a simplified domain
		allRoutes.computeAllDistinctRoute(SimplifiedDomainBuilder.build(domain), nodes.size(), nbPaths, verbose);

		int[][][][] InPath = new int[links.size()][nodes.size()][nodes.size()][];
		for (int src = 0; src < nodes.size(); ++src) {
			for (int dst = 0; dst < nodes.size(); ++dst) {
				paths[src][dst] = (ArrayList) allRoutes.getAllDistinctRoutes(src, dst);
				if (paths[src][dst] == null)
					paths[src][dst] = new ArrayList(0);

				for (int l = 0; l < links.size(); ++l) {
					InPath[l][src][dst] = new int[paths[src][dst].size()];
					for (int i = 0; i < paths[src][dst].size(); ++i) {
						linkIds = ((ArrayList<SimplifiedPath>) paths[src][dst]).get(i).getLinkIdPath();
						InPath[l][src][dst][i] = 0;

						// if the current path contains the link, sets InPath[][][][] to 1
						for (int j = 0; j < linkIds.length; ++j) {
							if (links.get(l).getId().equals(convertor.getLinkId(linkIds[j])))
								InPath[l][src][dst][i] = 1;
							
						}	
					}
				}
			}
		}
		
		///////////////////////////////////////////////////////
		// Common variables for the Link-Path implementation
		///////////////////////////////////////////////////////
		cplex = new IloCplex();

		// use network optimizer
//		 cplex.setParam(IloCplex.IntParam.RootAlg, 3);

		// namei are used to give names to the variables
		String[][][] name1 = new String[nodes.size()][nodes.size()][];

		for (int u = 0; u < nodes.size(); ++u) {
			for (int v = 0; v < nodes.size(); ++v) {
				name1[u][v] = new String[paths[u][v].size()];
				for (int i = 0; i < paths[u][v].size(); ++i) {
					name1[u][v][i] = "load of path n " + i + " from node " + nodes.get(u).getId() + " to node "	+ nodes.get(v).getId();
				}
			}
		}
		String[] name2 = new String[links.size()];
		String[] name3 = new String[links.size()];

		for (int i = 0; i < links.size(); ++i) {
			name2[i] = "load(" + links.get(i).getId() + ")";
			name3[i] = "utilization(" + links.get(i).getId() + ")";

		}

		// x(u,v,i) >= 0
		x = new IloNumVar[nodes.size()][nodes.size()][];
		for (int src = 0; src < nodes.size(); ++src) {
			for (int dst = 0; dst < nodes.size(); ++dst) {
					x[src][dst] = cplex.numVarArray(paths[src][dst].size(), 0.0d, Double.MAX_VALUE, name1[src][dst]);
			}
		}

		// 0 <= load(l) <= Capa(l)
		load = cplex.numVarArray(links.size(), ZerosArray, Capa, name2);
		// 0 <= utilization(l) <= 1
		utilization = cplex.numVarArray(links.size(), ZerosArray, OnesArray, name3);

		///////////////////////////////////////////////////////
		// Common constraints for the Link-Path implementation
		///////////////////////////////////////////////////////

		// Demand satisfaction
		IloLinearNumExpr flow, dem;
		IloConstraint[][] flowConservationC = new IloConstraint[nodes.size()][nodes.size()];

		for (int src = 0; src < nodes.size(); ++src) {
			for (int dst = 0; dst < nodes.size(); ++dst) {
				if (src == dst)
					tm.set(src, dst, 0);
				
				dem = cplex.linearNumExpr(tm.get(src,dst));
				flow = cplex.linearNumExpr();
				if (paths[src][dst] != null) {
					for (int i = 0; i < paths[src][dst].size(); ++i) {
						flow.addTerm(1.0, x[src][dst][i]);
					}
				}
				flowConservationC[src][dst] = cplex.addEq(dem, flow,
						("Demand satisfaction for src = " + nodes.get(src).getId() + " and dst = " + nodes.get(dst).getId()));
			}
		}


		// Load and utilization constraints
		IloLinearNumExpr calcLoad, calcUtil;
		IloConstraint[] loadC = new IloConstraint[links.size()];
		IloConstraint[] utilC = new IloConstraint[links.size()];

		for (int l = 0; l < links.size(); ++l) {
			calcLoad = cplex.linearNumExpr();
			for (int src = 0; src < nodes.size(); ++src) {
				for (int dst = 0; dst < nodes.size(); ++dst) {
					for (int i = 0; i < paths[src][dst].size(); ++i) {
						calcLoad.addTerm(InPath[l][src][dst][i], x[src][dst][i]);
					}
				}
			}

			loadC[l] = cplex.addEq(load[l], calcLoad, ("Load of link " + links.get(l).getId()));

			calcUtil = cplex.linearNumExpr();
			calcUtil.addTerm(1 / Capa[l], load[l]);

			utilC[l] = cplex.addEq(utilization[l], calcUtil, ("Utilization of link " + links.get(l).getId()));
		}
	}

	/**
	 * Computes and displays the traffic engineering metrics
	 * 
	 */

	private void displayTEMetrics() {
		double umax = util_sol[0];
		double utot = util_sol[0];
		double ABWmin = Capa[0] - load_sol[0];
		double ltot = load_sol[0];
		double theta_sol[] = new double[nodes.size() * nodes.size()];
		double theta_tot = 0;

		for (int i=1; i < links.size(); ++i) {
			if (util_sol[i] > umax)
				umax = util_sol[i];

			utot = utot + util_sol[i];
			ltot = ltot + load_sol[i];

			if (Capa[i] - load_sol[i] < ABWmin)
				ABWmin = Capa[i] - load_sol[i];
		}

		// copy the utilizations list and sort it
		double[] sortedUtil = (double[]) util_sol.clone();
		triRapide(sortedUtil);

		double uper10 = sortedUtil[((Double)(0.9 * links.size())).intValue()];

		// compute the max-flows

		////////////////////////////////////////////////
		// variables
		////////////////////////////////////////////////
		try {
			IloCplex maxFlow = new IloCplex();
			
			// use network optimizer
			 maxFlow.setParam(IloCplex.IntParam.RootAlg, 3);

			String[] name1 = new String[nodes.size() * nodes.size() * links.size()];
			String[] name2 = new String[nodes.size() * nodes.size()];

			for (int s = 0; s < nodes.size(); ++s) {
				for (int t = 0; t < nodes.size(); ++t) {
					for (int l = 0; l < links.size(); ++l) {
						name1[s + t * nodes.size() + l * nodes.size()
						      * nodes.size()] = "v(" + nodes.get(s).getId() + ","
						      + nodes.get(t).getId() + "," + links.get(l).getId() + ")";
					}
					name2[s + t * nodes.size()] = "theta(" + nodes.get(s).getId() + ","
					+ nodes.get(t).getId() + ")";
				}
			}

			// v(s,t,l) >= 0
			IloNumVar[] v = maxFlow.numVarArray(nodes.size() * nodes.size() * links.size(), 0.0, Double.MAX_VALUE, name1);
			// theta(s,t) >= 0
			IloNumVar[] theta = maxFlow.numVarArray(nodes.size() * nodes.size(), 0.0,	Double.MAX_VALUE, name2);

			
			////////////////////////////////////////////////
			// constraints
			////////////////////////////////////////////////

			// maxFlow conservation at node n
			IloLinearNumExpr th, partMaxFlow;
			IloConstraint[][][] maxFlowC = new IloConstraint[nodes.size()][nodes.size()][nodes.size()];

			for (int n = 0; n < nodes.size(); ++n) {
				for (int src = 0; src < nodes.size(); ++src) {
					for (int dst = 0; dst < nodes.size(); ++dst) {
						partMaxFlow = maxFlow.linearNumExpr();
						th = maxFlow.linearNumExpr();
						if (n == dst)
							th.addTerm(1.0, theta[src + nodes.size() * dst]);
						else if (n == src)
							th.addTerm(-1.0, theta[src + nodes.size() * dst]);
						for (int l = 0; l < links.size(); ++l) {
							if (InLinks[n][l] == 1)
								partMaxFlow.addTerm(1.0, v[src + dst * nodes.size() + l * nodes.size() * nodes.size()]);
							else if (OutLinks[n][l] == 1)
								partMaxFlow.addTerm(-1.0, v[src + dst * nodes.size() + l * nodes.size() * nodes.size()]);
						}
						maxFlowC[src][dst][n] = maxFlow.addEq(th, partMaxFlow, ("maxFlow conservation for node " + nodes.get(n).getId() + ", with src = " + nodes.get(src).getId() + " and dst = " + nodes.get(dst).getId()));
					}
				}
			}

			// Skip max flow from a node to self
			IloConstraint skipC[] = new IloConstraint[nodes.size()];

			for (int i = 0; i < nodes.size(); ++i) {
				skipC[i] = maxFlow.addEq(0.0, theta[i * (nodes.size() + 1)], "Skip maxFlow from node " + nodes.get(i).getId() + " to self");
			}

			// MaxFlow upper bound of v(a) for a given src-dst pair
			IloConstraint linkMaxFlowC[] = new IloConstraint[nodes.size() * nodes.size() * links.size()];

			for (int l = 0; l < links.size(); ++l) {
				for (int src = 0; src < nodes.size(); ++src) {
					for (int dst = 0; dst < nodes.size(); ++dst) {
						linkMaxFlowC[src + dst * nodes.size() + l * nodes.size() * nodes.size()] 
						             = maxFlow.addLe(v[src + dst * nodes.size() + l * nodes.size() * nodes.size()], Capa[l]-load_sol[l], 
						            		 "MaxFlow upper bound for link " + links.get(l).getId() + ", with src = " 
						            		 + nodes.get(src).getId() + " and dst = " + nodes.get(dst).getId());
					}
				}

			}
			
			////////////////////////////////////////////////
			// solve the problem
			////////////////////////////////////////////////
			maxFlow.addMaximize(maxFlow.sum(theta), "max flows");
			
			////////////////////////////////////////////////
			// get the results
			////////////////////////////////////////////////
			
			if (maxFlow.solve()) {
				theta_sol = maxFlow.getValues(theta);
			}
			else {
				System.out.println("Error computing the max-flows : " + maxFlow.getStatus().toString());
			}
		} catch (IloException e) {
			System.err.println("Concert exception caught: " + e);
			System.out.println("displayTEMetrics : couldn't compute the max flows.");
		}

		for (int i=0; i<theta_sol.length; ++i) {
			theta_tot = theta_tot + theta_sol[i];
		}

		System.out.println("umax = " + 100 * umax + " %");
		System.out.println("uper10 = " + 100 * uper10 + " %");
		System.out.println("umean = " + 100 * (utot / links.size()) + " %");
		System.out.println("ABWmin = " + ABWmin); // TODO : ajouter unités
		System.out.println("lmean = " + ltot / links.size()); // TODO : ajouter unités
		System.out.println("theta_tot = " + theta_tot); // TODO : ajouter unités
	}

	/**
	 * Computes and displays the results
	 * 
	 * @throws EventExecutionException
	 * @throws IloException
	 */
	private void optimize() throws EventExecutionException, IloException {
		// solve the model and display the solution if one was found
		if (cplex.solve()) {

			////////////////////////////////////////////////
			// Displays common results
			////////////////////////////////////////////////

			load_sol = cplex.getValues(load);
			util_sol = cplex.getValues(utilization);

			cplex.output().println("Solution status = " + cplex.getStatus());
			cplex.output().println("Solution value  = " + cplex.getObjValue());
			if (verbose) {
				x_sol = new double[nodes.size()][nodes.size()][];
				for (int u = 0; u < nodes.size(); ++u) {
					for (int v = 0; v < nodes.size(); ++v) {
						x_sol[u][v] = cplex.getValues(x[u][v]);
						for (int l = 0; l < x[u][v].length; ++l) {
							cplex.output().print("x(" + u + "," + v + "," + l + ") = " + x_sol[u][v][l] + "\t");
						}
						System.out.println();
					}
				}
			}
			for (int i = 0; i < links.size(); ++i) {
				cplex.output().println("load[" + links.get(i).getId() + "] = " + load_sol[i] + "\tutilization[" + links.get(i).getId() + "] = " + util_sol[i]);
			}
			////////////////////////////////////////////////
			// Displays specific results
			////////////////////////////////////////////////

			System.out.println("Specific results");
			// all specific results are placed in the specific[][] table
			specific_sol = new double[specific.length][];

			for (int i = 0; i < specific.length; ++i) {
				specific_sol[i] = cplex.getValues(specific[i]);
				for (int j = 0; j < specific[i].length; ++j) {
					cplex.output().println(specific[i][j].getName() + " = "	+ specific_sol[i][j]);
				}
			}
		} 
		else {
			System.out.println("Error solving the problem");
			System.out.println("Solution status : " + cplex.getStatus().toString());
			throw new EventExecutionException();
		}
	}

	/**
	 * Minimize Fortz Objective function on the default domain and default
	 * traffic matrix
	 * 
	 * @throws IloException
	 */
	private void minimizeFortzObjective() throws IloException {

		////////////////////////////////////////////////
		// Fortz specific variables
		////////////////////////////////////////////////

		specific = new IloNumVar[1][];

		String[] name4 = new String[links.size()];
		for (int i = 0; i < links.size(); ++i) {
			name4[i] = "phi_a(" + links.get(i).getId() + ")";
		}

		// phi_a(l) >= 0	
		specific[0] = cplex.numVarArray(links.size(), 0.0, Double.MAX_VALUE, name4);

		////////////////////////////////////////////////
		// Fortz specific constraints
		////////////////////////////////////////////////
		IloLinearNumExpr min_cost1, min_cost2, min_cost3, min_cost4, min_cost5, min_cost6;
		IloConstraint Fortz1[] = new IloConstraint[links.size()];
		IloConstraint Fortz2[] = new IloConstraint[links.size()];
		IloConstraint Fortz3[] = new IloConstraint[links.size()];
		IloConstraint Fortz4[] = new IloConstraint[links.size()];
		IloConstraint Fortz5[] = new IloConstraint[links.size()];
		IloConstraint Fortz6[] = new IloConstraint[links.size()];

		for (int l = 0; l < links.size(); ++l) {
			min_cost1 = cplex.linearNumExpr();
			min_cost1.addTerm(1.0, load[l]);

			min_cost2 = cplex.linearNumExpr();
			min_cost2.setConstant(-0.666666667 * Capa[l]);
			min_cost2.addTerm(3.0, load[l]);

			min_cost3 = cplex.linearNumExpr();
			min_cost3.setConstant(-5.333333333 * Capa[l]);
			min_cost3.addTerm(10.0, load[l]);

			min_cost4 = cplex.linearNumExpr();
			min_cost4.setConstant(-59.33333333 * Capa[l]);
			min_cost4.addTerm(70.0, load[l]);

			min_cost5 = cplex.linearNumExpr();
			min_cost5.setConstant(-489.3333333 * Capa[l]);
			min_cost5.addTerm(500.0, load[l]);

			min_cost6 = cplex.linearNumExpr();
			min_cost6.setConstant(-5439.333333 * Capa[l]);
			min_cost6.addTerm(5000.0, load[l]);

			Fortz1[l] = cplex.addGe(specific[0][l], min_cost1,
					("Fortz1 for link " + links.get(l).getId()));
			Fortz2[l] = cplex.addGe(specific[0][l], min_cost2,
					("Fortz2 for link " + links.get(l).getId()));
			Fortz3[l] = cplex.addGe(specific[0][l], min_cost3,
					("Fortz3 for link " + links.get(l).getId()));
			Fortz4[l] = cplex.addGe(specific[0][l], min_cost4,
					("Fortz4 for link " + links.get(l).getId()));
			Fortz5[l] = cplex.addGe(specific[0][l], min_cost5,
					("Fortz5 for link " + links.get(l).getId()));
			Fortz6[l] = cplex.addGe(specific[0][l], min_cost6,
					("Fortz6 for link " + links.get(l).getId()));
		}

		////////////////////////////////////////////////
		// Fortz objective function
		////////////////////////////////////////////////
		System.out.println("***Minimize Fortz objective function***");
		cplex.addMinimize(cplex.sum(specific[0]), "Fortz objective function");

	}

	/**
	 * Maximize MIRA Objective function on the default domain and default
	 * traffic matrix
	 * 
	 * @throws IloException
	 */
	private void maximizeMIRAObjective() throws IloException {

		////////////////////////////////////////////////
		// MIRA specific variables
		////////////////////////////////////////////////

		specific = new IloNumVar[2][];

		String[] name4 = new String[nodes.size() * nodes.size() * links.size()];
		String[] name5 = new String[nodes.size() * nodes.size()];

		for (int s = 0; s < nodes.size(); ++s) {
			for (int t = 0; t < nodes.size(); ++t) {
				for (int l = 0; l < links.size(); ++l) {
					name4[s + t * nodes.size() + l * nodes.size()
					      * nodes.size()] = "v(" + nodes.get(s).getId() + ","
					      + nodes.get(t).getId() + "," + links.get(l).getId() + ")";
				}
				name5[s + t * nodes.size()] = "theta(" + nodes.get(s).getId() + ","
					  + nodes.get(t).getId() + ")";
			}
		}

		// v(s,t,l) >= 0
		specific[0] = cplex.numVarArray(nodes.size() * nodes.size()	* links.size(),
				0.0, Double.MAX_VALUE, name4);
		// 0 <= theta(s,t)
		specific[1] = cplex.numVarArray(nodes.size() * nodes.size(), 0.0,
				Double.MAX_VALUE, name5);

		////////////////////////////////////////////////
		// MIRA specific constraints
		////////////////////////////////////////////////

		// maxFlow conservation at a node n
		IloLinearNumExpr th, partMaxFlow;
		IloConstraint[][][] maxFlowC = new IloConstraint[nodes.size()][nodes.size()][nodes.size()];

		for (int n = 0; n < nodes.size(); ++n) {
			for (int src = 0; src < nodes.size(); ++src) {
				for (int dst = 0; dst < nodes.size(); ++dst) {
					partMaxFlow = cplex.linearNumExpr();
					th = cplex.linearNumExpr();
					if (n == dst)
						th.addTerm(1.0, specific[1][src + nodes.size() * dst]);
					else if (n == src)
						th.addTerm(-1.0, specific[1][src + nodes.size() * dst]);
					for (int l = 0; l < links.size(); ++l) {
						if (InLinks[n][l] == 1)
							partMaxFlow.addTerm(1.0, specific[0][src + dst
							                                     * nodes.size() + l * nodes.size()
							                                     * nodes.size()]);
						else if (OutLinks[n][l] == 1)
							partMaxFlow.addTerm(-1.0, specific[0][src + dst
							                                      * nodes.size() + l * nodes.size()
							                                      * nodes.size()]);
					}
					maxFlowC[src][dst][n] = cplex.addEq(th, partMaxFlow,
							("maxFlow conservation for node " + nodes.get(n).getId()
							                                             + ", with src = " + nodes.get(src).getId()
							                                                                          + " and dst = " + nodes.get(dst).getId()));
				}
			}
		}

		// Skip max flow from a node to self
		IloConstraint skipC[] = new IloConstraint[nodes.size()];

		for (int i = 0; i < nodes.size(); ++i) {
			skipC[i] = cplex.addEq(0.0, specific[1][i * (nodes.size() + 1)],
					"Skip maxFlow from node " + nodes.get(i).getId() + " to self");
		}

		// MaxFlow upper bound of v(a) for a given src-dst pair
		IloLinearNumExpr bound;
		IloConstraint linkMaxFlowC[] = new IloConstraint[nodes.size()
		                                                 * nodes.size() * links.size()];

		for (int l = 0; l < links.size(); ++l) {
			// upper bound = C(a) - l(a)
			bound = cplex.linearNumExpr(Capa[l]);
			bound.addTerm(-1.0, load[l]);

			for (int src = 0; src < nodes.size(); ++src) {
				for (int dst = 0; dst < nodes.size(); ++dst) {
					linkMaxFlowC[src + dst * nodes.size() + l
					             * nodes.size() * nodes.size()] = cplex.addLe(
					            		 specific[0][src + dst * nodes.size() + l
					            		             * nodes.size() * nodes.size()],
					            		             bound, "MaxFlow upper bound for link " + links.get(l).getId()
					            		                                                           + ", with src = " + nodes.get(src).getId()
					            		                                                                                        + " and dst = " + nodes.get(dst).getId());
				}
			}

		}

		////////////////////////////////////////////////
		// MIRA objective function
		////////////////////////////////////////////////

		System.out.println("***Maximize MIRA objective function***");
		cplex.addMaximize(cplex.sum(specific[1]), "MIRA objective function");

	}

	/**
	 * Minimize Blanchy Objective function on the default domain and default
	 * traffic matrix
	 * 
	 * @param alpha
	 * @throws IloException
	 */
	private void minimizeBlanchyObjective(double alpha) throws IloException {

		////////////////////////////////////////////////
		// Blanchy specific variables
		////////////////////////////////////////////////

		specific = new IloNumVar[1][];

		String[] name4 = new String[links.size()];
		String[] name5 = new String[links.size()];

		for (int l = 0; l < links.size(); ++l) {
			name4[l] = "(u(" + links.get(l).getId() + ") - u_mean)^2";
			name5[l] = "(u(" + links.get(l).getId() + "))^2";
		}

		// 0 <= u_mean <= 1
		specific[0] = new IloNumVar[1];
		specific[0][0] = cplex.numVar(0.0, 1.0, "u_mean");

		////////////////////////////////////////////////
		// Blanchy specific constraints
		////////////////////////////////////////////////

		// computes u_mean
		IloNumExpr u_meanCalc;
		IloConstraint uMeanC[] = new IloConstraint[1];

		u_meanCalc = cplex.prod((1 / (double) links.size()), cplex.sum(utilization));
		uMeanC[0] = cplex.addEq(u_meanCalc, specific[0][0], "u_mean");

		// computes sum_a (u_a - u_mean) and sum_a (u_a)
		IloLinearNumExpr left_sqrt;
		IloNumExpr[] calcLeft = new IloNumExpr[links.size()];
		IloNumExpr[] calcRight = new IloNumExpr[links.size()];

		for (int l = 0; l < links.size(); ++l) {
			// (u_a - u_mean)^2
			left_sqrt = cplex.linearNumExpr();
			left_sqrt.addTerm(1.0, utilization[l]);
			left_sqrt.addTerm(-1.0, specific[0][0]);

			calcLeft[l] = cplex.square(left_sqrt);

			// (u_a)^2
			calcRight[l] = cplex.square(utilization[l]);
		}

		////////////////////////////////////////////////
		// Blanchy objective function
		////////////////////////////////////////////////
		IloNumExpr objective = cplex.sum(cplex.sum(calcLeft), cplex.prod(alpha, cplex.sum(calcRight)));
		System.out.println("***Minimize Blanchy objective function***");
		// on multiplie l'objectif par 1000 de manière à compenser le manque de précision de l'optimiseur
		// TODO : vérifier s'il n'y a pas moyen de régler la précision de l'optimiseur
		cplex.addMinimize(cplex.prod(1000, objective), "Blanchy objective function");

	}

	/**
	 * Minimize MeanDelay Objective function on the default domain and default
	 * traffic matrix
	 * 
	 * @param nbInt
	 * @throws IloException
	 */
	private void minimizeMeanDelayObjective(int nbInt) throws IloException {

		////////////////////////////////////////////////
		// MeanDelay specific variables
		////////////////////////////////////////////////

		specific = new IloNumVar[1][];

		String[] name4 = new String[links.size()];
		for (int i = 0; i < links.size(); ++i) {
			name4[i] = "phi_a(" + links.get(i).getId() + ")";
		}

		// phi_a(l) >= 0	
		specific[0] = cplex.numVarArray(links.size(), 0.0, Double.MAX_VALUE, name4);

		////////////////////////////////////////////////
		// MeanDelay specific constraints
		////////////////////////////////////////////////
		IloLinearNumExpr[] min_cost = new IloLinearNumExpr[nbInt];
		IloConstraint[][] MeanDelayC = new IloConstraint[links.size()][];
		
		for (int l = 0; l < links.size(); ++l) {

			// approximation de la fonction pour le lien l
			double pas = Capa[l] / nbInt;

			MeanDelayC[l] = new IloConstraint[nbInt];

			for (int n = 0; n < (nbInt - 1); ++n) {
				min_cost[n] = cplex.linearNumExpr();

				// calcul de la pente entre les 2 points
				double y0 = 1 / (Capa[l] - pas * n);
				double y1 = 1 / (Capa[l] - pas * (n + 1));

				double a = (y1 - y0) / pas;

				// on force le passage de cette droite par (xn, yn)
				double b = y0 - a * (pas * n);

				// min_cost[n] = a * load(l) + b
				min_cost[n].setConstant(b);
				min_cost[n].addTerm(a, load[l]);

				MeanDelayC[l][n] = cplex.addGe(specific[0][l], min_cost[n]);
			}

			// dernier tronçon
			min_cost[nbInt - 1] = cplex.linearNumExpr();

			// calcul de la pente du tronçon
			//  = pente de la fonction au milieu de celui-ci
			double a = 4 / (pas * pas);

			// on force le passage de cette droite par (x(nb-1), y(nb-1))
			double b = 1 / (Capa[l] - pas * (nbInt - 1)) - a * (pas * (nbInt - 1));

			min_cost[nbInt - 1].setConstant(b);
			min_cost[nbInt - 1].addTerm(a, load[l]);

			MeanDelayC[l][nbInt - 1] = cplex.addGe(specific[0][l], min_cost[nbInt - 1]);
		}

		////////////////////////////////////////////////
		// MeanDelay objective function
		////////////////////////////////////////////////
		System.out.println("***Minimize MeanDelay objective function***");
		// on multiplie l'objectif par 10000 de manière à compenser le manque de précision de l'optimiseur
		// TODO : vérifier s'il n'y a pas moyen de régler la précision de l'optimiseur
		cplex.addMinimize(cplex.prod(10000,cplex.sum(specific[0])), "MeanDelay objective function");

	}

	/**
	 * Minimize WMeanDelay Objective function on the default domain and default
	 * traffic matrix
	 * 
	 * @param nbInt
	 * @throws IloException
	 */
	private void minimizeWMeanDelayObjective(int nbInt) throws IloException {

		////////////////////////////////////////////////
		// WMeanDelay specific variables
		////////////////////////////////////////////////

		specific = new IloNumVar[1][];

		String[] name4 = new String[links.size()];
		for (int i = 0; i < links.size(); ++i) {
			name4[i] = "phi_a(" + links.get(i).getId() + ")";
		}

		// phi_a(l) >= 0	
		specific[0] = cplex.numVarArray(links.size(), 0.0, Double.MAX_VALUE,	name4);

		////////////////////////////////////////////////
		// WMeanDelay specific constraints
		////////////////////////////////////////////////
		IloLinearNumExpr[] min_cost = new IloLinearNumExpr[nbInt];
		IloConstraint[][] WMeanDelayC = new IloConstraint[links.size()][];

		for (int l = 0; l < links.size(); ++l) {

			// approximation de la fonction pour le lien l
			double pas = Capa[l] / nbInt;

			WMeanDelayC[l] = new IloConstraint[nbInt];

			for (int n = 0; n < (nbInt - 1); ++n) {
				min_cost[n] = cplex.linearNumExpr();

				// calcul de la pente entre les 2 points
				double y0 = (pas * n) / (Capa[l] - pas * n);
				double y1 = (pas * (n + 1)) / (Capa[l] - pas * (n + 1));

				double a = (y1 - y0) / pas;

				// on force le passage de cette droite par (xn, yn)
				double b = y0 - a * (pas * n);

				// min_cost[n] = a * load(l) + b
				min_cost[n].setConstant(b);
				min_cost[n].addTerm(a, load[l]);

				WMeanDelayC[l][n] = cplex.addGe(specific[0][l], min_cost[n]);
			}

			// dernier tronçon
			min_cost[nbInt - 1] = cplex.linearNumExpr();

			// calcul de la pente du tronçon
			//  = pente de la fonction au milieu de celui-ci
			double a = (4 * Capa[l]) / (pas * pas);

			// on force le passage de cette droite par (x(nb-1), y(nb-1))
			double b = (pas * (nbInt-1)) / (Capa[l] - pas * (nbInt-1)) - a * pas * (nbInt-1);

			min_cost[nbInt - 1].setConstant(b);
			min_cost[nbInt - 1].addTerm(a, load[l]);

			WMeanDelayC[l][nbInt - 1] = cplex.addGe(specific[0][l],
					min_cost[nbInt - 1]);
		}

		////////////////////////////////////////////////
		// WMeanDelay objective function
		////////////////////////////////////////////////
		System.out.println("***Minimize WMeanDelay objective function***");
		
		cplex.addMinimize(cplex.sum(specific[0]), "WMeanDelay objective function");

	}

	/**
	 * Minimize InvCap Objective function on the default domain and default
	 * traffic matrix
	 * 
	 * @throws IloException
	 */
	private void minimizeInvCapObjective() throws IloException {

		////////////////////////////////////////////////
		// InvCap specific variables
		////////////////////////////////////////////////

		// no specific variables...
		specific = new IloNumVar[0][];

		////////////////////////////////////////////////
		// InvCap specific constraints
		////////////////////////////////////////////////

		////////////////////////////////////////////////
		// InvCap objective function
		////////////////////////////////////////////////
		System.out.println("***Minimize InvCap objective function***");
		// on multiplie l'objectif par 1000 de manière à compenser le manque de précision de l'optimiseur
		// TODO : vérifier s'il n'y a pas moyen de régler la précision de l'optimiseur
		cplex.addMinimize(cplex.prod(1000, cplex.sum(utilization)), "InvCap objective function");

	}

	/**
	 * Minimize umax Objective function on the default domain and default
	 * traffic matrix
	 * 
	 * @throws IloException
	 */
	private void minimizeUmaxObjective() throws IloException {

		////////////////////////////////////////////////
		// umax specific variables
		////////////////////////////////////////////////

		specific = new IloNumVar[1][];

		// umax
		specific[0] = new IloNumVar[1];
		specific[0][0] = cplex.numVar(0.0, 1.0, "umax");

		////////////////////////////////////////////////
		// umax specific constraints
		////////////////////////////////////////////////

		IloConstraint umaxC[] = new IloConstraint[links.size()];

		for (int l = 0; l < links.size(); ++l) {
			// u(l) <= u_max
			umaxC[l] = cplex.addLe(utilization[l], specific[0][0]);
		}

		////////////////////////////////////////////////
		// umax objective function
		////////////////////////////////////////////////
		System.out.println("***Minimize umax objective function***");
		// on multiplie l'objectif par 1000 de manière à compenser le manque de précision de l'optimiseur
		// TODO : vérifier s'il n'y a pas moyen de régler la précision de l'optimiseur
		cplex.addMinimize(cplex.prod(1000,specific[0][0]), "umax objective function");

	}

	/**
	 * Minimize Degrande Objective function on the default domain and default
	 * traffic matrix
	 * 
	 * @param C_B
	 * @param C_U
	 * @throws IloException
	 */
	private void minimizeDegrandeObjective(double C_B, double C_U)
	throws IloException {

		////////////////////////////////////////////////
		// Degrande specific variables
		////////////////////////////////////////////////

		specific = new IloNumVar[2][];

		// umax
		specific[0] = new IloNumVar[1];
		specific[0][0] = cplex.numVar(0.0, 1.0, "umax");

		// sum_a u(a)
		specific[1] = new IloNumVar[1];
		specific[1][0] = cplex.numVar(0.0, Double.MAX_VALUE, "sum_a u(a)");

		////////////////////////////////////////////////
		// Degrande specific constraints
		////////////////////////////////////////////////

		IloConstraint umaxC[] = new IloConstraint[links.size()];

		for (int l = 0; l < links.size(); ++l) {
			// u(l) <= u_max
			umaxC[l] = cplex.addLe(utilization[l], specific[0][0]);
		}

		// Computes sum_a u(a)
		IloConstraint sumC = cplex.addEq(specific[1][0], cplex.sum(utilization));

		////////////////////////////////////////////////
		// Degrande objective function
		////////////////////////////////////////////////
		IloLinearNumExpr objective = cplex.linearNumExpr();
		objective.addTerm(C_B, specific[0][0]);
		objective.addTerm(C_U, specific[1][0]);

		System.out.println("***Minimize Degrande objective function***");
		// on multiplie l'objectif par 1000 de manière à compenser le manque de précision de l'optimiseur
		// TODO : vérifier s'il n'y a pas moyen de régler la précision de l'optimiseur
		cplex.addMinimize(cplex.prod(1000, objective), "Degrande objective function");

	}

	/**
	 * Minimize MinHop Objective function on the default domain and default
	 * traffic matrix
	 * 
	 * @throws IloException
	 */
	private void minimizeMinHopObjective() throws IloException {

		////////////////////////////////////////////////
		// MinHop specific variables
		////////////////////////////////////////////////

		// no specific variables...
		specific = new IloNumVar[0][];

		////////////////////////////////////////////////
		// MinHop specific constraints
		////////////////////////////////////////////////

		////////////////////////////////////////////////
		// MinHop objective function
		////////////////////////////////////////////////
		System.out.println("***Minimize MinHop objective function***");
		cplex.addMinimize(cplex.sum(load), "MinHop objective function");

	}

	/**
	 * Implémentation du quick sort
	 * 
	 * source:
	 * http://www.dailly.info/algorithmes-de-tri/rapide.php
	 */

	private static void triRapide(double tableau[])
	{
		int longueur=tableau.length;
		triRapide(tableau,0,longueur-1);
	}

	private static int partition(double tableau[],int deb,int fin)
	{
		int compt=deb;
		double pivot=tableau[deb];

		for(int i=deb+1;i<=fin;i++)
		{
			if (tableau[i]<pivot)
			{
				compt++;

				echanger(tableau,compt,i);
			}
		}
		echanger(tableau,deb,compt);
		return(compt);
	}

	private static void triRapide(double tableau[],int deb,int fin)
	{
		if(deb<fin)
		{
			int positionPivot=partition(tableau,deb,fin);
			triRapide(tableau,deb,positionPivot-1);
			triRapide(tableau,positionPivot+1,fin);
		}
	}
	private static void echanger(double tableau[], int i, int j) {
		double tmp = tableau[i];
		tableau[i] = tableau[j];
		tableau[j] = tmp;
	}
}
