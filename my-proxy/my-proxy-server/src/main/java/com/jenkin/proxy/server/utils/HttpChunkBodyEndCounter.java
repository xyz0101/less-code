package com.jenkin.proxy.server.utils;

/**
 * @author jenkin
 * @className HeaderEndCounter
 * @description TODO
 * @date 2021/4/6 14:22
 */
public class HttpChunkBodyEndCounter implements ByteEndCounter {
    byte[] bytes = new byte[]{'\r','\n','0','\r','\n','\r','\n'};
    @Override
    public boolean countCase(byte o) {
        return o=='\r' || o == '\n' || o == '0';
    }
    /**
     * 计数器计数的条件
     *
     * @param index
     * @param c
     * @return
     */
    @Override
    public boolean countCaseIndex(int index ,byte c) {
        return bytes[index]==c;
    }
    /**
     * 计数器最大的计数数目，达到此条件会终止计数
     *
     * @return
     */
    @Override
    public int getEndCount() {
        return 7;
    }
}
