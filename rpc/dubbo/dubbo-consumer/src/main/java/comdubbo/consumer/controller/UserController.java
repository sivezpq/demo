package comdubbo.consumer.controller;

import com.dubbo.api.entity.User;
import com.dubbo.api.service.UserDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author lxy
 * @version 1.0
 * @date 2021/6/3 15:07
 */
@RestController
public class UserController {
    @DubboReference
    private UserDubboService userDubboService;

    @GetMapping("user")
    public User getUser(Long id) {
        return userDubboService.getUser(id);
    }
}
