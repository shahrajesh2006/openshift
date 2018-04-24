package com.loyalty.web.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.loyalty.common.ilclient.DBUtil;
import com.loyalty.web.constants.Constants;
import com.loyalty.web.errors.DAOException;

/**
 * This class is used to get the Keystore.
 */
@Repository
public class KeystoreDAO {
	public KeystoreDAO() {
	}
	
	@Autowired
	DBUtil DBUtil;
    
    /**
	 * This method returns a vector of keys for a client program (partner is
	 * null) This is the default set of keys for the client program, since it is
	 * not associated with a partner.
	 * 
     * @param program
     * @return
     * @throws DAOException
     */
	public Vector getKeyStore(int program) throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        Vector keyStoreVec = new Vector(4);
        String key1 = "";
        String key2 = "";
        String key3 = "";
        String iv = "";
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String METHOD_NAME = "KeystoreDAO: getKeyStore() ";
        Connection conn = DBUtil.getConnection(Constants.LOLA_DS);
        
        try {
            // String to call DB
			sql
					.append(
							"SELECT UTL_RAW.CAST_TO_VARCHAR2(KEY1) KEY1,UTL_RAW.CAST_TO_VARCHAR2(KEY2)")
					.append(
							"KEY2,UTL_RAW.CAST_TO_VARCHAR2(KEY3) KEY3,UTL_RAW.CAST_TO_VARCHAR2(INIT_VECTOR)")
					.append(
							"INIT_VECTOR FROM KEYSTORES WHERE CLIENT_PROGRAM =? AND PARTNER IS NULL AND ")
					.append(
							"((? BETWEEN EFFECTIVE_DATE AND EXPIRATION_DATE) OR (? >= EFFECTIVE_DATE AND EXPIRATION_DATE IS NULL))");
            //prepare stored procedure
            stmt = conn.prepareStatement(sql.toString());
            stmt.setInt(1, program);
            stmt.setTimestamp(2, currentTime);
            stmt.setTimestamp(3, currentTime);
            rs = stmt.executeQuery();
            while (rs.next()) {
                key1 = rs.getString("KEY1");
                key2 = rs.getString("KEY2");
                key3 = rs.getString("KEY3");
                iv = rs.getString("INIT_VECTOR");
            }
            keyStoreVec.addElement(key1);
            keyStoreVec.addElement(key2);
            keyStoreVec.addElement(key3);
            keyStoreVec.addElement(iv);
        } catch (SQLException ex) {
            throw new DAOException(METHOD_NAME + "Database Error");
		} finally {
            try {
                DBUtil.close(conn, rs, stmt);
			} catch (Exception e) {
            }
            }
        return keyStoreVec;
    }
    
    /**
	 * This method returns a map of partner codes and the vector of keys for
	 * each partner
	 * 
     * @param program
     * @return
     * @throws DAOException
     */
	public Map getPartnerKeyStores(int program) throws DAOException {
      PreparedStatement stmt = null;
      ResultSet rs = null;
      StringBuffer sql = new StringBuffer();
      Map keyStoreMap = new HashMap();
      String key1 = "";
      String key2 = "";
      String key3 = "";
      String iv = "";
      Timestamp currentTime = new Timestamp(System.currentTimeMillis());
      Connection conn = DBUtil.getConnection(Constants.LOLA_DS);
      String METHOD_NAME = "KeystoreDAO: getPartnerKeyStores() ";

      try {
          // String to call DB
			sql
					.append(
							"SELECT UTL_RAW.CAST_TO_VARCHAR2(KS.KEY1) KEY1,UTL_RAW.CAST_TO_VARCHAR2(KS.KEY2) KEY2,")
					.append(
							"UTL_RAW.CAST_TO_VARCHAR2(KS.KEY3) KEY3,UTL_RAW.CAST_TO_VARCHAR2(KS.INIT_VECTOR) INIT_VECTOR,")
					.append(
							"PAR.CODE PAR_CODE FROM KEYSTORES KS, PARTNERS PAR WHERE KS.CLIENT_PROGRAM = ? AND KS.PARTNER = PAR.ID ")
					.append(
							"AND ((? BETWEEN KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE) ")
					.append(
							"OR (? >= KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE IS NULL))");
          //prepare stored procedure
          stmt = conn.prepareStatement(sql.toString());
          stmt.setInt(1, program);
          stmt.setTimestamp(2, currentTime);
          stmt.setTimestamp(3, currentTime);
          rs = stmt.executeQuery();
          while (rs.next()) {
              key1 = rs.getString("KEY1");
              key2 = rs.getString("KEY2");
              key3 = rs.getString("KEY3");
              iv = rs.getString("INIT_VECTOR");
              
              Vector keyStoreVec = new Vector(4);
              keyStoreVec.addElement(key1);
              keyStoreVec.addElement(key2);
              keyStoreVec.addElement(key3);
              keyStoreVec.addElement(iv);
              keyStoreMap.put(rs.getString("PAR_CODE"), keyStoreVec);
          }

      } catch (SQLException ex) {
          ex.printStackTrace();
          throw new DAOException(METHOD_NAME + "Database Error");
		} finally {
          try {
              DBUtil.close(conn, rs, stmt);
			} catch (Exception e) {
          }
          }
      return keyStoreMap;
    }

    /**
     * Gets Key and Vector by partner ID
	 * 
     * @param partnerId
     * @return
     * @throws DAOException
     */
	public Map<String, Vector> getPartnerKeyStores(String partnerCode) throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();

        Map<String, Vector> keyStoreMap = new HashMap<String, Vector>();
        String key1 = "";
        String key2 = "";
        String key3 = "";
        String iv = "";
        
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Connection conn = DBUtil.getConnection(Constants.LOLA_DS);
        String METHOD_NAME = "KeystoreDAO: getPartnerKeyInitVector() ";
        try {
            // String to call DB
            sql.append("SELECT KS.KEY1 KEY1, KS.KEY2 KEY2, KS.KEY3 KEY3, ");
            sql.append(" KS.INIT_VECTOR INIT_VECTOR, PAR.CODE PAR_CODE ");
            sql.append(" FROM KEYSTORES KS, PARTNERS PAR ");
            sql.append(" WHERE PAR.CODE = ? AND KS.PARTNER = PAR.ID ");
			sql
					.append(" AND ((? BETWEEN KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE) ");
			sql
					.append(" OR (? >= KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE IS NULL)) ");
             
            //prepare stored procedure
            stmt = conn.prepareStatement(sql.toString());
            stmt.setString(1, partnerCode);
            stmt.setTimestamp(2, currentTime);
            stmt.setTimestamp(3, currentTime);
            rs = stmt.executeQuery();
            
            if(rs.next()) {
                key1 = rs.getString("KEY1");
                key2 = rs.getString("KEY2");
                key3 = rs.getString("KEY3");
                iv = rs.getString("INIT_VECTOR");
                
                Vector keyStoreVec = new Vector(4);
                keyStoreVec.addElement(key1);
                keyStoreVec.addElement(key2);
                keyStoreVec.addElement(key3);
                keyStoreVec.addElement(iv);
                keyStoreMap.put(rs.getString("PAR_CODE"), keyStoreVec);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException(METHOD_NAME + "Database Error");
		} finally {
            try {
                DBUtil.close(conn, rs, stmt);
			} catch (Exception e) {
            }
            }
        return keyStoreMap;
      }
    
    /**
     * Gets Key and Vector by partner ID(RAW Format)
	 * 
     * @param partnerId
     * @return
     * @throws DAOException
     */
	public Map getPartnerRawKeyStores(String partnerCode) throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();

        Map keyStoreMap = new HashMap();
        String key1 = "";
        String key2 = "";
        String key3 = "";
        String iv = "";
        
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Connection conn = DBUtil.getConnection(Constants.LOLA_DS);
        String METHOD_NAME = "KeystoreDAO: getPartnerKeyInitVector() ";
        try {
            // String to call DB
			sql
					.append("SELECT UTL_RAW.CAST_TO_VARCHAR2(KS.KEY1) KEY1, UTL_RAW.CAST_TO_VARCHAR2(KS.KEY2) KEY2, UTL_RAW.CAST_TO_VARCHAR2(KS.KEY3) KEY3, ");
			sql
					.append(" UTL_RAW.CAST_TO_VARCHAR2(KS.INIT_VECTOR) INIT_VECTOR, PAR.CODE PAR_CODE ");
            sql.append(" FROM KEYSTORES KS, PARTNERS PAR ");
            sql.append(" WHERE PAR.CODE = ? AND KS.PARTNER = PAR.ID ");
			sql
					.append(" AND ((? BETWEEN KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE) ");
			sql
					.append(" OR (? >= KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE IS NULL)) ");
             
            //prepare stored procedure
            stmt = conn.prepareStatement(sql.toString());
            stmt.setString(1, partnerCode);
            stmt.setTimestamp(2, currentTime);
            stmt.setTimestamp(3, currentTime);
            rs = stmt.executeQuery();
            
            if(rs.next()) {
                key1 = rs.getString("KEY1");
                key2 = rs.getString("KEY2");
                key3 = rs.getString("KEY3");
                iv = rs.getString("INIT_VECTOR");
                
                Vector keyStoreVec = new Vector(4);
                keyStoreVec.addElement(key1);
                keyStoreVec.addElement(key2);
                keyStoreVec.addElement(key3);
                keyStoreVec.addElement(iv);
                keyStoreMap.put(rs.getString("PAR_CODE"), keyStoreVec);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException(METHOD_NAME + "Database Error");
		} finally {
            try {
                DBUtil.close(conn, rs, stmt);
			} catch (Exception e) {
            }
            }
        return keyStoreMap;
      }
    
	/**
	 * Gets Key and Vector by partner code and client program code
	 * 
	 * @param partnerId
	 * @return
	 * @throws DAOException
	 */
	public Vector getClientPartnerProgramKeystore(String clientProgramCode,
			String partnerCode, String partnerType) throws DAOException {
		String METHOD_NAME = "KeystoreDAO: getClientPartnerProgramKeystore() ";
    
		Connection conn = DBUtil.getConnection(Constants.LOLA_DS);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		Vector keyStoreVec = new Vector();
		String key1 = "";
		String key2 = "";
		String key3 = "";
		String iv = "";

		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		try {
			// String to call DB
			sql.append("SELECT KEY1, KEY2, KEY3, INIT_VECTOR");
			sql.append(" FROM KEYSTORES KS, PARTNERS PAR, CLIENT_PROGRAMS CP ");
			sql
					.append(" WHERE PAR.CODE = ? AND PAR.PARTNER_TYPE = ? AND KS.PARTNER = PAR.ID ");
			sql.append(" AND KS.CLIENT_PROGRAM = CP.ID AND CP.CODE = ? ");
			sql
					.append(" AND ((? BETWEEN KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE) ");
			sql
					.append(" OR (? >= KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE IS NULL)) ");

			// prepare stored procedure
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, partnerCode);
			stmt.setString(2, partnerType);
			stmt.setString(3, clientProgramCode);
			stmt.setTimestamp(4, currentTime);
			stmt.setTimestamp(5, currentTime);
			rs = stmt.executeQuery();

			if (rs.next()) {
				key1 = rs.getString("KEY1");
				key2 = rs.getString("KEY2");
				key3 = rs.getString("KEY3");
				iv = rs.getString("INIT_VECTOR");

				keyStoreVec = new Vector(4);
				keyStoreVec.addElement(key1);
				keyStoreVec.addElement(key2);
				keyStoreVec.addElement(key3);
				keyStoreVec.addElement(iv);
}

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new DAOException(METHOD_NAME + "Database Error");
		} finally {
			DBUtil.close(conn, rs, stmt);
		}
		return keyStoreVec;
	}

    /**
     * This method returns a vector of keys for a client program (partner is
     * null) This is the default set of keys for the client program, since it is
     * not associated with a partner.
     * This version gets the raw format, which in combination with Conversion.hexStringToByteArray,
     * should be used when getting AES keys.
     * 
     * @param program
     * @return
     * @throws DAOException
     */
    public Vector getClientProgramRawKeystore(int clientProgramId) throws DAOException {
        String METHOD_NAME = "KeystoreDAO: getClientProgramRawKeystore() ";
        
        Connection conn = DBUtil.getConnection(Constants.LOLA_DS);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();

        Vector keyStoreVec = new Vector();
        String key1 = "";
        String key2 = "";
        String key3 = "";
        String iv = "";

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        try {
            // String to call DB
            sql.append("SELECT KEY1, KEY2, KEY3, INIT_VECTOR");
            sql.append(" FROM KEYSTORES KS, CLIENT_PROGRAMS CP ");
            sql
                    .append(" WHERE KS.PARTNER IS NULL ");
            sql.append(" AND KS.CLIENT_PROGRAM = CP.ID AND CP.ID = ? ");
            sql
                    .append(" AND ((? BETWEEN KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE) ");
            sql
                    .append(" OR (? >= KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE IS NULL)) ");

            // prepare stored procedure
            stmt = conn.prepareStatement(sql.toString());
            stmt.setInt(1, clientProgramId);
            stmt.setTimestamp(2, currentTime);
            stmt.setTimestamp(3, currentTime);
            rs = stmt.executeQuery();

            if (rs.next()) {
                key1 = rs.getString("KEY1");
                key2 = rs.getString("KEY2");
                key3 = rs.getString("KEY3");
                iv = rs.getString("INIT_VECTOR");

                keyStoreVec = new Vector(4);
                keyStoreVec.addElement(key1);
                keyStoreVec.addElement(key2);
                keyStoreVec.addElement(key3);
                keyStoreVec.addElement(iv);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException(METHOD_NAME + "Database Error");
        } finally {
            DBUtil.close(conn, rs, stmt);
        }
        return keyStoreVec;
    }
}
