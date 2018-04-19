package com.github.sy.client;

import java.util.Scanner;

/**
 * on 2018/4/17.
 */
public class Main {
    public static void main(String[] a) throws Exception {
        try (Connection connection = new Connection("localhost", 8033);) {
            String line = "";
            while (!(line = new Scanner(System.in).nextLine()).equals("end")) {
                String[] command = line.split("\\s+");
                String result;
                if (command[0].equals("get")) {
                    result = connection.get(command[1]);
                } else if (command[0].equals("set")) {
                    result = connection.put(command[1], command[2]);
                } else if (command[0].equals("del")) {
                    result = connection.del(command[1]);
                } else {
                    result = "unknown command";
                }
                System.err.println(result);
            }
        }
    }
}
