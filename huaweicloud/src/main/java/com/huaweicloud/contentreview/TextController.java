package com.huaweicloud.contentreview;

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

/**
 * @Desc: 内容审核-文本
 */
@Controller
@RequestMapping("/contentreview/text")
public class TextController {

    @Value("${AK:MUL4TVLSXOORBJSMBW3Q}")
    private String ak;

    @Value("${SK:Qsx8dBFciHAnj2p3nHmsBITtMO6YKNHVTvUYasiM}")
    private String sk;

    @Value("${north:cn-north-4}")
    private String north;

    @GetMapping(value = "/v3")
    @ResponseBody
    public String v3(String text, String blacklist) throws Exception{
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);

        ModerationClient client = ModerationClient.newBuilder()
                .withCredential(auth)
                .withRegion(ModerationRegion.valueOf(north))  //把xxx替换成服务所在的区域，例如北京四：cn-north-4。
                .build();
        RunTextModerationRequest request = new RunTextModerationRequest();
        TextDetectionReq body = new TextDetectionReq();
        TextDetectionDataReq databody = new TextDetectionDataReq();
        databody.withText(text);
        body.withData(databody);
        body.withEventType("comment");
        if (blacklist!=null && blacklist.trim().length()>0) {
            body.addGlossaryNamesItem(blacklist);
        }
        request.withBody(body);
        RunTextModerationResponse response = null;
        try {
            response = client.runTextModeration(request);
            System.out.println(response.toString());
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (RequestTimeoutException e) {
            e.printStackTrace();
        } catch (ServiceResponseException e) {
            e.printStackTrace();
            System.out.println(e.getHttpStatusCode());
            System.out.println(e.getErrorCode());
            System.out.println(e.getErrorMsg());
        }
        return response.toString();
    }
}
