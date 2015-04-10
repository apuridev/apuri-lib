/*
 * Copyright 2015 apuri Developers
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.apuri.utils;

import android.content.res.AssetFileDescriptor;
import android.util.Base64;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSACryptoUtils {

    public static String encrypt (String plain, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");

        cipher.init(Cipher.ENCRYPT_MODE, generatePublicKey(key));
        byte[] encryptedBytes= cipher.doFinal(plain.getBytes());

        String encrypted = bytesToString(encryptedBytes);
        return encrypted;
    }

    private static PublicKey generatePublicKey(String key) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
//        FileInputStream fis = new FileInputStream(keyFile.getFileDescriptor());
//        DataInputStream dis = new DataInputStream(fis);
//        byte[] keyBytes = new byte[(int)keyFile.getLength()];
//        dis.readFully(keyBytes);
//        dis.close();
//
//        X509EncodedKeySpec spec =
//                new X509EncodedKeySpec(keyBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePublic(spec);

        byte[] decoded = Base64.decode(key,Base64.DEFAULT);

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);

    }

    public  static String bytesToString(byte[] b) {
        byte[] b2 = new byte[b.length + 1];
        b2[0] = 1;
        System.arraycopy(b, 0, b2, 1, b.length);
        return new BigInteger(b2).toString(36);
    }

    public  byte[] stringToBytes(String s) {
        byte[] b2 = new BigInteger(s, 36).toByteArray();
        return Arrays.copyOfRange(b2, 1, b2.length);
    }
}
