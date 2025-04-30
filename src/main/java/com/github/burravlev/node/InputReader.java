package com.github.burravlev.node;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Scanner;

@RequiredArgsConstructor
public class InputReader {
    private final Scanner scanner;
    private final NodeInstance runner;

    public void read() {
        String line;
        outInput();
        while (!(line = scanner.nextLine()).equals("q")) {
            outInput();
            parse(line);
        }
    }

    private static void outInput() {
        System.out.print("> ");
    }

    private void parse(String line) {
        if (line.equals("tx")) {
            transfer();
        } else if (line.equals("balance")) {
            String balance = runner.balance();
            System.out.println(balance);
        } else if (line.equals("me")) {
            System.out.println("address: " + runner.wallet().getAddress() + "\n\nkey: " + runner.wallet().getPrivateKey());
        }
    }

    private void transfer() {
        System.out.print("> Please, input receiver address:");
        String address = scanner.nextLine();
        System.out.print("> Please, amount:");
        String amount = scanner.nextLine();
        try {
            BigDecimal parsed = new BigDecimal(amount);
            runner.transfer(address, amount);
        } catch (Exception e) {
            System.out.println("> Invalid amount!");
        }
    }
}
