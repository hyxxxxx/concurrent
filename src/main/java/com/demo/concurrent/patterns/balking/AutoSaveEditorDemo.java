package com.demo.concurrent.patterns.balking;

/**
 * balking模式经典实现
 * 案例使用文章的自动保存
 * （多线程版本的if）
 */
public class AutoSaveEditorDemo {

    boolean changed = false;

    public void autoSave() {
        synchronized (this) {
            if (!changed) {
                return;
            }
            changed = false;
        }
        //存盘操作
        this.execSave();
    }

    public void edit() {
        //对文章进行编辑
        editArticle();

        //改变状态
        change();
    }

    private void change() {
        synchronized (this) {
            changed = true;
        }
    }

    public void editArticle() {

    }

    private void execSave() {

    }


}
