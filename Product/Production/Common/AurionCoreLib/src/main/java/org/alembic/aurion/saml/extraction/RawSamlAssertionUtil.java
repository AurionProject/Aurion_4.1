package org.alembic.aurion.saml.extraction;

import com.sun.xml.ws.security.opt.impl.incoming.SAMLAssertion;
import com.sun.xml.wss.impl.MessageConstants;
import java.io.ByteArrayOutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used to extract the raw SAML Assertion message as received.
 * The SAML Assertion has been found to be missing some closing tags so it is
 * not well formed. In this case, the raw SAML Assertion XML message is
 * corrected.
 * 
 * @author Neil Webb
 */
public class RawSamlAssertionUtil {
    private Log log = null;
    private final WebServiceContext wsContext;
    private String rawAssertion = null;
    private String repairedAssertion = null;

    RawSamlAssertionUtil(WebServiceContext context) {
        log = createLogger();
        wsContext = context;
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected XMLOutputFactory createXMLOutputFactory() {
        return XMLOutputFactory.newInstance();
    }

    protected ByteArrayOutputStream createByteArrayOutputStream() {
        return new ByteArrayOutputStream();
    }

    public String captureRawSAMLAssertion() {
        if(wsContext != null) {
            populateRawAssertion();
            repairSamlToken();
        }
        return repairedAssertion;
    }

    private void populateRawAssertion() {
        log.debug("In populateRawAssertion");
        try {
            MessageContext msgContext = wsContext.getMessageContext();
            if(msgContext != null)
            {
                log.debug("MessageContext was not null");
                Object incomingSamlAssertionObj = msgContext.get(MessageConstants.INCOMING_SAML_ASSERTION);
                if((incomingSamlAssertionObj != null) && (incomingSamlAssertionObj instanceof SAMLAssertion)) {
                    log.debug("Incomming SAML assertion object found");
                    SAMLAssertion as = (SAMLAssertion)incomingSamlAssertionObj;
                    try {
                        log.debug("SAMLAssertion processNoValidation...");
                        XMLOutputFactory xof = createXMLOutputFactory();
                        ByteArrayOutputStream os = createByteArrayOutputStream();
                        XMLStreamWriter xtw = xof.createXMLStreamWriter(os);
                        as.processNoValidation(as.getSamlReader(), xtw);
                        xtw.flush();
                        rawAssertion = os.toString();
                        xtw.close();
                    } catch (Throwable e) {
                        log.error("Error attempting to print SAMLAssertion.processNoValidation: " + e.getMessage(), e);
                    }

                } else {
                    log.error("Incoming SAML assertion object was null");
                }
            } else {
                log.error("Message context was null");
            }

        } catch(Throwable t){
            log.error("Error logging header information: " + t.getMessage(), t);
        }
    }

    private void repairSamlToken() {
        if((rawAssertion != null) && (rawAssertion.length() > 0)) {
            String assertionString = rawAssertion;
            try {
                int insertionPoint = rawAssertion.lastIndexOf("</");
                int beginIndex = insertionPoint + 2;
                int endIndex = rawAssertion.lastIndexOf(":");
                if(endIndex > beginIndex) {
                    String nsPrefix = rawAssertion.substring(beginIndex, endIndex);
                    if(log.isDebugEnabled()) {
                        log.debug("Namespace prefix used in repairExtractedSamlToken" + nsPrefix);
                    }
                    String closingTags = "</" + nsPrefix + ":Assertion></" + nsPrefix + ":Evidence></" + nsPrefix + ":AuthzDecisionStatement>";
                    String assertionStart = rawAssertion.substring(0, insertionPoint);
                    if(!(assertionStart.endsWith(closingTags))) {
                        String assertionEnd = rawAssertion.substring(insertionPoint);
                        assertionString = assertionStart + closingTags + assertionEnd;
                    }
                }

            } catch(Throwable t) {
                log.error("Error repairing the extracted SAML token (this error is ignored): " + t.getMessage(), t);
            }
            repairedAssertion = assertionString;
        }
    }
}
