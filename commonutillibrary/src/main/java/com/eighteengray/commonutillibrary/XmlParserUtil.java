package com.eighteengray.commonutillibrary;

import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by lutao on 2017/11/21.
 */
public class XmlParserUtil
{
    //创建xml文件
    public static void createXmlFile(final String xmlPath)
    {
        File xmlFile = new File(xmlPath);
        FileOutputStream fileOPStream = null;
        try
        {
            fileOPStream = new FileOutputStream(xmlFile);
        }
        catch (FileNotFoundException e)
        {
            Log.e("FileNotFoundException", "can't create FileOutputStream");
        }

        XmlSerializer serializer = Xml.newSerializer();
        try
        {
            serializer.setOutput(fileOPStream,"UTF-8");
            serializer.startDocument(null, true);
            serializer.startTag(null, "books");

            for(int i = 0; i < 5; i ++)
            {
                serializer.startTag(null, "book");
                serializer.startTag(null, "bookname");
                serializer.text("Android教程" + i);
                serializer.endTag(null, "bookname");
                serializer.startTag(null, "bookauthor");
                serializer.text("Frankie" + i);
                serializer.endTag(null, "bookauthor");
                serializer.endTag(null, "book");
            }

            serializer.endTag(null, "books");
            serializer.endDocument();

            serializer.flush();
            fileOPStream.close();
        }
        catch (Exception e)
        {
            Log.e("XmlParserUtil","error occurred while creating xml file");
        }
    }

    /** dom解析xml文件
     * xmlPath xml的路径
     */
    public static void domParseXML(final String xmlPath)
    {
        File file = new File(xmlPath);
        if(!file.exists()||file.isDirectory())
        {
            Log.e("domParseXML", "file not exists");
            return;
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }

        Document doc = null;
        try
        {
            doc = db.parse(file);
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Element root = doc.getDocumentElement();
        NodeList books = root.getElementsByTagName("book");
        String res = "本结果是通过dom解析:" + "\n";

        for(int i = 0; i < books.getLength();i++)
        {
            Element book = (Element)books.item(i);
            Element bookname = (Element)book.getElementsByTagName("bookname").item(0);
            Element bookauthor = (Element)book.getElementsByTagName("bookauthor").item(0);
            res += "书名: " + bookname.getFirstChild().getNodeValue() + " " +
                    "作者: " + bookauthor.getFirstChild().getNodeValue() + "\n";
        }

    }

    /** xmlPullParser解析xml文件
     * xmlPath xml的路径
     */
    public static void xmlPullParseXML(final String xmlPath)
    {
        String res = "本结果是通过XmlPullParse解析:" + "\n";
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            try
            {
                xmlPullParser.setInput(new StringReader(bufferedReaderFile(xmlPath)));
            }
            catch (Exception e)
            {
                Log.e("xmlPullParseXML", e.toString());
            }

            int eventType = xmlPullParser.getEventType();
            try
            {
                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    String nodeName = xmlPullParser.getName();
                    switch (eventType)
                    {
                        case XmlPullParser.START_TAG:
                            if("bookname".equals(nodeName))
                            {
                                res += "书名: " + xmlPullParser.nextText() + " ";
                            }
                            else if("bookauthor".equals(nodeName))
                            {
                                res += "作者: " + xmlPullParser.nextText() + "\n";
                            }
                            break;

                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
    }

    //从sd卡中读取xml文件的内容
    private static String bufferedReaderFile(final String path) throws IOException
    {
        File file=new File(path);
        if(!file.exists()||file.isDirectory())
            throw new FileNotFoundException();

        BufferedReader br=new BufferedReader(new FileReader(file));
        String temp=null;
        StringBuffer sb=new StringBuffer();
        temp=br.readLine();
        while(temp!=null)
        {
            sb.append(temp+" ");
            temp=br.readLine();
        }
        br.close();

        return sb.toString();
    }

}
