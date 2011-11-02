package org.alembic.aurion.docrepository.adapter.model;

/**
 * HL7 CE type representation.
 * 
 * @author Neil Webb
 */
public class CodedElement {

    private String code;
    private String codeSystem;
    private String displayName;

    /**
     * Get the value of code
     *
     * @return the value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the value of code
     *
     * @param code new value of code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Get the value of codeSystem
     *
     * @return the value of codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * Set the value of codeSystem
     *
     * @param codeSystem new value of codeSystem
     */
    public void setCodeSystem(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * Get the value of displayName
     *
     * @return the value of displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set the value of displayName
     *
     * @param displayName new value of displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
