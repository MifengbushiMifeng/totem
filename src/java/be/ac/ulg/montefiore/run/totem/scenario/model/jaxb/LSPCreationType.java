//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.01 at 04:43:24 CEST 
//


package be.ac.ulg.montefiore.run.totem.scenario.model.jaxb;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/data/software/totem/totem.git/src/resources/scenario/Scenario-v1_3.xsd line 341)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}BaseLSPCreationType">
 *       &lt;attribute name="dst" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="src" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface LSPCreationType
    extends be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.BaseLSPCreationType
{


    /**
     * Gets the value of the dst property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDst();

    /**
     * Sets the value of the dst property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDst(java.lang.String value);

    boolean isSetDst();

    void unsetDst();

    /**
     * Gets the value of the src property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSrc();

    /**
     * Sets the value of the src property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSrc(java.lang.String value);

    boolean isSetSrc();

    void unsetSrc();

}
