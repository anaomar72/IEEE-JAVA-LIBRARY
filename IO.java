import java.io.*;
import java.util.*;

/**
 * Starting template for Java competitive programming.
 * Keep the class name Main for online judges.
 */
public class Main {
    static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        String next() throws IOException {
            StringBuilder s = new StringBuilder();
            int c;
            do {
                c = read();
            } while (c != -1 && c <= ' ');

            if (c == -1) return null;

            while (c != -1 && c > ' ') {
                s.append((char) c);
                c = read();
            }
            return s.toString();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        long nextLong() throws IOException {
            return Long.parseLong(next());
        }

        double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        // Example:
        // int n = fs.nextInt();
        // out.append(n).append('\n');

        System.out.print(out);
    }
}
