import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.security.*;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECGenParameterSpec;
public class Main {
    // Обчислення функцій Ейлера та Мьобіуса
    public static class EulerMiobiusFunctionCalculation {
        // Приватний допоміжний метод для підрахунку кількості простих дільників
        private static Map<BigInteger, Integer> primeFactorization(BigInteger N){
            Map<BigInteger, Integer> factors = new HashMap<>();
            while(N.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)){
                N = N.divide(BigInteger.valueOf(2));
                factors.put(BigInteger.valueOf(2), factors.getOrDefault(BigInteger.valueOf(2), 0) + 1);
            }
            for(BigInteger i = BigInteger.valueOf(3); i.multiply(i).compareTo(N) <= 0; i = i.add(BigInteger.valueOf(2))){
                while(N.mod(i).equals(BigInteger.ZERO)){
                    N = N.divide(i);
                    factors.put(i, factors.getOrDefault(i, 0) + 1);
                }
            }
            if (N.compareTo(BigInteger.valueOf(2)) > 0) {
                factors.put(N, 1);
            }
            return factors;
        }
        // Публічний метод для обчислення функції Мьобіуса
        public static BigInteger MiobiusFunctionEvaluation(BigInteger N){
            if(N.equals(BigInteger.ONE)){
                return BigInteger.ONE;
            }
            Map<BigInteger, Integer> primeFactors = primeFactorization(N);
            for(Map.Entry<BigInteger, Integer> entry: primeFactors.entrySet()){
                if(entry.getValue() > 1) {
                    return BigInteger.ZERO;
                }
            }
            if (primeFactors.size() % 2 == 0) {
                return BigInteger.ONE;
            } else {
                return BigInteger.valueOf(-1);
            }
        }
        // Метод для обчислення функції Ейлера
        public static BigInteger phiEulerEvaluation(BigInteger n) {
            BigInteger count = BigInteger.ZERO;
            for (BigInteger i = BigInteger.ONE; i.compareTo(n) <= 0; i = i.add(BigInteger.ONE)) {
                if (i.gcd(n).equals(BigInteger.ONE)) {
                    count = count.add(BigInteger.ONE);
                }
            }
            return count;
        }
        public static BigInteger LCMofTwo(BigInteger a, BigInteger b){
            return a.multiply(b).divide(a.gcd(b));
        }
        public static BigInteger LCMofSet(BigInteger[] nums){
            BigInteger lcm = BigInteger.ONE;
            for(BigInteger num: nums){
                lcm = LCMofTwo(lcm, num);
            }
            return lcm;
        }
    }
    // Розв'язання системи лінійних алгебраїчних рівнянь за модулем(китайська теорема про лишки)
    public class LinearSystemOfEquationsEvaluation{
        public static BigInteger solveCRT(BigInteger[] remainders, BigInteger[] mods){
            BigInteger prod = BigInteger.ONE;
            for(BigInteger md: mods){
                prod = prod.multiply(md);
            }
            BigInteger result = BigInteger.ZERO;
            for(int i = 0; i < mods.length; i++){
                BigInteger Mi = prod.divide(mods[i]);
                BigInteger Ni = Mi.divide(mods[i]);
                result = result.add(Mi.multiply(Ni).multiply(remainders[i]));
            }
            return result.mod(prod);
        }
    }
    public class LegandrYacobiEvaluation{
        // Обчислення символу Лежандра
        public static int LegendreEvaluation(BigInteger a, BigInteger p){
            if(a.compareTo(BigInteger.ZERO) == 0)
                return 0;
            BigInteger exponent = p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2));
            BigInteger result = a.modPow(exponent, p);
            if(result.equals(BigInteger.ONE) || result.equals(BigInteger.ZERO))
                return 1;
            return -1;
        }
        // Обчислення символу Якобі 
        public static int YacobiEvaluation(BigInteger a, BigInteger m){
            if(a.compareTo(BigInteger.ZERO) == 0){
                return 0;
            }
            if(a.compareTo(BigInteger.ONE) == 0){
                return 1;
            }
            int result = 1;
            if(a.negate().compareTo(BigInteger.ZERO) < 0){
                a = a.negate();
                if(m.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))){
                    result = -result;
                }
            }
            while(!a.equals(BigInteger.ZERO)){
                int t = 0;
                while(a.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)){
                    t++;
                    a = a.divide(BigInteger.valueOf(2));
                }
                if(t % 2 != 0){
                    BigInteger remainder = m.mod(BigInteger.valueOf(8));
                    if (remainder.equals(BigInteger.valueOf(3)) || remainder.equals(BigInteger.valueOf(5))) {
                        result = -result;
                    }
                }
                if (a.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) && m.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
                    result = -result;
                }
                BigInteger temp = a;
                a = m.mod(a);
                m = temp;
            }
            if (m.equals(BigInteger.ONE)) {
                return result;
            }
            return 0;
        }
    }
    // Алгоритм факторизації довгих цілих чисел алгоритмом ро-Полларда
    public class RhoPollard {
        // Допоміжна функція для генерації чисел
        public static BigInteger generateRandom(int bitLength){
            SecureRandom srand = new SecureRandom();
            BigInteger result;
            do {
                result = new BigInteger(bitLength, srand);
            }while(result.signum() == 0);

            return result;
        }
        public static int RhoPollardAlgo(BigInteger n){
            BigInteger x = BigInteger.ONE;
            BigInteger a = generateRandom(n.bitLength() - 2);
            for(BigInteger i = BigInteger.ZERO; i.compareTo(n) < 0; i.add(BigInteger.ONE)){
                x = x.multiply(a).mod(n);
                if(x.equals(BigInteger.ONE))
                    break;
                int length = i.intValue();
                if(!n.gcd(BigInteger.valueOf(length)).equals(BigInteger.ONE)){
                    return n.gcd(BigInteger.valueOf(length)).intValue();
                }
            }
            return -1;
        }
    }
    // Алгоритм великий крок - малий крок для знаходження дискретного логарифма
    public class DiscreteLogarithmEvaluation{
        public BigInteger BabyStepGiantStep(BigInteger g, BigInteger h, BigInteger p){
            BigInteger squareRoot = BigInteger.valueOf(p.subtract(BigInteger.ONE).longValue()).sqrt();
            BigInteger N = BigInteger.valueOf(squareRoot.longValue()).divide(BigInteger.valueOf(1));
            Map<BigInteger, BigInteger> map = new HashMap<>();
            for(int i = 0; i < N.intValue(); i++){
                map.put(g.modPow(BigInteger.valueOf(i), p), BigInteger.valueOf(i));
            }
            BigInteger c = g.modPow(N.multiply(p.subtract(BigInteger.TWO)), p);
            for(int j = 0; j < N.intValue(); j++){
                BigInteger y = h.multiply(c.modPow(BigInteger.valueOf(j),p)).mod(p);
                if(map.containsKey(y)) {
                    return BigInteger.valueOf(j).multiply(N).add(map.get(y));
                }
            }
            return null;
        }
    }
   // Алгоритм Чіпполі для знаходження дискретного квадратного кореня
    public class ChippolyDiscreteSquareRootEvaluation{
        public static BigInteger[] Chippoly(BigInteger n, BigInteger p){
            BigInteger x; // Наш результат який ми повернемо з функції в якості дискретного квадратного кореня
            Random rnd = new Random();
            BigInteger a = new BigInteger(p.bitLength(), rnd).mod(p);
            BigInteger aSquareMinusN = a.multiply(a).subtract(n).mod(p);
            int legendreSymbol = LegandrYacobiEvaluation.LegendreEvaluation(a, p);
            if(legendreSymbol == -1){
                x = (a.add(aSquareMinusN.sqrt())).pow(p.add(BigInteger.valueOf(1)).divide(BigInteger.valueOf(2)).intValue());
                if(x.multiply(x).equals(n)){
                    BigInteger minusX = x.negate().mod(p);
                    return new BigInteger[]{x, minusX};
                }
            }
            return null;
        }
   }
  // Алгоритм перевірки чисел на простоту
  public class CheckIfPrime{
        public static boolean MillerRabinAlgorithm(BigInteger a, int N){
            if(a.equals(BigInteger.TWO)){
                return true;
            }
            if(a.mod(BigInteger.TWO).equals(BigInteger.ZERO) || a.equals(BigInteger.ONE)){
                return false;
            }
            BigInteger q = a.subtract(BigInteger.ONE);
            int n = 0;
            while(q.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
                q = q.divide(BigInteger.TWO);
                n++;
            }
            Random rand = new Random();
            for(int i = 0; i < N; i++){
                BigInteger k = new BigInteger(a.bitLength() - 1, rand).add(BigInteger.TWO);
                BigInteger b = k.modPow(q, a);
                if(b.equals(BigInteger.ONE) || b.equals(a.subtract(BigInteger.ONE))){
                    continue;
                }
                int j = 0;
                for(j = 0; j < n - 1; j++){
                    b = b.modPow(BigInteger.TWO, a);
                    if(b.equals(BigInteger.ONE)){
                        return false;
                    }
                    if(b.equals(a.subtract(BigInteger.ONE))){
                        break;
                    }
                }
                if(j == n - 1){
                    return false;
                }
            }
            return true;
        }
  }
  // Криптосистема RSA
  public class RSA {
        private BigInteger n, d, e;
        public RSA(int bitLen){
            SecureRandom r = new SecureRandom();
            BigInteger p = new BigInteger(bitLen / 2, 100, r);
            BigInteger q = new BigInteger(bitLen / 2, 100, r);
            n = p.multiply(q);
            BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            BigInteger e = new BigInteger("65537");
            d = e.modInverse(phi);
        }
        public BigInteger encrypt(BigInteger message){
            return message.modPow(e, n);
        }
        public BigInteger decrypt(BigInteger encrypted){
            return encrypted.modPow(d, n);
        }
  }
