//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.01 at 04:43:24 CEST 
//


package be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl;

public class IGPRoutingTypeImpl
    extends be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.ASTMEventTypeImpl
    implements be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.IGPRoutingType, com.sun.xml.bind.JAXBObject, be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.UnmarshallableObject, be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.XMLSerializable, be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.ValidatableObject
{

    protected java.lang.String _LlcId;
    protected be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StrategyType _Strategy;
    protected boolean has_ECMP;
    protected boolean _ECMP;
    protected java.lang.String _SPFtype;
    public final static java.lang.Class version = (be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.IGPRoutingType.class);
    }

    public java.lang.String getLlcId() {
        return _LlcId;
    }

    public void setLlcId(java.lang.String value) {
        _LlcId = value;
    }

    public boolean isSetLlcId() {
        return (_LlcId!= null);
    }

    public void unsetLlcId() {
        _LlcId = null;
    }

    public be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StrategyType getStrategy() {
        if (_Strategy == null) {
            return be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StrategyType.IP;
        } else {
            return _Strategy;
        }
    }

    public void setStrategy(be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StrategyType value) {
        _Strategy = value;
    }

    public boolean isSetStrategy() {
        return (_Strategy!= null);
    }

    public void unsetStrategy() {
        _Strategy = null;
    }

    public boolean isECMP() {
        if (!has_ECMP) {
            return javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.DatatypeConverterImpl.installHook("false"));
        } else {
            return _ECMP;
        }
    }

    public void setECMP(boolean value) {
        _ECMP = value;
        has_ECMP = true;
    }

    public boolean isSetECMP() {
        return has_ECMP;
    }

    public void unsetECMP() {
        has_ECMP = false;
    }

    public java.lang.String getSPFtype() {
        return _SPFtype;
    }

    public void setSPFtype(java.lang.String value) {
        _SPFtype = value;
    }

    public boolean isSetSPFtype() {
        return (_SPFtype!= null);
    }

    public void unsetSPFtype() {
        _SPFtype = null;
    }

    public be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.UnmarshallingEventHandler createUnmarshaller(be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.UnmarshallingContext context) {
        return new be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.IGPRoutingTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeBody(context);
    }

    public void serializeAttributes(be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (has_ECMP) {
            context.startAttribute("", "ECMP");
            try {
                context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _ECMP)), "ECMP");
            } catch (java.lang.Exception e) {
                be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_SPFtype!= null) {
            context.startAttribute("", "SPFtype");
            try {
                context.text(((java.lang.String) _SPFtype), "SPFtype");
            } catch (java.lang.Exception e) {
                be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_LlcId!= null) {
            context.startAttribute("", "llcId");
            try {
                context.text(((java.lang.String) _LlcId), "LlcId");
            } catch (java.lang.Exception e) {
                be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_Strategy!= null) {
            context.startAttribute("", "strategy");
            try {
                context.text(((be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StrategyType) _Strategy).toString(), "Strategy");
            } catch (java.lang.Exception e) {
                be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        super.serializeAttributes(context);
    }

    public void serializeURIs(be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeURIs(context);
    }

    public java.lang.Class getPrimaryInterface() {
        return (be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.IGPRoutingType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com."
+"sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameCla"
+"sst\u0000\u001fLcom/sun/msv/grammar/NameClass;xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Bool"
+"ean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000"
+"\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000!com.sun."
+"msv.datatype.xsd.TimeType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype"
+".xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd"
+".BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Co"
+"ncreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatype"
+"Impl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNa"
+"meq\u0000~\u0000\u001bL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceP"
+"rocessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0004timesr\u00005com"
+".sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xpsr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlo"
+"calNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxpq\u0000~\u0000\u001fq\u0000~\u0000\u001esr\u0000#com.sun.msv."
+"grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamesp"
+"aceURIq\u0000~\u0000\u001bxr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004"
+"timet\u0000\u0000sr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0010\u0001q\u0000~\u0000-sq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0011psq\u0000~\u0000\u0012ppsr\u0000\'"
+"com.sun.msv.datatype.xsd.FinalComponent\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\nfinalVa"
+"luexr\u0000\u001ecom.sun.msv.datatype.xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypet"
+"\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;xq\u0000~\u0000\u001at\u00009http://j"
+"axb.model.scenario.totem.run.montefiore.ulg.ac.bet\u0000\bASIdType"
+"q\u0000~\u0000\"sr\u0000 com.sun.msv.datatype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com."
+"sun.msv.datatype.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFac"
+"etsq\u0000~\u00004xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0003intq\u0000~\u0000\"sr\u0000*com.sun.msv.datatype.xsd.M"
+"axInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.Rang"
+"eFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;xr\u00009com.s"
+"un.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT"
+"\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005"
+"Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypeq\u0000~\u00004L\u0000\fconcr"
+"eteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNam"
+"eq\u0000~\u0000\u001bxq\u0000~\u0000\u001appq\u0000~\u0000\"\u0000\u0001sr\u0000*com.sun.msv.datatype.xsd.MinInclusi"
+"veFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000=ppq\u0000~\u0000\"\u0000\u0000sr\u0000!com.sun.msv.datatype.xs"
+"d.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00009q\u0000~\u0000\u001et\u0000\u0004longq\u0000~\u0000\"sq\u0000~\u0000<ppq\u0000~\u0000\"\u0000\u0001s"
+"q\u0000~\u0000Cppq\u0000~\u0000\"\u0000\u0000sr\u0000$com.sun.msv.datatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00009q\u0000~\u0000\u001et\u0000\u0007integerq\u0000~\u0000\"sr\u0000,com.sun.msv.datatype.xsd."
+"FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.dataty"
+"pe.xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000@pp"
+"q\u0000~\u0000\"\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0007decimalq\u0000~\u0000\"q\u0000~\u0000Qt\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000~\u0000Kt\u0000\fmin"
+"Inclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010java.lang."
+"Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u0000Kt\u0000\fmaxInclusivesq\u0000~\u0000U\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff"
+"\u00ffq\u0000~\u0000Fq\u0000~\u0000Tsr\u0000\u0011java.lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~\u0000V\u0080\u0000\u0000"
+"\u0000q\u0000~\u0000Fq\u0000~\u0000Xsq\u0000~\u0000Z\u007f\u00ff\u00ff\u00ff\u0000\u0000\u0000\u0000q\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u0000;q\u0000~\u00006sq\u0000~\u0000\'t\u0000\u0004ASIDq"
+"\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0011psq\u0000~\u0000\u0012ppsq\u0000~\u00002q\u0000~\u00006t\u0000\bTMIdTypeq"
+"\u0000~\u0000\"q\u0000~\u0000:\u0000\u0000\u0000\u0000q\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u0000;q\u0000~\u00006sq\u0000~\u0000\'t\u0000\u0004TMIDq\u0000~\u0000+q\u0000~\u0000-sq\u0000"
+"~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0011psq\u0000~\u0000\u0012ppsr\u0000$com.sun.msv.datatype.xsd.Boolea"
+"nType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0007booleanq\u0000~\u0000\"q\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u0000mq\u0000"
+"~\u0000\u001esq\u0000~\u0000\'t\u0000\u0004ECMPq\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0011psq\u0000~\u0000\u0012ppsr\u0000#co"
+"m.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValid"
+"xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceP"
+"rocessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000!\u0001q\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u0000vq\u0000~\u0000\u001esq\u0000~"
+"\u0000\'t\u0000\u0007SPFtypeq\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0011pq\u0000~\u0000ssq\u0000~\u0000\'t\u0000\u0005llcI"
+"dq\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0011psq\u0000~\u0000\u0012ppsr\u0000)com.sun.msv.datat"
+"ype.xsd.EnumerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Se"
+"t;xq\u0000~\u0000?q\u0000~\u00006t\u0000\fstrategyTypeq\u0000~\u0000x\u0000\u0000q\u0000~\u0000uq\u0000~\u0000ut\u0000\u000benumerations"
+"r\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0004t\u0000\u0003BISt\u0000\u0002IPt\u0000"
+"\u0007OVERLAYt\u0000\u0002ISxq\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u0000\u0086q\u0000~\u00006sq\u0000~\u0000\'t\u0000\bstrategyq\u0000~\u0000+q\u0000~"
+"\u0000-sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTab"
+"let\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-co"
+"m.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005cou"
+"ntB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Expressio"
+"nPool;xp\u0000\u0000\u0000\r\u0001pq\u0000~\u0000\u0005q\u0000~\u0000\u0006q\u0000~\u0000\nq\u0000~\u0000hq\u0000~\u0000\u0080q\u0000~\u0000\fq\u0000~\u0000`q\u0000~\u0000\tq\u0000~\u0000\u0007q"
+"\u0000~\u0000qq\u0000~\u0000/q\u0000~\u0000\bq\u0000~\u0000|x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.UnmarshallingContext context) {
            super(context, "--------------");
        }

        protected Unmarshaller(be.ac.ulg.montefiore.run.totem.util.jaxb.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.IGPRoutingTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  13 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("", "ECMP");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        attIdx = context.getAttribute("", "strategy");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case  12 :
                        attIdx = context.getAttribute("", "TMID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("", "ASID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("", "time");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        spawnHandlerFromEnterElement((((be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.ASTMEventTypeImpl)be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.IGPRoutingTypeImpl.this).new Unmarshaller(context)), 13, ___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        attIdx = context.getAttribute("", "SPFtype");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("", "llcId");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 9;
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ECMP = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_ECMP = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Strategy = be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.StrategyType.fromString(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SPFtype = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _LlcId = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  13 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("", "ECMP");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        attIdx = context.getAttribute("", "strategy");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case  12 :
                        attIdx = context.getAttribute("", "TMID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "ASID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "time");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        spawnHandlerFromLeaveElement((((be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.ASTMEventTypeImpl)be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.IGPRoutingTypeImpl.this).new Unmarshaller(context)), 13, ___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        attIdx = context.getAttribute("", "SPFtype");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("", "llcId");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 9;
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  13 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        if (("ECMP" == ___local)&&("" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        if (("strategy" == ___local)&&("" == ___uri)) {
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  12 :
                        if (("TMID" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.ASTMEventTypeImpl)be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.IGPRoutingTypeImpl.this).new Unmarshaller(context)), 13, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("ASID" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.ASTMEventTypeImpl)be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.IGPRoutingTypeImpl.this).new Unmarshaller(context)), 13, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("time" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.ASTMEventTypeImpl)be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.IGPRoutingTypeImpl.this).new Unmarshaller(context)), 13, ___uri, ___local, ___qname);
                            return ;
                        }
                        spawnHandlerFromEnterAttribute((((be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.ASTMEventTypeImpl)be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.IGPRoutingTypeImpl.this).new Unmarshaller(context)), 13, ___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        if (("SPFtype" == ___local)&&("" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  6 :
                        if (("llcId" == ___local)&&("" == ___uri)) {
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  8 :
                        if (("llcId" == ___local)&&("" == ___uri)) {
                            state = 9;
                            return ;
                        }
                        break;
                    case  13 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("", "ECMP");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        attIdx = context.getAttribute("", "strategy");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case  11 :
                        if (("strategy" == ___local)&&("" == ___uri)) {
                            state = 12;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("ECMP" == ___local)&&("" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  12 :
                        attIdx = context.getAttribute("", "TMID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "ASID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "time");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        spawnHandlerFromLeaveAttribute((((be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.ASTMEventTypeImpl)be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.IGPRoutingTypeImpl.this).new Unmarshaller(context)), 13, ___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("SPFtype" == ___local)&&("" == ___uri)) {
                            state = 6;
                            return ;
                        }
                        break;
                    case  3 :
                        attIdx = context.getAttribute("", "SPFtype");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("", "llcId");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 9;
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                try {
                    switch (state) {
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  4 :
                            eatText3(value);
                            state = 5;
                            return ;
                        case  7 :
                            eatText4(value);
                            state = 8;
                            return ;
                        case  13 :
                            revertToParentFromText(value);
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "ECMP");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case  9 :
                            attIdx = context.getAttribute("", "strategy");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText2(v);
                                state = 12;
                                continue outer;
                            }
                            state = 12;
                            continue outer;
                        case  10 :
                            eatText2(value);
                            state = 11;
                            return ;
                        case  12 :
                            attIdx = context.getAttribute("", "TMID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("", "ASID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("", "time");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            spawnHandlerFromText((((be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.ASTMEventTypeImpl)be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.impl.IGPRoutingTypeImpl.this).new Unmarshaller(context)), 13, value);
                            return ;
                        case  3 :
                            attIdx = context.getAttribute("", "SPFtype");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText3(v);
                                state = 6;
                                continue outer;
                            }
                            state = 6;
                            continue outer;
                        case  6 :
                            attIdx = context.getAttribute("", "llcId");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText4(v);
                                state = 9;
                                continue outer;
                            }
                            state = 9;
                            continue outer;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
