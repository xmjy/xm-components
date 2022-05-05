package indi.xm.component.libcommon.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ID加密工具类（如简历ID，用户ID）
 *
 * @author kuikui.he
 * @date 2019年8月8日上午10:19:20
 * @Description:
 */
public class EncryptIdUtil {

    private static final int SIZE_100 = 100;
    private static final String LEVEL_985 = "985";
    private static final String LEVEL_211 = "211";
    private static final int LENGTH_18 = 18;
    private static final int LENGTH_19 = 19;

    private static List<String[]> arrayKeyList = new ArrayList<String[]>();

    private static List<String[]> getArrayKeyList() {
        if (arrayKeyList.size() != SIZE_100) {
            initArrayBySeed();
        }
        return arrayKeyList;
    }

    public static String getReverseString(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        StringBuilder str2 = stringBuilder.reverse();
        return str2.toString();

    }

    private static String[] getArrayBySeed(String seed) {
        List<String[]> keyList = getArrayKeyList();
        int index = (int) (Long.parseLong(seed) + Long.parseLong(getReverseString(seed))) % 100;
        return keyList.get(index);
    }

    private static void initArrayBySeed() {
        List<String[]> keyList = new ArrayList<String[]>();
        keyList.add(new String[]{"0", "7", "2", "9", "4", "6", "5", "3", "1", "8"});
        keyList.add(new String[]{"0", "8", "6", "1", "9", "7", "2", "3", "4", "5"});
        keyList.add(new String[]{"0", "8", "5", "4", "1", "3", "9", "7", "2", "6"});
        keyList.add(new String[]{"0", "3", "2", "7", "6", "1", "4", "5", "9", "8"});
        keyList.add(new String[]{"0", "4", "1", "6", "7", "2", "9", "3", "8", "5"});
        keyList.add(new String[]{"0", "2", "1", "5", "8", "9", "3", "7", "6", "4"});
        keyList.add(new String[]{"0", "6", "7", "2", "5", "1", "4", "3", "8", "9"});
        keyList.add(new String[]{"0", "7", "3", "9", "1", "5", "2", "4", "8", "6"});
        keyList.add(new String[]{"0", "8", "1", "7", "9", "2", "6", "5", "3", "4"});
        keyList.add(new String[]{"0", "3", "8", "4", "2", "5", "6", "9", "7", "1"});
        keyList.add(new String[]{"0", "2", "7", "5", "6", "8", "3", "9", "1", "4"});
        keyList.add(new String[]{"0", "9", "1", "6", "2", "7", "4", "3", "8", "5"});
        keyList.add(new String[]{"0", "2", "3", "1", "5", "9", "7", "4", "8", "6"});
        keyList.add(new String[]{"0", "6", "5", "4", "3", "9", "2", "1", "8", "7"});
        keyList.add(new String[]{"0", "2", "1", "7", "4", "6", "3", "8", "5", "9"});
        keyList.add(new String[]{"0", "1", "9", "4", "7", "2", "3", "8", "5", "6"});
        keyList.add(new String[]{"0", "4", "6", "1", "3", "7", "8", "5", "9", "2"});
        keyList.add(new String[]{"0", "8", "2", "1", "6", "5", "4", "3", "7", "9"});
        keyList.add(new String[]{"0", "4", "3", "8", "6", "7", "5", "9", "1", "2"});
        keyList.add(new String[]{"0", "3", "1", "7", "8", "2", "5", "6", "9", "4"});
        keyList.add(new String[]{"0", "5", "1", "2", "6", "4", "3", "8", "7", "9"});
        keyList.add(new String[]{"0", "8", "5", "4", "3", "7", "9", "1", "6", "2"});
        keyList.add(new String[]{"0", "9", "1", "6", "3", "8", "4", "7", "5", "2"});
        keyList.add(new String[]{"0", "8", "1", "9", "6", "2", "4", "3", "7", "5"});
        keyList.add(new String[]{"0", "6", "3", "7", "8", "2", "4", "1", "5", "9"});
        keyList.add(new String[]{"0", "2", "9", "3", "4", "6", "1", "7", "8", "5"});
        keyList.add(new String[]{"0", "2", "5", "8", "6", "4", "3", "9", "1", "7"});
        keyList.add(new String[]{"0", "3", "6", "9", "2", "7", "5", "4", "8", "1"});
        keyList.add(new String[]{"0", "2", "8", "4", "5", "6", "9", "3", "1", "7"});
        keyList.add(new String[]{"0", "3", "9", "6", "2", "8", "5", "4", "7", "1"});
        keyList.add(new String[]{"0", "2", "9", "1", "7", "4", "6", "5", "3", "8"});
        keyList.add(new String[]{"0", "3", "4", "8", "6", "1", "2", "7", "5", "9"});
        keyList.add(new String[]{"0", "2", "7", "5", "9", "3", "6", "1", "8", "4"});
        keyList.add(new String[]{"0", "3", "2", "5", "9", "6", "4", "7", "1", "8"});
        keyList.add(new String[]{"0", "2", "4", "7", "6", "9", "5", "1", "8", "3"});
        keyList.add(new String[]{"0", "1", "9", "2", "3", "8", "6", "4", "7", "5"});
        keyList.add(new String[]{"0", "8", "2", "9", "5", "7", "1", "3", "6", "4"});
        keyList.add(new String[]{"0", "4", "6", "7", "9", "8", "2", "3", "5", "1"});
        keyList.add(new String[]{"0", "8", "9", "3", "4", "2", "5", "7", "1", "6"});
        keyList.add(new String[]{"0", "3", "6", "8", "9", "2", "7", "1", "4", "5"});
        keyList.add(new String[]{"0", "3", "4", "2", "5", "7", "9", "6", "8", "1"});
        keyList.add(new String[]{"0", "9", "1", "6", "4", "8", "5", "2", "3", "7"});
        keyList.add(new String[]{"0", "7", "9", "6", "1", "5", "3", "8", "4", "2"});
        keyList.add(new String[]{"0", "8", "2", "5", "6", "7", "1", "4", "3", "9"});
        keyList.add(new String[]{"0", "5", "2", "7", "8", "9", "3", "1", "4", "6"});
        keyList.add(new String[]{"0", "2", "5", "8", "7", "6", "3", "9", "4", "1"});
        keyList.add(new String[]{"0", "3", "5", "4", "2", "6", "1", "9", "8", "7"});
        keyList.add(new String[]{"0", "3", "5", "6", "2", "9", "8", "4", "1", "7"});
        keyList.add(new String[]{"0", "7", "3", "2", "6", "1", "5", "9", "4", "8"});
        keyList.add(new String[]{"0", "2", "7", "5", "6", "8", "3", "9", "4", "1"});
        keyList.add(new String[]{"0", "2", "9", "3", "8", "6", "4", "5", "1", "7"});
        keyList.add(new String[]{"0", "3", "7", "1", "2", "4", "8", "6", "9", "5"});
        keyList.add(new String[]{"0", "6", "8", "4", "7", "3", "5", "1", "2", "9"});
        keyList.add(new String[]{"0", "1", "9", "7", "2", "3", "4", "5", "6", "8"});
        keyList.add(new String[]{"0", "5", "7", "6", "8", "3", "1", "4", "9", "2"});
        keyList.add(new String[]{"0", "7", "2", "3", "1", "5", "9", "6", "8", "4"});
        keyList.add(new String[]{"0", "5", "8", "3", "6", "9", "7", "2", "4", "1"});
        addLastData(keyList);
        arrayKeyList = keyList;
    }

