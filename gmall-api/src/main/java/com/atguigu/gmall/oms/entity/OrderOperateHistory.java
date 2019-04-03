package com.atguigu.gmall.oms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单操作历史记录
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oms_order_operate_history")
@ApiModel(value="OrderOperateHistory对象", description="订单操作历史记录")
public class OrderOperateHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单id")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty(value = "操作人：用户；系统；后台管理员")
    @TableField("operate_man")
    private String operateMan;

    @ApiModelProperty(value = "操作时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    @TableField("order_status")
    private Integer orderStatus;

    @ApiModelProperty(value = "备注")
    @TableField("note")
    private String note;


}
