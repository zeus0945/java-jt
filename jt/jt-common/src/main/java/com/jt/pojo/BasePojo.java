package com.jt.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;
//定义公共的POJO对象
@Data
@Accessors(chain = true)
public class BasePojo implements Serializable{
	 private Date created;
	 private Date updated;
}