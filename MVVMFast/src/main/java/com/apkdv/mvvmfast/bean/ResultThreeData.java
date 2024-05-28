package com.apkdv.mvvmfast.bean;

public class ResultThreeData<T,K,S> {
    public T data1;
    public K data2;
    public S data3;

    public ResultThreeData() {
    }

    public ResultThreeData(T data1, K data2, S data3) {
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    public T getData1() {
        return data1;
    }

    public void setData1(T data1) {
        this.data1 = data1;
    }

    public K getData2() {
        return data2;
    }

    public void setData2(K data2) {
        this.data2 = data2;
    }

    public S getData3() {
        return data3;
    }

    public void setData3(S data3) {
        this.data3 = data3;
    }
    
    
}
