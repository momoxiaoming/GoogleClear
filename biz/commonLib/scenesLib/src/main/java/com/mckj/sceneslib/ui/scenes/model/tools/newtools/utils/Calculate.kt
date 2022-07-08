package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import java.lang.Exception
import java.util.*
import kotlin.Throws
import kotlin.math.floor

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.wjm.util
 * @data 2022/4/15 14:45
 */
object Calculate {
    //使用后缀 表达式来计算原表达式的值，得到double类型的结果。
    fun calculate(exp: String): String {
        return try {
            val inOrderExp = getStringList(exp) //String转换为List,得到中缀表达式
            val postOrderExp = getPostOrder(inOrderExp)
            val res = calPostOrderExp(postOrderExp)
           if (res == floor(res)) res.toString() else "" //当结果是整数的时候，输出不要加小数点
        } catch (exception: Exception) {
             ""
        }
    }

    //把数字和符号加入list
    private fun getStringList(s: String): ArrayList<String> {
        val res = ArrayList<String>()
        var num = ""
        for (i in s.indices) {
            if (Character.isDigit(s[i]) || s[i] == '.') {
                num += s[i]
            } else {
                if (num !== "") {
                    res.add(num) //把上一个数字加到list
                }
                res.add(s[i].toString() + "") //把当前符号加到list
                num = ""
            }
        }
        //最后一个数字
        if (num !== "") {
            res.add(num)
        }
        return res
    }

    //将中缀表达式转化为后缀表达式
    private fun getPostOrder(inOrderExp: ArrayList<String>): ArrayList<String> {
        val postOrderExp = ArrayList<String>() //储存结果
        val operStack = Stack<String>() //运算符栈
        for (i in inOrderExp.indices) {
            val cur = inOrderExp[i]
            if (isOper(cur)) {
                while (!operStack.isEmpty() && compareOper(operStack.peek(), cur)) {
                    //只要运算符栈不为空，并且栈顶符号优先级大与等于cur
                    postOrderExp.add(operStack.pop())
                }
                operStack.push(cur)
            } else {
                postOrderExp.add(cur)
            }
        }
        while (!operStack.isEmpty()) {
            postOrderExp.add(operStack.pop())
        }
        return postOrderExp
    }

    //比较两个运算符的大小，如果peek优先级大于等于cur，返回true
    private fun compareOper(peek: String, cur: String): Boolean {
        if ("*" == peek && ("/" == cur || "*" == cur || "+" == cur || "-" == cur)) {
            return true
        } else if ("/" == peek && ("/" == cur || "*" == cur || "+" == cur || "-" == cur)) {
            return true
        } else if ("+" == peek && ("+" == cur || "-" == cur)) {
            return true
        } else if ("-" == peek && ("+" == cur || "-" == cur)) {
            return true
        }
        return false
    }

    //判断一个字符串是否是运算符，+-*/
    private fun isOper(c: String): Boolean {
        return c == "+" || c == "-" || c == "*" || c == "/"
    }

    //计算一个后缀表达式
    @Throws(Exception::class)
    private fun calPostOrderExp(postOrderExp: ArrayList<String>): Double {
        val stack = Stack<String>()
        for (i in postOrderExp.indices) {
            val curString = postOrderExp[i]
            if (isOper(curString)) {
                val a = stack.pop().toDouble()
                val b = stack.pop().toDouble()
                var res = 0.0
                when (curString[0]) {
                    '+' -> res = b + a
                    '-' -> res = b - a
                    '/' -> {
                        if (a == 0.0) throw Exception()
                        res = b / a
                    }
                    '*' -> res = b * a
                }
                stack.push(res.toString() + "")
            } else {
                stack.push(curString)
            }
        }
        return stack.pop().toDouble()
    }
}