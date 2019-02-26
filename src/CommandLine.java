/*
 * Copyright (c) 2019 Aidan Lloyd-Tucker.
 */

import org.jetbrains.annotations.NotNull;

public class CommandLine {

    public static void main(@NotNull String[] args) {
        int width = 30;
        int height = 30;

        if (args.length >= 1) {
            width = Integer.parseInt(args[0]);
        }
        if (args.length >= 2) {
            height = Integer.parseInt(args[1]);
        }

        Maze m = new Maze(width, height);
        m.generate();
        System.out.println(m);
    }
}
