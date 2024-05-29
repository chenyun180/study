package com.cloud.test.demo.stream;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

public class CalculateStreamDemo {

    // 读取Excel文件内容
    public static void readExcel() {


    }

    public static void main(String[] args) {
//        int[] arr = {3, 2, 31, 4, 15, 6, 13, 28, 9, 10};
//        quickSort(arr, 0, arr.length - 1);
//        System.out.println(JSONObject.toJSONString(arr));


        LocalDateTime l = LocalDateTime.now();
        String format = l.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println(format);
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(l));
    }

    // 归并排序
    public static void mergeSort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int mid = (end + start) / 2;
        mergeSort(arr, start, mid);
        mergeSort(arr, mid + 1, end);

        int[] temp = new int[end - start + 1];
        int i = start;
        int j = mid + 1;
        int k = 0;
        while (i <= mid || j <= end) {
            if (i > mid) {
                temp[k++] = arr[j++];
            } else if (j > end) {
                temp[k++] = arr[i++];
            } else if (arr[i] < arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        for (int l = 0; l < temp.length; l++) {
            arr[start + l] = temp[l];
        }
    }

    // 快速排序
    public static void quickSort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int i = start;
        int j = end;
        int temp = arr[start];
        while (i < j) {
            while (i < j && arr[j] >= temp) {
                j--;
            }
            if (i < j) {
                arr[i++] = arr[j];
            }
            while (i < j && arr[i] < temp) {
                i++;
            }
            if (i < j) {
                arr[j--] = arr[i];
            }
        }
        arr[i] = temp;
        quickSort(arr, start, i - 1);
        quickSort(arr, i + 1, end);
    }


}
