package graph;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Dijkstra {
    static Integer lines = 2;
    static List<Integer>[] plants;

    public static void init(){
        for (Integer integer = 0; integer < lines; integer++) {
            plants[integer] = new ArrayList<>();
        }
        plants[0].add(1);
        plants[0].add(3);
        plants[0].add(5);
        plants[0].add(7);
        plants[1].add(4);
        plants[1].add(5);
        plants[1].add(8);
    }

    public static class Node{
        Integer val;
        List<Node> nodes;
    }

    public static void main(String[] args) {
        init();

        int max = 0;
        for (Integer integer = 0; integer < lines; integer++) {
            for(Integer i : plants[integer]){
                max = Math.max(max, i);
            }
        }

        int[][] matrix = new int[2][];
        matrix[0] = new int[4];
        matrix[0][0] = 1;
        matrix[0][1] = 3;
        matrix[0][2] = 5;
        matrix[0][3] = 7;

        matrix[1] = new int[3];
        matrix[1][0] = 2;
        matrix[1][1] = 5;
        matrix[1][2] = 8;
        System.out.println(dijkstra(1, 2, matrix));
    }

    public static int[][] toLinjie(int[][] matrix){

        return null;
    }

    public static int dijkstra(int start, int end, int[][] matrix){
        return 0;
    }
}
