package com.huaweicloud;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.moderation.v3.ModerationClient;
import com.huaweicloud.sdk.moderation.v3.model.RunTextModerationRequest;
import com.huaweicloud.sdk.moderation.v3.model.RunTextModerationResponse;
import com.huaweicloud.sdk.moderation.v3.model.TextDetectionDataReq;
import com.huaweicloud.sdk.moderation.v3.model.TextDetectionReq;
import com.huaweicloud.sdk.moderation.v3.region.ModerationRegion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/huaweicloud")
public class TestController {

    @GetMapping(value = "/test")
    public String test(){
        return "OK";
    }
}
