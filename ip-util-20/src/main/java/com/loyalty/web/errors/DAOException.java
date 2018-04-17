package com.loyalty.web.errors;

import java.util.ArrayList;
import java.util.Iterator;

import com.loyalty.web.constants.ErrorConstants;
import com.loyalty.web.vo.TLSErrorVO;

public class DAOException extends TLSException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 847242699986500602L;
	private static final String USER_MSG_DESC = "A Database Error Occurred.";

	public DAOException() {
		super();
		logError(tlsErrorVO);
	}

	public DAOException(TLSErrorVO tlsErrorVO) {
		super(tlsErrorVO);
		logError(tlsErrorVO);
	}

	public DAOException(ArrayList tlsErrorVOs) {
		super(tlsErrorVOs);
		Iterator iterator = tlsErrorVOs.iterator();
		TLSErrorVO tlsErrorVO = null;
		while (iterator.hasNext()) {
			tlsErrorVO = (TLSErrorVO) iterator.next();
			logError(tlsErrorVO);
		}
	}

	/**
	 * @param tlsErrorVO
	 */
	private void logError(TLSErrorVO tlsErrorVO) {
		if (this.tlsErrorVO.getErrorCode() == null
				|| this.tlsErrorVO.getErrorCode().equals(ErrorConstants.UNDEFINED_ERROR)) {
			tlsErrorVO.setErrorCode(ErrorConstants.UNDEFINED_DB_EXCEPTION);
		}
		tlsErrorVO.setUserMsgDesc(USER_MSG_DESC);
		if (tlsErrorVO.getLogFlag().booleanValue()) {
			TLSLoggerInterface tlslogger = TLSLoggerFactory
					.getTLSLogger("com.loyalty.web.errors.DAOException");
			tlslogger.error(tlsErrorVO);
		}
	}

	/**
	 * Constructs an Exception with a detailed message.
	 * 
	 * @param Message
	 *            The message associated with the exception.
	 */
	public DAOException(String message) {
		super(message);
	}

}