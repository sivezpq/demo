package com.document.tika;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

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

    @GetMapping("/getDocumentContent")
    @ResponseBody
    public String template() throws Exception
    {
        String content = tika.parseToString(new File("/Users/huagang/Downloads/mysql参数.xlsx"));
        return content;
    }

}
