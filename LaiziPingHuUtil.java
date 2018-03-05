package com.flankleen.util.pai;

import java.util.ArrayList;
import java.util.List;

/*
 * 检测含癞子的平胡，只用于含有癞子的情况下
 */
public class LaiziPingHuUtil {
	
    /**
     * 检测平胡，innerPaiList指玩家可以操作的手牌
     */
    public static boolean checkPingHu(List<Pai> innerPaiList, Pai pai) {
    	List<Pai> tempList = PaiSetUtil.getOrderedTempList(innerPaiList, pai);
    	//移除癞子，得出癞子数目
	int laiZiCount = getLaiZiCount(tempList);
    	return checkLaiZiPingHu(laiZiCount, tempList);
    }

    //遍历，移除癞子，记录癞子数目
    private static int getLaiZiCount(List<Pai> tempList) {
    	int laiZiCount = 0;
    	Iterator<Pai> itr = tempList.iterator();  
    	while(itr.hasNext()) {
    	    Pai p = itr.next();
    	    if(p.getIsLaiZi()) {
    		if(p.getIsZiMo() && !p.getIsJiaGangPai()) {
    		    itr.remove();
    	            laiZiCount++;
    		}else {
    		    //非自摸牌去除癞子属性
    	            p.setIsLaiZi(false);
    		}
    	    }
	}
    	return laiZiCount;
    }

    /**
     * 检测癞子平胡，基本思路如下，对此博客有所参考：http://blog.csdn.net/skillart/article/details/40422885
     * 将去除癞子后的牌按照类型分开，再将不同类型的牌用不同的拆分方式，考虑带将或不带将，然后得出所需最少的癞子数
     * 如果已有的癞子数大于或等于所需最少的癞子数，就可以判断是否可以胡牌
     */
    public static boolean checkLaiZiPingHu(int laiZiCount, List<Pai> tempList) {
    	//获取各种类型牌带将和不带将所需的癞子数集合
    	List<Integer> laiZiNumListWithJiang = getLaiZiNumList(tempList, false);
    	List<Integer> laiZiNumListWithoutJiang = getLaiZiNumList(tempList, true);
    	for(int i = 1; i < 5; i++) {
    	    //由一种类型的牌出将
	    if(laiZiCount >= getLaiZiNumSum(i, laiZiNumListWithJiang, laiZiNumListWithoutJiang)) {
    		return true;
    	    }
    	}
    	return false;
    }
	
    //获取各类型牌带将或不带将的癞子数集合
    private static List<Integer> getLaiZiNumList(List<Pai> tempList, boolean foundJiang) {
    	List<Integer> laiZiNums = new ArrayList<Integer>();
    	//1-4分别代表万、筒、条、字
    	for(int i = 1; i < 5; i++) {
    	    int laiZiNum = getMinLaiZiNum(0, foundJiang, getSingleTypePais(tempList, i));
    	    laiZiNums.add(laiZiNum);
    	}
	    return laiZiNums;
    }

    //根据传入的牌类型，获取该牌类型带将的癞子数和其他牌类型不带将的癞子数之和
    private static int getLaiZiNumSum(int paiType, List<Integer> laiZiNumListWithJiang, 
    		List<Integer> laiZiNumListWithoutJiang) {
    	int laiZiNumSum = laiZiNumListWithJiang.get(paiType - 1);
    	for(int i = 0; i < laiZiNumListWithoutJiang.size(); i++) {
    		if(paiType - 1 != i) {
    			laiZiNumSum += laiZiNumListWithoutJiang.get(i);
    		}
    	}
    	return laiZiNumSum;
    }
    
    //获取单一牌类型的牌集合
    private static List<Pai> getSingleTypePais(List<Pai> paiList, int paiType) {
    	List<Pai> singleTypePais = new ArrayList<Pai>();
    	for(Pai pai : paiList) {
    	    if (pai.getPaiType() == paiType) {
		singleTypePais.add(pai);
	    }
    	}
    	Collections.sort(singleTypePais);
    	return singleTypePais;
    }
    
