package com.stone.common.util;


import com.stone.common.constant.CommonConstant;
import com.stone.common.exception.BizException;

import java.util.Objects;

public class BitUtil {

    public static void main(String[] args) {
        int flag = 0b0;
        System.out.println(flag = BitUtil.getBit(flag, 3));
        System.out.println(flag = BitUtil.setBit(flag, 2, 1));
        System.out.println(flag = BitUtil.setBit(flag, 1, 1));
        System.out.println(flag = BitUtil.setBit(flag, 2, 0));
        System.out.println(flag = BitUtil.setBit(flag, 3, 1));
    }

    public static final Integer FLAG_DEFAULT_VALUE = 0b000;
    /**
     * 初始化flag时，状态为否时
     * 请在dto中定义单个dto的初始值
     */
    @Deprecated
    public static final int BIT_DEFAULT_INVALID_FLAG = 0B0;
    /**
     * 初始化flag时，状态为是时
     * 请在dto中定义单个dto的初始值
     */
    @Deprecated
    public static int BIT_FLAG = 0B1;

    /**
     * @param flag     十进制数
     * @param position 左向右 第{position}位 position >= 1 && <= 32
     * @return
     */
    public static Integer getBit(int flag, Integer position) {
        if (position < 1 || position > 32) {
            throw new BizException("position is out of bound.");
        }

        int BIT_INIT = getValue(position);

        return (BIT_INIT & flag) == BIT_INIT ? CommonConstant.STATUS_VALID : CommonConstant.STATUS_INVALID;
    }

    /**
     * @param flag           十进制数
     * @param defaultFlagVal 如果flag值为null，那么flag取defaultFlagVal的值
     * @param position       左向右 第{position}位 position >= 1 && <= 32
     * @return
     */
    public static Integer getBit(Integer flag, int defaultFlagVal, Integer position) {
        if (position < 1 || position > 32) {
            throw new BizException("position is out of bound.");
        }

        int BIT_INIT = getValue(position);

        return (BIT_INIT & ObjTool.get(flag, defaultFlagVal)) == BIT_INIT ? CommonConstant.STATUS_VALID : CommonConstant.STATUS_INVALID;
    }

    /**
     * @param flag     old-flag value，旧的flag值
     * @param position 左向右 第{position}位 position >= 1 && <= 32
     * @return
     */
    public static Integer setBit(int flag, Integer position, Integer status) {
        if (position < 1 || position > 32) {
            throw new BizException("position is out of bound.");
        }

        if (Objects.isNull(status)) {
            throw new BizException("status value is not allow null.");
        }

        if (!CommonConstant.STATUS_VALID.equals(status) && !CommonConstant.STATUS_INVALID.equals(status)) {
            throw new BizException("status value is error value, it is only allow 1 or 0.");
        }

        if (CommonConstant.STATUS_VALID.equals(status)) {
            return flag | (BIT_FLAG << (position - 1));
        } else if (CommonConstant.STATUS_INVALID.equals(status)) {
            return flag & (~(BIT_FLAG << (position - 1)));
        } else {
            throw new BizException("status value is error value, it is only allow 1 or 0.");
        }
    }

    /**
     * @param flag           old-flag value，旧的flag值
     * @param defaultFlagVal 如果flag值为null，那么flag取defaultFlagVal的值
     * @param position       左向右 第{position}位 position >= 1 && <= 32
     * @return
     */
    public static Integer setBit(Integer flag, int defaultFlagVal, Integer position, Integer status) {
        if (position < 1 || position > 32) {
            throw new BizException("position is out of bound.");
        }

        if (Objects.isNull(status)) {
            throw new BizException("status value is not allow null.");
        }

        if (!CommonConstant.STATUS_VALID.equals(status) && !CommonConstant.STATUS_INVALID.equals(status)) {
            throw new BizException("status value is error value, it is only allow 1 or 0.");
        }

        if (CommonConstant.STATUS_VALID.equals(status)) {
            return ObjTool.get(flag, defaultFlagVal) | (BIT_FLAG << (position - 1));
        } else if (CommonConstant.STATUS_INVALID.equals(status)) {
            return ObjTool.get(flag, defaultFlagVal) & (~(BIT_FLAG << (position - 1)));
        } else {
            throw new BizException("status value is error value, it is only allow 1 or 0.");
        }
    }

    /**
     * @param flag      初始值
     * @param groupFlag 需要设置得flag组
     * @return
     */
    public static Integer setBatchBit(int flag, Integer... groupFlag) {
        if (Objects.isNull(groupFlag) || groupFlag.length % 2 != 0) {
            throw new BizException("group flag is error format params.");
        }

        Integer tempFlag = flag;
        for (int idx = 0; idx < groupFlag.length; idx += 2) {
            tempFlag = setBit(tempFlag, groupFlag[idx], groupFlag[idx + 1]);
        }

        return tempFlag;
    }

    /**
     * @param position 左移位数
     * @return
     */
    public static Integer getValue(Integer position) {
        if (position < 1 || position > 32) {
            throw new BizException("position is out of bound.");
        }

        return BIT_FLAG << (position - 1);
    }

    public static boolean getBooleanBit(int flag, Integer position) {
        return CommonConstant.STATUS_VALID.equals(getBit(flag, position));
    }

    public static boolean getBooleanBit(Integer flag, int defaultFlagVal, Integer position) {
        return CommonConstant.STATUS_VALID.equals(getBit(ObjTool.get(flag, defaultFlagVal), position));
    }
}
