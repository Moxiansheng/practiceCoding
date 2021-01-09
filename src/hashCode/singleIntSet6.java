package hashCode;

public class singleIntSet6 extends singleIntSet5{
    public singleIntSet6(int capacity)
    {
        int size = GetPrime(capacity);
        _values = new Object[size];
    }

    protected int H(int value)
    {
        return value % _values.length;
    }

    // 质数表
    private int[] primes = {
            3, 7, 11, 17, 23, 29, 37, 47, 59, 71, 89, 107, 131, 163, 197, 239, 293, 353, 431, 521, 631, 761, 919,
            1103, 1327, 1597, 1931, 2333, 2801, 3371, 4049, 4861, 5839, 7013, 8419, 10103, 12143, 14591,
            17519, 21023, 25229, 30293, 36353, 43627, 52361, 62851, 75431, 90523, 108631, 130363, 156437,
            187751, 225307, 270371, 324449, 389357, 467237, 560689, 672827, 807403, 968897, 1162687, 1395263,
            1674319, 2009191, 2411033, 2893249, 3471899, 4166287, 4999559, 5999471, 7199369};

    // 如果 min 是质数，返回 min；否则返回比 min 稍大的那个质数
    private int GetPrime(int min)
    {
        // 从质数表中查找比 min 稍大的质数
        for (int i = 0; i < primes.length; i++)
        {
            int prime = primes[i];
            if (prime >= min) return prime;
        }

        // min 超过了质数表的范围时，探查 min 之后的每一个奇数，直到发现下一个质数
        for (int i = (min | 1); i < Integer.MAX_VALUE; i += 2)
        {
            if (common.common.isPrime1(i))
                return i;
        }
        return min;
    }
}
