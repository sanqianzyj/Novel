package com.hao.out.base.utils.listener


/**
 * Created by haozhang on 2018/1/31.
 */
interface SearchFileLisener {
    fun updateProgress(vararg values: String?)
    fun finish(result: MutableList<Any?>?)
    fun onCancal()
    fun onStart()
}

/**
 * Created by haozhang on 2018/2/1.
 * 添加于文件遍历时的监听
 * 用于监听正在遍历的文件和文件夹
 * 可能存在线程安全问题
 */
interface TraverseFileListener {
    fun traverseFileItem(file: String): Any?
    fun traverseDirectoryItem(directroy: String)
}

/**
 * Created by haozhang on 2018/1/31.
 * 用于使用AsyncTask针对文件夹进行遍历音乐
 * 回调于AsyncTask的各个进度和状态
 */
interface SearchMusicLisener {
    fun updateProgress(vararg values: String?)
    fun finish(result: MutableList<Any>?)
    fun onCancal()
    fun onStart()
}