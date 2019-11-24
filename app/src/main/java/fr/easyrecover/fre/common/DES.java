package fr.easyrecover.fre.common;

public class DES {
    private static int[] IP = new int[]{58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
    private static byte[][] K1;
    private static byte[][] K2;
    private static int[] PC1 = new int[]{57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};
    private static int[] PC2 = new int[]{14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};
    private static int[] expandTbl = new int[]{32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};
    private static byte[][] f1235K;
    private static int[] f1236P = new int[]{16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25};
    private static int[] invIP = new int[]{40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
    private static int[] keyShift = new int[]{1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private static int[][][] sboxes;

    private static final int[][][] r0 = new int[8][][];
    ;

    static {

        r0[0] = new int[][]{new int[]{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7}, new int[]{0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8}, new int[]{4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0}, new int[]{15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}};
        r0[1] = new int[][]{new int[]{15, 1, 8, 14, 6, 11, 3, 2, 9, 7, 2, 13, 12, 0, 5, 10}, new int[]{3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5}, new int[]{0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15}, new int[]{13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}};
        r0[2] = new int[][]{new int[]{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8}, new int[]{13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1}, new int[]{13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7}, new int[]{1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}};
        r0[3] = new int[][]{new int[]{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15}, new int[]{13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9}, new int[]{10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4}, new int[]{3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}};
        r0[4] = new int[][]{new int[]{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9}, new int[]{14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6}, new int[]{4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14}, new int[]{11, 8, 12, 7, 1, 14, 2, 12, 6, 15, 0, 9, 10, 4, 5, 3}};
        r0[5] = new int[][]{new int[]{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11}, new int[]{10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8}, new int[]{9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6}, new int[]{4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}};
        r0[6] = new int[][]{new int[]{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1}, new int[]{13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6}, new int[]{1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2}, new int[]{6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}};
        r0[7] = new int[][]{new int[]{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7}, new int[]{1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2}, new int[]{7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8}, new int[]{2, 1, 14, 7, 4, 10, 18, 13, 15, 12, 9, 0, 3, 5, 6, 11}};
        sboxes = r0;
    }

    private static void setBit(byte[] data, int pos, int val) {
        int posByte = pos / 8;
        int posBit = pos % 8;
        data[posByte] = (byte) ((val << (8 - (posBit + 1))) | ((byte) (((65407 >> posBit) & data[posByte]) & 255)));
    }

    private static int extractBit(byte[] data, int pos) {
        return (data[pos / 8] >> (8 - ((pos % 8) + 1))) & 1;
    }

    private static byte[] rotLeft(byte[] input, int len, int pas) {
        byte[] out = new byte[(((len - 1) / 8) + 1)];
        for (int i = 0; i < len; i++) {
            setBit(out, i, extractBit(input, (i + pas) % len));
        }
        return out;
    }

    private static byte[] extractBits(byte[] input, int pos, int n) {
        byte[] out = new byte[(((n - 1) / 8) + 1)];
        for (int i = 0; i < n; i++) {
            setBit(out, i, extractBit(input, pos + i));
        }
        return out;
    }

    private static byte[] permutFunc(byte[] input, int[] table) {
        byte[] out = new byte[(((table.length - 1) / 8) + 1)];
        for (int i = 0; i < table.length; i++) {
            setBit(out, i, extractBit(input, table[i] - 1));
        }
        return out;
    }

    private static byte[] xor_func(byte[] a, byte[] b) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ b[i]);
        }
        return out;
    }

    private static byte[] encrypt64Bloc(byte[] bloc, byte[][] subkeys, boolean isDecrypt) {
        byte[] tmp = new byte[bloc.length];
        byte[] R = new byte[(bloc.length / 2)];
        byte[] L = new byte[(bloc.length / 2)];
        tmp = permutFunc(bloc, IP);
        L = extractBits(tmp, 0, IP.length / 2);
        R = extractBits(tmp, IP.length / 2, IP.length / 2);
        for (int i = 0; i < 16; i++) {
            byte[] tmpR = R;
            if (isDecrypt) {
                R = f_func(R, subkeys[15 - i]);
            } else {
                R = f_func(R, subkeys[i]);
            }
            R = xor_func(L, R);
            L = tmpR;
        }
        return permutFunc(concatBits(R, IP.length / 2, L, IP.length / 2), invIP);
    }

    private static byte[] f_func(byte[] R, byte[] K) {
        return permutFunc(s_func(xor_func(permutFunc(R, expandTbl), K)), f1236P);
    }

    private static byte[] s_func(byte[] in) {
        in = separateBytes(in, 6);
        byte[] out = new byte[(in.length / 2)];
        int halfByte = 0;
        for (int b = 0; b < in.length; b++) {
            byte valByte = in[b];
            int val = sboxes[b][(((valByte >> 7) & 1) * 2) + ((valByte >> 2) & 1)][(valByte >> 3) & 15];
            if (b % 2 == 0) {
                halfByte = val;
            } else {
                out[b / 2] = (byte) ((halfByte * 16) + val);
            }
        }
        return out;
    }

    private static byte[] separateBytes(byte[] in, int len) {
        int numOfBytes = (((in.length * 8) - 1) / len) + 1;
        byte[] out = new byte[numOfBytes];
        for (int i = 0; i < numOfBytes; i++) {
            for (int j = 0; j < len; j++) {
                setBit(out, (i * 8) + j, extractBit(in, (len * i) + j));
            }
        }
        return out;
    }

