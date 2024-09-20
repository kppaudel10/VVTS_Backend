package com.vvts.traffic_congestion.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;

/**
 * @auther kul.paudel
 * @created at 2024-08-06
 */
@Slf4j
public class FileAppender {

    public static void AppendToFile(String filename, String content) {
        System.out.println(content);
        try {
            PrintStream out =
                    new PrintStream(new AppendFileStream(filename));
            out.print(content + "\n");
            out.close();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

}

class AppendFileStream extends OutputStream {
    RandomAccessFile fd;

    public AppendFileStream(String file) throws IOException {
        fd = new RandomAccessFile(file, "rw");
        fd.seek(fd.length());
    }

    public void close() throws IOException {
        fd.close();
    }

    public void write(byte[] b) throws IOException {
        fd.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        fd.write(b, off, len);
    }

    public void write(int b) throws IOException {
        fd.write(b);
    }
}
