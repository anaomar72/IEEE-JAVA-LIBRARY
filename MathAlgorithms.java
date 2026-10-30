import java.util.*;
import java.io.*;
import java.math.*;
import java.util.function.*;

/** Common number-theory helpers. */
class MathAlgorithms {

    static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    // May overflow long if the true result is too large.
    static long lcm(long a, long b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        return Math.abs((a / gcd(a, b)) * b);
    }

    static boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }

        for (int i = 2; i <= n / i; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    // prime[x] is true exactly when x is prime, for 0 <= x <= n.
    static boolean[] sieve(int n) {
        boolean[] prime = new boolean[n + 1];
        Arrays.fill(prime, true);

        if (n >= 0) {
            prime[0] = false;
        }

        if (n >= 1) {
            prime[1] = false;
        }

        for (int i = 2; i <= n / i; i++) {
            if (prime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    prime[j] = false;
                }
            }
        }

        return prime;
    }

    // Computes base^exp mod mod. Requires mod > 0 and exp >= 0.
    // This multiplication version assumes intermediate products fit in long.
    static long modPow(long base, long exp, long mod) {
        if (mod <= 0 || exp < 0) {
            throw new IllegalArgumentException("mod must be positive and exp nonnegative");
        }

        long result = 1 % mod;
        base = Math.floorMod(base, mod);

        while (exp > 0) {
            if ((exp & 1L) != 0) {
                result = (result * base) % mod;
            }

            base = (base * base) % mod;
            exp >>= 1;
        }

        return result;
    }
}


// ==========================================
// ADDITIONAL ALGORITHMS FROM EXTENDED LIBRARY
// Each class is package-private for single-folder compilation.
// ==========================================