    /**
     * @param keyList :
     * @Description: 主要是为了拆分方法行数
     * @Date: 2019年8月9日上午8:28:01
     */
    private static void addLastData(List<String[]> keyList) {
        keyList.add(new String[]{"0", "4", "8", "5", "3", "6", "1", "2", "9", "7"});
        keyList.add(new String[]{"0", "1", "8", "9", "7", "2", "3", "4", "6", "5"});
        keyList.add(new String[]{"0", "1", "5", "4", "7", "2", "9", "8", "3", "6"});
        keyList.add(new String[]{"0", "1", "8", "4", "6", "9", "3", "5", "7", "2"});
        keyList.add(new String[]{"0", "9", "4", "8", "2", "1", "7", "3", "5", "6"});
        keyList.add(new String[]{"0", "9", "4", "6", "2", "7", "5", "1", "3", "8"});
        keyList.add(new String[]{"0", "4", "8", "5", "2", "3", "7", "9", "1", "6"});
        keyList.add(new String[]{"0", "2", "1", "9", "3", "6", "5", "4", "8", "7"});
        keyList.add(new String[]{"0", "5", "9", "3", "1", "2", "7", "6", "4", "8"});
        keyList.add(new String[]{"0", "7", "4", "5", "3", "8", "1", "6", "2", "9"});
        keyList.add(new String[]{"0", "5", "4", "9", "3", "8", "6", "7", "1", "2"});
        keyList.add(new String[]{"0", "5", "4", "2", "3", "1", "8", "7", "9", "6"});
        keyList.add(new String[]{"0", "7", "9", "3", "2", "4", "1", "5", "6", "8"});
        keyList.add(new String[]{"0", "9", "6", "8", "7", "4", "3", "1", "5", "2"});
        keyList.add(new String[]{"0", "5", "6", "1", "8", "2", "3", "9", "4", "7"});
        keyList.add(new String[]{"0", "2", "3", "6", "5", "7", "4", "9", "1", "8"});
        keyList.add(new String[]{"0", "1", "3", "8", "7", "4", "6", "2", "9", "5"});
        keyList.add(new String[]{"0", "3", "8", "7", "1", "2", "9", "4", "5", "6"});
        keyList.add(new String[]{"0", "7", "9", "2", "5", "4", "1", "8", "6", "3"});
        keyList.add(new String[]{"0", "6", "4", "1", "5", "3", "7", "9", "8", "2"});
        keyList.add(new String[]{"0", "7", "4", "5", "8", "1", "6", "3", "2", "9"});
        keyList.add(new String[]{"0", "4", "9", "1", "3", "8", "5", "7", "6", "2"});
        keyList.add(new String[]{"0", "1", "3", "2", "5", "9", "8", "6", "7", "4"});
        keyList.add(new String[]{"0", "4", "7", "6", "3", "2", "9", "8", "1", "5"});
        keyList.add(new String[]{"0", "2", "7", "6", "3", "8", "5", "9", "4", "1"});
        keyList.add(new String[]{"0", "9", "5", "1", "4", "6", "2", "3", "8", "7"});
        keyList.add(new String[]{"0", "6", "8", "9", "2", "5", "7", "4", "3", "1"});
        keyList.add(new String[]{"0", "4", "5", "2", "1", "3", "9", "6", "7", "8"});
        keyList.add(new String[]{"0", "4", "5", "2", "3", "9", "6", "7", "1", "8"});
        keyList.add(new String[]{"0", "1", "9", "7", "5", "4", "2", "3", "8", "6"});
        keyList.add(new String[]{"0", "6", "7", "5", "3", "4", "2", "9", "1", "8"});
        keyList.add(new String[]{"0", "4", "6", "7", "8", "3", "5", "2", "9", "1"});
        keyList.add(new String[]{"0", "3", "6", "2", "1", "4", "9", "8", "7", "5"});
        keyList.add(new String[]{"0", "6", "4", "3", "7", "8", "5", "2", "1", "9"});
        keyList.add(new String[]{"0", "5", "9", "3", "8", "7", "6", "1", "2", "4"});
        keyList.add(new String[]{"0", "7", "6", "2", "1", "9", "3", "4", "8", "5"});
        keyList.add(new String[]{"0", "2", "3", "6", "5", "8", "4", "9", "7", "1"});
        keyList.add(new String[]{"0", "4", "9", "6", "1", "5", "8", "7", "2", "3"});
        keyList.add(new String[]{"0", "3", "1", "9", "6", "2", "4", "7", "5", "8"});
        keyList.add(new String[]{"0", "9", "5", "7", "2", "4", "3", "8", "6", "1"});
        keyList.add(new String[]{"0", "8", "2", "5", "1", "6", "3", "7", "4", "9"});
        keyList.add(new String[]{"0", "9", "6", "8", "3", "1", "5", "4", "2", "7"});
        keyList.add(new String[]{"0", "5", "6", "7", "3", "2", "1", "8", "4", "9"});
    }

