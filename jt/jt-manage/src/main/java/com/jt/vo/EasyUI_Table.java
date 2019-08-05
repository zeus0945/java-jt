package com.jt.vo;

import java.util.List;

import com.jt.pojo.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUI_Table {
	private Integer total;	//总记录数
	private List<Item> rows;//分页后记录数
	
}