    /**
     * 检测同类型牌成为全刻、顺最少需要几个癞子
     * 当需要该类型牌出将时，参数foundJiang为false，否则为true
     */
    private static int getMinLaiZiNum(int curNeedLaiZiNum, boolean foundJiang, List<Pai> singleTypeList) {
  	int size = singleTypeList.size();
  	if(size < 3) {
  	    return laiZiNumIfSizeLessThanThree(foundJiang, curNeedLaiZiNum, singleTypeList);
  	}
  	return laiZiNumIfSizeBiggerThanThree(foundJiang, curNeedLaiZiNum, singleTypeList);
    }

    //当集合size小于3时所需癞子数
    private static int laiZiNumIfSizeLessThanThree(boolean foundJiang, 
		int curNeedLaiZiNum, List<Pai> singleTypeList) {
	int size = singleTypeList.size();
	switch (size) {
	//0张的情况
	case 0 : 
	    if(foundJiang) {
  		return curNeedLaiZiNum + 0;
  	    }
  	    return curNeedLaiZiNum + 2;
  	//1张的情况
	case 1 :
	    if(foundJiang) {
  		return curNeedLaiZiNum + 2;
  	    }
  	    return curNeedLaiZiNum + 1;
  	//默认为2张的情况
	default :
	    int paiOrder1 = singleTypeList.get(0).getPaiOrder();
	    int paiOrder2 = singleTypeList.get(1).getPaiOrder();
	    int difValue = Math.abs(paiOrder1 - paiOrder2);
	    if(difValue <= 0) {//牌相同
		if(foundJiang) {
		    return curNeedLaiZiNum + 1;
		}
		return curNeedLaiZiNum + 0;
	    }
	    if(1 <= difValue && difValue <= 2) {//差值在2以内
		if(foundJiang) {
		    return curNeedLaiZiNum + 1;
		}
		    return curNeedLaiZiNum + 3;
		}
		//差值大于2
		if(foundJiang) {
		    return curNeedLaiZiNum + 4;
		}
		return curNeedLaiZiNum + 3;
	    }
	}
    }
    
    //当集合size大于等于3时所需癞子数
    private static int laiZiNumIfSizeBiggerThanThree(boolean foundJiang, 
			int curNeedLaiZiNum, List<Pai> singleTypeList) {
	//移除单刻子，例如1
	List<Pai> singleKeZiList = getSpecifiedNumKeZi(singleTypeList, 1);
	int singleKeZiLaiZiNum = findJiangAndGetLaiZiNum(foundJiang, curNeedLaiZiNum, 
				1, singleTypeList, singleKeZiList);
	//移除半刻子，例如11
	List<Pai> halfKeZiList = getSpecifiedNumKeZi(singleTypeList, 2);
	int halfKeZiLaiZiNum = findJiangAndGetLaiZiNum(foundJiang, curNeedLaiZiNum, 
				0, singleTypeList, halfKeZiList);
	//移除刻子，例如111
	List<Pai> keZiList = getSpecifiedNumKeZi(singleTypeList, 3);
	int keZiLaiZiNum = ignoreJiangAndGetLaiZiNum(foundJiang, curNeedLaiZiNum, 
				0, singleTypeList, keZiList);
	//移除半顺子1，例如12*
	List<Pai> halfShunZiList1 = getSpecifiedNumShunZi(singleTypeList, 1);
	int halfShunZiLaiZiNum1 = ignoreJiangAndGetLaiZiNum(foundJiang, curNeedLaiZiNum, 
				1, singleTypeList, halfShunZiList1);
	//移除半顺子2，例如1*3
	List<Pai> halfShunZiList2 = getSpecifiedNumShunZi(singleTypeList, 2);
	int halfShunZiLaiZiNum2 = ignoreJiangAndGetLaiZiNum(foundJiang, curNeedLaiZiNum, 
				1, singleTypeList, halfShunZiList2);
	//移除顺子，例如123
	List<Pai> shunZiList = getSpecifiedNumShunZi(singleTypeList, 1, 2);
	int shunZiLaiZiNum = ignoreJiangAndGetLaiZiNum(foundJiang, curNeedLaiZiNum, 
				0, singleTypeList, shunZiList);
	return minLaiZiNum(singleKeZiLaiZiNum, halfKeZiLaiZiNum, keZiLaiZiNum, 
				halfShunZiLaiZiNum1, halfShunZiLaiZiNum2, shunZiLaiZiNum);
    }

    /**
     * 根据牌集合第一张牌，获取特定数目相同的刻子牌集合，完全或不完全
     */
    private static List<Pai> getSpecifiedNumKeZi(List<Pai> singleTypeList, int specifiedNum) {
  	List<Pai> paiList = new ArrayList<Pai>();
  	Pai pai = singleTypeList.get(0);
  	paiList.add(pai);
  	int count = 0;
  	for(Pai p : singleTypeList) {
  	    if(p.getPaiId() == pai.getPaiId()) {
  		count++;
  	    }
  	}
  	if(count < specifiedNum) {
  	    paiList.clear();
  	}else {
  	    for(int i = 0; i < specifiedNum - 1; i++) {
  		paiList.add(pai);
  	    }
  	 }
  	return paiList;
    }
	
    /**
     * 根据牌集合第一张牌以及差值数组获取指定数目的顺子牌集合，完全或不完全
     */
    private static List<Pai> getSpecifiedNumShunZi(List<Pai> singleTypeList, int... difValues) {
  	List<Pai> paiList = new ArrayList<Pai>();
  	Pai firstPai = singleTypeList.get(0);
  	paiList.add(firstPai);
  	for(int difValue : difValues) {
  	    for(Pai p : singleTypeList) {
  	  	if((p.getPaiType() != 4) && (p.getPaiId() - firstPai.getPaiId() == difValue)) {
  	  	    paiList.add(p);
  	  	    break;
  	  	}
  	    }
  	}
  	if(paiList.size() < (difValues.length + 1)) {
  	    paiList.clear();
  	}
  	return paiList;
    }
  	
    /**
     * 找将，移除集合后并获取癞子数目
     */
    private static int findJiangAndGetLaiZiNum(boolean foundJiang, int curNeedLaiZiNum, 
  			int plusLaiZiNum, List<Pai> singleTypeList, List<Pai> removePais) {
	if(removePais.size() > 0) {
	    List<Pai> leftPaiList = getLeftPaiList(singleTypeList, removePais);
	    if(foundJiang) {
		return getMinLaiZiNum(curNeedLaiZiNum + (plusLaiZiNum + 1), true, leftPaiList);
	    }else {
		return getMinLaiZiNum(curNeedLaiZiNum + plusLaiZiNum, true, leftPaiList);
	    }
	}
	return -1;
    }
  	
    /**
     * 不考虑找将，移除集合后并获取癞子数目
     */
    private static int ignoreJiangAndGetLaiZiNum(boolean foundJiang, int curNeedLaiZiNum, 
  			int plusLaiZiNum, List<Pai> singleTypeList, List<Pai> removePais) {
	if(removePais.size() > 0) {
	    List<Pai> leftPaiList = getLeftPaiList(singleTypeList, removePais);
	    return getMinLaiZiNum(curNeedLaiZiNum + plusLaiZiNum, foundJiang, leftPaiList);
	}
	return -1;
    }
  	
    /**
     * 获取移除牌后剩余的牌集合
     */
    private static List<Pai> getLeftPaiList(List<Pai> singleTypeList,
  			List<Pai> removePaiList) {
  	List<Pai> leftPais = new ArrayList<Pai>();
  	leftPais.addAll(singleTypeList);
  	PaiSetUtil.removePai(leftPais, removePaiList);
  	return leftPais;
    }
  	
    /**
     * 获取除-1外最小的数值
     */
    private static int minLaiZiNum(int... laiZiNums) {
  	int minLaiZiNum = -1;
  	for(int laiZiNum : laiZiNums) {
  	    if(laiZiNum != -1) {
  		//-1证明未初始化
  		if(minLaiZiNum == -1) {
  		    minLaiZiNum = laiZiNum;
  		}else if(minLaiZiNum > laiZiNum) {
  		    minLaiZiNum = laiZiNum;
  		}
  	    }
  	}
  	return minLaiZiNum;
  }

}
