package com.centro.platform.api.java.security.business;

public class EncryptionUtil {

    /**
	 * base16 encode the given ciphertext byte array
	 * @param ciphertext the cipher text array
	 * @return String the encoded string
	 */
    public static String base16encode(byte[] ciphertext)
    {
    	int bytelen = ciphertext.length;
        int bytelenX2 = bytelen * 2;
        StringBuffer b16string = new StringBuffer(bytelenX2);
        int[] bit4 = new int[bytelenX2];
        int i = 0;
        int k;
        //bytelen = Len(CStr(encrppwd))

        //break every byte to nibbles
        for (k = 0; k < bytelen; k++) {
            bit4[i] = (0xF0 & ciphertext[k]) >> 4;
            bit4[i + 1] = 0xF & ciphertext[k];
            i = i + 2;
        }

        //chk every nibble for appropriate hex value & concatenete in a string
        for (i = 0; i < bytelenX2; i++) {
            switch (bit4[i]) {
            case 0:
                b16string.append("0");
                break;
            case 1:
                b16string.append("1");
                break;
            case 2:
                b16string.append("2");
                break;
            case 3:
                b16string.append("3");
                break;
            case 4:
                b16string.append("4");
                break;
            case 5:
                b16string.append("5");
                break;
            case 6:
                b16string.append("6");
                break;
            case 7:
                b16string.append("7");
                break;
            case 8:
                b16string.append("8");
                break;
            case 9:
                b16string.append("9");
                break;
            case 10:
                b16string.append("A");
                break;
            case 11:
                b16string.append("B");
                break;
            case 12:
                b16string.append("C");
                break;
            case 13:
                b16string.append("D");
                break;
            case 14:
                b16string.append("E");
                break;
            case 15:
                b16string.append("F");
                break;
            }
        }
        
        return b16string.toString();
    }


	/**
	 * encode the given string and return a byte array.
	 * @param ciphertext the ciphertext string
	 * @return byte[] the return array
	 */
    public static byte[] base16decode(String ciphertext)
    {
        int j = 0;
        int m = 0;
        int bytelen;
        char ch;
        byte[] twochar = new byte[2];

        bytelen = ciphertext.length();
        //each element Bytes() Stores 4 bits of every byte in string
        byte[] results = new byte[bytelen / 2];

        twochar[0] = 20;
        twochar[1] = 20;

        //Fetch charracter from string & put corrospondin no in Bytes array
        for (int i = 0; i < bytelen; i++) {
            ch = ciphertext.charAt(i);

            switch (ch) {
            case '0':
                twochar[m % 2] = 0;
                break;
            case '1':
                twochar[m % 2] = 1;
                break;
            case '2':
                twochar[m % 2] = 2;
                break;
            case '3':
                twochar[m % 2] = 3;
                break;
            case '4':
                twochar[m % 2] = 4;
                break;
            case '5':
                twochar[m % 2] = 5;
                break;
            case '6':
                twochar[m % 2] = 6;
                break;
            case '7':
                twochar[m % 2] = 7;
                break;
            case '8':
                twochar[m % 2] = 8;
                break;
            case '9':
                twochar[m % 2] = 9;
                break;
            case 'A':
                twochar[m % 2] = 10;
                break;
            case 'B':
                twochar[m % 2] = 11;
                break;
            case 'C':
                twochar[m % 2] = 12;
                break;
            case 'D':
                twochar[m % 2] = 13;
                break;
            case 'E':
                twochar[m % 2] = 14;
                break;
            case 'F':
                twochar[m % 2] = 15;
                break;
            default:
                break;
            }
            if (twochar[0] == 20) {
                //throw new SabaException(111);
            }
            //if both nibbles fetched
            if (i % 2 == 1) {
                Byte b = Byte.decode("0x0");
                twochar[0] = (byte)(((twochar[0]) | b.byteValue()) << 4);
                twochar[1] = (byte)(twochar[1] | b.byteValue());
                results[j] = (byte)(twochar[0] | twochar[1]);
                j++;
            }
            m++;
        }
        return results;
    }
    
}

