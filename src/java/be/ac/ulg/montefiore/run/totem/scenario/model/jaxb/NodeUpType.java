//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.01 at 04:43:24 CEST 
//


package be.ac.ulg.montefiore.run.totem.scenario.model.jaxb;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/data/software/totem/totem.git/src/resources/scenario/Scenario-v1_3.xsd line 200)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}ASEventType">
 *       &lt;attribute name="nodeId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface NodeUpType
    extends be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ASEventType
{


    /**
     * Gets the value of the nodeId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getNodeId();

    /**
     * Sets the value of the nodeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setNodeId(java.lang.String value);

    boolean isSetNodeId();

    void unsetNodeId();

}
