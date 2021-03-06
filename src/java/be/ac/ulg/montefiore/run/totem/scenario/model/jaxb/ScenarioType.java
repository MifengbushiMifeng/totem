//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.01 at 04:43:24 CEST 
//


package be.ac.ulg.montefiore.run.totem.scenario.model.jaxb;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/data/software/totem/totem.git/src/resources/scenario/Scenario-v1_3.xsd line 84)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}event" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="pathsRelativeTo" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ScenarioType {


    /**
     * Gets the value of the pathsRelativeTo property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPathsRelativeTo();

    /**
     * Sets the value of the pathsRelativeTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPathsRelativeTo(java.lang.String value);

    boolean isSetPathsRelativeTo();

    void unsetPathsRelativeTo();

    /**
     * Gets the value of the Event property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Event property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEvent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LoadTrafficMatrix}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartDeletion}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StartScenarioServer}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.BgpAwareIGPWO}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ListShortestPaths}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LSPCreation}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ComputeMCF}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.NodeUp}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.IGPRouting}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.IGPWOCalculateWeights}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LinkDown}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.NodeDown}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LinkUp}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ShowLinkInfo}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.MplsCosRouting}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.TopologyGeneration}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LSPDetourCreation}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StopAlgo}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LinkTeMetricChange}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.RemoveTrafficMatrix}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LoadDomain}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ShowLinkReservableBandwidth}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LSPDeletion}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.FastIPMetric}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LSPBWChange}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.DeleteAllLSP}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LoadDistantDomain}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartSave}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ShowLinkReservation}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LSPBackupCreation}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.RemoveNetworkController}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.Template}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LSPBypassCreation}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.OptDivideTM}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.GenerateIntraTM}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.AddNetworkController}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ListenToLSPsDemands}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartAddSeries}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StartAlgo}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LinkMetricChange}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ShowLinkLoad}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ECMPAnalysis}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.SaveDomain}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.Echo}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.TrafficMatrixGeneration}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.EnableTrafficSwitching}
     * {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartCreation}
     * 
     */
    java.util.List getEvent();

    boolean isSetEvent();

    void unsetEvent();

}