    /**
     * 加密数字
     *
     * @param num:
     * @param seed:
     * @return :
     * @author: albert.fang
     * @date: 2022/1/12 10:15
     */
    public static String encryptNum(String num, String seed) {
        //num不能为985、211，长度不能大于18
        if (LEVEL_985.equals(num) || LEVEL_211.equals(num) || num.length() > LENGTH_18) {
            return num;
        }
        //num不能为空，seed不能为空
        if (EhireStringUtil.isEmpty(num) || EhireStringUtil.isEmpty(seed)) {
            return num;
        }
        //num、seed为数字，不能小于0
        try {
            long lNum = Long.parseLong(num);
            long lSeed = Long.parseLong(seed);
            if (lNum < 0 || lSeed < 0) {
                return num;
            }
        } catch (Exception e) {
            return num;
        }
        // 更改数组必须保证下标0必须是0固定，1-9的下标可以随意，但必须满足正好填满0-9 10个数字
        String[] downNumList = getArrayBySeed(seed);
        //计算newId
        StringBuilder newId = new StringBuilder();
        for (int i = 0; i < num.length(); i++) {
            newId.append(downNumList[Integer.parseInt(num.substring(i, i + 1))]);
        }
        //计算seedValue
        long seedValue = 0;
        char[] seedChar = seed.toCharArray();
        for (int i = 0; i < seed.length(); i++) {
            if (i >= 9) {
                break;
            }
            seedValue += (long) seedChar[i] * Integer.parseInt(downNumList[i + 1]);
        }
        return (Long.parseLong(newId.toString()) + seedValue + 1000) + "";
    }