// Криптосистема Ель-Гамаля над еліптичними кривими
    public class ElGamal{
        static class Point{
            private BigInteger x;
            private BigInteger y;
            public Point(){
                x = BigInteger.ZERO;
                y = BigInteger.ZERO;
            }
            public Point(BigInteger x, BigInteger y){
                this.x.equals(x);
                this.y.equals(y);
            }
            public BigInteger getX(){
                return this.x;
            }
            public BigInteger getY(){
                return this.y;
            }
            public void setX(BigInteger x){
                this.x.equals(x);
            }
            public void setY(BigInteger y){
                this.y.equals(y);
            }
            @Override
            public String toString(){
                String result = x + " " + y;
                return result;
            }
            public boolean isEqual(Point p){
                return x.equals(p.x) && y.equals(p.y);
            }
        } // Кінець класу Point
        static class Pair<K, V> {
            private K key;
            private V value;
            public Pair(K key, V value){
                this.key = key;
                this.value = value;
            }
            public K getKey(){
                return this.key;
            }
            public V getValue(){
                return this.value;
            }
            public void setKey(K key){
                this.key = key;
            }
            public void setValue(V value){
                this.value = value;
            }
        } // Кінець класу Pair
        static class PointProccessor {
            public Point doublePoint(Point P){
                Point result = new Point();
                if(P.getX().equals(0)){
                    result.setX(BigInteger.ZERO);
                    result.setY(BigInteger.ZERO);
                } else{
                    BigInteger lambda, inv, xr, yr;
                    BigInteger p = ElGamal.P;
                    BigInteger a = ElGamal.A;
                    lambda = BigInteger.valueOf(3).multiply(P.getX()).multiply(P.getX()).mod(p).add(a).mod(p);
                    inv = getInverse(BigInteger.valueOf(2).multiply(P.getX()), p);
                    lambda.multiply(inv);
                    lambda.mod(p);
                    xr = lambda.multiply(lambda).mod(p).subtract(BigInteger.valueOf(2).multiply(P.getX()).mod(p).add(p)).mod(p);
                    yr = ((lambda.multiply(P.getX().subtract(xr)).mod(p)).subtract(P.getY()).add(p)).mod(p);
                    if(yr.compareTo(BigInteger.ZERO) == -1){
                        yr.add(p);
                    }
                    result.setX(xr);
                    result.setY(yr);
                }
                return result;
            }
            public Point multiply(BigInteger n, Point P){
                Point result = new Point();
                Point base = new Point(P.getX(), P.getY());
                String binary = Long.toBinaryString(n.longValue());
                for(int i = binary.length() - 1; i >= 0; i--){
                    if(binary.charAt(i) == '1'){
                        if(i == binary.length() - 1){
                            result = base;
                        }else{
                            result = add(result, base);
                        }
                    }
                    base = doublePoint(base);
                }
                return result;
            }
            public BigInteger getInverse(BigInteger n, BigInteger m){
                while (n.compareTo(m) > 0){
                    n.subtract(m);
                }
                while(n.compareTo(BigInteger.ZERO) < 0){
                    n.add(m);
                }
                BigInteger gq = m, gy = BigInteger.valueOf(0);
                BigInteger lq = n, ly = BigInteger.valueOf(1);
                BigInteger tq = lq, ty = ly;
                while(lq.intValue() != 1){
                    BigInteger d = gq.divide(lq);
                    lq = gq.subtract(d.multiply(lq));
                    ly = gy.subtract(d.multiply(ly));
                    gq = tq;
                    gy = ty;
                    tq = lq;
                    ty = ly;
                }
                if(ly.intValue() < 0){
                    ly.add(m);
                }
                return ly;
            }
            public Point add(Point p1, Point p2){
                Point result = new Point();
                BigInteger p = ElGamal.P;
                if(p1.getX().equals(BigInteger.valueOf(0)) && p2.getX().equals(BigInteger.valueOf(0))){
                    result.setX(p2.getX());
                    result.setY(p2.getY());
                }else if(p2.getX().equals(BigInteger.valueOf(0)) && p2.getY().equals(BigInteger.valueOf(0))){
                    result.setX(p1.getX());
                    result.setY(p1.getY());
                }else if(p1.getY().equals(p2.getY().negate())){
                    result.setX(BigInteger.valueOf(0));
                    result.setY(BigInteger.valueOf(0));
                }else if(p1.getX().subtract(p2.getX()).equals(BigInteger.valueOf(0))){
                    result.setX(BigInteger.valueOf(Long.MAX_VALUE));
                    result.setY(BigInteger.valueOf(Long.MAX_VALUE));
                }else{
                    BigInteger lambda, xr, yr;
                    lambda = (p1.getY().subtract(p2.getY())).mod(p);
                    BigInteger valueofX1 = p1.getX(), valueofX2 = p2.getX();
                    BigInteger inv = getInverse(valueofX1.subtract(valueofX2), p);
                    lambda.multiply(inv);
                    lambda.mod(p);
                    xr = lambda.multiply(lambda).mod(p).subtract(valueofX1).subtract(valueofX2).add(BigInteger.valueOf(2).multiply(p)).mod(p);
                    yr = lambda.multiply(valueofX1.subtract(xr)).mod(p).subtract(p1.getY().add(BigInteger.valueOf(2).multiply(p))).mod(p);
                    result.setX(xr);
                    result.setY(yr);
                }
                return result;
            }
            public Point minus(Point p1, Point p2){
                Point temp = new Point();
                Point res = new Point();
                temp.setX(p2.getX());
                temp.setY(p2.getY().negate());
                res = add(p1, temp);
                return res;
            }
            public Pair<Point, Point> encrypt(Point pm, Point pub, Point base){
                Pair<Point, Point> Pc = null;
                BigInteger p = ElGamal.P;
                BigInteger k = BigInteger.valueOf(5);
                Point px = new Point();
                Point py = new Point();
                px = multiply(k, base);
                py = add(pm, multiply(k, pub));
                Pc = new Pair(px, py);
                return Pc;
            }
            public Point decrypt(Pair<Point, Point> Pc, BigInteger pri, Point base){
                Point temp = new Point();
                Point pm = new Point();
                temp = multiply(pri, Pc.getKey());
                pm = minus(Pc.getValue(), temp);
                return pm;
            }
        }// Кінець класу PointProcessor
        public static BigInteger P = BigInteger.valueOf(239);
        public static BigInteger A = BigInteger.valueOf(8);
        public static BigInteger B = BigInteger.valueOf(10);
        public static Point base;
        private BigInteger nullvalue = BigInteger.valueOf(-1);
        private ArrayList<Point> field;
        private BigInteger[] poweredByTwo;
        public ElGamal(){
            field = generateElGamalField();
            base = field.get(10);
        }
        private ArrayList<Point> generateElGamalField(){
            ArrayList<Point> points = new ArrayList<>();
            ArrayList<BigInteger> temp = new ArrayList<>();
            generatePoweredByTwo();
            for(int i = 0; i < Long.valueOf(String.valueOf(P)); i++){
                temp = function(BigInteger.valueOf(i));
                for(int j = 0; j < temp.size(); j++) {
                    Point p = new Point(BigInteger.valueOf(i), temp.get(j));
                    points.add(p);
                }
            }
            return points;
        }
       private ArrayList<BigInteger> function(BigInteger x){
            ArrayList<BigInteger> result = new ArrayList<>();
            BigInteger y = nullvalue;
            y = x.multiply(x).multiply(x).add(A.multiply(x)).add(B);
            y = y.mod(P);
            for(int i = 0; i < poweredByTwo.length; i++){
                if(poweredByTwo[i].mod(P).equals(y)){
                    result.add(BigInteger.valueOf(i));
                }
            }
            return result;
       }
       private void generatePoweredByTwo(){
            poweredByTwo = new BigInteger[P.intValue()];
            for(int i = 0; i < P.intValue(); i++){
                poweredByTwo[i] = BigInteger.valueOf(i*i);
            }
       }
       private byte map(Point p){
            byte result = 0;
            for(int i = 0; i < field.size(); i++){
                if(field.get(i).isEqual(p)){
                    result = (byte) i;
                    break;
                }
            }
            return result;
       }
       public Point generatePublicKey(BigInteger pri){
            PointProccessor proccessor = new PointProccessor();
            Point result = proccessor.multiply(pri, base);
            return result;
       }
       public BigInteger[] encrypt(BigInteger[] bytes, Point pub){
            PointProccessor proccessor = new PointProccessor();
            BigInteger[] result = new BigInteger[bytes.length * 2];
            Point[] p = new Point[bytes.length];
            int j = 0;
            for(int i = 0; i < bytes.length; i++){
                p[i].equals(bytes[i]);
                Pair<Point, Point> pair = proccessor.encrypt(p[i], pub, base);
                result[j] = BigInteger.valueOf(map(pair.getKey()));
                j++;
                result[j] = BigInteger.valueOf(map(pair.getKey()));
                j++;
            }
            return result;
       }
       public BigInteger[] decrypt(BigInteger[] bytes, BigInteger pri){
            PointProccessor proccessor = new PointProccessor();
            BigInteger[] result = new BigInteger[bytes.length / 2];
            int j = 0;
            for(int i = 0; i < result.length; i++){
                Pair<Point, Point> pair = new Pair(Byte.valueOf(String.valueOf(map(field.get(Integer.valueOf(bytes[j].intValue()))))),
                        Byte.valueOf(String.valueOf(map(field.get(Integer.valueOf(bytes[j].intValue()))))));
                j += 2;
                Point temp = proccessor.decrypt(pair, pri, base);
                result[i] = BigInteger.valueOf(map(temp));
            }
            return result;
       }
    }
    // main
    public static void main(String[] args) {

    }
}




