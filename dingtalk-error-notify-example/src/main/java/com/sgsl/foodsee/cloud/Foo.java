package com.sgsl.foodsee.cloud;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;

/**
 * Created by maoxianzhi.
 * CreateTime: 2017/10/19
 * ModifyBy  maoxianzhi
 * ModifyTime: 2017/10/19
 * Description:
 */

@Data
@ApiModel
public class Foo {
    @ApiModelProperty(notes = "原始int类型", dataType = "int", required = true, allowableValues = "range[1,0x7fffffffffffffffL]")
    @Min(1)
    private int nativeInt;

    @ApiModelProperty(notes = "包装int类型", dataType = "int", required = true, allowableValues = "range[1,0x7fffffffffffffffL]")
    @Min(1)
    private Integer packageInt;

    @ApiModelProperty(notes = "message", dataType = "int", required = true)
    @Length(min = 1)
    private String message;
}
