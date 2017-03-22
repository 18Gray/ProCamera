package com.eighteengray.commonutillibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;



/**
 * 数据格式转换工具类，包括InputStream、byte[]、String、
 */
public class DataConvertUtil
{

    /**
     * InputStream转为byte[]
     * @param inputStream
     * @return
     */
    public static byte[] inputStream2Bytes(InputStream inputStream)
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try
        {
            while ((len = inputStream.read(buffer)) != -1)
            {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            inputStream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return outStream.toByteArray();
    }




    /**
     * InputStream转String
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1)
        {
            bos.write(buffer, 0, length);//写入输出流
        }
        inputStream.close();//读取完毕，关闭输入流

        return new String(bos.toByteArray(), "UTF-8");
    }


    /**
     * String转为InputStream
     * @param str
     * @return
     */
    public static InputStream string2InputStream(String str)
    {
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        return inputStream;
    }




    /**
     * byte[]转为16进制的字符串
     * @param src
     * @return
     */
    public static String bytes2HexString(byte[] src)
    {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0)
        {
            return null;
        }
        for (int i = 0; i < src.length; i++)
        {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
            {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * 16进制字符串转为byte[]
     * @param s
     * @return
     */
    public static byte[] hexString2Bytes(String s)
    {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }




    //Bitmap相关

    /**
     * Bitmap转byte[]
     * @param bm
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm)
    {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bas);
        return bas.toByteArray();
    }


    /**
     * byte[]转Bitmap
     * @param b
     * @return
     */
    public static Bitmap bytes2Bimap(byte[] b)
    {
        if (b.length != 0)
        {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else
        {
            return null;
        }
    }


    /**
     * Drawable转Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable)
    {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }


    /**
     * Bitmap转Drawable
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap)
    {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }


    /**
     * Bitmap转16进制字符串
     * @param bitmap
     * @return
     */
    public static String bitmap2HexString(Bitmap bitmap)
    {
        String result = null;
        ByteArrayOutputStream baos = null;
        try
        {
            if (bitmap != null)
            {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = bytes2HexString(bitmapBytes);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (baos != null)
                {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * Bitmap转Base64 String
     * @param bitmap
     * @return
     */
    public static String bitmap2Base64String(Bitmap bitmap)
    {
        String result = null;
        ByteArrayOutputStream baos = null;
        try
        {
            if (bitmap != null)
            {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (baos != null)
                {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * Base64 String转Bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64String2Bitmap(String base64Data)
    {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    /**
     * Base64 String转byte[]
     * @param base64String
     * @return
     */
    public static byte[] base642Bytes(String base64String)
    {
        byte[] bytes = Base64.decode(base64String, Base64.DEFAULT);
        return bytes;
    }


    /**
     * byte[]转Base64 String
     * @param bytes
     * @return
     */
    public static String bytes2Base64(byte[] bytes)
    {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    /**
     * resouceId转Bitmap
     * @param context
     * @param resouceId
     * @return
     */
    public static Bitmap resouceId2Bitmap(Context context, int resouceId)
    {
        Bitmap resultBitmap = null;
        Drawable drawable = context.getResources().getDrawable(resouceId);
        resultBitmap = drawable2Bitmap(drawable);
        return resultBitmap;
    }



    //xml相关

    /**
     * xml文件转特定对象实例
     * @param xml
     * @return
     * @throws Exception
     */
    public static List<Person> xml2Pojo(InputStream xml) throws Exception
    {
        List<Person> persons = null;
        Person person = null;
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "UTF-8"); //为Pull解释器设置要解析的XML数据
        int event = pullParser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT)
        {
            switch (event)
            {
                case XmlPullParser.START_DOCUMENT:
                    persons = new ArrayList<Person>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("person".equals(pullParser.getName()))
                    {
                        person = new Person();
                        person.setId(pullParser.getAttributeValue(0));
                    }
                    if ("name".equals(pullParser.getName()))
                    {
                        String name = pullParser.nextText();
                        person.setName(name);
                    }
                    if ("age".equals(pullParser.getName()))
                    {
                        person.setAge(pullParser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("person".equals(pullParser.getName()))
                    {
                        persons.add(person);
                        person = null;
                    }
                    break;
            }
            event = pullParser.next();
        }
        return persons;
    }


    /**
     * 特定对象转xml文件实例
     * @param persons
     * @param out
     * @throws Exception
     */
    public static void pojo2String(List<Person> persons, OutputStream out) throws Exception
    {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "persons");
        for (Person person : persons)
        {
            serializer.startTag(null, "person");
            serializer.attribute(null, "id", person.getId().toString());
            serializer.startTag(null, "name");
            serializer.text(person.getName().toString());
            serializer.endTag(null, "name");
            serializer.startTag(null, "age");
            serializer.text(person.getAge().toString());
            serializer.endTag(null, "age");
            serializer.endTag(null, "person");
        }
        serializer.endTag(null, "persons");
        serializer.endDocument();
        out.flush();
        out.close();
    }


    public static class Person
    {
        String id;
        String name;
        String age;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getAge()
        {
            return age;
        }

        public void setAge(String age)
        {
            this.age = age;
        }
    }

}
