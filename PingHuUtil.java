package com.flankleen.util.pai;

import java.util.ArrayList;
import java.util.List;

/*
 * 牌的类
 */
public class PingHuUtil {
	
    /**
     * 检测平胡，基本思想如下
     * 先找到一对相同的牌作为将牌，移除将牌后检测剩下的牌是否皆由顺子或刻子组成
     */
    public static boolean checkPingHu(List<Pai> innerPaiList, Pai pai) {
    	//获取临时牌组检测平胡
    	List<Pai> tempList = PaiSetUtil.getOrderedTempList(innerPaiList(), pai);
    	return canPingHu(tempList);
    }

   /**
    * 可否平胡
    */
    private static boolean canPingHu(List<Pai> paiList) {
    	Collections.sort(paiList);
        //放将牌的集合
        List<Pai> jiangPaiList = new ArrayList<Pai>();
        //中间集合
        List<Pai> midList = new ArrayList<Pai>();
        midList.addAll(paiList);
        Pai jiangPai1;
        Pai jiangPai2;
        //是否找到将牌
        boolean foundJiangPai = false;
        for (int i = 0; i < paiList.size() - 1; i++) {
            if (!foundJiangPai){
                //没有发现将牌就去找将牌，如果有就不用去找
                jiangPai1 = paiList.get(i);
                jiangPai2 = paiList.get(i + 1);
                if (jiangPai1.getPaiId() == jiangPai2.getPaiId()) {
                    //移除这两张将牌
                    foundJiangPai = true;
                    PaiSetUtil.removePai(paiList, jiangPai1, jiangPai2);
                    jiangPaiList.add(jiangPai1);
                    jiangPaiList.add(jiangPai2);
                    PaiSetUtil.removePai(midList, jiangPai1, jiangPai2);
                    //接下来判断是否为刻子或者顺子
                    if(isAllKeShun(midList)) {
                    	return true;
                    }
                    i = i - 2;
                }
            }else {
            	//如果胡不了，中间集合还要加上之前移除的将牌
                foundJiangPai = false;
                midList.addAll(jiangPaiList);
                jiangPaiList.clear();
                Collections.sort(midList);
            }
        }
        return false;
    }
    
    //检测剔除将牌后的牌是否都是刻子和顺子
    private static boolean isAllKeShun(List<Pai> pingHuList) {
    	List<Pai> tempPingHuList = PaiSetUtil.getOrderedTempList(pingHuList, null);
    	if(tempPingHuList.size() < 3) {
    	    return true;
    	}
    	if(removeKeZi(tempPingHuList)) {//如果前三个是刻子，则移除
    	    return isAllKeShun(tempPingHuList);
    	}else if(removeShunZi(tempPingHuList)) {//否则移除顺子
	    return isAllKeShun(tempPingHuList);
	}
	//如果一张牌不能和后面的牌组成刻子或顺子，则不是平胡
    	return false;
    }

    //从集合中移除刻子
    private static boolean removeKeZi(List<Pai> checkPingHuList) {
    	Pai pai1 = checkPingHuList.get(0);
        Pai pai2 = checkPingHuList.get(1);
        Pai pai3 = checkPingHuList.get(2);
        if (pai1.getPaiId() == pai2.getPaiId() && pai1.getPaiId() == pai3.getPaiId()) {
            //成立说明为刻子，移除这三张牌
            PaiSetUtil.removePai(checkPingHuList, pai1, pai2, pai3);
            return true;
        }
        return false;
    }
    
    //从集合中移除顺子
    private static boolean removeShunZi(List<Pai> checkPingHuList) {
    	List<Pai> tempList = new ArrayList<Pai>();
    	Pai pai1 = checkPingHuList.get(0);
    	if(pai1.getPaiType() == 4) {
    	    //风牌不能做顺子
    	    return false;
    	}
    	tempList.add(pai1);
    	for(Pai pai2 : checkPingHuList) {
    	    if(pai1.getPaiId() + 1 == pai2.getPaiId()) {
    		tempList.add(pai2);
    		break;
    	    }
    	}
    	if(tempList.size() < 2) {
    	    //构成不了顺子
    	    return false;
    	}
    	for(Pai pai3 : checkPingHuList) {
    	    if(pai1.getPaiType() == pai3.getPaiType() && (pai1.getPaiId() + 2) == pai3.getPaiId()) {
    		tempList.add(pai3);
    		break;
    	    }
    	}
    	//构成顺子，移除
    	if(tempList.size() == 3) {
    	    PaiSetUtil.removePai(checkPingHuList, tempList);
    	    return true;
    	}
    	return false;
    }

}
