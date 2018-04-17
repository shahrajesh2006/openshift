package com.loyalty.web.errors;

import java.util.ArrayList;
import java.util.Collection;

import com.loyalty.web.vo.TLSErrorVO;

public abstract class TLSException extends Exception {

	
	private String errorCode = "";
	private int errorType = 0;
	private Throwable t = null;
	   
    protected TLSErrorVO tlsErrorVO = new TLSErrorVO();

    protected ArrayList tlsErrorVOs;

    public TLSException() {
        super();
    }

    /**
     * @param tlsErrorVO
     * @roseuid 3FE66D1102C6
     */
    public TLSException(TLSErrorVO tlsErrorVO) {
        if (tlsErrorVO == null) {
            //Accept default values on tlsErrorVO
        } else {
            this.tlsErrorVO = tlsErrorVO;
        }
    }

    /**
     * @param errorList
     * @roseuid 3FE66D3F00A1
     */
    public TLSException(ArrayList errorList) {
        Collection tlsErrorVOs = new ArrayList();
        if (errorList == null) {
            //Use default set-up by creating array with one default vo
            tlsErrorVOs.add(tlsErrorVO);
            this.tlsErrorVOs = (ArrayList) tlsErrorVOs;
        } else {
            tlsErrorVOs.addAll(errorList);
            this.tlsErrorVOs = errorList;
        }
    }

    /**
     * Construct an exception.
     * 
     * This constructor saves the error code and the message in the TLSErrorVO
     * object. Additionally, it saves an optionally provided cause in the
     * Exception's cause attribute. This allows nested exceptions to be held,
     * from which stack traces can be produced.
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
    public TLSException(String code, String message, Throwable cause) {
        super(code + ':' + message);
        tlsErrorVO.setErrorCode(code);
        tlsErrorVO.setMsgDesc(message);
        initCause(cause);
    }

    public TLSException(String errorMsg, Throwable cause) {
        super(errorMsg + cause.getMessage());
        this.t = cause;
        tlsErrorVO.setErrorCode("");
        tlsErrorVO.setMsgDesc(errorMsg);
        initCause(cause);
    }    
    
    public TLSException(String errorMsg) {
        super(errorMsg);
        tlsErrorVO.setErrorCode("");
        tlsErrorVO.setMsgDesc(errorMsg);
    }      
    
    /**
     * @param exception
     */
    public TLSException(Exception exception) {
        tlsErrorVO.setMsgDesc(exception.toString());
    }

    /**
     * @return @deprecated Use getTlsErrorVOs()
     */
    public ArrayList getErrorList() {
        return tlsErrorVOs;
    }

    /**
     * @return @deprecated Use getTlsErrorVO()
     */
    public TLSErrorVO getErrorVO() {
        return tlsErrorVO;
    }

    /**
     * @return Returns the tlsErrorVO.
     */
    public TLSErrorVO getTlsErrorVO() {
        return tlsErrorVO;
    }

    /**
     * @param tlsErrorVO
     *            The tlsErrorVO to set.
     */
    public void setTlsErrorVO(TLSErrorVO tlsErrorVO) {
        this.tlsErrorVO = tlsErrorVO;
    }

    /**
     * @return Returns the tlsErrorVOs.
     */
    public ArrayList getTlsErrorVOs() {
        return tlsErrorVOs;
    }

    /**
     * @param tlsErrorVOs
     *            The tlsErrorVOs to set.
     */
    public void setTlsErrorVOs(ArrayList tlsErrorVOs) {
        this.tlsErrorVOs = tlsErrorVOs;
    }
    
    /**
     * Gets the error code
     * @return
     */
    public String getErrorCode()
    {
       return errorCode;
    }
    
    /**
     * Sets the error code
     * @param errorCode
     */
    public void setErrorCode(String errorCode)
    {
       this.errorCode = errorCode;
    }
    
    /**
     * Gets the error type
     * @return
     */
    public int getErrorType()
    {
       return errorType;
    }
    
    /**
     * Sets the error type
     * @param type
     */
    public void setErrorType(int type)
    {
       this.errorType = type;
    }

    /**
     * Gets the throwable
     * @return
     */
    public Throwable getThrowable()
    {
       return t;
    }
}