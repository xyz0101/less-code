package com.jenkin.proxy.server.nio;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/7 21:15
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class ByteBufferTest {


    public static void main(String[] args) {
        byte[] bytes = new byte[]{1,2,3,4,5,5,6,7,8,9};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        System.out.println("偏移量："+ byteBuffer.arrayOffset());
        System.out.println("初始位置："+ byteBuffer.position());
        byteBuffer.put((byte) 10);
         System.out.println("当前位置："+byteBuffer.position());
        byteBuffer.limit(3);
        byteBuffer.put((byte) 11);
        byteBuffer.put((byte) 12);
        byteBuffer.limit(bytes.length);
        byteBuffer.put((byte) 13);
        byteBuffer.mark();
        System.out.println("标记");
        byteBuffer.put((byte) 14);
        byteBuffer.put((byte) 15);
        byteBuffer.put((byte) 16);
        System.out.println("当前位置："+byteBuffer.position());
        int position1 = byteBuffer.position();
        byteBuffer.reset();
        System.out.println("reset标记之后当前位置："+byteBuffer.position()+"  limit："+byteBuffer.limit());
        byteBuffer.position(position1);
        byteBuffer.limit(3);
        System.out.println("limit小于Mark之后的位置： "+byteBuffer.position());
        System.out.println("----------------------测试slice--------------------");
        bytes = new byte[]{1,2,3,4,5,5,6,7,8,9};
        byteBuffer = ByteBuffer.wrap(bytes);

        for (int i = 0; i < 5; i++) {
            byteBuffer.get();
        }

        byte[] array = byteBuffer.slice().array();

        System.out.println(Arrays.toString(array));

        System.out.println("----------------------测试byteBuffer--------------------");
        byteBuffer = ByteBuffer.allocate(12);
        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 2);
        System.out.println(Arrays.toString(byteBuffer.array()));
    }

}
