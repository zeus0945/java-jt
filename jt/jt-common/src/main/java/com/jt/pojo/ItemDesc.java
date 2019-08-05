package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_item_desc")
public class ItemDesc extends BasePojo{
	//item表id与itemDesc表Id一致的
	@TableId   //标识主键信息
	private Long itemId;
	private String itemDesc;//商品详情信息
}
