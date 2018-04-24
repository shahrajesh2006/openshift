package com.loyalty.web.errors;

import java.util.ArrayList;
import java.util.Iterator;

import com.loyalty.web.vo.TLSErrorVO;

public class SystemException extends TLSException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5447697492495139382L;
	private static final String USER_MSG_DESC = "System Exception: An unexpected failure occurred, please try again or call the help desk.";

    public SystemException() {
        super();
        logError(tlsErrorVO);
    }

    public SystemException(TLSErrorVO tlsErrorVO) {
        super(tlsErrorVO);
        logError(tlsErrorVO);
    }

    public SystemException(ArrayList tlsErrorVOs) {
        super(tlsErrorVOs);
        Iterator iterator = tlsErrorVOs.iterator();
        TLSErrorVO tlsErrorVO = null;
        while (iterator.hasNext()) {
            tlsErrorVO = (TLSErrorVO) iterator.next();
            logError(tlsErrorVO);
        }
    }

    /**
     * @param exception
     */
    public SystemException(Exception exception) {
        super(exception);
        logError(tlsErrorVO);
    }

    /**
     * Construct an exception.
     * 
     * This constructor builds an exception with an error code, a text message,
     * and an optional exception cause.
     * 
     * @param code
     *            the error code for the exception
     * @param message
     *            a supporting message describing the error, and intended for
     *            troubleshooting. This is not intended for end-user display.
     * @param cause
     *            an optional Throwable, which caused the calling class to
     *            generate this new Exception. The cause may be null.
     */
    public SystemException(String code, String message, Throwable cause) {
        super(code, message, cause);
        if (tlsErrorVO.getLogFlag().booleanValue()) {
            TLSLoggerInterface tlslogger = TLSLoggerFactory
                    .getTLSLogger("com.trilegiantloyalty.common.exception.SystemException");
            tlslogger.error(tlsErrorVO, cause);
        }
    }

    public SystemException( String message, Throwable cause) {
        super( message, cause);
        if (tlsErrorVO.getLogFlag().booleanValue()) {
            TLSLoggerInterface tlslogger = TLSLoggerFactory
                    .getTLSLogger("com.trilegiantloyalty.common.exception.SystemException");
            tlslogger.error(tlsErrorVO, cause);
        }
    }    
    
    public SystemException( String message) {
        super( message);
        if (tlsErrorVO.getLogFlag().booleanValue()) {
            TLSLoggerInterface tlslogger = TLSLoggerFactory.getTLSLogger("com.trilegiantloyalty.common.exception.SystemException");
            tlslogger.error(tlsErrorVO);
        }
    }     
    
    /**
     * @param tlsErrorVO
     */
    private void logError(TLSErrorVO tlsErrorVO) {
        if (tlsErrorVO.getLogFlag().booleanValue()) {
            TLSLoggerInterface tlslogger = TLSLoggerFactory
                    .getTLSLogger("com.trilegiantloyalty.common.exception.SystemException");
            tlslogger.error(tlsErrorVO);
        }
    }

}