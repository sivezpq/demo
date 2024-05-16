package com.website.framework.security.controller;

import com.website.application.entity.User;
import com.website.application.service.HiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SecurityController {
    Log log = LogFactory.getLog(SecurityController.class);

    @RequestMapping("/security_check")
    public String security_check() throws Exception{
        return "";
    }

}
