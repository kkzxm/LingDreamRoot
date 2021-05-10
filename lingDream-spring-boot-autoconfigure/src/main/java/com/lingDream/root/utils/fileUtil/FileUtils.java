package com.lingDream.root.utils.fileUtil;


import java.io.*;
import java.util.List;

import static com.lingDream.root.utils.strUtil.StringUtils.truncatedString;


/**
 * @Author: LI_Lingfei
 * @CreateTime: 2020-08-09 13:51
 */
public class FileUtils {
    /**
     * 创建文件的方法
     *
     * @param fileAddress 文件地址字符串
     * @return 文件对象
     */
    public static File newFile(String fileAddress) {
        File file = new File(fileAddress);
        try {
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("文件路径不符合规范!");
        }
        return file;
    }

    /**
     * 创建文件的方法
     *
     * @param file 文件对象
     * @return 文件对象
     */
    public static File newFile(File file) {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
        }
        return file;
    }

    /**
     * 删除文件的方法
     *
     * @param fileAddress 文件地址字符串
     * @return 是否删除成功
     */
    public static boolean deleteFile(String fileAddress) {
        return deleteFile(new File(fileAddress));
    }

    /**
     * 删除文件的方法
     *
     * @param file 要删除的文件对象
     * @return 是否删除成功
     */
    public static boolean deleteFile(File file) {
        return file.delete();
    }

    /**
     * 向文件中写入内容的方法
     *
     * @param file    需要写入信息的文件对象
     * @param infoTxt 需要写入的内容
     */
    public static void writeFileInfo(File file, String infoTxt) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(infoTxt);
        } catch (IOException e) {
            newFile(file.getAbsolutePath());
            System.out.println(file.getAbsoluteFile());
        } finally {
            try {
                if (fileWriter != null) bufferedWriter.close();
                if (bufferedWriter != null) bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件内容的方法
     *
     * @param filePath 需要读取的文件地址
     * @return 读取到的内容字符串
     */
    public static String readFileInfo(String filePath) {
        return readFileInfo(new File(filePath));
    }

    /**
     * 读取文件内容的方法
     *
     * @param file 需要读取的文件对象
     * @return 读取到的内容
     */
    public static String readFileInfo(File file) {
        return readFileInfo(file, false);
    }

    /**
     * 读取文件内容的方法
     *
     * @param filePath 需要读取的文件地址
     * @param format   是否保留文本原格式
     * @return 读取到的内容
     */
    public static String readFileInfo(String filePath, boolean format) {
        return readFileInfo(new File(filePath), format);
    }

    /**
     * 读取文件内容的方法
     *
     * @param file   需要读取的文件对象
     * @param format 是否保留原格式
     * @return 读取到的内容
     */
    public static String readFileInfo(File file, boolean format) {
        FileReader fr = null;
        BufferedReader br = null;
        StringBuffer s1 = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String s = null;
            s1 = new StringBuffer();
            if (format)
                while ((s = br.readLine()) != null) s1.append(s + "\r\n");
            else
                while ((s = br.readLine()) != null) s1.append(s);
        } catch (IOException e) {
            System.out.println("系统找不到文件:" + file.getAbsolutePath());
        } finally {
            try {
                if (fr != null) fr.close();
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(s1);
    }

    /**
     * 复制文件
     *
     * @param originalFile 原文件对象
     * @param outFile      输出文件对象
     */
    public static void copyFile(File originalFile, File outFile) {
        String fileInfo = FileUtils.readFileInfo(originalFile, true);
        FileUtils.writeFileInfo(outFile, fileInfo);
    }

    /**
     * @param originalPath 原文件地址
     * @param outPath      输出文件地址
     */
    public static void copyBinFile(String originalPath, String outPath) {
        copyBinFile(new File(originalPath), new File(outPath));
    }

    /**
     * 复制二进制文件
     *
     * @param originalFile 原文件对象
     * @param outFile      输出文件对象
     */
    public static void copyBinFile(File originalFile, File outFile) {
        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        FileOutputStream fileOutputStream = null;
        DataOutputStream dataOutputStream = null;
        boolean flag = true;
        try {
            fileInputStream = new FileInputStream(originalFile);
            dataInputStream = new DataInputStream(fileInputStream);
            fileOutputStream = new FileOutputStream(outFile);
            dataOutputStream = new DataOutputStream(fileOutputStream);

            int temp;
            while ((temp = dataInputStream.read()) != -1) dataOutputStream.write(temp);
        } catch (IOException e) {
            System.out.println("系统找不到文件" + originalFile.getAbsolutePath());
        } finally {
            try {
                if (dataOutputStream != null) dataOutputStream.close();
                if (fileOutputStream != null) fileOutputStream.close();
                if (dataInputStream != null) dataInputStream.close();
                if (fileInputStream != null) fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前文件后缀名的集合
     */
    public static List<String> getFileSuffixes(File file) {
        String name = file.getName();
        return truncatedString("\\.[\\w]+", name);
    }

    /**
     * 得到文件后缀名(最后一个)
     *
     * @param file 需要得到后缀名的文件
     * @return
     */
    public static String getFileSuffix(File file) {
        List<String> fileSuffixes = getFileSuffixes(file);
        int last = fileSuffixes.size() - 1;
        if (last > -1) return fileSuffixes.get(last);
        return null;
    }

    /**
     * 使用类加载器,返回输入流<br/>
     * (文件放在classes下,直接输入文件名即可读取)
     */
    public static InputStream getClassLoaderInputStream(String filePath) {
        return FileUtils.class.getClassLoader().getResourceAsStream(filePath);
    }
}
