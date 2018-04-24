package com.loyalty.web.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This Class wraps the encryption and decryption step necessary for AES.  
 * 
 * What Is AES?  AES is a federal standard for private-key or symmetric 
 * cryptography. It supports combinations of key and block sizes of 
 * 128, 192, and 256. The algorithm chosen for this
 *  standard -- called Rijndael -- was invented by two Belgian 
 *  cryptographers. As part of the evaluation process, the candidate 
 *  algorithms (including Rijndael) were implemented in the Java language. 
 */
 public class AESHelper
 {     
     /** Handle to the Cipher object. */
     Cipher cipher = null;
     
     /**
      * AES/ECB/PKCS7Padding
      * AES - Algorithm = AES: Advanced Encryption Standard
      * ECB - Mode = ECB: Electronic Codebook Mode
      * PKCS7Padding - Padding = The padding scheme
      */     
     private static final String AES_Block_Padding_Level = "AES/CBC/PKCS5Padding"; 

     /** Algorithm name */
     private static final String ENCRYPTION_ALGORITHM = "AES";
     
     public AESHelper() {
     }
     
     /**
      * Gets an instance of this class.
      * @return AESHelper
      */
     public static AESHelper getInstance() {
         return (new AESHelper());
     }
     
     /**
      * 
      * @return SecretKeySpec
      * @throws NoSuchAlgorithmException
      */
     public SecretKeySpec generateAESKey() throws NoSuchAlgorithmException
     {
         return generateAESKey(128);
     }
     
     public SecretKeySpec generateAESKey(int size) throws NoSuchAlgorithmException
     {
         KeyGenerator kgen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
         kgen.init(size);

         // Generate the secret key specs.
         SecretKey skey = kgen.generateKey();
         byte[] raw = skey.getEncoded();

         SecretKeySpec skeySpec = new SecretKeySpec(raw, ENCRYPTION_ALGORITHM);
         return skeySpec;
     }
     
     
     /**
      * 
      * @param key
      * @return
      */
     public SecretKeySpec getSecretKeySpec(byte[] key)
     {
         SecretKeySpec skeySpec = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
         return skeySpec;
     }

     
     /**
      * 
      * @param skeySpec
      * @param strToEncrypt
      * @return byte[]
      * @throws InvalidKeyException
      * @throws IllegalStateException
      * @throws IllegalBlockSizeException
      * @throws BadPaddingException
      * @throws InvalidAlgorithmParameterException
      * @throws NoSuchAlgorithmException
      * @throws NoSuchProviderException
      * @throws NoSuchPaddingException
      */
     public byte[] encrypt(SecretKeySpec skeySpec, byte[] iv, String strToEncrypt) throws InvalidKeyException, IllegalStateException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
     {
        // Instantiate the cipher
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes());
        return encrypted;
     }
    
     
    /**
     * 
     * @param key - Encryption key
     * @param strToEncrypt - String to encrypt
     * @return byte[]
     * @throws InvalidKeyException
     * @throws IllegalStateException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws NoSuchPaddingException
     */
    public byte[] encrypt(byte[] key, String strToEncrypt) throws InvalidKeyException, IllegalStateException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
    {
        SecretKeySpec skeySpec = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
        return encrypt(skeySpec, null, strToEncrypt);
    }
    
    /**
     * 
     * @param key - Encryption key
     * @param iv - Initialization vector
     * @param strToEncrypt - String to encrypt
     * @return byte[]
     * @throws InvalidKeyException
     * @throws IllegalStateException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws NoSuchPaddingException
     */
    public byte[] encrypt(byte[] key, byte[] iv, String strToEncrypt) throws InvalidKeyException, IllegalStateException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
    {
        SecretKeySpec skeySpec = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
        return encrypt(skeySpec, iv, strToEncrypt);
    }
    
    /**
     * 
     * @param skeySpec
     * @param encrypted
     * @return String
     * @throws InvalidKeyException
     * @throws IllegalStateException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws NoSuchPaddingException
     */
    public String decrypt(SecretKeySpec skeySpec, byte[] iv, byte[] encrypted) throws InvalidKeyException, IllegalStateException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
    {
        byte[] original = (getCipher(Cipher.DECRYPT_MODE, skeySpec, iv)).doFinal(encrypted);
        String originalString = new String(original);
        return originalString;
    }
    
    /**
     * 
     * @param key - AES key to use for decryption.
     * @param encryptedValue - value to decrypt.
     * @return String
     * @throws InvalidKeyException
     * @throws IllegalStateException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws NoSuchPaddingException
     */
    public String decrypt(byte[] key, byte[] iv, byte[] encryptedValue) throws InvalidKeyException, IllegalStateException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
    {
        SecretKeySpec skeySpec = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
        return decrypt(skeySpec, iv, encryptedValue); 
    }
    
    /**
     * 
     * @param mode
     * @param skeySpec
     * @return Cipher
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws NoSuchPaddingException
     */
    private Cipher getCipher(int mode, SecretKeySpec skeySpec, byte[] iv) 
                   throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
    {
        if(this.cipher == null ) {
            this.cipher = Cipher.getInstance(AES_Block_Padding_Level);
        }
        
        if(iv == null) {
            this.cipher.init(mode, skeySpec);
        } else {
            this.cipher.init(mode, skeySpec, new IvParameterSpec(iv));    
        }
                    
        return this.cipher;
    }

    public byte[] getInitVector()
    {
        return this.cipher.getIV();
    }   
 }

