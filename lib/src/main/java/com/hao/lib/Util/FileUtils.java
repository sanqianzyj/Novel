package com.hao.lib.Util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.arraycopy;

public class FileUtils {

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] fileToBytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    /**
     * 根据byte数组，生成文件
     */
    public static boolean byteToFile(byte[] bfile, File file) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            if (!file.exists() && file.isDirectory()) {//判断文件目录是否存在
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("转换文件出错", e.getMessage());
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return true;
        }
    }

    /* 把batmap 转file
     * @param bitmap
     * @param filepath
     */
    public static File saveBitmapFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * @param fileName 文件名
     * @param context  .
     * @return 读取assets文件夹下的文本文件
     */
    public static String readAssetsFile(String fileName, Context context) {
        StringBuilder builder = new StringBuilder();
        AssetManager manager = context.getAssets();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(manager.open(fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    /**
     * @param fileName 文件名
     * @param context  .
     * @return 读取assets文件夹下的文本文件
     */
    public static byte[] readAssetsFileTobyte(String fileName, Context context) {
        byte[] buffer = null;
        AssetManager manager = context.getAssets();
        try {
            InputStream reader = manager.open(fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = reader.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            reader.close();
            bos.close();
            buffer = bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    public static byte[] readBL(String fileName, Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager manager = context.getAssets();
            InputStream stream = manager.open(fileName);
            int length = stream.available();
            byte[] buffer = new byte[length];
            stream.read(buffer);
            stream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FileUtil",
                    "readBL(FileUtil.java:32)" + e.toString());
        }

        return null;
    }


    public static byte[] readFile(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            int length = stream.available();
            byte[] buffer = new byte[length];
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length
                    && (numRead = stream.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }

            Log.d("FileUtil",
                    "readFile(FileUtil.java:63)" + file.getName() + "读取成功………………");
            stream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FileUtil",
                    "readBL(FileUtil.java:32)" + e.toString());
        }

        return null;
    }


    /**
     * byte 转hex
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * byte转string
     *
     * @param src
     * @return
     */
    public static String byteToString(byte[] src) {
        return hexStringToString(bytesToHexString(src));
    }


    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    /**
     * hex to int
     *
     * @param hex
     * @return
     */
    public static int hexStringToInt(String hex) {
        return Integer.parseInt(hex, 16);
    }


    /**
     * 低字节数组到short的转换
     *
     * @param b .
     * @return .
     */
    public static short lBytesToShort(byte[] b) {
        int s = 0;
        //        if (b[1] >= 0) {
        //            s = s + b[1];
        //        } else {
        //            s = s + 256 + b[1];
        //        }
        //        s = s * 256;
        //        if (b[0] >= 0) {
        //            s = s + b[0];
        //        } else {
        //            s = s + 256 + b[0];
        //        }
        s = (((short) b[0] & 0xff) << 0) | (((short) b[1] & 0xff) << 8);
        return (short) s;
    }

    /**
     * word
     * 将长度为2的byte数组转换为16位int
     *
     * @param res byte[]
     * @return int
     */
    public static int byte2int(byte[] res) {
        // res = InversionByte(res);
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // | 表示安位或
        return targets;
    }

    /**
     * Long转byte[]
     *
     * @param values
     * @return
     */
    public static byte[] LongToBytes(long values) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((values >> offset) & 0xff);
        }
        return buffer;
    }

    /**
     * byte[]转long
     *
     * @param buffer
     * @return
     */
    public static int BytesToLong(byte[] buffer) {
        int values = 0;
        for (int i = 0; i < 8; i++) {
            values <<= 8;
            values |= (buffer[i] & 0xff);
        }
        return values;
    }


    /**
     * Dword
     * 将低字节数组转换为int
     *
     * @param b byte[]
     * @return int
     */
    public static int lBytesToInt(byte[] b) {
        int s = 0;
        //        for (int i = 0; i < 3; i++) {
        //            if (b[3 - i] >= 0) {
        //                s = s + b[3 - i];
        //            } else {
        //                s = s + 256 + b[3 - i];
        //            }
        //            s = s * 256;
        //        }
        s = (((int) b[0] & 0xff)) | (((int) b[1] & 0xff) << 8) | (((int) b[2] & 0xff) << 16) | (((int) b[3] & 0xff) << 24);

        return s;
    }

    /**
     * hex 转2进制字符串
     *
     * @param hexString .
     * @return .
     */
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(hexString
                    .substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }


    /**
     * DWORD
     * int 转byte
     *
     * @param n int
     * @return 低在前高在后
     */
    public static byte[] int2byte(long n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }


    /**
     * WORD
     * 将short转为低字节在前，高字节在后的byte数组
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] wrod2Byte(int n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * WORD
     * 将short转为低字节在前，高字节在后的byte数组
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] short2Byte(int n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }


    /**
     * BCD码转为10进制串(阿拉伯数据)<br/>
     * * BCD与十进制字符串的转换.<br/>
     * BCD（Binary Coded Decimal）是用二进制编码表示的十进制数，<br/>
     * 十进制数采用0~9十个数字，是人们最常用的。在计算机中，同一个数可以用两种BCD格式来表示：<br/>
     * ①压缩的BCD码 ②非压缩的BCD码<br/>
     * 压缩的BCD码：<br/>
     * 压缩的BCD码用4位二进制数表示一个十进制数位，整个十进制数用一串BCD码来表示。<br/>
     * 例如，十进制数59表示成压缩的BCD码为0101 1001，十进制数1946表示成压缩的BCD码为0001 1001 0100 0110。<br/>
     * 非压缩的BCD码：<br/>
     * 非压缩的BCD码用8位二进制数表示一个十进制数位，其中低4位是BCD码，高4位是0。<br/>
     * 例如，十进制数78表示成压缩的BCD码为0111 1000。<br/>
     * 从键盘输入数据时，计算机接收的是ASCII码，要将ASCII码表示的数转换成BCD码是很简单的，<br/>
     * 只要把ASCII码的高4位清零即可。反之，如果要把BCD码转换成ASII码，只要把BCD码 "或|"00110000即可。
     *
     * @param bytes BCD码
     * @return String 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        if (bytes.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            int h = ((aByte & 0xff) >> 4) + 48;
            sb.append((char) h);
            int l = (aByte & 0x0f) + 48;
            sb.append((char) l);
        }
        return sb.toString();
    }

    /**
     * 10进制串转为BCD码<br/>
     *
     * @param data 10进制串
     * @return byte[] BCD码
     */
    public static byte[] str2Bcd(String data) {
        if (data.length() == 0) {
            return new byte[0];
        }

        String str = data;
        // 奇数个数字需左补零
        if (str.length() % 2 != 0) {
            str = "0" + str;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        char[] cs = str.toCharArray();
        for (int i = 0; i < cs.length; i += 2) {
            int high = cs[i] - 48;
            int low = cs[i + 1] - 48;
            baos.write(high << 4 | low);
        }
        return baos.toByteArray();
    }


    /**
     * 10进制串转为BCD码<br/>
     *
     * @param data 10进制串
     * @return byte[] BCD码
     */
    static byte[] str2Bcd(String data, int length) {
        if (data.length() == 0) {
            return new byte[0];
        }

        String str = data;
        // 奇数个数字需左补零
        if (str.length() % 2 != 0) {
            str = "0" + str;
        }
        int l = length - str.length();
        for (int i = 0; i < l; i++) {
            str = "0" + str;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        char[] cs = str.toCharArray();
        for (int i = 0; i < cs.length; i += 2) {
            int high = cs[i] - 48;
            int low = cs[i + 1] - 48;
            baos.write(high << 4 | low);
        }
        return baos.toByteArray();
    }


    /**
     * @param posi 下标
     * @param list 源
     * @return 黑名单 位置(0)、项数8:全量，4：增量
     */
    public static int list(int posi, byte[] list) {
        byte[] position = new byte[4];
        arraycopy(list, posi, position, 0, 4);
        return lBytesToInt(position);
    }

    /**
     * 合并Type数组
     *
     * @param datas
     * @return
     */
    public static Type[] mergeType(Type[]... datas) {
        int length = 0;
        Type[] temp = new Type[128];
        for (Type[] data : datas) {
            arraycopy(data, 0, temp, length, data.length);
            length += data.length;
        }
        Type[] dataString = new Type[length];
        arraycopy(temp, 0, dataString, 0, length);
        return dataString;
    }


    /**
     * 合并String数组
     *
     * @param datas
     * @return
     */
    public static String[] mergeString(String[]... datas) {
        int length = 0;
        String[] temp = new String[128];
        //  Log.e("TAG", "mergeString: " + datas.length);
        for (String[] data : datas) {
            arraycopy(data, 0, temp, length, data.length);
            length += data.length;
        }
        String[] dataString = new String[length];
        arraycopy(temp, 0, dataString, 0, length);
        return dataString;
    }


    /**
     * 合并int数组
     *
     * @param datas
     * @return
     */
    public static int[] mergeInt(int[]... datas) {
        int length = 0;
        int[] temp = new int[128];
        for (int[] data : datas) {
            arraycopy(data, 0, temp, length, data.length);
            length += data.length;
        }
        int[] dataString = new int[length];
        arraycopy(temp, 0, dataString, 0, length);
        return dataString;
    }

    /**
     * 合并byte数组
     *
     * @param datas .
     * @return .
     */
    public static byte[] mergeByte(byte[]... datas) {
        int length = 0;
        byte[] endData = new byte[2048];
        for (byte[] data : datas) {
            arraycopy(data, 0, endData, length, data.length);
            length += data.length;
        }
        byte[] data = new byte[length];
        arraycopy(endData, 0, data, 0, length);
        return data;
    }


    /**
     * 获取高四位
     *
     * @param data .
     * @return .
     */
    public static int getHeight4(byte data) {
        int height;
        height = ((data & 0xf0) >> 4);
        return height;
    }

    /**
     * 获取低四位
     *
     * @param data .
     * @return .
     */
    public static int getLow4(byte data) {
        int low;
        low = (data & 0x0f);
        return low;
    }

    /**
     * byte[] 转换成bit
     *
     * @param bytes .
     * @return .
     */
    public static String bytesToBits(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            sb.append(byteToBits(b));
        }
        return sb.toString();
    }

    /**
     * byte转换成8位bit
     *
     * @param b .
     * @return .
     */
    public static String byteToBits(byte b) {
        int z = b;
        z |= 256;
        String str = Integer.toBinaryString(z);
        int len = str.length();
        return str.substring(len - 8, len);
    }

    /**
     * byte[]转换成8位bit的字符串
     *
     * @param bt .
     * @return .
     */
    public static StringBuilder byteReverseBits(byte[] bt) {
        StringBuilder sb;
        StringBuilder sb1 = new StringBuilder();
        for (byte b : bt) {
            int z = b;
            z |= 256;
            String str = Integer.toBinaryString(z);
            int len = str.length();
            sb = new StringBuilder(str.substring(len - 8, len));
            sb1.append(sb/*.reverse()*/);
        }
        return sb1;
    }


    /**
     * 将short转为高字节在前，低字节在后的byte数组
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] toHH(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 将byte数组中的元素倒序排列
     */
    public static byte[] bytesReverseOrder(byte[] b) {
        int length = b.length;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[length - i - 1] = b[i];
        }
        return result;
    }

    /**
     * 十六进制串转化为byte数组
     *
     * @return the array of byte
     */
    public static byte[] hex2byte(String hex)
            throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = Integer.valueOf(byteint).byteValue();
        }
        return b;
    }


    /**
     * 参数 转 byte[]
     *
     * @param object 参数
     * @param type   类型
     * @return .
     */
    public static byte[] param2Byte(Object object, Type type) {

        if (object instanceof Integer) {
            if (type == Type.WORD) {
                return short2Byte((Integer) object);
            } else if (type == Type.DWORD || type == Type.LONG) {
                return int2byte((Integer) object);
            } else {
                //默认1字节byte
                int i = (int) object;
                return new byte[]{(byte) i};
            }
        } else if (object instanceof Long) {
            return int2byte((Long) object);
        } else {
            return (type == Type.BCDB || type == Type.BCDW) ? str2Bcd((String) object) : hex2byte((String) object);
        }
    }

    /**
     * 参数 转 byte[]
     *
     * @param object 参数
     * @param type   类型
     * @return .
     */
    public static byte[] param2Byte2(Object object, Type type, int len) {
        if (object instanceof Integer) {
            if (type == Type.WORD) {
                return short2Byte((Integer) object);
            } else if (type == Type.DWORD || type == Type.LONG) {
                return int2byte((Integer) object);
            } else {
                //默认1字节byte
                int i = (int) object;
                return new byte[]{(byte) i};
            }
        } else {
            return (type == Type.BCDB || type == Type.BCDW) ? str2Bcd((String) object, len) : hex2byte((String) object);
        }
    }

    /**
     * 数据解析
     *
     * @param data 源数据
     * @param type 类型
     * @return .
     */
    public static Object byte2Parm(byte[] data, Type type) {
        if (type == Type.WORD) {
            return byte2int(data);
        } else if (type == Type.DWORD || type == Type.LONG) {
            return lBytesToInt(data);
        } else if (type == Type.LINECHAR) {
            int lenth = 0;
            for (int i = 0; i < data.length; i++) {
                if (data[i] == 0x00) {
                    break;
                } else {
                    lenth++;
                }
            }
            byte[] temp = new byte[lenth];
            arraycopy(data, 0, temp, 0, temp.length);
            return byteToString(temp);
        } else if (type == Type.CHAR) {
            return byteToString(data);
        } else if (type == Type.BCDW || type == Type.BCDB) {
            return bcd2Str(data);
        } else if (type == Type.BYTEINT) {
            return hexStringToInt(bytesToHexString(data));
        } else if (type == Type.REVERSE) {
            return bytesToHexString(bytesReverseOrder(data));
        } else if (type == Type.GETSTRING) {
            List<Byte> list = new ArrayList<>();
            for (int i = 0; i < data.length; i++) {
                if (data[i] == 0x00) {
                    break;
                } else {
                    list.add(data[i]);
                }
            }
            byte[] newByte = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                newByte[i] = list.get(i);
            }
            return new String(newByte);
        } else if (type == Type.ASCLL) {
            char[] tChars = new char[data.length];
            for (int i = 0; i < data.length; i++) {
                tChars[i] = (char) data[i];
            }
            StringBuffer tStringBuf = new StringBuffer();
            tStringBuf.append(tChars);
            return tStringBuf.toString();
        }/*else if (type == Type.BTTEINTS) {

        }*/ else {
            return bytesToHexString(data);
        }
    }


    /**
     * 将目标中的数据转入到指定大小的byte数组中
     */
    public static byte[] GetAssignByte(Object target, byte[] assign) {
        if (target instanceof String) {
            if (((String) target).getBytes().length < assign.length) {
                System.arraycopy(((String) target).getBytes(), 0, assign, 0, ((String) target).getBytes().length);
            } else {
                System.arraycopy(((String) target).getBytes(), 0, assign, 0, assign.length);
            }
        } else if (target instanceof byte[]) {
            if (((byte[]) target).length < assign.length) {
                System.arraycopy(target, 0, assign, 0, ((byte[]) target).length);
            } else {
                System.arraycopy(target, 0, assign, 0, assign.length);
            }
        } else if (target instanceof Integer) {
            byte[] object = param2Byte(target, Type.BYTE);
            if (object.length < assign.length) {
                System.arraycopy(object, 0, assign, 0, object.length);
            } else {
                System.arraycopy(object, 0, assign, 0, assign.length);
            }
        } else if (target instanceof Long) {
            byte[] object = param2Byte(target, Type.BYTE);
            if (object.length < assign.length) {
                System.arraycopy(object, 0, assign, 0, object.length);
            } else {
                System.arraycopy(object, 0, assign, 0, assign.length);
            }
        }

        return assign;
    }

    public static byte[] char2Byte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    /**
     * 求长度用的 int转byte[]
     *
     * @param value
     * @param len
     * @return
     */
    public static byte[] int2Bytes(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

    public static String int2ByteStr(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return bytesToHexString(b);
    }

    public static String fen2Yuan(int prices) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format((float) prices / (float) 100);
    }


    /*
     *将指定字符串转换为固定长度的byte容纳的字符串
     */
    public static String formatStringToByteString(int i, String string) {
        byte[] newByte = new byte[i];
        byte[] strByte = FileUtils.hex2byte(string);
        if (strByte.length >= newByte.length) {
            arraycopy(strByte, strByte.length - newByte.length, newByte, 0, newByte.length);
        } else {
            arraycopy(strByte, 0, newByte, newByte.length - strByte.length, strByte.length);
        }
        return bytesToHexString(newByte);
    }

    /**
     * 字符串转化为字节数组
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d;
        //转换的时候应该注意的是单双数的情况，网上很多都只处理了双数的情况，也就是默认刚好填满，这样16进制字符串长度是单数的话会丢失掉一个字符 因为length/2 是舍去余数的
        if (hexString.length() % 2 != 0) {// 16进制字符串长度是单数
            length = length + 1;
            d = new byte[length];
            // 这里把byte数组从后往前填，字符串也是翻转着填的，最后会空出byte数组的第一个（用来填充我们单出来的那个字符）
            for (int i = length - 1; i > 0; i--) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) | charToByte(hexChars[pos - 1]) << 4);
            }
            d[0] = charToByte(hexChars[0]);
        } else {// 双数情况
            d = new byte[length];
            for (int i = 0; i < length; i++) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            }
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] compress(byte[] pk64) {
        byte[] pk33 = new byte[33];
        System.arraycopy(pk64, 0, pk33, 1, pk33.length - 1);
        pk33[0] = (byte) (2 + (pk64[32 * 2 - 1] & 0x01));
        return pk33;
    }


    public static String deleteCover(String string) {
        while (string.startsWith("0")) {
            string = string.replaceFirst("0", "");
        }
        return string;
    }


    /**
     * 保存数据至文件
     *
     * @param string
     * @param file
     * @return
     */
    public static boolean saveStrToFile(String string, File file) {
        try {
            checkFile(file);
            FileWriter writer;
            writer = new FileWriter(file.getPath());
            writer.write("");//清空原文件内容
            writer.write(string);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    /**
     * 判断文件是否存在  不存在则创建
     *
     * @param file
     */
    public static void checkFile(File file) {
        try {
            String string = file.getPath();
            String[] mirs = string.split("/");
            String checkMirs = "";
            File mir;
            for (int i = 0; i < mirs.length; i++) {
                if (!mirs[i].equals("")) {
                    checkMirs = checkMirs + "/" + mirs[i];
                    mir = new File(checkMirs);
                    if (!mir.exists()) {
                        if (checkMirs.contains(".")) {
                            mir.createNewFile();
                        } else {
                            mir.mkdir();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i("错误", "checkFile(FileUtils.java:1016) 文件验证错误" + e.getMessage());
        }
    }


    /**
     * 复制文件夹
     *
     * @param sourcePath
     * @param newPath
     * @throws IOException
     */
    public static void copyDir(String sourcePath, String newPath) throws IOException {
        File file = new File(sourcePath);
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(sourcePath + file.separator + filePath[i])).isDirectory()) {
                copyDir(sourcePath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }

            if (new File(sourcePath + file.separator + filePath[i]).isFile()) {
                copyFile(sourcePath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }

        }
    }


    /**
     * 复制文件
     *
     * @param oldPath
     * @param newPath
     * @throws IOException
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);

        byte[] buffer = new byte[2097152];
        int readByte = 0;
        while ((readByte = in.read(buffer)) != -1) {
            out.write(buffer, 0, readByte);
        }

        in.close();
        out.close();
    }


    /**
     * 删除所有文件
     *
     * @param path
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除文件夹
     *
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 遍历文件夹查询 包含第一个str的文件
     *
     * @param rootDirectory
     * @param str
     * @param path
     * @return
     */
    public static String searchFile(String rootDirectory, String str, String path) {
        File file = new File(rootDirectory);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        String searchPath = searchFile(files[i].getPath(), str, path);
                        if (!searchPath.equals("")) {
                            path = searchPath;
                        }
                    } else if (files[i].getName().contains(str)) {
                        path = files[i].getPath();
                    }
                }
            }
        }
        return path;
    }


    /**
     * 遍历文件夹查询 包含str的所有文件
     *
     * @param rootDirectory 根目录  用于查询文件的主目录
     * @param str           需要查询的文件的标识
     * @return
     */
    public static List<String> searchFiles(String rootDirectory, String str) {
        List<String> strings = new ArrayList<>();
        File file = new File(rootDirectory);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        strings.addAll(searchFiles(files[i].getPath(), str));
                    } else if (files[i].getName().contains(str)) {
                        strings.add(files[i].getPath());
                    }
                }
            }
        }
        return strings;
    }


}
