package com.jt.service;

import java.util.List;

import com.jt.vo.EasyUI_Tree;

public interface ItemCatService {

	String findItemCatNameById(Long itemCatId);

	List<EasyUI_Tree> findItemCatByParentId(Long parentId);

	List<EasyUI_Tree> findItemCatByCache(Long parentId);
	
}
