//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.01 at 04:43:24 CEST 
//


package be.ac.ulg.montefiore.run.totem.scenario.model.jaxb;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/data/software/totem/totem.git/src/resources/scenario/Scenario-v1_3.xsd line 495)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}ASEventType">
 *       &lt;attribute name="classType" type="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}ctType" />
 *       &lt;attribute name="linkId" type="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}linkIdType" />
 *       &lt;attribute name="perCT" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="perLink" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="perPrio" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="preemptionLevel" type="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}preemptionType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ShowLinkReservationType
    extends be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ASEventType
{


    /**
     * Gets the value of the perPrio property.
     * 
     */
    boolean isPerPrio();

    /**
     * Sets the value of the perPrio property.
     * 
     */
    void setPerPrio(boolean value);

    boolean isSetPerPrio();

    void unsetPerPrio();

    /**
     * Gets the value of the perCT property.
     * 
     */
    boolean isPerCT();

    /**
     * Sets the value of the perCT property.
     * 
     */
    void setPerCT(boolean value);

    boolean isSetPerCT();

    void unsetPerCT();

    /**
     * Gets the value of the perLink property.
     * 
     */
    boolean isPerLink();

    /**
     * Sets the value of the perLink property.
     * 
     */
    void setPerLink(boolean value);

    boolean isSetPerLink();

    void unsetPerLink();

    /**
     * Gets the value of the classType property.
     * 
     */
    int getClassType();

    /**
     * Sets the value of the classType property.
     * 
     */
    void setClassType(int value);

    boolean isSetClassType();

    void unsetClassType();

    /**
     * Gets the value of the preemptionLevel property.
     * 
     */
    int getPreemptionLevel();

    /**
     * Sets the value of the preemptionLevel property.
     * 
     */
    void setPreemptionLevel(int value);

    boolean isSetPreemptionLevel();

    void unsetPreemptionLevel();

    /**
     * Gets the value of the linkId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getLinkId();

    /**
     * Sets the value of the linkId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setLinkId(java.lang.String value);

    boolean isSetLinkId();

    void unsetLinkId();

}
