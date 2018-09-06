package com.nis.galaxy.domain.dict;

/**
 * @author GouGe
 * @data 2018/8/30 16:22
 */


public class UiSysDictInfoZh {
    private int sysDictId;//字典ID
    private String itemDesc;//汉语描述信息
    private String itemValue;//英文

    public UiSysDictInfoZh(int sysDictId, String itemDesc, String itemValue) {
        this.sysDictId = sysDictId;
        this.itemDesc = itemDesc;
        this.itemValue = itemValue;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public int getSysDictId() {
        return sysDictId;
    }

    public void setSysDictId(int sysDictId) {
        this.sysDictId = sysDictId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
}
