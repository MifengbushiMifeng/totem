//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.01 at 04:43:24 CEST 
//


package be.ac.ulg.montefiore.run.totem.scenario.model.jaxb;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/data/software/totem/totem.git/src/resources/scenario/Scenario-v1_3.xsd line 510)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}ASTMEventType">
 *       &lt;attribute name="ECMP" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="SPFtype" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="llcId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="strategy" type="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}strategyType" default="IP" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface IGPRoutingType
    extends be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ASTMEventType
{


    /**
     * Gets the value of the llcId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getLlcId();

    /**
     * Sets the value of the llcId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setLlcId(java.lang.String value);

    boolean isSetLlcId();

    void unsetLlcId();

    /**
     * Gets the value of the strategy property.
     * 
     * @return
     *     possible object is
     *     {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StrategyType}
     */
    be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StrategyType getStrategy();

    /**
     * Sets the value of the strategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StrategyType}
     */
    void setStrategy(be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StrategyType value);

    boolean isSetStrategy();

    void unsetStrategy();

    /**
     * Gets the value of the ecmp property.
     * 
     */
    boolean isECMP();

    /**
     * Sets the value of the ecmp property.
     * 
     */
    void setECMP(boolean value);

    boolean isSetECMP();

    void unsetECMP();

    /**
     * Gets the value of the spFtype property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSPFtype();

    /**
     * Sets the value of the spFtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSPFtype(java.lang.String value);

    boolean isSetSPFtype();

    void unsetSPFtype();

}
