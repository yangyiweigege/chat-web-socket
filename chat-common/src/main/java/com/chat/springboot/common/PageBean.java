package com.chat.springboot.common;

import java.util.List;

/**
 * 用于分页对象返回
 * 
 * @author yangyiwei
 * @date 2018年6月22日
 * @time 上午10:41:45
 * @param <A>
 */
public class PageBean<A> {
	/**
	 * 当前页码
	 */
	private int currentPage;

	/**
	 * 分页尺寸
	 */
	private int pageSize;

	/**
	 * 总记录数
	 */
	private int totalCount;

	/**
	 * 总共多少页
	 */
	private int totalPage;

	/**
	 * 返回的数据对象
	 */
	private List<A> list;

	public PageBean() {
		super();
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount)
	{
		this.totalCount = totalCount;
		this.totalPage = (totalCount + pageSize - 1) / pageSize;
		if (totalPage < 1) {
			this.totalPage = 1;
		}
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
		if (totalPage < 1) {
			this.totalPage = 1;
		}
	}

	public List<A> getList() {
		return list;
	}

	public void setList(List<A> list) {
		this.list = list;
	}

}
