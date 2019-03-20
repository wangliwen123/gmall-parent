package com.atguigu.gmall.admin.pms.vo;

import com.atguigu.gmall.admin.validator.FlagValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;

/**
 * 商品属性参数
 *
 * JSR303 ==后端校验
 *
 * 用户注册：
 *     email\phone\idcard
 *     dhhdhd\dhhsha\iuy
 *
 * 数据校验：
 *  1）、前端校验；jquery获取到值，利用正则表达式进行校验；
 *  2）、后端校验；
 *         1）、挨个判断
 *         2）、SpringMVC 使用 JSR303（规范===HibernateValidator）
 *              1）、username=sss&password=xxx Controller(AdminParam parma)【只要入参的javaBean属性名和提交的属性名一致，就会自动封装】
 *              2）、给每一个字段标注我们的校验逻辑。注解的方式
 *                  @NotEmpty @Email 支持Hibernate自己的注解和javax.validation.constraints规定的注解
 *              3）、要验证的javaBean加上@Valid 注解，并且在其后面紧跟一个BindingResult。这个BindingResult就能将校验出现的错误信息封装起来
 *
 *
 *  3）、获取错误校验的详细信息
 *   List<FieldError> fieldErrors = result.getFieldErrors();
*             for (FieldError fieldError : fieldErrors) {
*                 //获取属性名 代表当前属性出错了
*                 String field = fieldError.getField();
*                 //获取当时不满足要求的这个值
*                 Object value = fieldError.getRejectedValue();
*                 //获取提示信息
*                 String message = fieldError.getDefaultMessage();
*             }
 *
 *
 */
@Data
public class PmsProductAttributeParam {
    @ApiModelProperty("属性分类ID")
    @NotEmpty(message = "属性分类不能为空")
    private Long productAttributeCategoryId;


    @ApiModelProperty("属性名称")
    @NotEmpty(message = "属性名称不能为空")
    private String name;
    @ApiModelProperty("属性选择类型：0->唯一；1->单选；2->多选")
    @FlagValidator({"0","1","2"})
    private Integer selectType;
    @ApiModelProperty("属性录入方式：0->手工录入；1->从列表中选取")
    @FlagValidator({"0","1"})
    private Integer inputType;
    @ApiModelProperty("可选值列表，以逗号隔开")
    private String inputList;

    private Integer sort;
    @ApiModelProperty("分类筛选样式：0->普通；1->颜色")
    @FlagValidator({"0","1"})
    private Integer filterType;
    @ApiModelProperty("检索类型；0->不需要进行检索；1->关键字检索；2->范围检索")
    @FlagValidator({"0","1","2"})
    private Integer searchType;
    @ApiModelProperty("相同属性产品是否关联；0->不关联；1->关联")
    @FlagValidator({"0","1"})
    private Integer relatedStatus;
    @ApiModelProperty("是否支持手动新增；0->不支持；1->支持")
    @FlagValidator({"0","1"})
    private Integer handAddStatus;
    @ApiModelProperty("属性的类型；0->规格；1->参数")
    @FlagValidator({"0","1"})
    private Integer type;


    private String email;

}
