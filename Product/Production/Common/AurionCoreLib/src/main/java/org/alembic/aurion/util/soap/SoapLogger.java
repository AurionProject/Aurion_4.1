package org.alembic.aurion.util.soap;

import java.io.ByteArrayOutputStream;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Logger for SOAP messages. A filter may be applied to messages to redact the
 * body of the message for security purposes. This service is disabled by
 * Changing the Log4J logging level to be at a higher threshold than the info
 * level.
 *
 * @author Neil Webb
 */
public class SoapLogger {
    private static final String PROPERTIES_FILE_NAME_GATEWAY = "gateway";
    private static final String PROPERTIES_FILE_KEY_SOAPMESSAGEFILTERLIST = "soap.message.filter.list";

    private Log log = null;

    public SoapLogger() {
        log = LogFactory.getLog(getClass());
    }

    /**
     * Log a SOAP message that is contained in a SOAPMessageContext object.
     *
     * @param context SOAP message context.
     */
    public void logSoapMessage(SOAPMessageContext context) {
        if(log.isInfoEnabled()) {
            log.info("******** Begin logSoapMessage(...) *************");
            SOAPMessage soapMessage = null;
            String soapMessageText = null;
            try {
                if (context != null) {
                    log.debug("******** Context was not null *************");

                    Boolean outboundProperty = (Boolean)context.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
                    if(outboundProperty != null) {
                        log.info("Message direction: " + ((outboundProperty.booleanValue()) ? "outbound" : "inbound"));
                    }

                    soapMessage = context.getMessage();
                    log.debug("******** After getMessage *************");

                    if (soapMessage != null) {
                        log.debug("******** Attempting to write out SOAP message *************");
                        try {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            soapMessage.writeTo(bos);
                            soapMessageText = bos.toString();
                            // Redact Content if matching list
                            String soapMessageToLog = applySoapFilters(soapMessageText);
                            log.info("SOAP message : " + soapMessageToLog);
                        } catch (Throwable t) {
                            log.debug("Exception writing out the message");
                            t.printStackTrace();
                        }
                    } else {
                        log.debug("SOAPMessage was null");
                    }
                } else {
                    log.debug("SOAPMessageContext was null.");
                }
            } catch (Throwable t) {
                log.debug("Error logging the SOAP message: " + t.getMessage());
                t.printStackTrace();
            }
            log.info("******** End logSoapMessage(...) *************");
        }
    }

    /**
     * Log the raw assertion data of an assertion.
     *
     * @param assertion SAML assertion data
     */
    public void logRawAssertion(AssertionType assertion) {
        if(log.isInfoEnabled()) {
            log.info("******** Begin logRawAssertion(...) *************");
            String rawAssertion = null;
            if(assertion != null) {
                rawAssertion = assertion.getRawAssertion();
            }
            log.info("Raw Assertion: " + rawAssertion);
            log.info("******** End logRawAssertion(...) *************");
        }
    }

    /**
     * Apply SOAP content filtering to a message.
     *
     * @param soapMessage SOAP message as a String
     * @return Filtered message
     */
    private String applySoapFilters(String soapMessage) {
        String filteredMessage = soapMessage;
        String[] filterList = getSOAPMessageLoggingFilters();
        for(String filter : filterList) {
            log.debug("Applying SOAP message filter: " + filter);
            int filterIndex = soapMessage.indexOf(filter);
            if(filterIndex != -1) {
                log.debug("Filtered element found - applying content filter to SOAP message");
                filteredMessage = null;
                int firstPartEnd = soapMessage.indexOf(">", filterIndex);
                if(firstPartEnd != -1) {
                    firstPartEnd = firstPartEnd + 1;
                    String workingSecondPart = soapMessage.substring(firstPartEnd);
                    log.debug("First part of message captured, determining end");
                    int secondPartFilterIndex = workingSecondPart.lastIndexOf(filter);
                    if(secondPartFilterIndex != -1) {
                        log.debug("Closing element found - determining the location of the closing element tag");
                        String messageBodyPart = workingSecondPart.substring(0, secondPartFilterIndex);
                        int secondPartBegin = messageBodyPart.lastIndexOf("</");
                        if(secondPartBegin != -1) {
                            log.debug("Have the information required to redact filtered message body - preparing message");
                            filteredMessage = soapMessage.substring(0, firstPartEnd) +
                                    "-- Redacted --" +
                                    workingSecondPart.substring(secondPartBegin);
                        }
                    }
                }
            } else {
                log.debug("Filter not found");
            }
        }
        if(filteredMessage == null) {
            filteredMessage = "A filter was found for this SOAP message but an unknown error condition occured when redacting the message body.";
        }
        return filteredMessage;
    }

    /**
     * Get the list of SOAP message logging filters. Each entry is the name of
     * an element in a SOAP message that will cause the content of that element to be redacted.
     *
     * @return SOAP message logging filter list
     */
    private String[] getSOAPMessageLoggingFilters() {
        String[] filters = null;
        try {
            String redactFilter = PropertyAccessor.getProperty(PROPERTIES_FILE_NAME_GATEWAY, PROPERTIES_FILE_KEY_SOAPMESSAGEFILTERLIST);
            filters = redactFilter.split("\\|");
        } catch(Exception e) {
            log.error("Error obtaining SOAP message filter: " + e.getMessage(), e);
        }
        return filters;
    }

}
