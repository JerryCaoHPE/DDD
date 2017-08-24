package ddd.simple.util;

import java.util.regex.Pattern;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
 



/**
 * 汉字转换位汉语拼音，英文字符不变
 * 
 * @author xuke 需要用到的jar包：pinyin4j-2.5.0.jar
 * 
 */
public class PinYin {

//	/**
//	 * 汉字转换位汉语拼音首字母，英文字符不变
//	 * 
//	 * @param chines
//	 *            汉字
//	 * @return 拼音
//	 */
//	public static String converterToFirstSpell(String chines) {
//		if (chines == null || chines.equals("")) {
//			return "";
//		}
//		String pinyinName = "";
//		char[] nameChar = chines.toCharArray();
//		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		for (int i = 0; i < nameChar.length; i++) {
//			if (nameChar[i] > 128) {
//				try {
//					String pinyins[] = PinyinHelper.toHanyuPinyinStringArray(
//							nameChar[i], defaultFormat);
//					if (pinyins == null) {
//						continue;
//					}
//					if (pinyins.length > 0) {
//						if (pinyins[0].length() > 0) {
//							pinyinName += pinyins[0].charAt(0);
//						} else {
//							pinyinName += nameChar[i];
//						}
//					} else {
//						pinyinName += nameChar[i];
//					}
//				} catch (Exception e) {
//					// System.err.println("取拼音首字母出错："+chines);
//					e.printStackTrace();
//				}
//			} else {
//				pinyinName += nameChar[i];
//			}
//		}
//		return pinyinName;
//	}
//	
//	/*
//	 * 得到一个字的所有开头字母
//	 */
//	public static List<String> getSingleFirstSpell(String chines) {
//		Set<String> allFirstSpellSet = new HashSet<String>();
//		if (chines == null || "".equals(chines)) {
//
//			return null;
//		}
//		char[] nameChar = chines.toCharArray();
//		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		for (int i = 0; i < nameChar.length; i++) {
//			if (nameChar[i] > 128) {
//				try {
//					String pinyins[] = PinyinHelper.toHanyuPinyinStringArray(
//							nameChar[i], defaultFormat);
//
//					if (pinyins == null) {
//						continue;
//					}
//					if (pinyins.length > 0) {
//						int index = 0;
//						while (index <= pinyins.length - 1) {
//							allFirstSpellSet.add(String.valueOf(pinyins[index]
//									.charAt(0)));
//							index++;
//						}
//					} else {
//						allFirstSpellSet.add(String.valueOf(nameChar[i]));
//					}
//
//				} catch (BadHanyuPinyinOutputFormatCombination e) {
//					e.printStackTrace();
//				}
//			} else {
//				allFirstSpellSet.add(String.valueOf(nameChar[i]));
//			}
//		}
//		List<String> allFirstSpells = new ArrayList<String>();
//		allFirstSpells.addAll(allFirstSpellSet);
//		return allFirstSpells;
//	}
//
//	// 得到一个字符串所有的组合可能
//	public static List<String> getAllFirstSpells(String chines,
//			List<String> allFirstSpells) {
//
//		chines = chines.trim();
//		if (chines == null || "".equals(chines)) {
//			return null;
//		}
//		List<String> tmp = new ArrayList<String>();
//
//		if (allFirstSpells == null || allFirstSpells.size() == 0) {
//			allFirstSpells = new ArrayList<String>();
//			allFirstSpells.add("");
//		}
//
//		if (chines.length() == 1) {
//
//			return getTempFirstSpells(chines, allFirstSpells);
//		} else {
//			char[] c = chines.toCharArray();
//			String tempSpell = String.valueOf(c[0]);
//
//			tmp = getTempFirstSpells(tempSpell, allFirstSpells);
//
//			String lastChines = chines.substring(1);
//			return getAllFirstSpells(lastChines, tmp);
//		}
//
//	}
//
//	//得到一个字符串所有的组合可能,拼接成字符串返回
//	public static String getAllFirstSpellsToString(String chines){
//		
//		List<String> firstSpellList = new ArrayList<String>();
//		System.out.println(chines);
//		System.out.println(filterSpecificChar(chines));
//		
//		firstSpellList = PinYin.getAllFirstSpells(filterSpecificChar(chines), firstSpellList);
//		
//		String firstSpells = firstSpellList.get(0);
//		
//		if(firstSpellList.size() > 1){
//			for(int i = 1; i < firstSpellList.size(); i ++){
//				firstSpells += "," +  firstSpellList.get(i);
//			}
//		}
//		
//		return firstSpells;
//	}
//	
//	// 得到递归中所有的中间字母组合
//	public static List<String> getTempFirstSpells(String chines,
//			List<String> allFirstSpells) {
//		List<String> tmp = new ArrayList<String>();
//		List<String> signleChineseFirstSpells = getSingleFirstSpell(chines);
//
//		for (int i = 0; i < allFirstSpells.size(); i++) {
//			for (int j = 0; j < signleChineseFirstSpells.size(); j++) {
//				tmp.add(allFirstSpells.get(i) + signleChineseFirstSpells.get(j));
//			}
//		}
//		return tmp;
//
//	}
//	
//	
//	/**
//	 * 汉字转换位汉语拼音，英文字符不变 35. *
//	 * 
//	 * @param chines
//	 *            汉字 36. *
//	 * @return 拼音 37.
//	 */
//	public static String converterToSpell(String chines) {
//		String pinyinName = "";
//		char[] nameChar = chines.toCharArray();
//		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		for (int i = 0; i < nameChar.length; i++) {
//			if (nameChar[i] > 128) {
//				try {
//					pinyinName += PinyinHelper.toHanyuPinyinStringArray(
//							nameChar[i], defaultFormat)[0];
//				} catch (BadHanyuPinyinOutputFormatCombination e) {
//					e.printStackTrace();
//				}
//			} else {
//				pinyinName += nameChar[i];
//			}
//		}
//		return pinyinName;
//	}

	public static void main(String[] args) {
		//System.out.println(converterToFirstSpell("欢迎来到最棒的Java中文社区"));
		
		
		String str = "你好世界 重庆";
		String out = "";
		out=PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_MARK); // nǐ,hǎo,shì,jiè
		System.out.println(out);
		out=PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_NUMBER); // ni3,hao3,shi4,jie4
		System.out.println(out);
		out=PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITHOUT_TONE); // ni,hao,shi,jie
		System.out.println(out);
		out=PinyinHelper.getShortPinyin(str); // nhsj
		System.out.println(out);
	}
	
	public static String getShortPinyin(String str)
	{
		return PinyinHelper.getShortPinyin(str);
	}
	private static String filterSpecificChar(String originalString) {
		String matchedString= "";
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5_a-zA-Z0-9]");
		for (int i=0; i<originalString.length(); i++) {
			if (pattern.matcher(originalString.charAt(i) + "").matches()) {
				matchedString += originalString.charAt(i);
			}
		}
		System.out.println(matchedString);
		return matchedString;
	}
}
