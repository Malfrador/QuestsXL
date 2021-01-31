package de.erethon.questsxl.tools;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * @author Fyreum
 */
public class Serialization {

    // string serialization

    public static byte[] serializeStringArray(String[] strings) {
        ArrayList<Byte> byteList = new ArrayList<>();
        for (String string : strings) {
            int len = string.getBytes().length;
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(len);
            byte[] lenArray = bb.array();
            for (byte b : lenArray) {
                byteList.add(b);
            }
            byte[] stringArray = string.getBytes();
            for (byte b : stringArray) {
                byteList.add(b);
            }
        }
        byte[] result = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            result[i] = byteList.get(i);
        }
        return result;
    }

    public static String[] deserializeStringArray(byte[] bytes) {
        ArrayList<String> stringList = new ArrayList<>();
        for (int i = 0; i < bytes.length;) {
            byte[] lenArray = new byte[4];
            System.arraycopy(bytes, i, lenArray, 0, i + 4 - i);
            ByteBuffer wrapped = ByteBuffer.wrap(lenArray);
            int len = wrapped.getInt();
            byte[] stringArray = new byte[len];
            for (int k = i + 4; k < i + 4 + len; k++) {
                stringArray[k - i - 4] = bytes[k];
            }
            stringList.add(new String(stringArray));
            i += 4 + len;
        }
        return stringList.toArray(new String[0]);
    }

    // java object serialization

    public static byte[] serialize(@NotNull Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException i) {
            i.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Object deserialize(@NotNull byte[] bytes) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInput objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        return null;
    }
}