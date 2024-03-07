package common;

import java.util.Base64;
import java.util.Scanner;

/**
 * @author spandit
 * @lastmodifiedby spandit
 * This class has encryption and decryption methods
 */
public class EncryptDecrypt {
    /**
     * @author spandit
     * @lastmodifiedby spandit
     * This method will encrypt the password and print it in the console
     */
    public static void main(String[] args) {
        UtilitiesCommon.setupLogger();
        Scanner sc = new Scanner(System.in); //System.in is a standard input stream
        UtilitiesCommon.log("Enter a string: ");
        String temp = sc.nextLine();
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedValue = encoder.encodeToString(temp.getBytes());
        UtilitiesCommon.log("Encoded string: " + encodedValue);
        sc.close();
    }

    /**
     * @param encryptedPassword Encrypted Password
     * @return Decrypted Password
     * @author spandit
     * @lastModifiedBy spandit
     * This method will return the decrypted password
     */
    public static String decryptPassword(String encryptedPassword) {
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            return new String(decoder.decode(encryptedPassword));
        }
        catch (Exception e){
            throw new CustomExceptions("Unable to decrypt the password because the value is incorrect in Environments.yaml: "+e.getMessage());
        }
    }
}