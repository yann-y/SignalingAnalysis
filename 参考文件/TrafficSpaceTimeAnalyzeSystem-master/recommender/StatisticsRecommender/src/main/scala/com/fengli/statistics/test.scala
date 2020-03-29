package com.fengli.statistics

import java.text.SimpleDateFormat

object test {
  def main(args: Array[String]): Unit = {
    val simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss")
    val start = simpleDateFormat.format(1538498482636L)
    val end = simpleDateFormat.format(1538498481670L)

    println(start)
    println(end)
  }
}
