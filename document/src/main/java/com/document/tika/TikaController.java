package com.document.tika;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 模板Controller
 * 
 * @author ruoyi
 * @date 2024-04-21
 */
@Controller
@RequestMapping("/tika")
public class TikaController
{
    @Autowired
    private Tika tika;

    @PostMapping("/monitor1")
    @ResponseBody
    public ResultEntity template(String data1, String data2) throws Exception
    {
//        String content = tika.parseToString(new File("/Users/huagang/study/配置防止浏览器直接打开下载的文件.txt"));
//        return content;
        System.out.println(data1 + "@@" + data2);
        return new ResultEntity(200, "success", data1 + "@@" + data2);
    }

}
