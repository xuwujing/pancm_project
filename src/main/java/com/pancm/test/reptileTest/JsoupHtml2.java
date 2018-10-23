package com.pancm.test.reptileTest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
public class JsoupHtml2 {
 
    public static void main(String[] args) {       
        try {
            String url ="http://sou.zhaopin.com/jobs/searchresult.ashx?";
            String city ="西安";
            String keywords = "java";
            BufferedWriter bWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream("output.html"),"utf-8"));
            bWriter.write("");
             
             
            File input = new File("input.html");
            Document doc2 = Jsoup.parse(input, "UTF-8", "");
            Element table = doc2.getElementById("workinfo");
            table.text("");
            Element theader = table.appendElement("tr");
            theader.appendElement("th").text("序号");
            theader.appendElement("th").text("职位名称");
            theader.appendElement("th").text("公司名称");
            theader.appendElement("th").text("职位月薪");
            theader.appendElement("th").text("工作地点");
            theader.appendElement("th").text("发布日期");          
         
             
            for(int page=0;page<10;page++){             
                Document doc = Jsoup.connect(url+city+"&kw="+keywords+"&p="+page).get();               
                Element content = doc.getElementById("newlist_list_content_table");        
                Elements zwmcEls = content.getElementsByClass("zwmc");
                Elements gsmcEls = content.getElementsByClass("gsmc");         
                Elements zwyxEls = content.getElementsByClass("zwyx");         
                Elements gzddEls = content.getElementsByClass("gzdd");         
                Elements gxsjEls = content.getElementsByClass("gxsj");
                 
                for(int i = 1;i<zwmcEls .size();i++){               
                    Element tr =table.appendElement("tr");
                    tr.appendElement("td").text((page+1)+"-"+i);
                    tr.appendElement("td").text(zwmcEls.get(i).tagName("a").text());
                    tr.appendElement("td").text(gsmcEls.get(i).tagName("a").text());
                    tr.appendElement("td").text(zwyxEls.get(i).tagName("a").text());
                    tr.appendElement("td").text(gzddEls.get(i).tagName("a").text());
                    tr.appendElement("td").text(gxsjEls.get(i).tagName("a").text());
                }
            }
            System.out.println(doc2.html());
            bWriter.write(doc2.html());
            bWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
    }
 
}