package com.loyalty.web.errors;

import java.util.ArrayList;

import com.loyalty.web.vo.TLSErrorVO;

public class BusinessException extends TLSException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3935586821457863635L;

	/**
     * @since 19-12-2003
     *  
     */
    public BusinessException() {
        super();
    }

    /**
     * @param errorList
     * @since 19-12-2003
     *  
     */
    public BusinessException(ArrayList errorList) {
        super(errorList);
    }

    /**
     * @param errorVO
     * @since 19-12-2003
     *  
     */
    public BusinessException(TLSErrorVO tlsErrorVO) {
        super(tlsErrorVO);
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
    public BusinessException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
    
    public String getErrorCode(){
     if(this.tlsErrorVO!=null&&this.tlsErrorVO.getErrorCode()!=null){
         return this.tlsErrorVO.getErrorCode();
     
     }
     else return "";
    }
    public String getErrorMessage(){
        if(this.tlsErrorVO!=null&&this.tlsErrorVO.getMsgDesc()!=null){
            return this.tlsErrorVO.getMsgDesc();
        
        }
        else return "";
       }
    

}