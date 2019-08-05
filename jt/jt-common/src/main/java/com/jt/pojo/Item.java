package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_item")
@Data
@Accessors(chain = true)
//json转化时忽略未知属性.忽略
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item extends BasePojo{
	@TableId(type = IdType.AUTO)
	private Long id;		    //定义主键
	private String title;       //标题
	private String sellPoint;	//卖点信息
	private Long   price;		//商品价格 1.dubbo有精度问题 0.88888..+0.1111112=0.9999.... //2.速度  int > long > dubbo 将商品扩大100倍保存
	private Integer num;		//商品数量
	private String barcode;		//二维码
	private String image;		//商品品图片信息 1.jpg,2.jpg,3.jpg
	private Long cid;			//商品分类信息
	private Integer status;		//商品状态 1正常，2下架，3删除'
	//private String images;		//因为该属性不是数据库字段.
	//el表达式获取数据时调用对象的get方法...
	public String[] getImages() {
		
		return image.split(",");
	}
}
