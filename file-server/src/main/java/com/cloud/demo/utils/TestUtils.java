package com.cloud.demo.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author weiwei
 * @Date 2022/9/11 上午10:19
 * @Version 1.0
 * @Desc
 */
public class TestUtils {
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode result = null,tail = null;
        int carry = 0;
        while (l1 != null || l2 != null) {
            int n1 =  l1 == null ? 0 : l1.val;
            int n2 =  l2 == null ? 0 : l2.val;
            int num = n1 + n2 + carry;
            if (result == null) {
                result = tail = new ListNode(num % 10);
            } else {
                tail.next = new ListNode(num % 10);
                tail = tail.next;
            }
            carry = num / 10;
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (carry > 0) {
            tail.next = new ListNode(carry);
        }
        return result;
    }

    public static class ListNode{
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(val + "");
            ListNode curr = this;
            while (curr.next != null) {
                sb.append("->").append(curr.next.val);
                curr = curr.next;
            }
            return sb.toString();
        }
    }

    /**
     * 给定一个二进制字符串 s 和一个正整数 n，如果对于 [1, n] 范围内的每个整数，其二进制表示都是 s 的 子字符串 ，就返回 true，否则返回 false 。
     *
     * 子字符串 是字符串中连续的字符序列。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode.cn/problems/binary-string-with-substrings-representing-1-to-n
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param s
     * @param n
     * @return
     */
    public boolean queryString(String s, int n) {
        int m = (n>>1) + 1;
        for (int i = m;i<=n;i++) {
            String b = Integer.toBinaryString(i);
            if (!s.contains(b)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 给定一个字符串 s ，找到 它的第一个不重复的字符，并返回它的索引 。如果不存在，则返回 -1 。
     * @param s
     * @return
     */
    public static int firstUniqChar(String s) {
        Map<Character, Boolean> map = new HashMap<>();
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (map.containsKey(c)) {
                map.put(c, true);
            } else {
                map.put(c, false);
            }
        }
        for (int i = 0;i < chars.length;i++) {
            if (!map.get(chars[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int firstUniqChar2(String s) {
        char[] chars = s.toCharArray();
        for (int i = 0;i < chars.length;i++) {
            if (s.indexOf(chars[i]) == s.lastIndexOf(chars[i])) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
//        ListNode n1 = new ListNode(2);
//        ListNode n2 = new ListNode(4);
//        ListNode n3 = new ListNode(3);
//        n1.next = n2;
//        n2.next = n3;
//
//        ListNode n4 = new ListNode(5);
//        ListNode n5 = new ListNode(6);
//        ListNode n6 = new ListNode(4);
//        n4.next = n5;
//        n5.next = n6;
//
//        System.out.println(n1);
//        System.out.println(n4);
//
//        System.out.println(addTwoNumbers(n1,n4));
        System.out.println(firstUniqChar2("loveleetcode"));
    }
}
