package com.sgsl.foodsee.cloud;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

/**
 * Created by maoxianzhi.
 * CreateTime: 2017/10/19
 * ModifyBy  maoxianzhi
 * ModifyTime: 2017/10/19
 * Description:
 */

@Api(description = "钉钉异常测试")
@RestController
@RequestMapping("/dingtalk-test")
@Validated
public class DemoController {
    @GetMapping("test/{id}")
    @ApiOperation(value = "钉钉异常测试", notes = "钉钉异常测试")
    public ResponseEntity testWithId(@PathVariable("id") @Min(1) int id,
                                     @RequestBody Foo foo) {
        return ResponseEntity.ok(ImmutableMap.of(
                "id", id,
                "message", "ok",
                "body", foo.toString()
        ));
    }

    @GetMapping("test")
    @ApiOperation(value = "钉钉异常测试", notes = "钉钉异常测试")
    public ResponseEntity test(@RequestBody Foo foo) {
        return ResponseEntity.ok(ImmutableMap.of(
                "id", 12,
                "message", "ok",
                "body", foo.toString()
        ));
    }
}
