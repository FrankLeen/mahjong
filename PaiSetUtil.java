package com.vs.majiang.util.pai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vs.majiang.bean.Pai;

/*
 * 牌组相关的工具类
 */
public class PaiSetUtil {
	
	//获取排序的临时牌集合，用于进行各种胡牌检测
    public static List<Pai> getOrderedTempList(List<Pai> innerPaiList, Pai pai) {
        List<Pai> tempList = getTempList(innerPaiList,pai);
        Collections.sort(tempList);
        return tempList;
    }

    //获取未排序的临时牌集合
    public static List<Pai> getTempList(List<Pai> innerPaiList, Pai pai) {
        List<Pai> tempList = new ArrayList<Pai>();
        for (Pai p : innerPaiList) {
            Pai tempPai = PaiUtil.getTempPai(p);
            tempList.add(tempPai);
        }
        if(pai != null) {
        	tempList.add(pai);
        }
        return tempList;
    }
    
    /*
	 * 获取一张属性一模一样的临时牌
	 */
	private static Pai getTempPai(Pai pai) {
		if(pai == null) {
			return null;
		}
		Pai tempPai = new Pai();
		//设置临时牌的类型
		tempPai.setPaiType(pai.getPaiType());
		//设置临时牌的顺序
		tempPai.setPaiOrder(pai.getPaiOrder());
		//设置是否癞子
		tempPai.setIsLaizi(pai.getIsLaizi());
		return tempPai;
	}
    
    //从集合中移除id相同的牌
  	public static void removePai(List<Pai> paiList, Pai... removePais) {
  		List<Pai> removePaiList = new ArrayList<Pai>();
  		//把数组转成集合后再移除
  		for (Pai pai : removePais) {
            removePaiList.add(pai);
        }
  		removePai(paiList, removePaiList);
  	}
    
    //从集合1中移除集合2中牌id相同的元素
    public static void removePai(List<Pai> list1, List<Pai> list2) {
    	for (int i = 0; i < list2.size(); i++) {
        	Pai pai = list2.get(i);
            int size = list1.size();
            for (int j = 0; j < size; j++) {
            	//牌的类型和顺序相同则移除
                if (pai.getPaiType() == list1.get(j).getPaiType() && pai.getPaiOrder() == list1.get(j).getPaiOrder()) {
                	list1.remove(j);
                    break;
                }
            }
        }
    }
    
}