    /**
     * 解密数字
     *
     * @param num:
     * @param seed:
     * @return :
     * @author: albert.fang
     * @date: 2022/1/12 10:16
     */
    public static String decryptNum(String num, String seed) {
        //num不能为985、211，长度不能大于19
        if (LEVEL_985.equals(num) || LEVEL_211.equals(num) || num.length() > LENGTH_19) {
            return num;
        }
        //num不能为空，seed不能为空
        if (EhireStringUtil.isEmpty(num) || EhireStringUtil.isEmpty(seed)) {
            return num;
        }
        //num、seed为数字，不能小于0
        try {
            long lNum = Long.parseLong(num);
            long lSeed = Long.parseLong(seed);
            if (lNum < 0 || lSeed < 0) {
                return num;
            }
        } catch (Exception e) {
            return num;
        }
        String[] downNumList = getArrayBySeed(seed);
        //计算seed的值
        long seedValue = 0;
        char[] seedChar = seed.toCharArray();
        for (int i = 0; i < seed.length(); i++) {
            if (i >= 9) {
                break;
            }
            seedValue += (long) seedChar[i] * Integer.parseInt(downNumList[i + 1]);
        }
        //计算oldNum
        String oldNum = (Long.parseLong(num) - seedValue - 1000) + "";
        //计算oldID
        StringBuilder oldId = new StringBuilder();
        List<String> newDownNumList = Arrays.asList(downNumList);
        for (int i = 0; i < oldNum.length(); i++) {
            oldId.append(newDownNumList.indexOf(oldNum.substring(i, i + 1)));
        }
        //校验oldId
        long lOldId = Long.parseLong(oldId.toString());
        if (lOldId < 0 || LEVEL_985.equals(oldId.toString()) || LEVEL_211.equals(oldId.toString())) {
            return "0";
        }
        if (oldId.length() > LENGTH_18) {
            return num;
        }
        return oldId.toString();
    }
}