// SOURCE: 08-Mathematics/BitwiseToolkit.java
/**
 * Tier 1 | bit operations, XOR prefix, bit counts
 * Use: bit tricks
 * Time: O(1) each; O(n) XOR aggregate. Space: O(1).
 * Edge cases: signed ints, shift range 0..31
 * Pitfalls: 1<<31 negative; use 1L<<bit for long
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class BitwiseToolkit {

    static boolean bit(long mask, int b) {
        return (mask & (1L << b)) != 0;
    }

    static long set(long mask, int b) {
        return mask | (1L << b);
    }

    static long clear(long mask, int b) {
        return mask & ~(1L << b);
    }

    static int popcount(long mask) {
        return Long.bitCount(mask);
    }

    static long xor0ToN(long n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        switch ((int) (n & 3)) {
            case 0:
                return n;
            case 1:
                return 1;
            case 2:
                return n + 1;
            default:
                return 0;
        }
    }

    static long xorRange(long l, long r) {
        if (l < 0 || r < l) {
            throw new IllegalArgumentException();
        }

        return xor0ToN(r) ^ (l == 0 ? 0 : xor0ToN(l - 1));
    }

    static int singleOddOccurrence(int[] a) {
        int x = 0;

        for (int v : a) {
            x ^= v;
        }

        return x;
    } // requires all other values even-frequency
}


// SOURCE: 08-Mathematics/Combinatorics.java
/**
 * Tier 2 | factorials, binomial, permutations, BigInteger
 * Use: counting arrangements
 * Time: O(n) precompute; O(1) nCr mod after setup. Space: O(n).
 * Edge cases: prime modulus and n<prime for inverse factorial method
 * Pitfalls: For large n>=prime, Lucas theorem or other method needed
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class Combinatorics {

    static BigInteger factorialBig(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        BigInteger ans = BigInteger.ONE;

        for (int i = 2; i <= n; i++) {
            ans = ans.multiply(BigInteger.valueOf(i));
        }

        return ans;
    }

    static BigInteger permutationsBig(int n, int r) {
        if (r < 0 || r > n) {
            return BigInteger.ZERO;
        }

        BigInteger a = BigInteger.ONE;

        for (int i = 0; i < r; i++) {
            a = a.multiply(BigInteger.valueOf(n - i));
        }

        return a;
    }

    static BigInteger chooseBig(int n, int k) {
        if (k < 0 || k > n) {
            return BigInteger.ZERO;
        }

        k = Math.min(k, n - k);
        BigInteger v = BigInteger.ONE;

        for (int i = 1; i <= k; i++) {
            v = v.multiply(BigInteger.valueOf(n - i + 1))
                 .divide(BigInteger.valueOf(i));
        }

        return v;
    }

    static final class ModChoose {
        final long mod;
        final long[] fact, invFact;

        ModChoose(int maxN, long prime) {
            if (maxN < 0 || prime <= maxN || !NumberTheory.isPrime(prime)) {
                throw new IllegalArgumentException("prime > maxN required");
            }

            mod = prime;
            fact = new long[maxN + 1];
            invFact = new long[maxN + 1];

            fact[0] = 1;

            for (int i = 1; i <= maxN; i++) {
                fact[i] = ModularArithmetic.multiply(fact[i - 1], i, mod);
            }

            invFact[maxN] = ModularArithmetic.pow(fact[maxN], mod - 2, mod);

            for (int i = maxN; i > 0; i--) {
                invFact[i - 1] = ModularArithmetic.multiply(invFact[i], i, mod);
            }
        }

        long choose(int n, int r) {
            if (n < 0 || n >= fact.length) {
                throw new IllegalArgumentException();
            }

            if (r < 0 || r > n) {
                return 0;
            }

            return ModularArithmetic.multiply(
                ModularArithmetic.multiply(fact[n], invFact[r], mod),
                invFact[n - r],
                mod
            );
        }
    }
}


// SOURCE: 08-Mathematics/MathUtilities.java
/**
 * Tier 2 | integer ceil division, extended gcd, safe arithmetic
 * Use: common math transforms
 * Time: O(1) or O(log n). Space: O(1).
 * Edge cases: signed division, checked overflow
 * Pitfalls: floating-point ceil for exact integer problems
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class MathUtilities {

    static long ceilDiv(long a, long b) {
        if (b == 0) {
            throw new ArithmeticException();
        }

        long q = Math.floorDiv(a, b);
        return a % b == 0 ? q : Math.addExact(q, 1);
    } // exact for all long a,b except if mathematical quotient exceeds long

    static BigInteger ceilDivExact(long a, long b) {
        if (b == 0) {
            throw new ArithmeticException();
        }

        BigInteger A = BigInteger.valueOf(a);
        BigInteger B = BigInteger.valueOf(b);
        BigInteger[] qr = A.divideAndRemainder(B);

        return qr[1].signum() != 0 && A.signum() == B.signum()
            ? qr[0].add(BigInteger.ONE)
            : qr[0];
    }

    static long checkedSum(long[] a) {
        long s = 0;

        for (long x : a) {
            s = Math.addExact(s, x);
        }

        return s;
    }

    static long[] extendedGcd(long a, long b) {
        long x0 = 1, y0 = 0, x1 = 0, y1 = 1;

        while (b != 0) {
            long q = a / b;
            long t = a % b;

            a = b;
            b = t;

            t = x0 - q * x1;
            x0 = x1;
            x1 = t;

            t = y0 - q * y1;
            y0 = y1;
            y1 = t;
        }

        return new long[]{a, x0, y0};
    } // may overflow on extreme long inputs
}


// SOURCE: 08-Mathematics/ModularArithmetic.java
/**
 * Tier 1 | mod pow, inverse for prime, safe modular multiplication
 * Use: modular math
 * Time: O(log exponent) pow with O(log modulus) multiply. Space: O(1).
 * Edge cases: modulus positive; inverse only if gcd(a,mod)=1
 * Pitfalls: a*b overflows before % when longs large
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class ModularArithmetic {

    static long norm(long x, long m) {
        if (m <= 0) {
            throw new IllegalArgumentException();
        }

        return Math.floorMod(x, m);
    }

    static long add(long a, long b, long m) {
        a = norm(a, m);
        b = norm(b, m);

        return a >= m - b ? a - (m - b) : a + b;
    }

    static long multiply(long a, long b, long m) {
        if (m <= 0) {
            throw new IllegalArgumentException();
        }

        a = norm(a, m);
        b = norm(b, m);

        long res = 0;

        while (b > 0) {
            if ((b & 1) != 0) {
                res = add(res, a, m);
            }

            b >>>= 1;

            if (b > 0) {
                a = add(a, a, m);
            }
        }

        return res;
    }

    static long pow(long a, long e, long m) {
        if (e < 0 || m <= 0) {
            throw new IllegalArgumentException();
        }

        long result = 1 % m;
        a = norm(a, m);

        while (e > 0) {
            if ((e & 1) != 0) {
                result = multiply(result, a, m);
            }

            e >>>= 1;

            if (e > 0) {
                a = multiply(a, a, m);
            }
        }

        return result;
    }

    static long inversePrime(long a, long prime) {
        if (!NumberTheory.isPrime(prime) || norm(a, prime) == 0) {
            throw new IllegalArgumentException("nonzero a and prime modulus required");
        }

        return pow(a, prime - 2, prime);
    }

    static long inverseCoprime(long a, long m) {
        if (m <= 0) {
            throw new IllegalArgumentException();
        }

        return BigInteger.valueOf(a)
            .mod(BigInteger.valueOf(m))
            .modInverse(BigInteger.valueOf(m))
            .longValueExact();
    }
}


// SOURCE: 08-Mathematics/NumberTheory.java
/**
 * Tier 1 | GCD, LCM, primes, sieve, factorization
 * Use: divisibility and prime questions
 * Time: O(log n) gcd; O(sqrt n) primality/factorization; O(n log log n) sieve. Space: O(n) sieve.
 * Edge cases: Long.MIN_VALUE absolute-value overflow; positive factorization only
 * Pitfalls: LCM may overflow long; use checked multiplication
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class NumberTheory {

    static long gcd(long a, long b) {
        if (a == Long.MIN_VALUE || b == Long.MIN_VALUE) {
            throw new ArithmeticException("gcd magnitude may overflow");
        }

        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }

        return a;
    }

    static long lcm(long a, long b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        long value = Math.multiplyExact(a / gcd(a, b), b);

        if (value == Long.MIN_VALUE) {
            throw new ArithmeticException("lcm overflow");
        }

        return Math.abs(value);
    }

    static boolean isPrime(long n) {
        if (n < 2) {
            return false;
        }

        if ((n & 1) == 0) {
            return n == 2;
        }

        for (long d = 3; d <= n / d; d += 2) {
            if (n % d == 0) {
                return false;
            }
        }

        return true;
    }

    static boolean[] sieve(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        boolean[] prime = new boolean[n + 1];
        Arrays.fill(prime, true);

        prime[0] = false;

        if (n >= 1) {
            prime[1] = false;
        }

        for (int p = 2; p <= n / p; p++) {
            if (prime[p]) {
                for (int j = p * p; j <= n; j += p) {
                    prime[j] = false;
                }
            }
        }

        return prime;
    }

    static Map<Long, Integer> factor(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("positive only");
        }

        Map<Long, Integer> map = new LinkedHashMap<>();

        for (long d = 2; d <= n / d; d += (d == 2 ? 1 : 2)) {
            while (n % d == 0) {
                map.merge(d, 1, Integer::sum);
                n /= d;
            }
        }

        if (n > 1) {
            map.merge(n, 1, Integer::sum);
        }

        return map;
    }
}


// SOURCE: 10-Geometry/EllipticCurve.java
/**
 * Tier 4 | prime-field short Weierstrass point addition
 * Use: specialized elliptic curve tasks
 * Time: O(log p) modular inverse expected using BigInteger. Space: O(log p).
 * Edge cases: point infinity, doubling y=0; validate nonsingular curve
 * Pitfalls: Use BigInteger to avoid overflow; not cryptographic constant-time
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class EllipticCurve {

    static final class Point {
        final BigInteger x, y;

        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    final BigInteger p, a, b;

    EllipticCurve(BigInteger prime, BigInteger a, BigInteger b) {
        if (prime.compareTo(BigInteger.valueOf(3)) <= 0 || !prime.isProbablePrime(40)) {
            throw new IllegalArgumentException("prime p>3");
        }

        p = prime;
        this.a = a.mod(p);
        this.b = b.mod(p);

        BigInteger delta = BigInteger.valueOf(4)
            .multiply(this.a.pow(3))
            .add(BigInteger.valueOf(27).multiply(this.b.pow(2)))
            .mod(p);

        if (delta.signum() == 0) {
            throw new IllegalArgumentException("singular curve");
        }
    }

    boolean onCurve(Point q) {
        return q == null || q.y.modPow(BigInteger.TWO, p).equals(
            q.x.modPow(BigInteger.valueOf(3), p)
                .add(a.multiply(q.x))
                .add(b)
                .mod(p)
        );
    }

    Point add(Point x, Point y) {
        if (!onCurve(x) || !onCurve(y)) {
            throw new IllegalArgumentException("off curve");
        }

        if (x == null) {
            return y;
        }

        if (y == null) {
            return x;
        }

        BigInteger slope;

        if (x.x.equals(y.x)) {
            if (x.y.add(y.y).mod(p).signum() == 0) {
                return null;
            }

            slope = x.x.pow(2)
                .multiply(BigInteger.valueOf(3))
                .add(a)
                .multiply(x.y.shiftLeft(1).modInverse(p))
                .mod(p);
        } else {
            slope = y.y.subtract(x.y)
                .multiply(y.x.subtract(x.x).mod(p).modInverse(p))
                .mod(p);
        }

        BigInteger rx = slope.pow(2).subtract(x.x).subtract(y.x).mod(p);
        BigInteger ry = slope.multiply(x.x.subtract(rx)).subtract(x.y).mod(p);

        return new Point(rx, ry);
    }
}


// SOURCE: 10-Geometry/Geometry2D.java
/**
 * Tier 2 | point distance, cross, orientation, segments, line intersection
 * Use: planar geometry
 * Time: O(1). Space: O(1).
 * Edge cases: collinearity, parallel lines, coincident lines
 * Pitfalls: double tolerance vs exact long; integer cross may overflow
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class Geometry2D {

    static final double EPS = 1e-9;

    static final class Point {
        final double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static double distance(Point a, Point b) {
        return Math.hypot(a.x - b.x, a.y - b.y);
    }

    static double cross(Point a, Point b) {
        return a.x * b.y - a.y * b.x;
    }

    static double cross(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }

    static int orientation(Point a, Point b, Point c) {
        double v = cross(a, b, c);
        double scale = Math.max(
            1.0,
            Math.max(distance(a, b), Math.max(distance(b, c), distance(a, c)))
        );

        double tol = EPS * scale * scale;

        return v > tol ? 1 : v < -tol ? -1 : 0;
    }

    static boolean onSegment(Point a, Point b, Point p) {
        return orientation(a, b, p) == 0
            && p.x >= Math.min(a.x, b.x) - EPS
            && p.x <= Math.max(a.x, b.x) + EPS
            && p.y >= Math.min(a.y, b.y) - EPS
            && p.y <= Math.max(a.y, b.y) + EPS;
    }

    static boolean segmentsIntersect(Point a, Point b, Point c, Point d) {
        int o1 = orientation(a, b, c);
        int o2 = orientation(a, b, d);
        int o3 = orientation(c, d, a);
        int o4 = orientation(c, d, b);

        if (o1 == 0 && onSegment(a, b, c)
            || o2 == 0 && onSegment(a, b, d)
            || o3 == 0 && onSegment(c, d, a)
            || o4 == 0 && onSegment(c, d, b)) {
            return true;
        }

        return o1 != o2 && o3 != o4;
    }

    static Point lineIntersection(Point a, Point b, Point c, Point d) {
        Point r = new Point(b.x - a.x, b.y - a.y);
        Point s = new Point(d.x - c.x, d.y - c.y);

        double den = cross(r, s);

        if (Math.abs(den) <= EPS) {
            return null;
        }

        double t = cross(new Point(c.x - a.x, c.y - a.y), s) / den;

        return new Point(a.x + t * r.x, a.y + t * r.y);
    }
}