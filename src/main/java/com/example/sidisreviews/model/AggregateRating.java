package com.example.sidisreviews.model;

import java.util.List;

public class AggregateRating {

    private String sku;
    private float average;
    private int count0; // 0
    private int count1; // [0,5 - 1,5[
    private int count2; // [1,5 - 2,5[
    private int count3; // [2,5 - 3,5[
    private int count4; // [3,5 - 4,5[
    private int count5; // [4,5 - 5[
    private float frequency0;
    private float frequency1;
    private float frequency2;
    private float frequency3;
    private float frequency4;
    private float frequency5;

    public AggregateRating(String sku, float average, int count0, int count1, int count2, int count3, int count4, int count5, float frequency0, float frequency1, float frequency2, float frequency3, float frequency4, float frequency5) {
        this.sku = sku;
        this.average = average;
        this.count0 = count0;
        this.count1 = count1;
        this.count2 = count2;
        this.count3 = count3;
        this.count4 = count4;
        this.count5 = count5;
        this.frequency0 = frequency0;
        this.frequency1 = frequency1;
        this.frequency2 = frequency2;
        this.frequency3 = frequency3;
        this.frequency4 = frequency4;
        this.frequency5 = frequency5;
    }

    public AggregateRating(List<ReviewDTO> list){
        setCountersAndFrequencies(list);
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public int getCount0() {
        return count0;
    }

    public void setCount0(int count0) {
        this.count0 = count0;
    }

    public int getCount1() {
        return count1;
    }

    public void setCount1(int count1) {
        this.count1 = count1;
    }

    public int getCount2() {
        return count2;
    }

    public void setCount2(int count2) {
        this.count2 = count2;
    }

    public int getCount3() {
        return count3;
    }

    public void setCount3(int count3) {
        this.count3 = count3;
    }

    public int getCount4() {
        return count4;
    }

    public void setCount4(int count4) {
        this.count4 = count4;
    }

    public int getCount5() {
        return count5;
    }

    public void setCount5(int count5) {
        this.count5 = count5;
    }

    public float getFrequency0() {
        return frequency0;
    }

    public void setFrequency0(float frequency0) {
        this.frequency0 = frequency0;
    }

    public float getFrequency1() {
        return frequency1;
    }

    public void setFrequency1(float frequency1) {
        this.frequency1 = frequency1;
    }

    public float getFrequency2() {
        return frequency2;
    }

    public void setFrequency2(float frequency2) {
        this.frequency2 = frequency2;
    }

    public float getFrequency3() {
        return frequency3;
    }

    public void setFrequency3(float frequency3) {
        this.frequency3 = frequency3;
    }

    public float getFrequency4() {
        return frequency4;
    }

    public void setFrequency4(float frequency4) {
        this.frequency4 = frequency4;
    }

    public float getFrequency5() {
        return frequency5;
    }

    public void setFrequency5(float frequency5) {
        this.frequency5 = frequency5;
    }

    public void setCountersAndFrequencies(List<ReviewDTO> list){
        int soma = 0;
        this.sku = list.get(1).getSku();
        for(int i = 0 ; i < list.size() ; i++){
            float rating = list.get(i).getRating();
            if (rating >= 0 && rating < 0.5){
                this.count0++;
            }
            else if (rating >= 0.5 && rating < 1.5){
                this.count1++;
            }
            else if (rating >= 1.5 && rating < 2.5){
                this.count2++;
            }
            else if (rating >= 2.5 && rating < 3.5){
                this.count3++;
            }
            else if (rating >= 3.5 && rating < 4.5){
                this.count4++;
            }
            else if (rating >= 4.5 && rating <= 5){
                this.count5++;
            }
            soma += rating;
        }

        this.frequency0 = ((float) this.count0) / list.size() * 100;
        this.frequency1 = ((float) this.count1) / list.size() * 100;
        this.frequency2 = ((float) this.count2) / list.size() * 100;
        this.frequency3 = ((float) this.count3) / list.size() * 100;
        this.frequency4 = ((float) this.count4) / list.size() * 100;
        this.frequency5 = ((float) this.count5) / list.size() * 100;

        this.average = soma / list.size();
    }
}
