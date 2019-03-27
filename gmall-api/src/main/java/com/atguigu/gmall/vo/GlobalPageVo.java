package com.atguigu.gmall.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分页响应对象
 *
 * @author: loy
 * @create: 2019-03-21 19:55:15
 */
@Data
@NoArgsConstructor
@ApiModel("分页响应对象")
public class GlobalPageVo<T> implements Serializable{

    @ApiModelProperty("总记录数")
    private Long total;

    @ApiModelProperty("总页数")
    private Long totalPage;

    @ApiModelProperty("每页显示记录数")
    private Long pageSize;

    @ApiModelProperty("数据")
    private List<T> list = new ArrayList<>();

    @ApiModelProperty("当前页")
    private Long pageNum;

    public GlobalPageVo(IPage<T> page){
        total = page.getTotal();
        totalPage = page.getPages();
        pageSize = page.getSize();
        list = page.getRecords();
        pageNum = page.getCurrent();
    }

}
