package com.hz.demo.core;

/**
 * 分页
 */
public class Page {
	

	private String msg;//解析提示文本
	private String[] limits; //每页条数的选择项,默认：[10,20,30,40,50,60,70,80,90]。
	private int limit=10; //每页显示的条数（默认：10）
	private int code;  ////解析接口状态
	private int page=1; //代表当前页码
	private Object data;//layui接受数据
	private Object rows;  //easyui接受数据
	private int total;//easyui数据总数
	private int count;//layui数据总数
	private int currentResult;	//当前记录起始索引
	private String back1;  //备用字段
	private String back2;//备用字段
	private PageData pd = new PageData();
	public Page(){
		try {

			/*if(limits==null){
				limits=new String[]{"10","20","30","40","50"};
			}*/
			//this.showCount = Integer.parseInt(Tools.readTxtFile(Const.PAGE));
		} catch (Exception e) {
			//this.showCount = 15;
		}
	}

	public int getCurrentResult() {
		currentResult = (getPage()-1)*getLimit();
		if(currentResult<0)
			currentResult = 0;
		return currentResult;
	}

	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public  String[] getLimits() {
		return limits;
	}

	public void setLimits( String[] limits) {
		this.limits = limits;
	}


	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		if(data==null){
			data=new Object();
		}
		this.data = data;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	public PageData getPd() {
		return pd;
	}

	public void setPd(PageData pd) {
		this.pd = pd;
	}

	public String getBack1() {
		return back1;
	}

	public void setBack1(String back1) {
		this.back1 = back1;
	}

	public String getBack2() {
		return back2;
	}

	public void setBack2(String back2) {
		this.back2 = back2;
	}
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
