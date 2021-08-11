package com.gz.dubbo;

public class commonServiceException extends RuntimeException {
    private static final long serialVersionUID = 319050145492689913L;
    public static final Throwable CAUSE_ROLEID_NOTFOUND = new Throwable("Roleid not found");
    public static final Throwable CAUSE_ROLEID_RELATED_TO_USER = new Throwable("Roleid has been related to user");
    public static final Throwable CAUSE_SIP_XMLFORMAT_WRONG = new Throwable("Sip xmlformat wrong");
    public static final Throwable CAUSE_TDG_XMLFORMAT_WRONG = new Throwable("Tdg xmlformat wrong");
    public static final Throwable CAUSE_MISSING_PARAMETER = new Throwable("Missing parameter");
    public static final Throwable CAUSE_SOCKET_ERROR = new Throwable("Socket error");
    public static final Throwable CAUSE_SIP_TRANSACTION_ERROR = new Throwable("Sip transaction return error");
    public static final Throwable CAUSE_NOTES_ERROR = new Throwable("Notes servlet error");
    public static final Throwable CAUSE_CSP_TRANSACTION_ERROR = new Throwable("Csp transaction return error");
    public static final Throwable CAUSE_MID_TRANSACTION_ERROR = new Throwable("Mid transaction return error");
    public static final Throwable CAUSE_TDG_TRANSACTION_ERROR = new Throwable("Tdg transaction return error");
    public static final Throwable CAUSE_SCF_TRANSACTION_ERROR = new Throwable("Scf transaction return error");
    public static final Throwable CAUSE_GLMS_TRANSACTION_ERROR = new Throwable("GLMS transaction return error");
    public static final Throwable CAUSE_EMP_ERROR = new Throwable("EMP Gate return error");

    public commonServiceException() {
    }

    public commonServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public commonServiceException(String message) {
        super(message);
    }

    public commonServiceException(Throwable cause) {
        super(cause);
    }

    public static long getSerialversionuid() {
        return 319050145492689913L;
    }
}
