package com.flankleen.bean;

import java.util.ArrayList;
import java.util.List;

/*
 * 牌的类
 */
public class Pai implements Comparable<Pai>{
	
    //牌的类型，1代表万，2代表筒，3代表条，4代表东南西北中发白
    private int paiType;

    //牌的顺序，代表各自牌类型的顺序，东南西北中发白的顺序分别为1-7
    private int paiOrder;

    //牌是否为癞子
    private boolean isLaizi;

    //获取牌的类型
    public int getPaiType() {
	return paiType;
    }
	
    //设置牌的类型
    public void setPaiType(int paiType) {
	this.paiType = paiType;
    }

    //获取牌的id
    public int getPaiOrder() {
	return paiOrder;
    }

    //设置牌的id
    public void setPaiOrder(int paiOrder) {
	this.paiOrder = paiOrder;
    }

    //获取牌是否为癞子
    public boolean getIsLaizi() {
	return isLaizi;
    }

    //设置牌是否为癞子
    public void setIsLaizi(boolean isLaizi) {
	this.isLaizi = ssLaizi;
    }

    @Override
    //重写toString，打印时显示完整
    public String toString() {
	return "Pai{" +
	       "type=" + paiOrder +
	       ", type='" + paiType + '\'' +
	       '}';
    }
    
    @Override
    //实现compareTo接口，用于排序
    public int compareTo(Pai another) {
        //癞子牌排在前面
	if(this.getIsLaizi()) {
	    if(another.getIsLaizi()) {
		if(this.getPaiType() != another.getPaiType()) {
		    return this.getPaiOrder() - another.getPaiOrder();
		}
		return this.getPaiType() - another.getPaiType();
	    }
	    return -1;
	}
	if(another.getIsLaizi()) {
	    return -1;
	}
	if(this.getPaiType() != another.getPaiType()) {
	    return this.getPaiOrder() - another.getPaiOrder();
	}
        return this.getPaiType() - another.getPaiType();
    }

}
