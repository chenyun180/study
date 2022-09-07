package com.cloud.common.model.test;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@TableName("person_copy")
@ApiModel(value = "person_copy表", description = "测试swagger2")
public class PersonCopy extends BaseModel {

    private static final long serialVersionUID = 6799108807024900276L;

    @ApiModelProperty(value = "本表主键，自增")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "年龄")
    private Integer personAge;

    @ApiModelProperty(value = "姓名")
    private String personName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
