package com.atguigu.gmall.to;

import com.atguigu.gmall.cms.entity.PrefrenceAreaProductRelation;
import com.atguigu.gmall.cms.entity.SubjectProductRelation;
import com.atguigu.gmall.pms.entity.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 创建和修改商品时使用的参数
 */
@Data
public class PmsProductParam extends Product {

    @ApiModelProperty("商品阶梯价格设置")
    private List<ProductLadder> productLadderList;
    @ApiModelProperty("商品满减价格设置")
    private List<ProductFullReduction> productFullReductionList;
    @ApiModelProperty("商品会员价格设置")
    private List<MemberPrice> memberPriceList;
    @ApiModelProperty("商品的sku库存信息")
    private List<SkuStock> skuStockList;
    @ApiModelProperty("商品参数及自定义规格属性")
    private List<ProductAttributeValue> productAttributeValueList;
    @ApiModelProperty("专题和商品关系")
    private List<SubjectProductRelation> subjectProductRelationList;
    @ApiModelProperty("优选专区和商品的关系")
    private List<PrefrenceAreaProductRelation> prefrenceAreaProductRelationList;


}