    private static byte[] concatBits(byte[] a, int aLen, byte[] b, int bLen) {
        int i;
        byte[] out = new byte[((((aLen + bLen) - 1) / 8) + 1)];
        int j = 0;
        for (i = 0; i < aLen; i++) {
            setBit(out, j, extractBit(a, i));
            j++;
        }
        for (i = 0; i < bLen; i++) {
            setBit(out, j, extractBit(b, i));
            j++;
        }
        return out;
    }

    private static byte[] deletePadding(byte[] input) {
        int count = 0;
        for (int i = input.length - 1; input[i] == (byte) 0; i--) {
            count++;
        }
        byte[] tmp = new byte[((input.length - count) - 1)];
        System.arraycopy(input, 0, tmp, 0, tmp.length);
        return tmp;
    }

    private static byte[][] generateSubKeys(byte[] key) {
        byte[][] tmp = new byte[16][];
        byte[] tmpK = permutFunc(key, PC1);
        byte[] C = extractBits(tmpK, 0, PC1.length / 2);
        byte[] D = extractBits(tmpK, PC1.length / 2, PC1.length / 2);
        for (int i = 0; i < 16; i++) {
            C = rotLeft(C, 28, keyShift[i]);
            D = rotLeft(D, 28, keyShift[i]);
            tmp[i] = permutFunc(concatBits(C, 28, D, 28), PC2);
        }
        return tmp;
    }

    public static byte[] encrypt(byte[] data, byte[] key) {
        int i;
        byte[] padding = new byte[1];
        int lenght = 8 - (data.length % 8);
        padding = new byte[lenght];
        padding[0] = Byte.MIN_VALUE;
        for (i = 1; i < lenght; i++) {
            padding[i] = (byte) 0;
        }
        byte[] tmp = new byte[(data.length + lenght)];
        byte[] bloc = new byte[8];
        f1235K = generateSubKeys(key);
        int count = 0;
        i = 0;
        while (i < data.length + lenght) {
            if (i > 0 && i % 8 == 0) {
                bloc = encrypt64Bloc(bloc, f1235K, false);
                System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
            }
            if (i < data.length) {
                bloc[i % 8] = data[i];
            } else {
                bloc[i % 8] = padding[count % 8];
                count++;
            }
            i++;
        }
        if (bloc.length == 8) {
            bloc = encrypt64Bloc(bloc, f1235K, false);
            System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
        }
        for (int j = 0; j < tmp.length; j++) {
            tmp[j] = (byte) (tmp[j] ^ 8);
        }
        return tmp;
    }

    public static byte[] TripleDES_Encrypt(byte[] data, byte[][] keys) {
        int i;
        byte[] padding = new byte[1];
        int lenght = 8 - (data.length % 8);
        padding = new byte[lenght];
        padding[0] = Byte.MIN_VALUE;
        for (i = 1; i < lenght; i++) {
            padding[i] = (byte) 0;
        }
        byte[] tmp = new byte[(data.length + lenght)];
        byte[] bloc = new byte[8];
        f1235K = generateSubKeys(keys[0]);
        K1 = generateSubKeys(keys[1]);
        K2 = generateSubKeys(keys[2]);
        int count = 0;
        i = 0;
        while (i < data.length + lenght) {
            if (i > 0 && i % 8 == 0) {
                bloc = encrypt64Bloc(encrypt64Bloc(encrypt64Bloc(bloc, f1235K, false), K1, true), K2, false);
                System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
            }
            if (i < data.length) {
                bloc[i % 8] = data[i];
            } else {
                bloc[i % 8] = padding[count % 8];
                count++;
            }
            i++;
        }
        if (bloc.length == 8) {
            bloc = encrypt64Bloc(encrypt64Bloc(encrypt64Bloc(bloc, f1235K, false), K1, true), K2, false);
            System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
        }
        return tmp;
    }

    public static byte[] TripleDES_Decrypt(byte[] data, byte[][] keys) {
        byte[] tmp = new byte[data.length];
        byte[] bloc = new byte[8];
        f1235K = generateSubKeys(keys[0]);
        K1 = generateSubKeys(keys[1]);
        K2 = generateSubKeys(keys[2]);
        int i = 0;
        while (i < data.length) {
            if (i > 0 && i % 8 == 0) {
                bloc = encrypt64Bloc(encrypt64Bloc(encrypt64Bloc(bloc, K2, true), K1, false), f1235K, true);
                System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
            }
            if (i < data.length) {
                bloc[i % 8] = data[i];
            }
            i++;
        }
        bloc = encrypt64Bloc(encrypt64Bloc(encrypt64Bloc(bloc, K2, true), K1, false), f1235K, true);
        System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
        return deletePadding(tmp);
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        byte[] tmp = new byte[data.length];
        byte[] bloc = new byte[8];
        f1235K = generateSubKeys(key);
        for (int j = 0; j < data.length; j++) {
            data[j] = (byte) (data[j] ^ 8);
        }
        int i = 0;
        while (i < data.length) {
            if (i > 0 && i % 8 == 0) {
                bloc = encrypt64Bloc(bloc, f1235K, true);
                System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
            }
            if (i < data.length) {
                bloc[i % 8] = data[i];
            }
            i++;
        }
        bloc = encrypt64Bloc(bloc, f1235K, true);
        System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
        return deletePadding(tmp);
    }
}
